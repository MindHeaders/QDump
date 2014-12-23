package org.dataart.qdump.entities.person;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.dataart.qdump.entities.helper.EntitiesUpdater;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.serializer.AnswerPersonSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "person_answers")
@AttributeOverride(name = "id", column = @Column(name = "id_person_answer", insertable = false, updatable = false))
@JsonAutoDetect
@JsonIgnoreProperties({"createdDate", "modifiedDate"})
public class PersonAnswerEntity extends BaseEntity implements
		Serializable {
	private static final long serialVersionUID = 5266384349299279727L;
	@JsonSerialize(using = AnswerPersonSerializer.class)
	private AnswerEntity answerEntity;
	@JsonProperty("person_answer")
	private String personAnswer;
	private boolean marked;
	@JsonBackReference
	private PersonQuestionEntity personQuestionEntity;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_person_question", referencedColumnName = "id_person_question")
	public PersonQuestionEntity getPersonQuestionEntity() {
		return personQuestionEntity;
	}

	public void setPersonQuestionEntity(
			PersonQuestionEntity personQuestionEntity) {
		this.personQuestionEntity = personQuestionEntity;
	}
	
	public void addPersonQuestionEntity(PersonQuestionEntity personQuestionEntity) {
		this.personQuestionEntity = personQuestionEntity;
		if(!personQuestionEntity.getPersonAnswerEntities().contains(this)) {
			personQuestionEntity.getPersonAnswerEntities().add(this);
		}
	}

	public boolean checkIdForCreation() {
		if(id != 0 || answerEntity.getId() == 0) {
			return false;
		}
		return true;
	}
	
	public void updateEntity(Object obj) {
		PersonAnswerEntity entity = (PersonAnswerEntity) obj;
		List<String> ignoredFields = Arrays.asList("personQuestionEntity", "answerEntity");
		EntitiesUpdater.updateEntity(entity, this, ignoredFields, PersonAnswerEntity.class);
	}
	
	public boolean entitiesIsEquals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		PersonAnswerEntity entity = (PersonAnswerEntity) obj;
		return new EqualsBuilder()
				.append(this.id, entity.id)
				.append(personAnswer, entity.personAnswer)
				.append(marked, entity.marked)
				.isEquals();
	}

	@Override
	public String toString() {
		return "PersonAnswerEntity [ getId()=" + getId()
				+ ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + "]";
	}

}
