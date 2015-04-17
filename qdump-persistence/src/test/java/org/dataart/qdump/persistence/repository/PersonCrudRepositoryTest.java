package org.dataart.qdump.persistence.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.persistence.config.AppConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 07/04/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DatabaseSetup("/META-INF/dbtest/TestData.xml")
public class PersonCrudRepositoryTest {
    @Autowired
    private PersonCrudRepository personCrudRepository;

    @Test
    public void testFindByEmailShouldReturnPersonEntity() {
        PersonEntity personEntity = personCrudRepository.findByEmail("testemail@mail.com");
        assertEquals(personEntity.getLogin(), "login");
    }
    @Test
    public void testFindByLoginShouldReturnPersonEntity() {
        PersonEntity personEntity = personCrudRepository.findByLogin("login2");
        assertEquals(personEntity.getEmail(), "testemail2@mail.com");
    }
    @Test
    public void testFindByPersonGroupShouldReturnList() {
        List<PersonEntity> personEntities = personCrudRepository.findByPersonGroup(PersonGroupEnums.ADMIN);
        assertEquals(personEntities.size(), 1);
    }
    @Test
    public void testExistsByLoginShouldReturnTrue() {
        assertTrue(personCrudRepository.existsByLogin("login2"));
    }
    @Test
    public void testExistsByLoginShouldReturnFalse() {
        assertFalse(personCrudRepository.existsByLogin("login4"));
    }
    @Test
    public void testExistsByEmailShouldReturnTrue() {
        assertTrue(personCrudRepository.existsByEmail("testemail@mail.com"));
    }
    @Test
    public void testExistsByEmailShouldReturnFalse() {
        assertFalse(personCrudRepository.existsByEmail("hello@mail.com"));
    }
    @Test
    public void testEnabledByEmailShouldReturnTrue() {
        assertTrue(personCrudRepository.enabledByEmail("testemail@mail.com"));
    }
    @Test
    public void testEnabledByLoginShouldReturnTrue() {
        assertTrue(personCrudRepository.enabledByLogin("login"));
    }
    @Test
    public void testEnabledByEmailShouldReturnFalse() {
        assertFalse(personCrudRepository.enabledByEmail("testemail2@mail.com"));
    }
    @Test
    public void testEnabledByLoginShouldReturnFalse() {
        assertFalse(personCrudRepository.enabledByLogin("login2"));
    }
    @Test
    public void testFindPersonRoleShouldReturnString() {
        PersonEntity personEntity = personCrudRepository.findByEmail("testemail@mail.com");
        PersonGroupEnums group = PersonGroupEnums.valueOf(personCrudRepository.findPersonRole(personEntity.getId()));
        assertEquals(personEntity.getPersonGroup(), group);
        assertTrue(group == PersonGroupEnums.ADMIN);
    }
    @Test
    public void testFindByIdShouldReturnPersonEntity() {
        PersonEntity personEntity = personCrudRepository.findByEmail("testemail@mail.com");
        PersonEntity personEntity1 = personCrudRepository.findOne(personEntity.getId());
        assertEquals(personEntity, personEntity1);
    }
    @Test
    public void testCountBeGenderShouldReturnLong() {
        assertEquals(1, personCrudRepository.countByGender((byte) 1));
        assertEquals(1, personCrudRepository.countByGender((byte) 2));
    }
    @Test
    public void testCountByEnabledShouldReturnLong() {
        assertEquals(2, personCrudRepository.countByEnabled(true));
    }
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void testDeleteByEmail() {
        personCrudRepository.deleteByEmail("testemail@mail.com");
        List<PersonEntity> personEntities = personCrudRepository.findAll();
        assertTrue(personEntities.size() == 2);
        assertTrue(personEntities.stream().noneMatch(entity -> entity.getEmail().equals("testemail@mail.com")));
    }
    @Test
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void testDeleteByLogin() {
        personCrudRepository.deleteByLogin("login2");
        List<PersonEntity> personEntities = personCrudRepository.findAll();
        assertTrue(personEntities.size() == 2);
        assertTrue(personEntities.stream().noneMatch(entity -> entity.getLogin().equals("login2")));
    }
}
