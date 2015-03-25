package org.dataart.qdump.entities.person;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.helper.EntitiesUpdater;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.serializer.QuestionPersonSerializer;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "person_questions")
@AttributeOverride(name = "id", column = @Column(name = "id_person_question", insertable = false, updatable = false))
@JsonAutoDetect
@JsonIgnoreProperties({"createdDate", "modifiedDate"})
public class PersonQuestionEntity extends BaseEntity implements
		Serializable {
	private static final long serialVersionUID = -6691017410211190245L;
    @JsonProperty("question_entity")
	@JsonSerialize(using = QuestionPersonSerializer.class)
	private QuestionEntity questionEntity;
	@JsonProperty("correct")
	private boolean correct;
	@JsonProperty("person_answer_entities")
	private List<PersonAnswerEntity> personAnswerEntities;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_question", referencedColumnName = "id_question")
	public QuestionEntity getQuestionEntity() {
		return questionEntity;
	}

	public void setQuestionEntity(QuestionEntity questionEntity) {
		this.questionEntity = questionEntity;
	}

	@Column(name = "correct", columnDefinition = "BIT(1) DEFAULT 0")
	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean isCorrect) {
		this.correct = isCorrect;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "id_person_question", referencedColumnName = "id_person_question")
	public List<PersonAnswerEntity> getPersonAnswerEntities() {
		return personAnswerEntities;
	}

	public void setPersonAnswerEntities(
			List<PersonAnswerEntity> personAnswerEntities) {
		this.personAnswerEntities = personAnswerEntities;
	}

	/**
	 * Check if question is correct. Question is set as correct when all answers
	 * is correct.
	 * 
	 * @return
	 */
	public void checkQuestionIsCorrect() {
		correct = checkPersonAnswersIsCorrect();
	}

	/**
	 * Validate {@link PersonAnswerEntity} if it is correct. Return true if and
	 * only if all {@link PersonAnswerEntity} that was marked by
	 * {@link PersonEntity} has a corresponding correct {@link AnswerEntity}.
	 * Validator return false if one of the {@link PersonAnswerEntity} that was
	 * marked by user has no corresponding correct {@link AnswerEntity} or if
	 * one of {@link PersonAnswerEntity} is not marked by {@link PersonEntity}
	 * by has corresponding correct {@link AnswerEntity}.
	 * 
	 * @return boolean
	 */
	private boolean checkPersonAnswersIsCorrect() {
		for (PersonAnswerEntity answerEntity : Preconditions
				.checkNotNull(personAnswerEntities)) {
            if(questionEntity.getType().equals(QuestionTypeEnums.TEXTAREA)) {
                return correct;
            } else if ((answerEntity.isMarked() && !Preconditions.checkNotNull(
					answerEntity.getAnswerEntity()).isCorrect())
					|| (!answerEntity.isMarked() && Preconditions
							.checkNotNull(answerEntity.getAnswerEntity()
									.isCorrect()))) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkIdForCreation() {
		if(id != 0 || questionEntity.getId() == 0) {
			return false;
		}
		for(PersonAnswerEntity entity : personAnswerEntities) {
            if(questionEntity.getType().equals(QuestionTypeEnums.TEXTAREA)) {
                return true;
            } else if(!entity.checkIdForCreation()) {
				return false;
			}
		}
		return true;
	}
	
	public void updateEntity(Object obj) {
		PersonQuestionEntity entity = (PersonQuestionEntity) obj;
		List<String> ignoredFields = Arrays.asList("personQuestionnaireEntity",
				"questionEntity", "personAnswerEntities");
		EntitiesUpdater.updateEntity(entity, this, ignoredFields, PersonQuestionEntity.class);
		EntitiesUpdater.updateEntities(entity.personAnswerEntities, personAnswerEntities, PersonAnswerEntity.class);
	}
	
	public boolean entitiesIsEquals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		PersonQuestionEntity entity = (PersonQuestionEntity) obj;
		return new EqualsBuilder()
				.append(this.id, entity.id)
				.append(correct, entity.correct)
				.isEquals();
	}

	@Override
	public String toString() {
		return "PersonQuestionEntity [isCorrect()=" + isCorrect()
				+ ", getId()=" + getId() + ", getCreatedDate()="
				+ getCreatedDate() + ", getModifiedDate()=" + getModifiedDate()
				+ "]";
	}

	
}
