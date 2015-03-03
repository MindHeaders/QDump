package org.dataart.qdump.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.dataart.qdump.entities.helper.EntitiesUpdater;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "answers")
@AttributeOverrides(value = { 
		@AttributeOverride(name = "id", column = @Column(name = "id_answer", insertable = false, updatable = false))})
@JsonAutoDetect
@JsonIgnoreProperties({"createdDate", "modifiedDate", "answer_id"})
public class AnswerEntity extends BaseEntity implements Serializable{
	private static final long serialVersionUID = -5973094404031746982L;
	private String answer;
	private boolean correct;
	@JsonBackReference
	private QuestionEntity questionEntity;

	@Column(name = "answer", length = 100)
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
	
	public void updateEntity(Object obj) {
		AnswerEntity entity = (AnswerEntity) obj;
		List<String> ignoredFields = Arrays.asList("questionEntity");
		EntitiesUpdater.updateEntity(entity, this, ignoredFields, AnswerEntity.class);
	}
	
	public boolean entitiesIsEquals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		AnswerEntity entity = (AnswerEntity) obj;
		return new EqualsBuilder()
				.append(this.id, entity.id)
				.append(answer, entity.answer)
				.append(correct, entity.correct)
				.isEquals();
	}

	@Override
	public String toString() {
		return "AnswerEntity [getAnswer()=" + getAnswer() + ", isCorrect()="
				+ isCorrect() + ", getId()=" + getId() + ", getCreatedBy()="
				+ ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + "]";
	}

}

