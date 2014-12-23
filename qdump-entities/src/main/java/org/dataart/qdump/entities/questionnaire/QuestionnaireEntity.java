package org.dataart.qdump.entities.questionnaire;

import java.io.Serializable;
import java.util.Comparator;
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

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	
	/**
	 * Update existing {@link QuestionnaireEntity} object in database
	 * Field published should be update separately
	 */
	public void updateQuestionnaireEntity(QuestionnaireEntity entity) {
		Comparator<QuestionEntity> comparator = new Comparator<QuestionEntity>() {
			@Override
			public int compare(QuestionEntity o1, QuestionEntity o2) {
				long questionId1 = o1.getId();
				long questionId2 = o2.getId();
				return (int) (questionId1 - questionId2);
			}
		};
		if (entity.getDescription() != this.getDescription()
				&& entity.getDescription() != null) {
			this.setDescription(entity.getDescription());
		}
		if (entity.getName() != this.getName() && entity.getName() != null) {
			this.setName(entity.getName());
		}
		if (entity.getModifiedBy() != null) {
			this.setModifiedBy(entity.getModifiedBy());
		}
		if (this.questionEntities != null && entity.questionEntities != null) {
			this.questionEntities.sort(comparator);
			entity.questionEntities.sort(comparator);
			if (this.questionEntities.size() != entity.questionEntities.size()) {
				throw new RuntimeException(
						"Target Answer Entities size is not equals to Source Answer Entities");
			} else {
				for (int i = 0; i < this.questionEntities.size(); i++) {
					if (entity.questionEntities.get(i).getId() == this.questionEntities
							.get(i).getId()) {
						this.questionEntities.get(i).updateQuestionEntity(
								entity.questionEntities.get(i));
					}
				}
			}
		}
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
