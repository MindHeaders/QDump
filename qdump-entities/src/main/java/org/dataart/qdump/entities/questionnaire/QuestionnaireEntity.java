package org.dataart.qdump.entities.questionnaire;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.dataart.qdump.entities.helper.EntitiesUpdater;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "questionnaires")
@AttributeOverrides(value = {
		@AttributeOverride(name = "id", column = @Column(name = "id_questionnaire", insertable = false, updatable = false)),
		@AttributeOverride(name = "created_by", column = @Column(name = "created_by", insertable = false, updatable = false, nullable = false)) })
@JsonAutoDetect
public class QuestionnaireEntity extends QuestionnaireBaseEntity implements
		Serializable {
	private static final long serialVersionUID = 8952388499186170808L;
	private String name;
	private String description;
	private boolean published;
	@JsonProperty("question_entities")
	private List<QuestionEntity> questionEntities;

	@Column(name = "name", nullable = false, length = 1000)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 4500)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "published", columnDefinition = "BIT(1) DEFAULT 0")
	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "questionnaireEntity", fetch = FetchType.EAGER, orphanRemoval = true, targetEntity = QuestionEntity.class)
	public List<QuestionEntity> getQuestionEntities() {
		return questionEntities;
	}

	public void setQuestionEntities(List<QuestionEntity> questionEntities) {
		this.questionEntities = questionEntities;
	}
	
	/**
	 * Update {@link QuestionEntity} that is associated with this {@link QuestionnaireEntity}. 
	 * This update is performed before persist current object to dataBase and before update.
	 * This update is needed for One To Many associations.
	 */
	@PrePersist
	@PreUpdate
	public void setQuestionEntities() {
		if (questionEntities != null) {
			questionEntities.stream().forEach(
					entity -> entity.addQuestionnaireEntity(this));
		}
	}
	
	public boolean checkIdForCreation() {
		if(id > 0) {
			return false;
		}
		for(QuestionEntity entity : questionEntities) {
			if(entity.id > 0 || !entity.checkIdForCreation()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean entitiesIsEquals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		QuestionnaireEntity entity = (QuestionnaireEntity) obj;
		return new EqualsBuilder()
				.append(this.id, entity.id)
				.append(name, entity.name)
				.append(description, entity.description)
				.append(published, entity.published)
				.isEquals();
	}
	
	public void updateEntity(Object obj) {
		QuestionnaireEntity entity = (QuestionnaireEntity) obj;
		List<String> ignoredFields = Arrays.asList("questionEntities");
		EntitiesUpdater.updateEntity(entity, this, ignoredFields, QuestionnaireEntity.class);
		EntitiesUpdater.updateEntities(entity.questionEntities, questionEntities, QuestionEntity.class);
	}

	@Override
	public String toString() {
		return "QuestionnaireEntity [getName()=" + getName()
				+ ", getDescription()=" + getDescription() + ", isPublished()="
				+ isPublished() + ", getId()=" + getId()
				+ ", getCreatedDate()=" + getCreatedDate()
				+ ", getModifiedDate()=" + getModifiedDate() + "]";
	}

}
