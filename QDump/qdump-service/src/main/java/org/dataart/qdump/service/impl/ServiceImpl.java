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
	
	@Autowired(required = false)
	AnswerCrudRepository answerEntityRepository;
	@Autowired(required = false)
	PersonAnswerCrudRepository personAnswerCrudRepository;
	@Autowired(required = false)
	PersonCrudRepository personCrudRepository;
	@Autowired(required = false)
	PersonQuestionCrudRepository personQuestionCrudRepository;
	@Autowired(required = false)
	PersonQuestionnaireCrudRepository personQuestionnaireCrudRepository;
	@Autowired(required = false)
	QuestionCrudRepository questionCrudRepository;
	@Autowired(required = false)
	QuestionnaireCrudRepository questionnaireCrudRepository;

	@Override
	public void addPersonEntity(PersonEntity personEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePersonEntity(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePersonEntityByEmail(String email) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePersonEntityByLogin(String login) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllPersonEntities() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PersonEntity getPersonEntity(long id) {
		return personCrudRepository.findOne(id);
	}

	@Override
	public List<PersonEntity> getPersonEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPersonQuestionnaireEntity(
			PersonQuestionnaireEntity personQuestionnaireEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePersonQuestionnaireEntity(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePersonQuestionnaireEntityByOwnBy(String login) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllPersonQuestionnaireEntities() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PersonQuestionnaireEntity getPersonQuestionnaireEntity(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPersonQuestionEntity(
			PersonQuestionEntity personQuestionEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePersonQuestionEntity(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePersonQuestionEntityByPersonQuestionnaireId(long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllPersonQuestionEntity() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PersonQuestionEntity getPersonQuestionEntity(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PersonQuestionEntity> getPersonQuestionEntities() {
		// TODO Auto-generated method stub
		return null;
	}
}
