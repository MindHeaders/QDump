package org.dataart.qdump.entities.questionnaire;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "answers")
@AttributeOverrides(value = { 
		@AttributeOverride(name = "id", column = @Column(name = "id_answer", insertable = false, updatable = false))})
@JsonAutoDetect
@JsonIgnoreProperties({"createdDate", "modifiedDate"})
@NamedQueries({
		@NamedQuery(name = "AnswerEntity.getAnswerByQuestionId", query = "FROM AnswerEntity a "
				+ "WHERE a.questionEntity.id = ?1"),
		@NamedQuery(name = "AnswerEntity.getAnswerByQuestionnaireId", query = "FROM AnswerEntity a  "
				+ "WHERE a.questionEntity.questionnaireEntity.id = ?1") })
public class AnswerEntity extends BaseEntity implements Serializable{
	private static final long serialVersionUID = -5973094404031746982L;
	private String answer;
	private boolean correct;
	@JsonBackReference
	private QuestionEntity questionEntity;

	@Column(name = "answer", length = 500)
	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	@Column(name = "correct", columnDefinition = "BIT(1) DEFAULT 0")
	public boolean isCorrect() {
		return correct;
	}

	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = QuestionEntity.class)
	@JoinColumn(name = "id_question", referencedColumnName = "id_question")
	public QuestionEntity getQuestionEntity() {
		return questionEntity;
	}

	public void setQuestionEntity(QuestionEntity questionEntity) {
		this.questionEntity = questionEntity;
	}
	
	public void addQuestionEntity(QuestionEntity questionEntity) {
		this.questionEntity = questionEntity;
		if(!questionEntity.getAnswerEntities().contains(this)) {
			questionEntity.getAnswerEntities().add(this);
		}
	}
	
	public void updateAnswerEntity(AnswerEntity entity) {
		if(this.getAnswer() != entity.getAnswer() && entity.getAnswer() != null) {
			this.setAnswer(entity.getAnswer());
		}
	}

	@Override
	public String toString() {
		return "AnswerEntity [getAnswer()=" + getAnswer() + ", isCorrect()="
				+ isCorrect() + ", getId()=" + getId() + ", getCreatedBy()="
				+ ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + "]";
	}
}

