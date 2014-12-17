package org.dataart.qdump.entities.person;

import java.io.Serializable;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.serializer.PersonEntitySerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@MappedSuperclass
public abstract class PersonQuestionnaireBaseEntity extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 7666829487657683004L;
	@JsonSerialize(using = PersonEntitySerializer.class)
	private PersonEntity ownBy;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ownBy", referencedColumnName = "id_person", updatable = false)
	public PersonEntity getOwnBy() {
		return ownBy;
	}

	public void setOwnBy(PersonEntity ownBy) {
		this.ownBy = ownBy;
	}
}
