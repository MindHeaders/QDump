package org.dataart.qdump.entities.person;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.serializer.QuestionnairePersonSerializer;
import org.dataart.qdump.entities.serializer.View;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "person_questionnaires")
@AttributeOverride(name = "id", column = @Column(name = "id_person_questionnaire", insertable = false, updatable = false))
@JsonAutoDetect
@NamedQueries({
        @NamedQuery(name = "PersonQuestionnaireEntity.findByPersonQuestionnaireIdAndPersonId",
                query = "SELECT pq FROM PersonQuestionnaireEntity pq, PersonEntity p " +
                        "WHERE pq.id = ?1 AND p.id = ?2"),
        @NamedQuery(name = "PersonQuestionnaireEntity.countCompletedByPersonId",
                query = "SELECT count(pq) FROM PersonQuestionnaireEntity pq, PersonEntity p WHERE p.id = ?1 AND pq.status NOT IN ('in progress', 'not specified')"),
        @NamedQuery(name = "PersonQuestionnaireEntity.countStartedByPersonId", query = "SELECT " +
                "COUNT(pq) FROM PersonQuestionnaireEntity pq, PersonEntity p WHERE p.id = ?1 AND pq.status = 'in " +
                "progress'"),
        @NamedQuery(name = "PersonQuestionnaireEntity.findByPersonQuestionId",
                query = "SELECT pq FROM PersonQuestionnaireEntity pq, PersonQuestionEntity pqe " +
                        "WHERE pqe MEMBER OF pq.personQuestionEntities AND pqe.id = ?1"),
        @NamedQuery(name = "PersonQuestionnaireEntity.countStartedQuestionnaires",
                query = "SELECT COUNT(pq) FROM PersonQuestionnaireEntity pq WHERE pq.status = 'in progress'"),
        @NamedQuery(name = "PersonQuestionnaireEntity.countCompletedQuestionnaires",
                query = "SELECT COUNT(pq) FROM PersonQuestionnaireEntity pq WHERE pq.status NOT IN ('in progress', 'not specified')")
})
public class PersonQuestionnaireEntity extends BaseEntity
		implements Serializable {
	private static final long serialVersionUID = 586942138808550795L;
    @JsonProperty("questionnaire_entity")
	@JsonSerialize(using = QuestionnairePersonSerializer.class)
	private QuestionnaireEntity questionnaireEntity;
	private String status;
	@JsonProperty("person_question_entities")
    @JsonView(View.User.class)
	private List<PersonQuestionEntity> personQuestionEntities;

    @OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_questionnaire", referencedColumnName = "id_questionnaire")
	public QuestionnaireEntity getQuestionnaireEntity() {
		return questionnaireEntity;
	}

	public void setQuestionnaireEntity(QuestionnaireEntity questionnaireEntity) {
		this.questionnaireEntity = questionnaireEntity;
	}

	@Column(name = "status", columnDefinition = "VARCHAR(100) DEFAULT 'not specified'")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "id_person_questionnaire", referencedColumnName = "id_person_questionnaire")
	public List<PersonQuestionEntity> getPersonQuestionEntities() {
		return personQuestionEntities;
	}

	public void setPersonQuestionEntities(
			List<PersonQuestionEntity> personQuestionEntities) {
		this.personQuestionEntities = personQuestionEntities;
	}
	
	@Override
	public String toString() {
		return "PersonQuestionnaireEntity [ getStatus()=" + getStatus()
				+ ", getId()=" + getId() + ", getCreatedDate()="
				+ getCreatedDate() + ", getModifiedDate()=" + getModifiedDate()
				+ "]";
	}
}
