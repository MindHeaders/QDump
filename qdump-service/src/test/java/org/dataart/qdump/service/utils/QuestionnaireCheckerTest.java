package org.dataart.qdump.service.utils;

import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 29/03/15.
 */
public class QuestionnaireCheckerTest {
    private QuestionnaireEntity questionnaireEntity;
    @Before
    public void setup() {
        questionnaireEntity = new QuestionnaireEntity();
        questionnaireEntity.setDescription("This test questionnaire for pre persist check");
        questionnaireEntity.setName("Pre persist questionnaire");
        questionnaireEntity.setPublished(true);
        List<QuestionEntity> questionEntities = new ArrayList<>();
        for(int i = 0; i < 4; i++) {
            QuestionEntity questionEntity = new QuestionEntity();
            questionEntity.setQuestion(String.format("This is the question #" + i + 1));
            switch (i) {
                case 0:
                    questionEntity.setType(QuestionTypeEnums.TEXTAREA);
                    break;
                case 1:
                    questionEntity.setType(QuestionTypeEnums.CHECKBOX);
                    break;
                case 2:
                    questionEntity.setType(QuestionTypeEnums.RADIO);
                    break;
                case 3:
                    questionEntity.setType(QuestionTypeEnums.SELECT);
                    break;
            }
            if(i != 0) {
                List<AnswerEntity> answerEntities = new ArrayList<>();
                int answerCounter = 0;
                for(int j = 0; j < 4; j++) {
                    AnswerEntity answerEntity = new AnswerEntity();
                    answerEntity.setAnswer("This is the test answer #" + j + ", for question #" + i);
                    if(i == 1 && answerCounter != 2) {
                        answerEntity.setCorrect(true);
                    } else if((i == 2 || i == 3) && answerCounter == 3) {
                        answerEntity.setCorrect(true);
                    }
                    answerCounter++;
                    answerEntities.add(answerEntity);
                }
                questionEntity.setAnswerEntities(answerEntities);
            }
            questionEntities.add(questionEntity);
        }
        questionnaireEntity.setQuestionEntities(questionEntities);
    }
    @Test
    public void testCheckPrePersistShouldReturnTrue() {
        assertTrue(QuestionnaireChecker.checkPrePersist(questionnaireEntity));
    }
    @Test
    public void testCheckPrePersistShouldReturnFalse() {
        assertFalse(QuestionnaireChecker.checkPrePersist(null));

        AnswerEntity answerEntity = questionnaireEntity.getQuestionEntities().get(1).getAnswerEntities().get(0);
        questionnaireEntity.getQuestionEntities().get(1).getAnswerEntities().set(0, null);
        assertFalse(QuestionnaireChecker.checkPrePersist(questionnaireEntity));
        questionnaireEntity.getQuestionEntities().get(1).getAnswerEntities().set(0, answerEntity);

        questionnaireEntity.getQuestionEntities().set(1, null);
        assertFalse(QuestionnaireChecker.checkPrePersist(questionnaireEntity));
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckPrePersistShouldThrowException() {
        questionnaireEntity.setId(1l);
        QuestionnaireChecker.checkPrePersist(questionnaireEntity);
    }
    @Test
    public void testCheckCanBePublishedReturnTrue() {
        assertTrue(QuestionnaireChecker.checkCanBePublished(questionnaireEntity));
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckCanBePublishedQuestionnaireShouldNotBeNull() {
        questionnaireEntity.setName(null);
        QuestionnaireChecker.checkCanBePublished(questionnaireEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckCanBePublishedQuestionnaireQuestionShouldNotBeNull() {
        questionnaireEntity.getQuestionEntities().get(0).setQuestion(null);
        QuestionnaireChecker.checkCanBePublished(questionnaireEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckCanBePublishedQuestionnaireQuestionAnswerShouldNotBeNull() {
        questionnaireEntity.getQuestionEntities().get(1).getAnswerEntities().get(0).setAnswer(null);
        QuestionnaireChecker.checkCanBePublished(questionnaireEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckCanBePublishedQuestionnaireQuestionShouldContainsOneCorrectAnswer() {
        questionnaireEntity.getQuestionEntities().get(2).getAnswerEntities().get(3).setCorrect(false);
        QuestionnaireChecker.checkCanBePublished(questionnaireEntity);
    }
    @Test
    public void testQuestionCheckPrePersistShouldReturnFalse() {
        assertFalse(QuestionnaireChecker.QuestionChecker.checkPrePersist(null));
        questionnaireEntity.getQuestionEntities().get(1).getAnswerEntities().set(1, null);
        assertFalse(QuestionnaireChecker.QuestionChecker.checkPrePersist(questionnaireEntity.getQuestionEntities()
                .get(1)));
    }
    @Test(expected = WebApplicationException.class)
    public void testQuestionCheckPrePersistQuestionIdShouldBeEqualsZero() {
        QuestionEntity questionEntity = questionnaireEntity.getQuestionEntities().get(1);
        questionEntity.setId(1l);
        QuestionnaireChecker.QuestionChecker.checkPrePersist(questionEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testQuestionCheckPrePersistQuestionAnswersIdShouldBeEqualsZero() {
        QuestionEntity questionEntity = questionnaireEntity.getQuestionEntities().get(1);
        questionEntity.getAnswerEntities().get(1).setId(1l);
        QuestionnaireChecker.QuestionChecker.checkPrePersist(questionEntity);
    }
}
