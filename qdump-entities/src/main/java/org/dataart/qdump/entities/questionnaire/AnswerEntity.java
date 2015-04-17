package org.dataart.qdump.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonView;
import org.dataart.qdump.entities.serializer.View;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "answers")
@AttributeOverrides(value = { 
		@AttributeOverride(name = "id", column = @Column(name = "id_answer", insertable = false, updatable = false))})
@JsonAutoDetect
public class AnswerEntity extends BaseEntity implements Serializable{
	private static final long serialVersionUID = -5973094404031746982L;
	private String answer;
    @JsonView(View.Admin.class)
	private boolean correct;

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

	@Override
	public String toString() {
		return "AnswerEntity [getAnswer()=" + getAnswer() + ", isCorrect()="
				+ isCorrect() + ", getId()=" + getId() + ", getCreatedBy()="
				+ ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + "]";
	}

}

