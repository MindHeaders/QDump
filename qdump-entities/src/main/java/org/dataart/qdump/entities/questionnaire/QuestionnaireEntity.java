package org.dataart.qdump.entities.questionnaire;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.dataart.qdump.entities.serializer.View;
import org.dataart.qdump.entities.utils.QdumpJsonNaming;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "questionnaires")
@AttributeOverrides(value = {
		@AttributeOverride(name = "id", column = @Column(name = "id_questionnaire", insertable = false, updatable = false)),
		@AttributeOverride(name = "created_by", column = @Column(name = "created_by", insertable = false, updatable = false, nullable = false)) })
@JsonAutoDetect
@NamedQueries({
        @NamedQuery(name = "QuestionnaireEntity.findPublishedQuestionnaires", query = "SELECT NEW org.dataart.qdump.entities.questionnaire.QuestionnaireEntity(q.id, q.name, q.description) FROM QuestionnaireEntity q WHERE q.published = true"),
        @NamedQuery(name = "QuestionnaireEntity.countPublishedQuestionnaires", query = "SELECT count(q) FROM QuestionnaireEntity q WHERE q.published = true"),
        @NamedQuery(name = "QuestionnaireEntity.findPublishedQuestionnairesById", query = "SELECT NEW org.dataart.qdump.entities.questionnaire.QuestionnaireEntity(q.id, q.name, q.description) FROM QuestionnaireEntity q WHERE q.published = true AND q.id = ?1"),
        @NamedQuery(name = "QuestionnaireEntity.findQuestionnaireById", query = "FROM QuestionnaireEntity q " +
                "WHERE q.id = ?1")
})
@JsonNaming(value = QdumpJsonNaming.class)
public class QuestionnaireEntity extends QuestionnaireBaseEntity implements
		Serializable {
	private static final long serialVersionUID = 8952388499186170808L;
	private String name;
	private String description;
    @JsonView(View.Create.class)
	private boolean published;
    @JsonProperty("question_entities")
	private List<QuestionEntity> questionEntities;

    public QuestionnaireEntity(){
        super();
    }
    public QuestionnaireEntity(Long id, String name, String description) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public QuestionnaireEntity(Long id, String name, String description, Date createdDate, Date modifiedDate ) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    @Column(name = "name", nullable = false, length = 250)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "description", length = 500)
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "id_questionnaire", referencedColumnName = "id_questionnaire", nullable = false)
	public List<QuestionEntity> getQuestionEntities() {
		return questionEntities;
	}

	public void setQuestionEntities(List<QuestionEntity> questionEntities) {
		this.questionEntities = questionEntities;
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
