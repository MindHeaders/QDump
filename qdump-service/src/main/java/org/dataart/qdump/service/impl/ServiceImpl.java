package org.dataart.qdump.service.impl;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.dataart.qdump.persistence.repository.AnswerCrudRepository;
import org.dataart.qdump.persistence.repository.PersonAnswerCrudRepository;
import org.dataart.qdump.persistence.repository.PersonCrudRepository;
import org.dataart.qdump.persistence.repository.PersonQuestionCrudRepository;
import org.dataart.qdump.persistence.repository.PersonQuestionnaireCrudRepository;
import org.dataart.qdump.persistence.repository.QuestionCrudRepository;
import org.dataart.qdump.persistence.repository.QuestionnaireCrudRepository;
import org.dataart.qdump.persistence.repository.VerificationTokenCrudRepository;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ServiceImpl implements ServiceQdump {
	
	@Autowired
	private AnswerCrudRepository answerCrudRepository;
	@Autowired
    private PersonAnswerCrudRepository personAnswerCrudRepository;
	@Autowired
    private PersonQuestionCrudRepository personQuestionCrudRepository;
	@Autowired
    private PersonQuestionnaireCrudRepository personQuestionnaireCrudRepository;
    @Qualifier("questionCrudRepository")
    @Autowired
    private QuestionCrudRepository questionCrudRepository;
	@Autowired
    private QuestionnaireCrudRepository questionnaireCrudRepository;
    @Qualifier("verificationTokenCrudRepository")
    @Autowired
    private VerificationTokenCrudRepository verificationTokenCrudRepository;
    @Qualifier("personCrudRepository")
    @Autowired
    private PersonCrudRepository personCrudRepository;

    //PersonEntity
    @Override
    public void addPersonEntity(PersonEntity personEntity) {
        personCrudRepository.save(personEntity);
    }

    @Override
    public void deletePersonEntity(long id) {
        personCrudRepository.delete(id);
    }

    @Override
    public void deletePersonEntities() {
        personCrudRepository.deleteAll();
    }

    @Override
    public boolean personEntityExists(long id) {
        return personCrudRepository.exists(id);
    }

    @Override
    public boolean personEntityExistsByLogin(String login) {
        return personCrudRepository.existsByLogin(login);
    }

    @Override
    public boolean personEntityExistsByEmail(String email) {
        return personCrudRepository.existsByEmail(email);
    }

    @Override
    public boolean personEntityIsEnabledByLogin(String login) {
        return personCrudRepository.enabledByLogin(login);
    }

    @Override
    public boolean personEntityIsEnabledByEmail(String email) {
        return personCrudRepository.enabledByEmail(email);
    }

    @Override
    public long personEntitiesCount() {
        return personCrudRepository.count();
    }

    @Override
    public long personEntitiesCount(boolean enabled) {
        return personCrudRepository.countByEnabled(enabled);
    }

    @Override
    public long personEntitiesCount(byte gender) {
        return personCrudRepository.countByGender(gender);
    }

    @Override
    public String getPersonEntityRole(long id) {
        return personCrudRepository.findPersonRole(id);
    }

    @Override
    public PersonEntity getPersonEntity(long id) {
        return personCrudRepository.findById(id);
    }

    @Override
    public PersonEntity getPersonEntityByEmail(String email) {
        return personCrudRepository.findByEmail(email);
    }

    @Override
    public PersonEntity getPersonEntityByLogin(String login) {
        return personCrudRepository.findByLogin(login);
    }

    @Override
    public PersonEntity mostActivePersonEntityInCreatingQuestionnaires() {
        return personCrudRepository.findMostActivePersonInCreatingQuestionnaires();
    }

    @Override
    public PersonEntity mostActivePersonEntityInPassingQuestionnaires() {
        return personCrudRepository.findMostActivePersonInPassingQuestionnaires();
    }

    @Override
    public List<PersonEntity> getPersonEntities() {
        return personCrudRepository.findAll();
    }

    @Override
    public List<PersonEntity> top10ActivePersonEntities() {
        return personCrudRepository.findTop10ActivePersonEntities();
    }

    @Override
    public List<PersonEntity> top10ActivePersonEntitiesTest() {
        return personCrudRepository.hello(new PageRequest(0, 10));
    }

    @Override
    public List<PersonEntity> getPersonEntities(Pageable pageable) {
        Page<PersonEntity> database = personCrudRepository.findAll(pageable);
        return database.getContent();
    }

    //PersonQuestionnaireEntity
    @Override
    public void addPersonQuestionnaireEntity(PersonQuestionnaireEntity personQuestionnaireEntity) {
        personQuestionnaireCrudRepository.save(personQuestionnaireEntity);
    }

    @Override
    public void deletePersonQuestionnaireEntity(long id) {
        personQuestionnaireCrudRepository.delete(id);
    }

    @Override
    public void deletePersonQuestionnaireEntities() {
        personQuestionnaireCrudRepository.deleteAll();
    }

    @Override
    public boolean personQuestionnaireEntityExists(long id) {
        return personQuestionnaireCrudRepository.exists(id);
    }

    @Override
    public long personQuestionnaireEntitiesCount() {
        return personQuestionnaireCrudRepository.count();
    }

    @Override
    public long completedPersonQuestionnaireEntitiesCount(long id) {
        return personQuestionnaireCrudRepository.countCompletedByPersonId(id);
    }

    @Override
    public long startedPersonQuestionnaireEntitiesCount(long id) {
        return personQuestionnaireCrudRepository.countStartedByPersonId(id);
    }

    @Override
    public long personQuestionnaireCountByStatus(String status) {
        return personQuestionnaireCrudRepository.countByStatus(status);
    }

    @Override
    public long startedQuestionnaireEntitiesCount() {
        return personQuestionnaireCrudRepository.countStartedQuestionnaires();
    }

    @Override
    public long completedQuestionnaireEntitiesCount() {
        return personQuestionnaireCrudRepository.countCompletedQuestionnaires();
    }

    @Override
    public PersonQuestionnaireEntity getPersonQuestionnaireEntity(long id) {
        return personQuestionnaireCrudRepository.findById(id);
    }

    @Override
    public PersonQuestionnaireEntity getPersonQuestionnaireEntity(long personQuestionnaireId, long personId) {
        return personQuestionnaireCrudRepository.findByPersonQuestionnaireIdAndPersonId(personQuestionnaireId, personId);
    }

    @Override
    public PersonQuestionnaireEntity getPersonQuestionnaireByPersonQuestion(long id) {
        return personQuestionnaireCrudRepository.findByPersonQuestionId(id);
    }

    @Override
    public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities() {
        return personQuestionnaireCrudRepository.findAll();
    }

    @Override
    public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities(String status) {
        return personQuestionnaireCrudRepository.findByStatus(status);
    }

    @Override
    public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntitiesByQuestionnaireName(String name) {
        return personQuestionnaireCrudRepository.findByQuestionnaireEntityName(name);
    }

    @Override
    public List<PersonQuestionnaireEntity> getPersonQuestionnairesInCheckingProcess(Pageable pageable) {
        return personQuestionnaireCrudRepository.findInCheckingProcess(pageable);
    }

    @Override
    public List<PersonQuestionnaireEntity> getCompletedPersonQuestionnaireEntities(long id, Pageable pageable) {
        return personQuestionnaireCrudRepository.findCompletedByPersonId(id, pageable);
    }

    @Override
    public List<PersonQuestionnaireEntity> getStartedPersonQuestionnaireEntities(long id, Pageable pageable) {
        return personQuestionnaireCrudRepository.findStartedByPersonId(id, pageable);
    }

    @Override
    public void deletePersonQuestionnaireEntities(long questionnaireId) {
        personQuestionnaireCrudRepository.deleteByQuestionnaireEntityId(questionnaireId);
    }

    //PersonQuestionEntity
    @Override
    public void addPersonQuestionEntity(PersonQuestionEntity personQuestionEntity) {
        personQuestionCrudRepository.save(personQuestionEntity);
    }

    @Override
    public void deletePersonQuestionEntity(long id) {
        personQuestionCrudRepository.delete(id);
    }

    @Override
    public void deletePersonQuestionEntities() {
        personQuestionCrudRepository.deleteAll();
    }

    @Override
    public boolean personQuestionEntityExists(long id) {
        return personQuestionCrudRepository.exists(id);
    }

    @Override
    public long personQuestionEntitiesCount() {
        return personQuestionCrudRepository.count();
    }

    @Override
    public long notCheckedPersonQuestionEntitiesCount() {
        return personQuestionCrudRepository.countNotCheckedQuestions();
    }

    @Override
    public PersonQuestionEntity getPersonQuestionEntity(long id) {
        return personQuestionCrudRepository.findById(id);
    }

    @Override
    public List<PersonQuestionEntity> getPersonQuestionEntities() {
        return personQuestionCrudRepository.findAll();
    }

    @Override
    public List<PersonQuestionEntity> getPersonQuestionEntitiesByPersonEntityId(long id) {
        return null;
    }

    @Override
    public List<PersonQuestionEntity> getNotCheckedPersonQuestionEntities(Pageable pageable) {
        return null;
    }

    //PersonAnswerEntity
    @Override
    public void addPersonAnswerEntity(PersonAnswerEntity personAnswerEntity) {
        personAnswerCrudRepository.save(personAnswerEntity);
    }

    @Override
    public void deletePersonAnswerEntity(long id) {
        personAnswerCrudRepository.delete(id);
    }

    @Override
    public void deletePersonAnswerEntities() {
        personAnswerCrudRepository.deleteAll();
    }

    @Override
    public boolean personAnswerEntityExists(long id) {
        return personAnswerCrudRepository.exists(id);
    }

    @Override
    public long personAnswerEntitiesCount() {
        return personAnswerCrudRepository.count();
    }

    @Override
    public PersonAnswerEntity getPersonAnswerEntity(long id) {
        return personAnswerCrudRepository.findById(id);
    }

    @Override
    public List<PersonAnswerEntity> getPersonAnswerEntities() {
        return personAnswerCrudRepository.findAll();
    }

    //QuestionnaireEntity
    @Override
    public void addQuestionnaireEntity(QuestionnaireEntity questionnaireEntity) {
        questionnaireCrudRepository.save(questionnaireEntity);
    }

    @Override
    public void deleteQuestionnaireEntity(long id) {
        questionnaireCrudRepository.delete(id);
    }

    @Override
    public void deleteQuestionnaireEntities() {
        questionnaireCrudRepository.deleteAll();
    }

    @Override
    public boolean questionnaireEntityExists(long id) {
        return questionnaireCrudRepository.exists(id);
    }

    @Override
    public long questionnaireEntitiesCount() {
        return questionnaireCrudRepository.count();
    }

    @Override
    public long publishedQuestionnaireEntitiesCount(boolean published) {
        return questionnaireCrudRepository.countByPublished(published);
    }

    @Override
    public QuestionnaireEntity getQuestionnaireEntity(long id) {
        return questionnaireCrudRepository.findById(id);
    }

    @Override
    public QuestionnaireEntity getQuestionnaireEntity(String name) {
        return questionnaireCrudRepository.findByName(name);
    }

    @Override
    public QuestionnaireEntity getQuestionnaireEntity(boolean published, long id) {
        return questionnaireCrudRepository.findByPublishedAndById(published, id);
    }

    @Override
    public QuestionnaireEntity getPopularQuestionnaireEntity() {
        return questionnaireCrudRepository.findPopularQuestionnaireEntity();
    }

    @Override
    public List<QuestionnaireEntity> getQuestionnaireEntities() {
        return questionnaireCrudRepository.findAll();
    }

    @Override
    public List<QuestionnaireEntity> getQuestionnaireEntities(Pageable pageable) {
        return questionnaireCrudRepository.findAll(pageable).getContent();
    }

    @Override
    public List<QuestionnaireEntity> getQuestionnaireEntities(boolean published, Pageable pageable) {
        return questionnaireCrudRepository.findByPublished(published, pageable);
    }

    @Override
    public LocalDateTime getLastQuestionnaireEntityCreatedDate() {
        return questionnaireCrudRepository.lastCreatedDate();
    }

    //QuestionEntity
    @Override
    public void addQuestionEntity(QuestionEntity questionEntity) {
        questionCrudRepository.save(questionEntity);
    }

    @Override
    public void deleteQuestionEntity(long id) {
        questionCrudRepository.delete(id);
    }

    @Override
    public void deleteQuestionEntities() {
        questionCrudRepository.deleteAll();
    }

    @Override
    public boolean questionEntityExists(long id) {
        return questionCrudRepository.exists(id);
    }

    @Override
    public long questionEntitiesCount() {
        return questionCrudRepository.count();
    }

    @Override
    public QuestionEntity getQuestionEntity(long id) {
        return questionCrudRepository.findById(id);
    }

    @Override
    public List<QuestionEntity> getQuestionEntities() {
        return questionCrudRepository.findAll();
    }

    //AnswerEntity
    @Override
    public void addAnswerEntity(AnswerEntity answerEntity) {
        answerCrudRepository.save(answerEntity);
    }

    @Override
    public void deleteAnswerEntity(long id) {
        answerCrudRepository.delete(id);
    }

    @Override
    public void deleteAnswerEntities() {
        answerCrudRepository.deleteAll();
    }

    @Override
    public boolean answerEntityExists(long id) {
        return answerCrudRepository.exists(id);
    }

    @Override
    public long answerEntitiesCount() {
        return answerCrudRepository.count();
    }

    @Override
    public AnswerEntity getAnswerEntity(long id) {
        return answerCrudRepository.findById(id);
    }

    @Override
    public List<AnswerEntity> getAnswerEntities() {
        return answerCrudRepository.findAll();
    }

    //VerificationToken
    @Override
    public void addVerificationTokenEntity(VerificationTokenEntity verificationTokenEntity) {
        verificationTokenCrudRepository.save(verificationTokenEntity);
    }

    @Override
    public void deleteVerificationTokenEntity(long id) {
        verificationTokenCrudRepository.delete(id);
    }

    @Override
    public void deleteAllVerificationTokenEntity() {
        verificationTokenCrudRepository.deleteAll();
    }

    @Override
    public void deleteExpired() {
        verificationTokenCrudRepository.deleteExpired();
    }

    @Override
    public void deleteVerified() {
        verificationTokenCrudRepository.deleteVerified();
    }

    @Override
    public void deleteVerificationTokenEntityByPersonEntityId(long id) {
        verificationTokenCrudRepository.deleteByPersonEntityId(id);
    }

    @Override
    public boolean verificationTokenEntityExists(String token) {
        return verificationTokenCrudRepository.exists(token);
    }

    @Override
    public VerificationTokenEntity getVerificationTokenEntity(String email) {
        return verificationTokenCrudRepository.findByPersonEntityEmail(email);
    }

    @Override
    public VerificationTokenEntity getVerificationTokenEntityConstructor(String email) {
        return verificationTokenCrudRepository.findByPersonEntityEmailConstructor(email);
    }

    @Override
    public VerificationTokenEntity getVerificationTokenEntityByToken(String token) {
        return verificationTokenCrudRepository.findByToken(token);
    }

    @Override
    public VerificationTokenEntity getVerificationTokenEntity(long id) {
        return verificationTokenCrudRepository.findById(id);
    }

    @Override
    public List<VerificationTokenEntity> getVerificationTokenEntities() {
        return verificationTokenCrudRepository.findAll();
    }
}
