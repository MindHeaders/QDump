package org.dataart.qdump.entities.person;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.validator.routines.EmailValidator;
import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.questionnaire.QuestionnaireBaseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCrypt;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "persons")
@AttributeOverride(name = "id", column = @Column(name = "id_person", insertable = false, updatable = false))
@JsonAutoDetect
@NamedQueries({
	@NamedQuery(name = "PersonEntity.getPersonByEmail", query = "FROM PersonEntity p "
			+ "WHERE p.email = ?1"),
	@NamedQuery(name = "PersonEntity.getPersonByLogin", query = "FROM PersonEntity p "
			+ "WHERE p.login = ?1"),
	@NamedQuery(name = "PersonEntity.getPersonByPersonGroup", query = "FROM PersonEntity p  "
			+ "WHERE p.personGroup = ?1"),
	@NamedQuery(name = "PersonEntity.getPersonsNameLastname", query = "SELECT NEW "
			+ "org.dataart.qdump.entities.person.PersonEntity(p.firstname, p.lastname, p.id) "
			+ "FROM PersonEntity p"),
	@NamedQuery(name = "PersonEntity.getPersonForAuthByLogin", query = "SELECT NEW "
			+ "org.dataart.qdump.entities.person.PersonEntity(p.email, p.password, p.login) "
			+ "FROM PersonEntity p WHERE p.login = ?1")
})
public class PersonEntity extends QuestionnaireBaseEntity implements Serializable {
	private static final long serialVersionUID = -219526512840281300L;
	private String firstname;
	private String lastname;
	private String email;
	private String login;
	private String password;
	@JsonProperty("enabled")
	private boolean isEnabled;
	private byte gender;
	@JsonProperty("person_group")
	private PersonGroupEnums personGroup;
	private List<PersonQuestionnaireEntity> personQuestionnaireEntities;

	public PersonEntity() {
		super();
	}

	public PersonEntity(String email, String password, String login) {
		super();
		this.email = email;
		this.password = password;
		this.login = login;
	}
	
	public PersonEntity(String firstname, String lastname, long id) {
		super();
		this.firstname = firstname;
		this.lastname = lastname;
		this.id = id;
	}

	/**
	 * Length is set as requirement of Data Standards Catalogue. Name
	 * 
	 * @return
	 */
	@Column(name = "firstname", length = 35)
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	@Column(name = "lastname", length = 35)
	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	@Column(name = "email", nullable = false, unique = true, length = 254)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "login", nullable = false, unique = true, length = 35)
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "password", nullable = false, length = 255)
	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty("password")
	public void setPassword(String password) {
		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
	}

	/**
	 * Set true if this use is verified by ADMIN {@link PersonGroupEnums} after
	 * email confirmation
	 * 
	 * @param isEnabled
	 */
	@Column(name = "enabled", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
	public boolean isEnabled() {
		return isEnabled;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * Gender has three values 1 = Male, 2 = Female, 9 = Not Specified. By
	 * default - 9. This values is equals to Data Standards Catalogue.
	 * 
	 * @param gender
	 */
	@Column(name = "gender", columnDefinition = "TINYINT DEFAULT 9")
	public byte getGender() {
		return gender;
	}

	public void setGender(byte gender) {
		if(gender == 1 || gender == 2) {
			this.gender = gender;
		} else {
			this.gender = 9;
		}
	}

	/**
	 * All user that was created should have {@link PersonGroupEnums} by default
	 * persons has USER group.
	 * 
	 * @return person group for the current person
	 */
	@Column(name = "person_group", columnDefinition = "VARCHAR(10) DEFAULT 'USER'", nullable = false)
	@Enumerated(EnumType.STRING)
	public PersonGroupEnums getPersonGroup() {
		return personGroup;
	}

	public void setPersonGroup(PersonGroupEnums personGroup) {
		if(personGroup == null) {
			this.personGroup = PersonGroupEnums.USER;
		} else {
			this.personGroup = personGroup;
		}
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "ownBy", orphanRemoval = true)
	@JsonProperty("person_questionnaires")
	public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities() {
		return personQuestionnaireEntities;
	}

	@JsonIgnore
	public void setPersonQuestionnaireEntities(
			List<PersonQuestionnaireEntity> personQuestionnaireEntities) {
		this.personQuestionnaireEntities = personQuestionnaireEntities;
	}

	/**
	 * Name validator, persisted name should contains only letters from A to Z .
	 * Max length 35
	 * 
	 * @param name
	 */
	public void validateFirstname() {
		if (!this.firstname.matches("[A-Z][a-zA-Z]*") && this.firstname != null
				&& firstname.length() > 35) {
			throw new RuntimeException("Invalid input firstname, max length - 35 characters, should contains"
					+ "only A-Z, a-z symbols (valid - John, invalid - john, j0hn)");
		}
	}

	/**
	 * Surname validator, persisted surname should contains letter from A to Z,
	 * spaces, -, '. Max length 35
	 * 
	 * @param surname
	 *            Inserted surname for person
	 * @return
	 */
	public void validateLastname(String lastname) {
		if (!this.lastname.matches("[a-zA-z]+([ '-][a-zA-Z]+)*") && this.lastname != null
				&& this.lastname.length() > 35) {
			throw new RuntimeException("Invalid input lastname, max length - 35 characters, should contains"
					+ "only A-Z, a-z, , ', - symbols (example valid - M`gain, invalid - 1Smith");
		}
	}

	/**
	 * Validate email with help of Apache Commons Validator
	 * 
	 * @param email
	 */
	public void validateEmail(String email) {
		if (!EmailValidator.getInstance().isValid(email)) {
			throw new RuntimeException("You enter invalid email address");
		}
	}
	
	/**
	 * Update all properties which is not equals to fields falue from database. Also this update is 
	 * @param personEntity
	 */
	public void updatePersonEntity(PersonEntity personEntity) {
		BeanWrapper trg = new BeanWrapperImpl(this);
		BeanWrapper src = new BeanWrapperImpl(personEntity);
		List<String> ignoredProperties = Arrays.asList("id", "createdDate",
				"createdBy");
		for (PropertyDescriptor descriptor : BeanUtils
				.getPropertyDescriptors(PersonEntity.class)) {
			String propName = descriptor.getName();
			if (trg.getPropertyValue(propName) != src
					.getPropertyValue(propName)
					&& !ignoredProperties.contains(propName)) {
				if (propName.equals("gender")
						&& src.getPropertyValue(propName).equals(9)) {
					continue;
				} else {
					trg.setPropertyValue(propName,
							src.getPropertyValue(propName));
				}
			}
		}
	}

	@Override
	public String toString() {
		return "PersonEntity [getName()=" + getFirstname() + ", getSurname()="
				+ getLastname() + ", getEmail()=" + getEmail()
				+ ", getLogin()=" + getLogin() + ", getPassword()="
				+ getPassword() + ", isEnabled()=" + isEnabled()
				+ ", getGender()=" + getGender() + ", getPersonGroup()="
				+ getPersonGroup() + ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + "]";
	}

}
