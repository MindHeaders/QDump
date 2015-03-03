package org.dataart.qdump.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.helper.EntitiesUpdater;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "questions")
@AttributeOverride(name = "id", column = @Column(name = "id_question", insertable = false, updatable = false))
@JsonAutoDetect
@JsonIgnoreProperties({ "createdDate", "modifiedDate", "question_id" })
public class QuestionEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7827573669263895832L;
	private String question;
	@JsonProperty("question_type")
	private QuestionTypeEnums type;
	@JsonProperty("answer_entities")
	private List<AnswerEntity> answerEntities;
	@JsonBackReference
	private QuestionnaireEntity questionnaireEntity;

	@Column(name = "question", nullable = false, length = 250)
	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Column(name = "question_type", nullable = false)
	@Enumerated(EnumType.STRING)
	public QuestionTypeEnums getType() {
		return type;
	}

	public void setType(QuestionTypeEnums type) {
		this.type = type;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "questionEntity", fetch = FetchType.EAGER, targetEntity = AnswerEntity.class)
	public List<AnswerEntity> getAnswerEntities() {
		return answerEntities;
	}

	public void setAnswerEntities(List<AnswerEntity> answerEntities) {
		this.answerEntities = answerEntities;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = QuestionnaireEntity.class)
	@JoinColumn(name = "id_questionnaire", referencedColumnName = "id_questionnaire")
	public QuestionnaireEntity getQuestionnaireEntity() {
		return questionnaireEntity;
	}

	public void setQuestionnaireEntity(QuestionnaireEntity questionnaireEntity) {
		this.questionnaireEntity = questionnaireEntity;
	}

	public void addQuestionnaireEntity(QuestionnaireEntity questionnaireEntity) {
		this.questionnaireEntity = questionnaireEntity;
		if (!questionnaireEntity.getQuestionEntities().contains(this)) {
			questionnaireEntity.getQuestionEntities().add(this);
		}
	}
	
	@PrePersist
	@PreUpdate
	public void setAnswerEntities() {
		if (answerEntities != null) {
			answerEntities.stream().forEach(
					entity -> entity.addQuestionEntity(this));
		}
	}
	
	public boolean checkIdForCreation() {
		if(id > 0) {
			return false;
		}
		for(AnswerEntity entity : answerEntities) {
			if(entity.id > 0) {
				return false;
			}
		}
		return true;
	}
	
	public void updateEntity(Object obj) {
		QuestionEntity entity = (QuestionEntity) obj;
		List<String> ignoredFields = Arrays.asList("answerEntities",
				"questionnaireEntity");
		EntitiesUpdater.updateEntity(entity, this, ignoredFields, QuestionEntity.class);
		EntitiesUpdater.updateEntities(entity.answerEntities, answerEntities, AnswerEntity.class);
	}

	public boolean entitiesIsEquals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		QuestionEntity entity = (QuestionEntity) obj;
		return new EqualsBuilder()
				.append(this.id, entity.id)
				.append(question, entity.question)
				.append(type, entity.type)
				.isEquals();
	}
	
	@Override
	public String toString() {
		return "QuestionEntity [getQuestion()=" + getQuestion()
				+ ", getType()=" + getType() + ", getId()=" + getId()
				+ ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + "]";
	}

}
