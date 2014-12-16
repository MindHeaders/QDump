package org.dataart.qdump.entities.person;

import java.io.Serializable;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.enums.QuestionnaireStatusEnums;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.serializer.QuestionnairePersonSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;

@Entity
@Table(name = "person_questionnaires")
@AttributeOverride(name = "id", column = @Column(name = "id_person_questionnaire", insertable = false, updatable = false))
@JsonAutoDetect
@NamedQueries({
		@NamedQuery(name = "PersonQuestionnaireEntity.findByOwnBy", query = "FROM PersonQuestionnaireEntity pinq "
				+ "WHERE pinq.ownBy = ?1"),
		@NamedQuery(name = "PersonQuestionnaireEntity.getPersonQuestionnaireByStatus", query = "FROM PersonQuestionnaireEntity pinq "
				+ "WHERE pinq.status = ?1"),
		@NamedQuery(name = "PersonQuestionnaireEntity.getPersonQuestionnaireByQuestionnaireName", query = "FROM PersonQuestionnaireEntity pinq "
				+ "WHERE pinq.questionnaireEntity.name = ?1") })
public class PersonQuestionnaireEntity extends PersonQuestionnaireBaseEntity
		implements Serializable {
	private static final long serialVersionUID = 586942138808550795L;
	@JsonProperty("questionnaire_entity")
	@JsonSerialize(using = QuestionnairePersonSerializer.class)
	private QuestionnaireEntity questionnaireEntity;
	private String status;
	@JsonProperty("person_questions")
	private List<PersonQuestionEntity> personQuestionEntities;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_questionnaire", referencedColumnName = "id_questionnaire")
	public QuestionnaireEntity getQuestionnaireEntity() {
		return questionnaireEntity;
	}

	public void setQuestionnaireEntity(QuestionnaireEntity questionnaireEntity) {
		this.questionnaireEntity = questionnaireEntity;
	}

	@Column(name = "status", columnDefinition = "VARCHAR(100) DEFAULT 'not specified'")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "personQuestionnaireEntity", orphanRemoval = true, targetEntity = PersonQuestionEntity.class)
	public List<PersonQuestionEntity> getPersonQuestionEntities() {
		return personQuestionEntities;
	}

	public void setPersonQuestionEntities(
			List<PersonQuestionEntity> personQuestionEntities) {
		this.personQuestionEntities = personQuestionEntities;
	}

	/**
	 * Check status of {@link PersonQuestionnaireEntity}, should be used after
	 * persistence {@link PersonQuestionnaireEntity}. If all
	 * {@link PersonQuestionEntity} in current object is correct, status of
	 * {@link PersonQuestionnaireEntity} should be set as "Passed". If all
	 * {@link PersonQuestionEntity} is not correct status should be set as
	 * "Not Passed". If any of {@link PersonQuestionEntity} is has
	 * {@link QuestionTypeEnums} set as Field and any of
	 * {@link PersonQuestionEntity} is not correct status should be set as
	 * "In Checking Process". Be default all started
	 * {@link PersonQuestionnaireEntity} should have status "In Progress"
	 */
	public void checkStatus() {
		if (checkPersonQuestionEntitiesIsCorrect()) {
			status = QuestionnaireStatusEnums.PASSED.getName();
		} else if (!checkPersonQuestionEntitiesIsCorrect()) {
			status = QuestionnaireStatusEnums.NOT_PASSED.getName();
		} else if (!checkQuestionsType()
				&& !checkPersonQuestionEntitiesIsCorrect()) {
			status = QuestionnaireStatusEnums.IN_CHECKING_PROCESS.getName();
		} else {
			status = QuestionnaireStatusEnums.IN_PROGRESS.getName();
		}
	}

	/**
	 * Validate if all {@link PersonQuestionEntity} is correct. Return true if
	 * and only if all {@link PersonQuestionEntity} isCorrect field is true.
	 * 
	 * @return
	 */
	private boolean checkPersonQuestionEntitiesIsCorrect() {
		for (PersonQuestionEntity questionEntity : Preconditions
				.checkNotNull(personQuestionEntities)) {
			if (questionEntity.isCorrect() == false) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Validate type of question. If one of this {@link QuestionTypeEnums} is
	 * equals to FIELD method will return false.
	 * 
	 * @return boolean
	 */
	private boolean checkQuestionsType() {
		for (PersonQuestionEntity questionEntity : Preconditions
				.checkNotNull(personQuestionEntities)) {
			if (Preconditions.checkNotNull(questionEntity.getQuestionEntity())
					.getType() == QuestionTypeEnums.FIELD) {
				return false;
			}
		}
		return true;
	}
	
	public void addPersonQuestionEntity(PersonQuestionEntity personQuestionEntity) {
		this.personQuestionEntities.add(personQuestionEntity);
		if(personQuestionEntity.getPersonQuestionnaireEntity() != this) {
			personQuestionEntity.setPersonQuestionnaireEntity(this);
		}
	}
	
	/**
	 * Update {@link PersonQuestionEntity} that is associated with this {@link PersonQuestionnaireEntity}. 
	 * This update is performed before persist current object to dataBase and before update.
	 * This update is needed for One To Many associations.
	 */
	@PrePersist
	@PreUpdate
	public void updatePersonQuestionEntities() {
		if (personQuestionEntities != null) {
			personQuestionEntities.stream().forEach(
					entity -> entity.addPersonQuestionnaireEntity(this));
		}
	}
	
	/**
	 * Update information for existing {@link PersonQuestionnaireEntity} in database.
	 * @param entity
	 */
	public void updatePersonQuestionnaireEntity(PersonQuestionnaireEntity entity) {
		
	}
	
	@Override
	public String toString() {
		return "PersonQuestionnaireEntity [ getStatus()=" + getStatus()
				+ ", getId()=" + getId() + ", getCreatedDate()="
				+ getCreatedDate() + ", getModifiedDate()=" + getModifiedDate()
				+ "]";
	}
}
