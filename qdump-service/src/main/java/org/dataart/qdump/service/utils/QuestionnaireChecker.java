package org.dataart.qdump.service.utils;

import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;

import javax.ws.rs.core.Response;

import static org.dataart.qdump.service.utils.WebApplicationUtils.exceptionCreator;

/**
 * Created by artemvlasov on 27/03/15.
 */
public class QuestionnaireChecker {
    public static boolean checkPrePersist(QuestionnaireEntity entity) {
        if(entity != null) {
            if (entity.getId() > 0) {
                exceptionCreator(Response.Status.NOT_ACCEPTABLE, "Persisted entity cannot be " +
                        "created with id more than 0");
            } else if (entity.getQuestionEntities() != null) {
                return entity.getQuestionEntities().stream().allMatch(question -> QuestionChecker.checkPrePersist
                        (question));
            }
            return true;
        }
        return false;
    }
    public static boolean checkCanBePublished(QuestionnaireEntity questionnaireEntity) {
        if(questionnaireEntity.getName() == null) {
            exceptionCreator(Response.Status.NOT_ACCEPTABLE, "Questionnaire name cannot be null");
        }
        for(QuestionEntity questionEntity : questionnaireEntity.getQuestionEntities()) {
            boolean questionHasCorrectAnswer = false;
            if(questionEntity.getQuestion() == null) {
                exceptionCreator(Response.Status.NOT_ACCEPTABLE, "All questionnaire questions should be filled ");
            }
            if(questionEntity.getType() == QuestionTypeEnums.TEXTAREA) continue;
            for(AnswerEntity answerEntity : questionEntity.getAnswerEntities()) {
                if(!questionHasCorrectAnswer && answerEntity.isCorrect()) {
                    questionHasCorrectAnswer = true;
                }
                if(answerEntity.getAnswer() == null) {
                    exceptionCreator(Response.Status.NOT_ACCEPTABLE, "All question answers should be filled");
                }
            }
            if(!questionHasCorrectAnswer) {
                exceptionCreator(Response.Status.NOT_ACCEPTABLE, "One of the answers should be correct");
            }
        }
        return true;
    }
    public static class QuestionChecker {
        public static boolean checkPrePersist(QuestionEntity entity) {
            if(entity != null) {
                if (entity.getId() > 0) {
                    WebApplicationUtils.exceptionCreator(Response.Status.NOT_ACCEPTABLE, "Persisted entity cannot be " +
                            "created with id more than 0");
                } else if (entity.getAnswerEntities() != null) {
                    return entity.getAnswerEntities().stream().allMatch(answer -> AnswerChecker.checkPrePersist(answer));
                }
                return true;
            }
            return false;
        }
        public static class AnswerChecker {
            public static boolean checkPrePersist(AnswerEntity entity) {
                if(entity != null) {
                    if (entity.getId() > 0) {
                        WebApplicationUtils.exceptionCreator(Response.Status.NOT_ACCEPTABLE, "Persisted entity cannot be " +
                                "created with id more than 0");
                    }
                    return true;
                }
                return false;
            }
        }
    }
}
