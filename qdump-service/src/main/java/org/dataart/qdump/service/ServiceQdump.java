package org.dataart.qdump.service;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ServiceQdump {
	//PersonEntity
	void addPersonEntity(PersonEntity personEntity);
	void deletePersonEntity(long id);
	void deletePersonEntities();
    boolean personEntityExists(long id);
    boolean personEntityExistsByLogin(String login);
    boolean personEntityExistsByEmail(String email);
    boolean personEntityIsEnabledByLogin(String login);
    boolean personEntityIsEnabledByEmail(String email);
    long personEntitiesCount();
    long personEntitiesCount(boolean enabled);
    long personEntitiesCount(byte gender);
    String getPersonEntityRole(long id);
	PersonEntity getPersonEntity(long id);
    PersonEntity getPersonEntityByEmail(String email);
    PersonEntity getPersonEntityByLogin(String login);
    PersonEntity mostActivePersonEntityInCreatingQuestionnaires();
    PersonEntity mostActivePersonEntityInPassingQuestionnaires();
	List<PersonEntity> getPersonEntities();
    List<PersonEntity> top10ActivePersonEntities();
    List<PersonEntity> top10ActivePersonEntitiesTest();
    List<PersonEntity> getPersonEntities(Pageable pageable);

	//PersonQuestionnaireEntity
	void addPersonQuestionnaireEntity(PersonQuestionnaireEntity personQuestionnaireEntity);
	void deletePersonQuestionnaireEntity(long id);
	void deletePersonQuestionnaireEntities();
    void deletePersonQuestionnaireEntities(long questionnaireId);
    boolean personQuestionnaireEntityExists(long id);
    long personQuestionnaireEntitiesCount();
    long completedPersonQuestionnaireEntitiesCount(long id);
    long startedPersonQuestionnaireEntitiesCount(long id);
    long personQuestionnaireCountByStatus(String status);
    long startedQuestionnaireEntitiesCount();
    long completedQuestionnaireEntitiesCount();
	PersonQuestionnaireEntity getPersonQuestionnaireEntity(long id);
    PersonQuestionnaireEntity getPersonQuestionnaireEntity(long personQuestionnaireId, long personId);
    PersonQuestionnaireEntity getPersonQuestionnaireByPersonQuestion(long id);
    List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities();
    List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities(String status);
    List<PersonQuestionnaireEntity> getPersonQuestionnaireEntitiesByQuestionnaireName(String name);
    List<PersonQuestionnaireEntity> getPersonQuestionnairesInCheckingProcess(Pageable pageable);
    List<PersonQuestionnaireEntity> getCompletedPersonQuestionnaireEntities(long id, Pageable pageable);
    List<PersonQuestionnaireEntity> getStartedPersonQuestionnaireEntities(long id, Pageable pageable);
	
	//PersonQuestionEntity
	void addPersonQuestionEntity(PersonQuestionEntity personQuestionEntity);
	void deletePersonQuestionEntity(long id);
	void deletePersonQuestionEntities();
    boolean personQuestionEntityExists(long id);
    long personQuestionEntitiesCount();
    long notCheckedPersonQuestionEntitiesCount();
	PersonQuestionEntity getPersonQuestionEntity(long id);
	List<PersonQuestionEntity> getPersonQuestionEntities();
    List<PersonQuestionEntity> getPersonQuestionEntitiesByPersonEntityId(long id);
    List<PersonQuestionEntity> getNotCheckedPersonQuestionEntities(Pageable pageable);
	
	//PersonAnswerEntity
	void addPersonAnswerEntity(PersonAnswerEntity personAnswerEntity);
	void deletePersonAnswerEntity(long id);
	void deletePersonAnswerEntities();
    boolean personAnswerEntityExists(long id);
    long personAnswerEntitiesCount();
	PersonAnswerEntity getPersonAnswerEntity(long id);
	List<PersonAnswerEntity> getPersonAnswerEntities();
	
	//QuestionnaireEntity
	void addQuestionnaireEntity(QuestionnaireEntity questionnaireEntity);
	void deleteQuestionnaireEntity(long id);
	void deleteQuestionnaireEntities();
    boolean questionnaireEntityExists(long id);
    long questionnaireEntitiesCount();
    long publishedQuestionnaireEntitiesCount(boolean published);
	QuestionnaireEntity getQuestionnaireEntity(long id);
	QuestionnaireEntity getQuestionnaireEntity(String name);
    QuestionnaireEntity getQuestionnaireEntity(boolean published, long id);
    QuestionnaireEntity getPopularQuestionnaireEntity();
	List<QuestionnaireEntity> getQuestionnaireEntities();
    List<QuestionnaireEntity> getQuestionnaireEntities(Pageable pageable);
    List<QuestionnaireEntity> getQuestionnaireEntities(boolean published, Pageable pageable);
    LocalDateTime getLastQuestionnaireEntityCreatedDate();
	
	//QuestionEntity
	void addQuestionEntity(QuestionEntity questionEntity);
	void deleteQuestionEntity(long id);
	void deleteQuestionEntities();
    boolean questionEntityExists(long id);
    long questionEntitiesCount();
	QuestionEntity getQuestionEntity(long id);
	List<QuestionEntity> getQuestionEntities();
	
	//AnswerEntity
	void addAnswerEntity(AnswerEntity answerEntity);
	void deleteAnswerEntity(long id);
	void deleteAnswerEntities();
    boolean answerEntityExists(long id);
    long answerEntitiesCount();
	AnswerEntity getAnswerEntity(long id);
	List<AnswerEntity> getAnswerEntities();

    //VerificationTokenEntity
    void addVerificationTokenEntity(VerificationTokenEntity verificationTokenEntity);
    void deleteVerificationTokenEntity(long id);
    void deleteAllVerificationTokenEntity();
    void deleteExpired();
    void deleteVerified();
    void deleteVerificationTokenEntityByPersonEntityId(long id);
    boolean verificationTokenEntityExists(String token);
    VerificationTokenEntity getVerificationTokenEntity(String email);
    VerificationTokenEntity getVerificationTokenEntityConstructor(String email);
    VerificationTokenEntity getVerificationTokenEntityByToken(String token);
    VerificationTokenEntity getVerificationTokenEntity(long id);
    List<VerificationTokenEntity> getVerificationTokenEntities();
}
