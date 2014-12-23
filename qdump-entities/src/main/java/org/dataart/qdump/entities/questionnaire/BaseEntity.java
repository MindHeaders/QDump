package org.dataart.qdump.entities.questionnaire;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = -1310872166991256747L;
	protected long id;
	private Date createdDate;
	private Date modifiedDate;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "created_date", nullable = false, updatable = false)
	public Date getCreatedDate() {
		return createdDate;
	}

	@JsonIgnore
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "modified_date", nullable = true)
	public Date getModifiedDate() {
		return modifiedDate;
	}

	@JsonIgnore
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@PrePersist
	public void putCreatedDate() {
		this.createdDate = new Date();
	}
	
	@PreUpdate
	public void putModifiedDate() {
		this.modifiedDate = new Date();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(id).append(createdDate)
				.append(modifiedDate).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(obj == this) {
			return true;
		}
		if(!(obj instanceof BaseEntity)) {
			return false;
		}
		BaseEntity entity = (BaseEntity) obj;
		return new EqualsBuilder().append(id, entity.id)
				.append(createdDate, entity.createdDate)
				.append(modifiedDate, entity.modifiedDate).isEquals();
	}
	
	public boolean entitiesIsEquals(Object obj) {
		return true;
	}
	
	public void updateEntity(Object obj) {}
}
