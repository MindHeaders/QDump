package org.dataart.qdump.service;

import java.util.List;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;


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
	
	
}
