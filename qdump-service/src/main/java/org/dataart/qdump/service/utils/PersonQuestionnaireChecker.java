package org.dataart.qdump.service.utils;

import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.enums.QuestionnaireStatusEnums;
import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;

import javax.ws.rs.core.Response.Status;
import java.util.List;

import static org.dataart.qdump.service.utils.WebApplicationUtils.exceptionCreator;




/**
 * Created by artemvlasov on 26/03/15.
 */
public class PersonQuestionnaireChecker {
    public static boolean checkPrePersist(PersonQuestionnaireEntity personQuestionnaireEntity) {
        if(personQuestionnaireEntity == null) {
            exceptionCreator(Status.FORBIDDEN, "Person questionnaire could not be null");
        } else if(checkDataPrePersist(personQuestionnaireEntity, personQuestionnaireEntity.getQuestionnaireEntity(),
                "Person Questionnaire"
                )) {
            if(checkCollectionDataPrePersist(personQuestionnaireEntity.getPersonQuestionEntities(),
                    personQuestionnaireEntity
                    .getQuestionnaireEntity().getQuestionEntities())) {
                return personQuestionnaireEntity.getPersonQuestionEntities().stream().allMatch
                        (question -> PersonQuestionChecker.checkPrePersist(question));
            }
            return true;
        }
        return false;
    }


    private static <T extends BaseEntity> boolean checkDataPrePersist(T personQuestionnaireData, T questionnaireData, String questionnaireDataName) {
        if(questionnaireData == null) {
            exceptionCreator(Status.FORBIDDEN, String.format("%s could not be null", questionnaireDataName));
        } else if(personQuestionnaireData.getId() > 0 || questionnaireData.getId() <= 0) {
            exceptionCreator(Status
                    .FORBIDDEN, String.format("Incorrect persisted data. " +
                            "Persisted %s should not contains id greater than 0 and " +
                            "%s id should not be equals to 0.", personQuestionnaireData.getClass().getSimpleName(),
                    questionnaireData.getClass().getSimpleName()));
        }
        return true;
    }

    private static boolean checkCollectionDataPrePersist(List<?> personsQuestionnairesData, List<?>
            questionnairesData) {
        if (personsQuestionnairesData != null && questionnairesData != null) {
            if (personsQuestionnairesData.size() != questionnairesData.size()) {
                exceptionCreator(Status
                        .FORBIDDEN, "Count of questionnaires data and person questionnaires data should be equals");
            }
            return true;
        }
        return false;
    }
    /**
     * Check status of {@link PersonQuestionnaireEntity}, should be used after
     * persistence {@link PersonQuestionnaireEntity}. If all
     * {@link PersonQuestionEntity} in current object is correct, status of
     * {@link PersonQuestionnaireEntity} should be set as "Passed". If all
     * {@link PersonQuestionEntity} is not correct status should be set as
     * "Not Passed". If any of {@link PersonQuestionEntity} is has
     * {@link org.dataart.qdump.entities.enums.QuestionTypeEnums} set as Field and any of
     * {@link PersonQuestionEntity} is not correct status should be set as
     * "In Checking Process". Be default all started
     * {@link PersonQuestionnaireEntity} should have status "In Progress"
     */
    public static void checkStatus(PersonQuestionnaireEntity entity, QuestionnaireEntity questionnaireEntity) {
        if(entity != null && questionnaireEntity != null && entity.getPersonQuestionEntities() != null &&
                questionnaireEntity.getQuestionEntities() != null) {
            boolean checkPersonQuestionEntitiesIsCorrect = personQuestionEntitiesIsCorrect(entity
                    .getPersonQuestionEntities(), questionnaireEntity.getQuestionEntities());
            boolean checkQuestionsType = checkQuestionsType(entity);
            if (checkPersonQuestionEntitiesIsCorrect) {
                entity.setStatus(QuestionnaireStatusEnums.PASSED.getName());
            } else if (!checkQuestionsType
                    && !checkPersonQuestionEntitiesIsCorrect) {
                entity.setStatus(QuestionnaireStatusEnums.IN_CHECKING_PROCESS.getName());
            } else if (!checkPersonQuestionEntitiesIsCorrect) {
                entity.setStatus(QuestionnaireStatusEnums.NOT_PASSED.getName());
            } else {
                entity.setStatus(QuestionnaireStatusEnums.IN_PROGRESS.getName());
            }
        }
    }

    private static boolean personQuestionEntitiesIsCorrect(List<PersonQuestionEntity> personQuestionEntities,
                                                           List<QuestionEntity> questionEntities) {
        for(int i = 0; i < personQuestionEntities.size(); i++) {
            personQuestionEntities.get(i).setCorrect(PersonQuestionChecker.IsCorrect(personQuestionEntities.get(i),
                    questionEntities
                            .get(i)));
            if((!personQuestionEntities.get(i).isCorrect() && personQuestionEntities.get(i).isChecked()) ||
                    !personQuestionEntities.get(i).isChecked()) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkQuestionsType(PersonQuestionnaireEntity entity) {
        for (PersonQuestionEntity questionEntity : entity.getPersonQuestionEntities()) {
            if (questionEntity.getQuestionEntity().getType() == QuestionTypeEnums.TEXTAREA && !questionEntity
                    .isCorrect() && !questionEntity.isChecked()) {
                return false;
            }
        }
        return true;
    }

    public static class PersonQuestionChecker {
        public static boolean checkPrePersist(PersonQuestionEntity personQuestionEntity) {
            if(personQuestionEntity == null) {
                exceptionCreator(Status.FORBIDDEN, "Person question cannot be null");
            } else if(checkDataPrePersist(personQuestionEntity, personQuestionEntity.getQuestionEntity(), "Person " +
                    "question")) {
                if(checkCollectionDataPrePersist(personQuestionEntity.getPersonAnswerEntities(), personQuestionEntity
                        .getQuestionEntity().getAnswerEntities())) {
                    return personQuestionEntity.getPersonAnswerEntities().stream().allMatch(answer ->
                            PersonAnswerChecker.checkPrePersist(answer));
                }
            }
            return true;
        }

        public static boolean IsCorrect(PersonQuestionEntity personQuestionEntity, QuestionEntity questionEntity) {
            if (!personQuestionEntity.getQuestionEntity().getType().equals(QuestionTypeEnums.TEXTAREA)) {
                if(personQuestionEntity.getPersonAnswerEntities() != null && questionEntity.getAnswerEntities() != null){
                    personQuestionEntity.setChecked(true);
                    return personAnswersIsCorrect(personQuestionEntity.getPersonAnswerEntities(), questionEntity.getAnswerEntities());
                } else {
                    return false;
                }
            } else {
                return personQuestionEntity.isCorrect();
            }
        }
        /**
         * Validate {@link org.dataart.qdump.entities.person.PersonAnswerEntity} if it is correct. Return true if and
         * only if all {@link org.dataart.qdump.entities.person.PersonAnswerEntity} that was marked by
         * {@link org.dataart.qdump.entities.person.PersonEntity} has a corresponding correct {@link org.dataart.qdump.entities.questionnaire.AnswerEntity}.
         * Validator return false if one of the {@link org.dataart.qdump.entities.person.PersonAnswerEntity} that was
         * marked by user has no corresponding correct {@link org.dataart.qdump.entities.questionnaire.AnswerEntity} or if
         * one of {@link org.dataart.qdump.entities.person.PersonAnswerEntity} is not marked by {@link org.dataart.qdump.entities.person.PersonEntity}
         * by has corresponding correct {@link org.dataart.qdump.entities.questionnaire.AnswerEntity}.
         *
         * @return boolean
         */
        private static boolean personAnswersIsCorrect(List<PersonAnswerEntity> personAnswerEntities,
                                                      List<AnswerEntity> answerEntities) {
            for(int i = 0; i < personAnswerEntities.size(); i++) {
                if(!PersonAnswerChecker.isCorrect(personAnswerEntities.get(i), answerEntities.get(i))) {
                    return false;
                }
            }
            return true;
        }
        public static class PersonAnswerChecker {
            public static boolean checkPrePersist(PersonAnswerEntity personAnswerEntity) {
                if(personAnswerEntity != null) {
                    return checkDataPrePersist(personAnswerEntity, personAnswerEntity.getAnswerEntity(), "Person " +
                            "Answer");
                }
                return true;
            }
            public static boolean isCorrect(PersonAnswerEntity personAnswerEntity, AnswerEntity answerEntity) {
                return personAnswerEntity.isMarked() == answerEntity.isCorrect();
            }
        }
    }
}
