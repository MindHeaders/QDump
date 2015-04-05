package org.dataart.qdump.entities.person;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.serializer.QuestionPersonSerializer;

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
@Table(name = "person_questions")
@AttributeOverride(name = "id", column = @Column(name = "id_person_question", insertable = false, updatable = false))
@JsonAutoDetect
@JsonIgnoreProperties({"createdDate", "modifiedDate"})
@NamedQueries({
        @NamedQuery(name = "PersonQuestionEntity.findByPersonId",
                query = "select pqu from PersonQuestionEntity pqu, PersonQuestionnaireEntity pq, PersonEntity p " +
                        "where pqu member of pq.personQuestionEntities " +
                        "and pq member of p.personQuestionnaireEntities " +
                        "and pqu.questionEntity.type = 'TEXTAREA' " +
                        "and p.id = ?1"),
        @NamedQuery(name = "PersonQuestionEntity.countNotCheckedQuestions",
                query = "SELECT COUNT(pqe) FROM PersonQuestionEntity pqe " +
                        "WHERE pqe.questionEntity.type = 'TEXTAREA' AND pqe.checked = false")
})
public class PersonQuestionEntity extends BaseEntity implements
        Serializable {
    private static final long serialVersionUID = -6691017410211190245L;
    @JsonProperty("question_entity")
    @JsonSerialize(using = QuestionPersonSerializer.class)
    private QuestionEntity questionEntity;
    @JsonProperty("correct")
    private boolean correct;
    @JsonProperty("person_answer_entities")
    private List<PersonAnswerEntity> personAnswerEntities;
    @JsonIgnore
    private boolean checked;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_question", referencedColumnName = "id_question")
    public QuestionEntity getQuestionEntity() {
        return questionEntity;
    }
    public void setQuestionEntity(QuestionEntity questionEntity) {
        this.questionEntity = questionEntity;
    }
    @Column(name = "correct", columnDefinition = "BIT(1) DEFAULT 0")
    public boolean isCorrect() {
        return correct;
    }
    public void setCorrect(boolean isCorrect) {
        this.correct = isCorrect;
    }
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "id_person_question", referencedColumnName = "id_person_question")
    public List<PersonAnswerEntity> getPersonAnswerEntities() {
        return personAnswerEntities;
    }
    public void setPersonAnswerEntities(
            List<PersonAnswerEntity> personAnswerEntities) {
        this.personAnswerEntities = personAnswerEntities;
    }
    public boolean isChecked() {
        return checked;
    }
    public void setChecked(boolean checked) {
        this.checked = checked;
    }
    @Override
    public String toString() {
        return "PersonQuestionEntity [isCorrect()=" + isCorrect()
                + ", getId()=" + getId() + ", getCreatedDate()="
                + getCreatedDate() + ", getModifiedDate()=" + getModifiedDate()
                + "]";
    }


}
