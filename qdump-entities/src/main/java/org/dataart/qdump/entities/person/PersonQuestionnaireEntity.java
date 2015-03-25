package org.dataart.qdump.entities.person;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.enums.QuestionnaireStatusEnums;
import org.dataart.qdump.entities.helper.EntitiesUpdater;
import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.serializer.QuestionnairePersonSerializer;

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
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "person_questionnaires")
@AttributeOverride(name = "id", column = @Column(name = "id_person_questionnaire", insertable = false, updatable = false))
@JsonAutoDetect
@NamedQueries({
		@NamedQuery(name = "PersonQuestionnaireEntity.findPersonQuestionnaireByStatus", query = "FROM " +
                "PersonQuestionnaireEntity pinq WHERE pinq.status = ?1"),
		@NamedQuery(name = "PersonQuestionnaireEntity.findPersonQuestionnaireByQuestionnaireName", query = "FROM " +
                "PersonQuestionnaireEntity pinq WHERE pinq.questionnaireEntity.name = ?1"),
        @NamedQuery(name = "PersonQuestionnaireEntity.findPersonQuestionnaireByPersonId", query = "SELECT pq FROM " +
                "PersonQuestionnaireEntity pq, PersonEntity p WHERE pq.id = ?1 AND p.id = ?2"),
        @NamedQuery(name = "PersonQuestionnaireEntity.countCompletedPersonQuestionnairesByPersonId", query = "SELECT count(pq) FROM PersonQuestionnaireEntity pq, PersonEntity p WHERE p.id = ?1 AND pq.status NOT IN ('in progress', 'not specified')"),
        @NamedQuery(name = "PersonQuestionnaireEntity.countStartedPersonQuestionnairesByPersonId", query = "SELECT " +
                "COUNT(pq) FROM PersonQuestionnaireEntity pq, PersonEntity p WHERE p.id = ?1 AND pq.status = 'in progress'")

})
public class PersonQuestionnaireEntity extends BaseEntity
		implements Serializable {
	private static final long serialVersionUID = 586942138808550795L;
    @JsonProperty("questionnaire_entity")
	@JsonSerialize(using = QuestionnairePersonSerializer.class)
	private QuestionnaireEntity questionnaireEntity;
	private String status;
	@JsonProperty("person_question_entities")
	private List<PersonQuestionEntity> personQuestionEntities;

    public PersonQuestionnaireEntity() {
        super();
    }
    public PersonQuestionnaireEntity(long id, String status, long questionnaireId, String questionnaireName, Date createdDate, Date modifiedDate) {
        this.id = id;
        this.status = status;
        this.questionnaireEntity = new QuestionnaireEntity(questionnaireId, questionnaireName, null);
        this.modifiedDate = modifiedDate;
        this.createdDate = createdDate;
    }

    @OneToOne(fetch = FetchType.EAGER)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = PersonQuestionEntity.class)
    @JoinColumn(name = "id_person_questionnaire", referencedColumnName = "id_person_questionnaire")
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
		} else if (!checkQuestionsType()
                && !checkPersonQuestionEntitiesIsCorrect()) {
            status = QuestionnaireStatusEnums.IN_CHECKING_PROCESS.getName();
        } else if (!checkPersonQuestionEntitiesIsCorrect()) {
			status = QuestionnaireStatusEnums.NOT_PASSED.getName();
		}  else {
			status = QuestionnaireStatusEnums.IN_PROGRESS.getName();
		}
	}

	private boolean checkPersonQuestionEntitiesIsCorrect() {
		for (PersonQuestionEntity questionEntity : Preconditions
				.checkNotNull(personQuestionEntities)) {
            questionEntity.checkQuestionIsCorrect();
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
					.getType() == QuestionTypeEnums.TEXTAREA) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkIdForCreation() {
		if(id != 0 || questionnaireEntity.getId() == 0) {
			return false;
		}
		for(PersonQuestionEntity entity : personQuestionEntities) {
			if(!entity.checkIdForCreation()) {
				return false;
			}
		}
		return true;
	}

	
	public void updateEntity(Object obj) {
		PersonQuestionnaireEntity entity = (PersonQuestionnaireEntity) obj;
		List<String> ignoredFields = Arrays.asList("questionnaireEntity",
				"personQuestionEntities");
		EntitiesUpdater.updateEntity(entity, this, ignoredFields, PersonQuestionnaireEntity.class);
		EntitiesUpdater.updateEntities(entity.personQuestionEntities, personQuestionEntities, PersonQuestionEntity.class);
	}
	
	public boolean entitiesIsEquals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		PersonQuestionnaireEntity entity = (PersonQuestionnaireEntity) obj;
		return new EqualsBuilder()
				.append(this.id, entity.id)
				.append(status, entity.status)
				.isEquals();
	}
	
	@Override
	public String toString() {
		return "PersonQuestionnaireEntity [ getStatus()=" + getStatus()
				+ ", getId()=" + getId() + ", getCreatedDate()="
				+ getCreatedDate() + ", getModifiedDate()=" + getModifiedDate()
				+ "]";
	}
}
