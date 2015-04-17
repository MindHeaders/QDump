package org.dataart.qdump.entities.statistics;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.dataart.qdump.entities.person.PersonEntity;

import java.util.List;

/**
 * Created by artemvlasov on 07/04/15.
 */
@JsonAutoDetect
public class PersonEntitiesStatistic {
    private long registeredPersonEntities;
    private long enabledPersonEntities;
    private long maleRegisteredPersonEntities;
    private long femaleRegisteredPersonEntities;
    private PersonEntity mostActivePersonEntityInPassingQuestionnaireEntities;
    private PersonEntity mostActivePersonEntityInCreatingQuestionnaireEntities;
    private List<PersonEntity> top10ActivePersonEntities;

    public long getRegisteredPersonEntities() {
        return registeredPersonEntities;
    }

    public void setRegisteredPersonEntities(long registeredPersonEntities) {
        this.registeredPersonEntities = registeredPersonEntities;
    }

    public long getEnabledPersonEntities() {
        return enabledPersonEntities;
    }

    public void setEnabledPersonEntities(long enabledPersonEntities) {
        this.enabledPersonEntities = enabledPersonEntities;
    }

    public long getMaleRegisteredPersonEntities() {
        return maleRegisteredPersonEntities;
    }

    public void setMaleRegisteredPersonEntities(long maleRegisteredPersonEntities) {
        this.maleRegisteredPersonEntities = maleRegisteredPersonEntities;
    }

    public long getFemaleRegisteredPersonEntities() {
        return femaleRegisteredPersonEntities;
    }

    public void setFemaleRegisteredPersonEntities(long femaleRegisteredPersonEntities) {
        this.femaleRegisteredPersonEntities = femaleRegisteredPersonEntities;
    }

    public PersonEntity getMostActivePersonEntityInPassingQuestionnaireEntities() {
        return mostActivePersonEntityInPassingQuestionnaireEntities;
    }

    public void setMostActivePersonEntityInPassingQuestionnaireEntities(PersonEntity mostActivePersonEntityInPassingQuestionnaireEntities) {
        this.mostActivePersonEntityInPassingQuestionnaireEntities = mostActivePersonEntityInPassingQuestionnaireEntities;
    }

    public PersonEntity getMostActivePersonEntityInCreatingQuestionnaireEntities() {
        return mostActivePersonEntityInCreatingQuestionnaireEntities;
    }

    public void setMostActivePersonEntityInCreatingQuestionnaireEntities(PersonEntity mostActivePersonEntityInCreatingQuestionnaireEntities) {
        this.mostActivePersonEntityInCreatingQuestionnaireEntities = mostActivePersonEntityInCreatingQuestionnaireEntities;
    }

    public List<PersonEntity> getTop10ActivePersonEntities() {
        return top10ActivePersonEntities;
    }

    public void setTop10ActivePersonEntities(List<PersonEntity> top10ActivePersonEntities) {
        this.top10ActivePersonEntities = top10ActivePersonEntities;
    }
}
