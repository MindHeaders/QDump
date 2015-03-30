package org.dataart.qdump.service;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ServiceQdump {
	//PersonEntity
	void addPersonEntity(PersonEntity personEntity);
	void deletePersonEntity(long id);
	void deleteAllPersonEntities();
	PersonEntity getPersonEntity(long id);
	List<PersonEntity> getPersonEntities();
	PersonEntity getPersonEntityByEmail(String email);
	PersonEntity getPersonEntityByLogin(String login);
	List<PersonEntity> getPersonEntitiesForAdminPanel();
	PersonEntity getPersonEntityAuthorization(String login);
	boolean personEntityExists(long id);
	long personEntitiesCount();
	boolean personEntityExistsByLogin(String login);
	boolean personEntityExistsByEmail(String email);
	String getPersonEntityPasswordByLogin(String login);
    boolean personEntityIsEnabledByLogin(String login);
    boolean personEntityIsEnabledByEmail(String email);
    String getPersonEntityRole(long id);
    Page<PersonEntity> getPersonQuestionnairesInCheckingProcess(Pageable pageable);

	//PersonQuestionnaireEntity
	void addPersonQuestionnaireEntity(PersonQuestionnaireEntity personQuestionnaireEntity);
	void deletePersonQuestionnaireEntity(long id);
	void deleteAllPersonQuestionnaireEntities();
	PersonQuestionnaireEntity getPersonQuestionnaireEntity(long id);
	List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities();
	boolean personQuestionnaireEntityExists(long id);
	long personQuestionnaireEntitiesCount();
    PersonQuestionnaireEntity getPersonQuestionnaireEntity(long personQuestionnaireId, long personId);
    Page<PersonQuestionnaireEntity> getCompletedPersonQuestionnaireEntities(long id, Pageable pageable);
    Page<PersonQuestionnaireEntity> getStartedPersonQuestionnaireEntities(long id, Pageable pageable);
    long countCompletedPersonQuestionnaireEntities(long id);
    long countStartedPersonQuestionnaireEntities(long id);
    long countPersonQuestionnaireByStatus(String status);
	
	//PersonQuestionEntity
	void addPersonQuestionEntity(PersonQuestionEntity personQuestionEntity);
	void deletePersonQuestionEntity(long id);
	void deleteAllPersonQuestionEntity();
	PersonQuestionEntity getPersonQuestionEntity(long id);
	List<PersonQuestionEntity> getPersonQuestionEntities();
	boolean personQuestionEntityExists(long id);
	long personQuestionEntitiesCount();
    List<PersonQuestionEntity> getPersonQuestionEntitiesByPersonEntityId(long id);
	
	//PersonAnswerEntity
	void addPersonAnswerEntity(PersonAnswerEntity personAnswerEntity);
	void deletePersonAnswerEntity(long id);
	void deleteAllPersonAnswerEntity();
	PersonAnswerEntity getPersonAnswerEntity(long id);
	List<PersonAnswerEntity> getPersonAnswerEntities();
	boolean personAnswerEntityExists(long id);
	long personAnswerEntitiesCount();
	
	//QuestionnaireEntity
	void addQuestionnaireEntity(QuestionnaireEntity questionnaireEntity);
	void deleteQuestionnaireEntity(long id);
	void deleteAllQuestionnaireEntity();
	QuestionnaireEntity getQuestionnaireEntity(long id);
	List<QuestionnaireEntity> getQuestionnaireEntities();
	boolean questionnaireEntityExists(long id);
    Page<QuestionnaireEntity> getAllQuestionnaireEntities(Pageable pageable);
	long questionnaireEntitiesCount();
    Page<QuestionnaireEntity> getPublishedQuestionnaireEntities(Pageable pageable);
    long countPublishedQuestionnaireEntities();
    QuestionnaireEntity getPublishedQuestionnaireEntities(long id);
	
	//QuestionEntity
	void addQuestionEntity(QuestionEntity questionEntity);
	void deleteQuestionEntity(long id);
    void deleteQuestionEntity(QuestionEntity questionEntity);
	void deleteAllQuestionEntity();
	QuestionEntity getQuestionEntity(long id);
	List<QuestionEntity> getQuestionEntities();
	boolean questionEntityExists(long id);
	long questionEntitiesCount();
	
	//AnswerEntity
	void addAnswerEntity(AnswerEntity answerEntity);
	void deleteAnswerEntity(long id);
	void deleteAllAnswerEntity();
	AnswerEntity getAnswerEntity(long id);
	List<AnswerEntity> getAnswerEntities();
	boolean answerEntityExists(long id);
	long answerEntitiesCount();

    //VerificationTokenEntity
    void addVerificationTokenEntity(VerificationTokenEntity verificationTokenEntity);
    void deleteVerificationTokenEntity(long id);
    void deleteAllVerificationTokenEntity();
    void deleteExpired();
    void deleteVerified();
    boolean verificationTokenEntityExists(String token);
    VerificationTokenEntity getVerificationTokenEntity(String email);
    VerificationTokenEntity getVerificationTokenEntityConstructor(String email);
    VerificationTokenEntity getVerificationTokenEntityByToken(String token);
    VerificationTokenEntity getVerificationTokenEntity(long id);
    List<VerificationTokenEntity> getVerificationTokenEntities();

}
