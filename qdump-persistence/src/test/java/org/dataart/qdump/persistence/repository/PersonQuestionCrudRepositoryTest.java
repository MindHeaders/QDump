package org.dataart.qdump.persistence.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.persistence.config.AppConfig;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

/**
 * Created by artemvlasov on 08/04/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/TestData.xml")
@Ignore
public class PersonQuestionCrudRepositoryTest {
    @Autowired
    private PersonQuestionCrudRepository personQuestionCrudRepository;

    @Test
    public void testFindTextareaQuestionByPersonId_ShouldReturnPersonQuestionEntity() {
        List<PersonQuestionEntity> personQuestionEntityList = personQuestionCrudRepository
                .findTextareaQuestionByPersonId(1l);
        Assert.assertTrue(1 == personQuestionEntityList.size());
        Assert.assertEquals(QuestionTypeEnums.TEXTAREA, personQuestionEntityList.get(0).getQuestionEntity().getType());
    }
    @Test
    public void testFindNotCheckedQuestions_ShouldReturnPersonQuestionEntities() {
        List<PersonQuestionEntity> personQuestionEntities = personQuestionCrudRepository.findNotCheckedQuestions(new
                PageRequest(0, 10));
        Assert.assertTrue(1 == personQuestionEntities.size());
        Assert.assertFalse(personQuestionEntities.get(0).isChecked());
    }
    @Test
    public void testCountNotCheckedQuestionsReturnLong() {
        long count = personQuestionCrudRepository.countNotCheckedQuestions();
        Assert.assertTrue(count == 1);
    }
}
