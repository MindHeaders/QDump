package org.dataart.qdump.entities.questionnaire;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.serializer.PersonEntitySerializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@MappedSuperclass
public abstract class QuestionnaireBaseEntity extends BaseEntity implements
		Serializable {
	private static final long serialVersionUID = -1310872166991256747L;
	@JsonSerialize(using = PersonEntitySerializer.class)
	@JsonProperty("created_by")
	private PersonEntity createdBy;
	@JsonSerialize(using = PersonEntitySerializer.class)
	@JsonProperty("modified_by")
	private PersonEntity modifiedBy;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", referencedColumnName = "id_person", nullable = true, updatable = false)
	public PersonEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(PersonEntity createdBy) {
		this.createdBy = createdBy;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "modified_by", referencedColumnName = "id_person", nullable = true, updatable = true)
	public PersonEntity getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(PersonEntity modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
}
