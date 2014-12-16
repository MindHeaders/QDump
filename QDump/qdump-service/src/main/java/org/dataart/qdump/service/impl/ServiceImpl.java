package org.dataart.qdump.service.impl;

import java.util.List;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.persistence.repository.AnswerCrudRepository;
import org.dataart.qdump.persistence.repository.PersonAnswerCrudRepository;
import org.dataart.qdump.persistence.repository.PersonCrudRepository;
import org.dataart.qdump.persistence.repository.PersonQuestionCrudRepository;
import org.dataart.qdump.persistence.repository.PersonQuestionnaireCrudRepository;
import org.dataart.qdump.persistence.repository.QuestionCrudRepository;
import org.dataart.qdump.persistence.repository.QuestionnaireCrudRepository;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceImpl implements ServiceQdump{
	
	@Autowired
	AnswerCrudRepository answerCrudRepository;
	@Autowired
	PersonAnswerCrudRepository personAnswerCrudRepository;
	@Autowired
	PersonCrudRepository personCrudRepository;
	@Autowired
	PersonQuestionCrudRepository personQuestionCrudRepository;
	@Autowired
	PersonQuestionnaireCrudRepository personQuestionnaireCrudRepository;
	@Autowired
	QuestionCrudRepository questionCrudRepository;
	@Autowired
	QuestionnaireCrudRepository questionnaireCrudRepository;

	
	//PersonEntity
	
	@Override
	public void addPersonEntity(PersonEntity personEntity) {
		personCrudRepository.save(personEntity);
	}

	@Override
	public void deletePersonEntity(long id) {
		personCrudRepository.delete(id);
	}

	/**
	 * This method "deletePersonEntityByEmail(String email)" 
	 *  should be implemented in a repository 
	 *  personCrudRepository
	 *  
	 */
	@Override
	public void deletePersonEntityByEmail(String email) {
		
	}

	/**
	 * This method "deletePersonEntityByLogin(String login)" 
	 *  should be implemented in a repository 
	 *  personCrudRepository
	 *  
	 */
	@Override
	public void deletePersonEntityByLogin(String login) {
		
	}

	@Override
	public void deleteAllPersonEntities() {
		personCrudRepository.deleteAll();
	}

	@Override
	public PersonEntity getPersonEntity(long id) {
		return personCrudRepository.findOne(id);
	}

	@Override
	public List<PersonEntity> getPersonEntities() {
		return (List<PersonEntity>) personCrudRepository.findAll();
	}
	
	//PersonQuestionnaireEntity
	@Override
	public void addPersonQuestionnaireEntity(
			PersonQuestionnaireEntity personQuestionnaireEntity
			) {
		personQuestionnaireCrudRepository.save(
				personQuestionnaireEntity);
	}

	@Override
	public void deletePersonQuestionnaireEntity(long id) {
		personQuestionnaireCrudRepository.delete(id);		
	}

	/**
	 * This method "deletePersonQuestionnaireEntityByOwnBy(String login)" 
	 *  should be implemented in a repository 
	 *  personQuestionnaireCrudRepository
	 *  
	 */
	@Override
	public void deletePersonQuestionnaireEntityByOwnBy(String login) {
		
	}

	@Override
	public void deleteAllPersonQuestionnaireEntities() {
		personQuestionnaireCrudRepository.deleteAll();
		
	}

	@Override
	public PersonQuestionnaireEntity getPersonQuestionnaireEntity(long id) {
		return personQuestionnaireCrudRepository.findOne(id);
	}

	@Override
	public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities() {
		return (List<PersonQuestionnaireEntity>) 
				personQuestionnaireCrudRepository.findAll();
	}

	//PersonQuestionEntity
	
	@Override
	public void addPersonQuestionEntity(
			PersonQuestionEntity personQuestionEntity) {
		personQuestionCrudRepository.save(
				personQuestionEntity);
	}

	@Override
	public void deletePersonQuestionEntity(long id) {
		personQuestionCrudRepository.delete(id);
	}
	
	
	/**
	 * This method "deletePersonQuestionEntityByPersonQuestionnaireId(long id)" 
	 *  should be implemented in a repository 
	 *  personQuestionCrudRepository
	 *  
	 */
	@Override
	public void deletePersonQuestionEntityByPersonQuestionnaireId(long id) {
		
	}
	
	
	@Override
	public void deleteAllPersonQuestionEntity() {
		personQuestionCrudRepository.deleteAll();
	}

	
	@Override
	public PersonQuestionEntity getPersonQuestionEntity(long id) {
		return personQuestionCrudRepository.findOne(id);
	}

	@Override
	public List<PersonQuestionEntity> getPersonQuestionEntities() {
		return (List<PersonQuestionEntity>) 
				personQuestionCrudRepository.findAll();
	}
	
	
	@Override
	public List<PersonQuestionEntity> getCorrectQuestion(boolean correct){
		return (List<PersonQuestionEntity>) 
				personQuestionCrudRepository.
				getCorrectQuestion(correct);
	}
	
	@Override
	public List<PersonQuestionEntity> getQuestionByPersonQuestionnaireId(
			long personQuestionnaireId){
		return (List<PersonQuestionEntity>) 
				personQuestionCrudRepository.
				getQuestionByPersonQuestionnaireId(
						personQuestionnaireId);
	}	
}
