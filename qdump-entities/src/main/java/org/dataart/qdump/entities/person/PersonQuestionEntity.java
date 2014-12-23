package org.dataart.qdump.entities.person;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.dataart.qdump.entities.helper.EntitiesUpdater;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.serializer.QuestionPersonSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Preconditions;

@Entity
@Table(name = "person_questions")
@AttributeOverride(name = "id", column = @Column(name = "id_person_question", insertable = false, updatable = false))
@JsonAutoDetect
@JsonIgnoreProperties({"createdDate", "modifiedDate"})
public class PersonQuestionEntity extends BaseEntity implements
		Serializable {
	private static final long serialVersionUID = -6691017410211190245L;
	@JsonBackReference
	private PersonQuestionnaireEntity personQuestionnaireEntity;
	@JsonSerialize(using = QuestionPersonSerializer.class)
	private QuestionEntity questionEntity;
	@JsonProperty("correct")
	private boolean correct;
	@JsonProperty("person_answers")
	private List<PersonAnswerEntity> personAnswerEntities;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = PersonQuestionnaireEntity.class)
	@JoinColumn(name = "id_person_questionnaire", referencedColumnName = "id_person_questionnaire")
	public PersonQuestionnaireEntity getPersonQuestionnaireEntity() {
		return personQuestionnaireEntity;
	}

	public void setPersonQuestionnaireEntity(
			PersonQuestionnaireEntity personQuestionnaireEntity) {
		this.personQuestionnaireEntity = personQuestionnaireEntity;
	}

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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "personQuestionEntity", orphanRemoval = true)
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
			if ((answerEntity.isMarked() && !Preconditions.checkNotNull(
					answerEntity.getAnswerEntity()).isCorrect())
					|| (!answerEntity.isMarked() && Preconditions
							.checkNotNull(answerEntity.getAnswerEntity()
									.isCorrect()))) {
				return false;
			}
		}
		return true;
	}
	
	public void addPersonQuestionnaireEntity(PersonQuestionnaireEntity personQuestionnaireEntity) {
		this.personQuestionnaireEntity = personQuestionnaireEntity;
		if(!personQuestionnaireEntity.getPersonQuestionEntities().contains(this)) {
			personQuestionnaireEntity.getPersonQuestionEntities().add(this);
		}
	}
	
	public void addPersonAnswerEntity(PersonAnswerEntity personAnswerEntity) {
		this.personAnswerEntities.add(personAnswerEntity);
		if(personAnswerEntity.getPersonQuestionEntity() != this) {
			personAnswerEntity.setPersonQuestionEntity(this);
		}
	}
	
	public boolean checkIdForCreation() {
		if(id != 0 || questionEntity.getId() == 0) {
			return false;
		}
		for(PersonAnswerEntity entity : personAnswerEntities) {
			if(!entity.checkIdForCreation()) {
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
	
	@PrePersist
	@PreUpdate
	public void updatePersonAnswerEntities() {
		if (personAnswerEntities != null) {
			personAnswerEntities.stream().forEach(
					entity -> entity.addPersonQuestionEntity(this));
		}
	}

	@Override
	public String toString() {
		return "PersonQuestionEntity [isCorrect()=" + isCorrect()
				+ ", getId()=" + getId() + ", getCreatedDate()="
				+ getCreatedDate() + ", getModifiedDate()=" + getModifiedDate()
				+ "]";
	}

	
}
