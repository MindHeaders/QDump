package org.dataart.qdump.entities.statistics;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Created by artemvlasov on 07/04/15.
 */
@JsonAutoDetect
public class PersonQuestionnaireEntitiesStatistic {
    private long totalPersonQuestionnaireEntities;
    private long startedPersonQuestionnaireEntities;
    private long completedPersonQuestionnaireEntities;

    public long getTotalPersonQuestionnaireEntities() {
        return totalPersonQuestionnaireEntities;
    }

    public void setTotalPersonQuestionnaireEntities(long totalPersonQuestionnaireEntities) {
        this.totalPersonQuestionnaireEntities = totalPersonQuestionnaireEntities;
    }

    public long getStartedPersonQuestionnaireEntities() {
        return startedPersonQuestionnaireEntities;
    }

    public void setStartedPersonQuestionnaireEntities(long startedPersonQuestionnaireEntities) {
        this.startedPersonQuestionnaireEntities = startedPersonQuestionnaireEntities;
    }

    public long getCompletedPersonQuestionnaireEntities() {
        return completedPersonQuestionnaireEntities;
    }

    public void setCompletedPersonQuestionnaireEntities(long completedPersonQuestionnaireEntities) {
        this.completedPersonQuestionnaireEntities = completedPersonQuestionnaireEntities;
    }
}
