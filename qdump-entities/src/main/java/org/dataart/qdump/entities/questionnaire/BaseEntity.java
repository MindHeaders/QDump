package org.dataart.qdump.entities.questionnaire;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.dataart.qdump.entities.converters.LocalDateTimePersistenceConverter;
import org.dataart.qdump.entities.serializer.LocalDateTimeDeserializer;
import org.dataart.qdump.entities.serializer.LocalDateTimeSerializer;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = -1310872166991256747L;
	protected long id;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	protected LocalDateTime createdDate;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
	protected LocalDateTime modifiedDate;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

    @Column(name = "created_date", nullable = false, updatable = false)
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

	@Column(name = "modified_date", nullable = true)
	public LocalDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(LocalDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@PrePersist
	public void putCreatedDate() {
        this.createdDate = LocalDateTime.now();
	}
	
	@PreUpdate
	public void putModifiedDate() {
		this.modifiedDate = LocalDateTime.now();
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
}
