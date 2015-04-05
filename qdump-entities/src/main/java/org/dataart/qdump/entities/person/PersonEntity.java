package org.dataart.qdump.entities.person;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.validator.routines.EmailValidator;
import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.questionnaire.QuestionnaireBaseEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "persons")
@AttributeOverride(name = "id", column = @Column(name = "id_person", insertable = false, updatable = false))
@JsonAutoDetect
@NamedQueries({
        @NamedQuery(name = "PersonEntity.findByEmail",
                query = "FROM PersonEntity p WHERE p.email = ?1"),
        @NamedQuery(name = "PersonEntity.findByLogin",
                query = "FROM PersonEntity p WHERE p.login = ?1"),
        @NamedQuery(name = "PersonEntity.findByPersonGroup",
                query = "FROM PersonEntity p WHERE p.personGroup = ?1"),
        @NamedQuery(name = "PersonEntity.findByLoginForAuth",
                query = "SELECT NEW org.dataart.qdump.entities.person.PersonEntity(p.email, p.password, p.login) "
                + "FROM PersonEntity p WHERE p.login = ?1"),
        @NamedQuery(name = "PersonEntity.deleteByEmail",
                query = "DELETE FROM PersonEntity p WHERE p.email = ?1"),
        @NamedQuery(name = "PersonEntity.deleteByLogin",
                query = "DELETE FROM PersonEntity p WHERE p.login = ?1"),
        @NamedQuery(name = "PersonEntity.existsByLogin",
                query = "SELECT CASE WHEN (COUNT(p) > 0) THEN true ELSE false END FROM PersonEntity p WHERE p.login = ?1"),
        @NamedQuery(name = "PersonEntity.existsByEmail",
                query = "SELECT CASE WHEN (COUNT(p) > 0) THEN true ELSE false END FROM PersonEntity p WHERE p.email = ?1"),
        @NamedQuery(name = "PersonEntity.enabledByLogin",
                query = "SELECT CASE WHEN (p.enabled = true) THEN true ELSE false END FROM PersonEntity p WHERE p.login = ?1"),
        @NamedQuery(name = "PersonEntity.enabledByEmail",
                query = "SELECT CASE WHEN (p.enabled = true) THEN true ELSE false END FROM PersonEntity p WHERE p.email = ?1"),
        @NamedQuery(name = "PersonEntity.findPersonRole",
                query = "SELECT p.personGroup FROM PersonEntity p WHERE p.id = ?1"),
        @NamedQuery(name = "PersonEntity.findById",
                query = "SELECT NEW org.dataart.qdump.entities.person.PersonEntity(p.id, p.email, p.login, p.firstname, p.lastname, p.gender) " +
                        "FROM PersonEntity p WHERE p.id = ?1")
})
public class PersonEntity extends QuestionnaireBaseEntity implements Serializable {
	private static final long serialVersionUID = -219526512840281300L;
	private String firstname;
	private String lastname;
	private String email;
	private String login;
	private String password;
	private boolean enabled;
	private byte gender;
	@JsonProperty("person_group")
	private PersonGroupEnums personGroup;
    @JsonProperty("person_questionnaire_entities")
	private List<PersonQuestionnaireEntity> personQuestionnaireEntities;

	public PersonEntity() {
		super();
		this.personGroup = PersonGroupEnums.USER;
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
    public PersonEntity(long id, Date createdDate, Date modifiedDate, String email, String login, boolean
            enabled, PersonGroupEnums personGroup) {
		super();
		this.id = id;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
        this.email = email;
        this.login = login;
        this.enabled = enabled;
        this.personGroup = personGroup;
	}
    public PersonEntity(long id, String email, String login, String firstname, String lastname, byte
      gender) {
		super();
		this.id = id;
        this.email = email;
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
	}
    public PersonEntity(long personId, String login, long personQuestionnaireId, Date createdDate, Date modifiedDate,
                        long questionnaireId, String questionnaireName) {
        this.id = personId;
        this.login = login;
        PersonQuestionnaireEntity personQuestionnaireEntity = new PersonQuestionnaireEntity();
        personQuestionnaireEntity.setId(personQuestionnaireId);
        personQuestionnaireEntity.setCreatedDate(createdDate);
        personQuestionnaireEntity.setModifiedDate(modifiedDate);
        QuestionnaireEntity questionnaireEntity = new QuestionnaireEntity();
        questionnaireEntity.setId(questionnaireId);
        questionnaireEntity.setName(questionnaireName);
        personQuestionnaireEntity.setQuestionnaireEntity(questionnaireEntity);
        List<PersonQuestionnaireEntity> entities = Arrays.asList(personQuestionnaireEntity);
        this.personQuestionnaireEntities = entities;
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
//		this.password = BCrypt.hashpw(password, BCrypt.gensalt());
		this.password = password;
	}

	/**
	 * Set true if this use is verified by ADMIN {@link PersonGroupEnums} after
	 * email confirmation
	 * 
	 * @param enabled
	 */
	@Column(name = "enabled", nullable = false, columnDefinition = "BIT(1) DEFAULT 0")
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = PersonQuestionnaireEntity.class,
            orphanRemoval = true)
    @JoinColumn(name = "id_person", referencedColumnName = "id_person", nullable = false)
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
	public static String validateFirstname(String firstname) {
		if (firstname == null) {
			return firstname;
		} else if (!firstname.matches("[A-Z][a-zA-Z]*")
				|| firstname.length() > 35) {
			throw new RuntimeException(
					"Invalid input firstname, max length - 35 characters, should contains"
							+ "only A-Z, a-z symbols (valid - John, invalid - john, j0hn)");
		} else {
			return firstname;
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
	public static String validateLastname(String lastname) {
		if (lastname == null) {
			return lastname;
		} else if (!lastname.matches("[a-zA-z]+([ '-][a-zA-Z]+)*")
				|| lastname.length() > 35) {
			throw new RuntimeException(
					"Invalid input lastname, max length - 35 characters, should contains"
							+ "only A-Z, a-z, , ', - symbols (example valid - M`gain, invalid - 1Smith)");
		} else {
			return lastname;
		}
	}

	public static String validateEmail(String email) {
		if (!EmailValidator.getInstance().isValid(email)) {
			throw new RuntimeException("You enter invalid email address");
		}
		return email;
	}
	
	@Override
	public String toString() {
		return "PersonEntity [getName()=" + getFirstname() + ", getSurname()="
				+ getLastname() + ", getEmail()=" + getEmail()
				+ ", getLogin()=" + getLogin() + ", getPassword()="
				+ getPassword() + ", isEnabled()=" + isEnabled()
				+ ", getGender()=" + getGender() + ", getPersonGroup()="
				+ getPersonGroup() + ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + ", ]";
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31)
			.appendSuper(super.hashCode())
			.append(firstname)
			.append(lastname)
			.append(email)
			.append(login)
			.append(password)
			.append(enabled)
			.append(gender)
			.append(personGroup)
			.append(personQuestionnaireEntities)
			.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof PersonEntity)) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		PersonEntity entity = (PersonEntity) obj;
		return new EqualsBuilder()
			.appendSuper(super.equals(obj))
			.append(firstname, entity.firstname)
			.append(lastname, entity.lastname)
			.append(email, entity.email)
			.append(login, entity.login)
			.append(password, entity.password)
			.append(enabled, entity.enabled)
			.append(gender, entity.gender)
			.append(personGroup, entity.personGroup)
			.append(personQuestionnaireEntities, entity.personQuestionnaireEntities)
			.isEquals();
	}
	
	
}
