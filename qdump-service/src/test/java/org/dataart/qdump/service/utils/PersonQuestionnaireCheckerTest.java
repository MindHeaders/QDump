package org.dataart.qdump.service.utils;

import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.enums.QuestionnaireStatusEnums;
import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by artemvlasov on 28/03/15.
 */
public class PersonQuestionnaireCheckerTest {
    private PersonQuestionnaireEntity personQuestionnaireEntity = new PersonQuestionnaireEntity();
    private QuestionnaireEntity questionnaireEntity = new QuestionnaireEntity();
    @Before
    public void beforTest() {
        questionnaireEntity.setName("Test questionnaire");
        questionnaireEntity.setPublished(true);
        questionnaireEntity.setDescription("This is the test questionnaire");
        questionnaireEntity.setId(1l);
        List<QuestionEntity> questionEntities = new ArrayList<>();
        List<PersonQuestionEntity> personQuestionEntities = new ArrayList<>();
        int answersCounter = 0;
        for(int i = 0; i < 4; i++) {
            QuestionEntity questionEntity = new QuestionEntity();
            PersonQuestionEntity personQuestionEntity = new PersonQuestionEntity();
            questionEntity.setQuestion(String.format("This is the question #" + i + 1));
            questionEntity.setId(i + 1);
            personQuestionEntity.setQuestionEntity(questionEntity);
            switch (i) {
                case 0:
                    questionEntity.setType(QuestionTypeEnums.TEXTAREA);
                    personQuestionEntity.setCorrect(true);
                    personQuestionEntity.setChecked(true);
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
                List<PersonAnswerEntity> personAnswerEntities = new ArrayList<>();
                int answerCounter = 0;
                for(int j = 0; j < 4; j++) {
                    AnswerEntity answerEntity = new AnswerEntity();
                    answerEntity.setId(answersCounter + 1);
                    PersonAnswerEntity personAnswerEntity = new PersonAnswerEntity();
                    answerEntity.setAnswer("This is the test answer #" + j + ", for question #" + i);
                    personAnswerEntity.setAnswerEntity(answerEntity);
                    if(i == 1 && answerCounter != 2) {
                        answerEntity.setCorrect(true);
                        personAnswerEntity.setMarked(true);
                    } else if((i == 2 || i == 3) && answerCounter == 3) {
                        answerEntity.setCorrect(true);
                        personAnswerEntity.setMarked(true);
                    }
                    answerCounter++;
                    answersCounter++;
                    answerEntities.add(answerEntity);
                    personAnswerEntities.add(personAnswerEntity);

                }
                questionEntity.setAnswerEntities(answerEntities);
                personQuestionEntity.setPersonAnswerEntities(personAnswerEntities);
            }
            questionEntities.add(questionEntity);
            personQuestionEntities.add(personQuestionEntity);
        }
        questionnaireEntity.setQuestionEntities(questionEntities);
        personQuestionnaireEntity.setPersonQuestionEntities(personQuestionEntities);
        personQuestionnaireEntity.setQuestionnaireEntity(questionnaireEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckPrePersistQuestionnaireShouldNotBeNull() {
        personQuestionnaireEntity.setQuestionnaireEntity(null);
        PersonQuestionnaireChecker.checkPrePersist(personQuestionnaireEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckPrePersistPersonQuestionnaireIdShouldNotBeGreaterThanZero() {
        personQuestionnaireEntity.setId(1l);
        PersonQuestionnaireChecker.checkPrePersist(personQuestionnaireEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckPrePersistQuestionnaireIdShouldBeEqualsZero() {
        personQuestionnaireEntity.getQuestionnaireEntity().setId(0);
        PersonQuestionnaireChecker.checkPrePersist(personQuestionnaireEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckPrePersistPersonQuestionsAndQuestionSizeShouldBeEquals() {
        questionnaireEntity.getQuestionEntities().remove(0);
        PersonQuestionnaireChecker.checkPrePersist(personQuestionnaireEntity);
    }
    @Test
    public void testCheckPrePersistShouldReturnTrue() {
        assertTrue(PersonQuestionnaireChecker.checkPrePersist(personQuestionnaireEntity));
    }
    @Test
    public void testCheckPrePersistShouldReturnTruePersonQuestionsNull() {
        personQuestionnaireEntity.setPersonQuestionEntities(null);
        assertTrue(PersonQuestionnaireChecker.checkPrePersist(personQuestionnaireEntity));
    }
    @Test(expected = WebApplicationException.class)
    public void testCheckPrePersistPersonQuestionnaireNullShouldThrowException() {
        assertFalse(PersonQuestionnaireChecker.checkPrePersist(null));
    }
    @Test
    public void testCheckPrePersistStatusShouldBePassed() {
        PersonQuestionnaireChecker.checkStatus(personQuestionnaireEntity, questionnaireEntity);
        assertEquals(QuestionnaireStatusEnums.PASSED.getName(), personQuestionnaireEntity.getStatus());
    }
    @Test
    public void testCheckPrePersistStatusShouldBeNotPassed() {
        personQuestionnaireEntity.getPersonQuestionEntities().get(0).setCorrect(false);
        PersonQuestionnaireChecker.checkStatus(personQuestionnaireEntity, questionnaireEntity);
        assertEquals(QuestionnaireStatusEnums.NOT_PASSED.getName(), personQuestionnaireEntity.getStatus());
        personQuestionnaireEntity.getPersonQuestionEntities().get(0).setCorrect(true);

        personQuestionnaireEntity.getPersonQuestionEntities().get(1).getPersonAnswerEntities().get(0).setMarked(false);
        PersonQuestionnaireChecker.checkStatus(personQuestionnaireEntity, questionnaireEntity);
        assertEquals(QuestionnaireStatusEnums.NOT_PASSED.getName(), personQuestionnaireEntity.getStatus());
        personQuestionnaireEntity.getPersonQuestionEntities().get(1).getPersonAnswerEntities().get(0).setMarked(true);

        personQuestionnaireEntity.getPersonQuestionEntities().get(1).getPersonAnswerEntities().get(0).getAnswerEntity
                ().setCorrect(false);
        PersonQuestionnaireChecker.checkStatus(personQuestionnaireEntity, questionnaireEntity);
        assertEquals(QuestionnaireStatusEnums.NOT_PASSED.getName(), personQuestionnaireEntity.getStatus());
    }
    @Test
    public void testCheckPrePersistStatusShouldBeInCheckingProcess() {
        personQuestionnaireEntity.getPersonQuestionEntities().get(0).setChecked(false);
        personQuestionnaireEntity.getPersonQuestionEntities().get(0).setCorrect(false);
        PersonQuestionnaireChecker.checkStatus(personQuestionnaireEntity, questionnaireEntity);
        assertEquals(QuestionnaireStatusEnums.IN_CHECKING_PROCESS.getName(), personQuestionnaireEntity.getStatus());
    }
    @Test(expected = WebApplicationException.class)
    public void testPersonQuestionCheckPrePersistQuestionShouldThrowException() {
        PersonQuestionEntity personQuestionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(0);
        personQuestionEntity.setQuestionEntity(null);
        PersonQuestionnaireChecker.PersonQuestionChecker.checkPrePersist(personQuestionEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testPersonQuestionCheckPrePersistQuestionIdShouldBeGreaterThanZero() {
        PersonQuestionEntity personQuestionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(0);
        personQuestionEntity.getQuestionEntity().setId(0);
        PersonQuestionnaireChecker.PersonQuestionChecker.checkPrePersist(personQuestionEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testPersonQuestionCheckPrePersistPersonQuestionIdShouldBeEqualsZero() {
        PersonQuestionEntity personQuestionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(0);
        personQuestionEntity.setId(1);
        PersonQuestionnaireChecker.PersonQuestionChecker.checkPrePersist(personQuestionEntity);
    }
    @Test(expected = WebApplicationException.class)
    public void testPersonQuestionCheckPrePersistPersonQuestionNullShouldThrowException() {
        PersonQuestionnaireChecker.PersonQuestionChecker.checkPrePersist(null);
    }
    @Test
    public void testPersonQuestionCheckPrePersistShouldReturnTrue() {
        PersonQuestionEntity personQuestionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(0);
        PersonQuestionnaireChecker.PersonQuestionChecker.checkPrePersist(personQuestionEntity);
    }
    @Test
    public void testPersonQuestionIsCorrectShouldReturnTrue() {
        PersonQuestionEntity personQuestionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(1);
        QuestionEntity questionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(1)
                .getQuestionEntity();
        assertTrue(PersonQuestionnaireChecker.PersonQuestionChecker.IsCorrect(personQuestionEntity,
                questionEntity));
        assertTrue(personQuestionEntity.isChecked());

        personQuestionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(0);
        questionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(0).getQuestionEntity();
        assertTrue(PersonQuestionnaireChecker.PersonQuestionChecker.IsCorrect(personQuestionEntity, questionEntity));
        assertTrue(personQuestionEntity.isChecked());
    }
    @Test
    public void testPersonQuestionIsCorrectShouldReturnFalse() {
        PersonQuestionEntity personQuestionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(1);
        QuestionEntity questionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(1)
                .getQuestionEntity();
        personQuestionEntity.getPersonAnswerEntities().get(0).setMarked(false);
        assertFalse(PersonQuestionnaireChecker.PersonQuestionChecker.IsCorrect(personQuestionEntity,
                questionEntity));
        assertTrue(personQuestionEntity.isChecked());
        personQuestionEntity.getPersonAnswerEntities().get(0).setMarked(true);

        personQuestionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(1);
        personQuestionEntity.setPersonAnswerEntities(null);
        questionEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(1)
                .getQuestionEntity();
        assertFalse(PersonQuestionnaireChecker.PersonQuestionChecker.IsCorrect(personQuestionEntity, questionEntity));
    }
    @Test
    public void testPersonAnswerCheckPrePersistShouldReturnTrue() {
        assertTrue(PersonQuestionnaireChecker.PersonQuestionChecker.PersonAnswerChecker.checkPrePersist(null));
        assertTrue(personQuestionnaireEntity.getPersonQuestionEntities().get(1).getPersonAnswerEntities().stream().allMatch
                (answer -> PersonQuestionnaireChecker.PersonQuestionChecker.PersonAnswerChecker.checkPrePersist
                        (answer)));
    }
    @Test(expected = WebApplicationException.class)
    public void testPersonAnswerCheckPerPersistShouldThrowException() {
        PersonAnswerEntity personAnswerEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(1)
                .getPersonAnswerEntities().get(0);
        personAnswerEntity.setId(1);
        PersonQuestionnaireChecker.PersonQuestionChecker.PersonAnswerChecker.checkPrePersist(personAnswerEntity);
        personAnswerEntity.setId(0);

        long answerEntityId =  personAnswerEntity.getAnswerEntity().getId();
        personAnswerEntity.getAnswerEntity().setId(0);
        PersonQuestionnaireChecker.PersonQuestionChecker.PersonAnswerChecker.checkPrePersist(personAnswerEntity);
        personAnswerEntity.getAnswerEntity().setId(answerEntityId);

        personAnswerEntity.setAnswerEntity(null);
        PersonQuestionnaireChecker.PersonQuestionChecker.PersonAnswerChecker.checkPrePersist(personAnswerEntity);
    }
    @Test
    public void testPersonAnswerIsCorrectShouldReturnFalse() {
        PersonAnswerEntity personAnswerEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(1)
                .getPersonAnswerEntities().get(0);
        AnswerEntity answerEntity = questionnaireEntity.getQuestionEntities().get(1).getAnswerEntities().get(0);
        personAnswerEntity.setMarked(false);
        assertFalse(PersonQuestionnaireChecker.PersonQuestionChecker.PersonAnswerChecker.isCorrect
                (personAnswerEntity, answerEntity));
    }
    @Test
    public void testPersonAnswerIsCorrectShouldReturnTrue() {
        PersonAnswerEntity personAnswerEntity = personQuestionnaireEntity.getPersonQuestionEntities().get(1)
                .getPersonAnswerEntities().get(0);
        AnswerEntity answerEntity = questionnaireEntity.getQuestionEntities().get(1).getAnswerEntities().get(0);
        assertTrue(PersonQuestionnaireChecker.PersonQuestionChecker.PersonAnswerChecker.isCorrect
                (personAnswerEntity, answerEntity));
    }
}
