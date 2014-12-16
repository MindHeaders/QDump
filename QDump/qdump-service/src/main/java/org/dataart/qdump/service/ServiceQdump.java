package org.dataart.qdump.service;

import java.util.List;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;


public interface ServiceQdump {
	//PersonEntity
	void addPersonEntity(PersonEntity personEntity);
	void deletePersonEntity(long id);
	void deletePersonEntityByEmail(String email);
	void deletePersonEntityByLogin(String login);
	void deleteAllPersonEntities();
	PersonEntity getPersonEntity(long id);
	List<PersonEntity> getPersonEntities();
	
	//PersonQuestionnaireEntity
	void addPersonQuestionnaireEntity(PersonQuestionnaireEntity personQuestionnaireEntity);
	void deletePersonQuestionnaireEntity(long id);
	void deletePersonQuestionnaireEntityByOwnBy(String login);
	void deleteAllPersonQuestionnaireEntities();
	PersonQuestionnaireEntity getPersonQuestionnaireEntity(long id);
	List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities();
	
	//PersonQuestionEntity
	void addPersonQuestionEntity(PersonQuestionEntity personQuestionEntity);
	void deletePersonQuestionEntity(long id);
	void deletePersonQuestionEntityByPersonQuestionnaireId(long id);
	void deleteAllPersonQuestionEntity();
	PersonQuestionEntity getPersonQuestionEntity(long id);
	List<PersonQuestionEntity> getPersonQuestionEntities();
	/**
	 * Added the implementation of the repository
	 * PersonQuestionCrudRepository
	 * 
	 */
	List<PersonQuestionEntity> getCorrectQuestion(boolean correct);
	List<PersonQuestionEntity> getQuestionByPersonQuestionnaireId(
			long personQuestionnaireId);
	
	//AnswerEntity
	void addAnswerEntity(AnswerEntity answerEntity);
	void deleteAnswerEntity(long id);
	void deleteAllAnswerEntity();
	AnswerEntity getAnswerEntity(long id);
	List<AnswerEntity> getAnswerEntities();
	
	//QuestionEntity
	void addQuestionEntity(QuestionEntity questionEntity);
	void deleteQuestionEntity(long id);
	void deleteAllQuestionEntity();
	QuestionEntity getQuestionEntity(long id);
	List<QuestionEntity> getQuestionEntities();
	
	//QuestionnaireEntity
	void addQuestionnaireEntity(QuestionnaireEntity questionnaireEntity);
	void deleteQuestionnaireEntity(long id);
	void deleteAllQuestionnaireEntity();
	QuestionnaireEntity getQuestionnaireEntity(long id);
	List<QuestionnaireEntity> getQuestionnaireEntities();
	
	//PersonAnswerEntity
	void addPersonAnswerEntity(PersonAnswerEntity personAnswerEntity);
	void deletePersonAnswerEntity(long id);
	void deleteAllPersonAnswerEntity();
	PersonAnswerEntity getPersonAnswerEntity(long id);
	List<PersonAnswerEntity> getPersonAnswerEntities();
	
}
