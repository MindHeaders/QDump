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
	PersonEntity getPersonByEmail(String email);
	PersonEntity getPersonByLogin(String login);
	List<PersonEntity> getPersonEntitiesForAdminPanel();
	PersonEntity getPersonByLoginForAuth(String login);
	boolean personEntityExists(long id);
	long personEntitiesCount();
	boolean personEntityExistsByLogin(String login);
	boolean personEntityExistsByEmail(String email);
	String getPersonPasswordByLogin(String login);
    boolean personEntityIsEnabledByLogin(String login);
    boolean personEntityIsEnabledByEmail(String email);

	//PersonQuestionnaireEntity
	void addPersonQuestionnaireEntity(PersonQuestionnaireEntity personQuestionnaireEntity);
	void deletePersonQuestionnaireEntity(long id);
	void deletePersonQuestionnaireEntityByOwnById(long id);
	void deleteAllPersonQuestionnaireEntities();
	PersonQuestionnaireEntity getPersonQuestionnaireEntity(long id);
	List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities();
	List<PersonQuestionnaireEntity> findByOwnById(Long id);
	boolean personQuestionnaireEntityExists(long id);
	long personQuestionnaireEntitiesCount();
    Page<PersonQuestionnaireEntity> personQuestionnairePagination(long id, Pageable pageable);
	
	//PersonQuestionEntity
	void addPersonQuestionEntity(PersonQuestionEntity personQuestionEntity);
	void deletePersonQuestionEntity(long id);
	void deleteAllPersonQuestionEntity();
	PersonQuestionEntity getPersonQuestionEntity(long id);
	List<PersonQuestionEntity> getPersonQuestionEntities();
	boolean personQuestionEntityExists(long id);
	long personQuestionEntitiesCount();
	
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
	long questionnaireEntitiesCount();
    Page<QuestionnaireEntity> questionnairesPagination(Pageable pageable);
    long countPublishedQuestionnaires();
	
	//QuestionEntity
	void addQuestionEntity(QuestionEntity questionEntity);
	void deleteQuestionEntity(long id);
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
    VerificationTokenEntity getTokenByPersonEntityEmail(String email);
    VerificationTokenEntity getTokenByPersonEntityEmailConstructor(String email);
    VerificationTokenEntity getVerificationTokenByToken(String token);
    VerificationTokenEntity getVerificationTokenEntity(long id);
    List<VerificationTokenEntity> getVerificationTokenEntities();

}
