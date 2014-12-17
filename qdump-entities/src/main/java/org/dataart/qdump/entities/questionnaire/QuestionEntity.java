package org.dataart.qdump.entities.questionnaire;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.dataart.qdump.entities.enums.QuestionTypeEnums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "questions")
@AttributeOverride(name = "id", column = @Column(name = "id_question", insertable = false, updatable = false))
@JsonAutoDetect
@JsonIgnoreProperties({ "createdDate", "modifiedDate" })
@NamedQueries({
		@NamedQuery(name = "QuestionEntity.getQuestionByName", query = "FROM QuestionEntity q "
				+ "WHERE q.question = ?1"),
		@NamedQuery(name = "QuestionEntity.getQuestionByQuestionnaireId", query = "FROM QuestionEntity q  "
				+ "WHERE q.questionnaireEntity.id = ?1") })
public class QuestionEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7827573669263895832L;
	private String question;
	@JsonProperty("question_type")
	private QuestionTypeEnums type;
	@JsonProperty("answer_entities")
	private List<AnswerEntity> answerEntities;
	@JsonBackReference
	private QuestionnaireEntity questionnaireEntity;

	@Column(name = "question", nullable = false, length = 1500)
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "questionEntity", fetch = FetchType.LAZY, targetEntity = AnswerEntity.class)
	public List<AnswerEntity> getAnswerEntities() {
		return answerEntities;
	}

	public void setAnswerEntities(List<AnswerEntity> answerEntities) {
		this.answerEntities = answerEntities;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = QuestionnaireEntity.class)
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
	
	/**
	 * Update {@link QuestionEntity} for update current object. This method will
	 * update all fields from source {@link QuestionEntity}. This fields cannot
	 * be updated in database.
	 * 
	 * @param entity
	 */
	public void updateQuestionEntity(QuestionEntity entity) {
		Comparator<AnswerEntity> comparator = new Comparator<AnswerEntity>() {
			@Override
			public int compare(AnswerEntity o1, AnswerEntity o2) {
				long answerId1 = o1.getId();
				long answerId2 = o2.getId();
				return (int) (answerId1 - answerId2);
			}
		};
		if (this.getQuestion() != entity.getQuestion()
				&& entity.getQuestion() != null) {
			this.setQuestion(entity.getQuestion());
		}
		if (this.getType() != entity.getType() && entity.getType() != null) {
			this.setType(entity.getType());
		}
		if (this.answerEntities != null && entity.answerEntities != null) {
			this.answerEntities.sort(comparator);
			entity.answerEntities.sort(comparator);
			if (this.answerEntities.size() != entity.answerEntities.size()) {
				throw new RuntimeException(
						"Target Answer Entities size is not equals to Source Answer Entities");
			} else {
				for (int i = 0; i < answerEntities.size(); i++) {
					if (entity.answerEntities.get(i).getId() == this.answerEntities
							.get(i).getId()) {
						this.answerEntities.get(i).updateAnswerEntity(
								entity.answerEntities.get(i));
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		return "QuestionEntity [getQuestion()=" + getQuestion()
				+ ", getType()=" + getType() + ", getId()=" + getId()
				+ ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + "]";
	}

}
