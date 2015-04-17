package org.dataart.qdump.entities.statistics;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.serializer.LocalDateTimeDeserializer;
import org.dataart.qdump.entities.serializer.LocalDateTimeSerializer;

import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 07/04/15.
 */
@JsonAutoDetect
public class QuestionnaireEntitiesStatistic {
    private long totalQuestionnaireEntities;
    private long publishedQuestionnaireEntities;
    private QuestionnaireEntity popularQuestionnaireEntity;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateOfLastCreatedQuestionnaireEntity;

    public long getTotalQuestionnaireEntities() {
        return totalQuestionnaireEntities;
    }

    public void setTotalQuestionnaireEntities(long totalQuestionnaireEntities) {
        this.totalQuestionnaireEntities = totalQuestionnaireEntities;
    }

    public long getPublishedQuestionnaireEntities() {
        return publishedQuestionnaireEntities;
    }

    public void setPublishedQuestionnaireEntities(long publishedQuestionnaireEntities) {
        this.publishedQuestionnaireEntities = publishedQuestionnaireEntities;
    }

    public QuestionnaireEntity getPopularQuestionnaireEntity() {
        return popularQuestionnaireEntity;
    }

    public void setPopularQuestionnaireEntity(QuestionnaireEntity popularQuestionnaireEntity) {
        this.popularQuestionnaireEntity = popularQuestionnaireEntity;
    }

    public LocalDateTime getDateOfLastCreatedQuestionnaireEntity() {
        return dateOfLastCreatedQuestionnaireEntity;
    }

    public void setDateOfLastCreatedQuestionnaireEntity(LocalDateTime dateOfLastCreatedQuestionnaireEntity) {
        this.dateOfLastCreatedQuestionnaireEntity = dateOfLastCreatedQuestionnaireEntity;
    }
}
