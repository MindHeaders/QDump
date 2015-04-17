package org.dataart.qdump.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.serializer.PersonEntitySerializer;
import org.dataart.qdump.entities.serializer.View;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.io.Serializable;

@MappedSuperclass
public abstract class QuestionnaireBaseEntity extends BaseEntity implements
		Serializable {
	private static final long serialVersionUID = -1310872166991256747L;
	@JsonSerialize(using = PersonEntitySerializer.class)
	private PersonEntity createdBy;
	@JsonSerialize(using = PersonEntitySerializer.class)
	private PersonEntity modifiedBy;

    public QuestionnaireBaseEntity() {
        super();
    }

    @OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "created_by", referencedColumnName = "id_person", nullable = true, updatable = false)
	public PersonEntity getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(PersonEntity createdBy) {
		this.createdBy = createdBy;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "modified_by", referencedColumnName = "id_person", nullable = true, updatable = true)
	public PersonEntity getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(PersonEntity modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17,31)
			.appendSuper(super.hashCode())
			.append(createdBy)
			.append(modifiedBy)
			.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof QuestionnaireBaseEntity)) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		QuestionnaireBaseEntity entity = (QuestionnaireBaseEntity) obj;
		return new EqualsBuilder()
			.appendSuper(super.equals(obj))
			.append(createdBy, entity.createdBy)
			.append(modifiedBy, entity.modifiedBy)
			.isEquals();
	}

	
}
