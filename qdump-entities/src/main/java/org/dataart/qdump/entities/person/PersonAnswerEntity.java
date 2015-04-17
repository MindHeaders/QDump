package org.dataart.qdump.entities.person;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.serializer.AnswerPersonSerializer;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "person_answers")
@AttributeOverride(name = "id", column = @Column(name = "id_person_answer", insertable = false, updatable = false))
@JsonAutoDetect
@JsonIgnoreProperties({"createdDate", "modifiedDate"})
public class PersonAnswerEntity extends BaseEntity implements
		Serializable {
	private static final long serialVersionUID = 5266384349299279727L;
    @JsonProperty("answer_entity")
	@JsonSerialize(using = AnswerPersonSerializer.class)
	private AnswerEntity answerEntity;
	@JsonProperty("person_answer")
	private String personAnswer;
	private boolean marked;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_answer")
	public AnswerEntity getAnswerEntity() {
		return answerEntity;
	}
	public void setAnswerEntity(AnswerEntity answerEntity) {
		this.answerEntity = answerEntity;
	}
	@Column(name = "person_answer", length = 500)
	public String getPersonAnswer() {
		return personAnswer;
	}
	public void setPersonAnswer(String personAnswer) {
		this.personAnswer = personAnswer;
	}
	@Column(name = "marked", columnDefinition = "BIT(1) DEFAULT 0")
	public boolean isMarked() {
		return marked;
	}
	public void setMarked(boolean marked) {
		this.marked = marked;
	}

    @Override
	public String toString() {
		return "PersonAnswerEntity [ getId()=" + getId()
				+ ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + "]";
	}

}
