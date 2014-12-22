package org.dataart.qdump.service.impl;

import java.util.List;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
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

	@Override
	public void deleteAllPersonEntities() {
		personCrudRepository.deleteAll();
	}

	@Override
	public PersonEntity getPersonEntity(long id) {
		return personCrudRepository.findOne(id);
	}

	@Override
	public PersonEntity getPersonByEmail(String email) {
		return personCrudRepository.getPersonByEmail(email);
	}

	@Override
	public PersonEntity getPersonByLogin(String login) {
		return personCrudRepository.getPersonByLogin(login);
	}
	
	@Override
	public List<PersonEntity> getPersonEntities() {
		return (List<PersonEntity>) personCrudRepository.findAll();
	}
	
	@Override
	public List<PersonEntity> getPersonEntitiesForAdminPanel() {
		return personCrudRepository.getPersonEntitiesForAdminPanel();
	}
	
	@Override
	public PersonEntity getPersonByLoginForAuth(String login) {
		return personCrudRepository.getPersonByLoginForAuth(login);
	}
	
	@Override
	public boolean personEntityExists(long id) {
		return personCrudRepository.exists(id);
	}

	@Override
	public long personEntitiesCount() {
		return personCrudRepository.count();
	}
	
	@Override
	public boolean existsByLogin(String login) {
		return personCrudRepository.existsByLogin(login);
	}
	
	@Override
	public boolean existsByEmail(String email) {
		return personCrudRepository.existsByEmail(email);
	}
	
	@Override
	public String getPersonPasswordByLogin(String login) {
		return personCrudRepository.getPersonPasswordByLogin(login);
	}
	
	//PersonQuestionnaireEntity
	@Override
	public void addPersonQuestionnaireEntity(
			PersonQuestionnaireEntity personQuestionnaireEntity) {
		personQuestionnaireCrudRepository.save(personQuestionnaireEntity);
	}

	@Override
	public void deletePersonQuestionnaireEntity(long id) {
		personQuestionnaireCrudRepository.delete(id);		
	}

	@Override
	public void deletePersonQuestionnaireEntityByOwnById(long id) {
		personQuestionnaireCrudRepository.deletePersonQuestionnaireEntityByOwnById(id);
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
	
	@Override
	public boolean personQuestionnaireEntityExists(long id) {
		return personQuestionnaireCrudRepository.exists(id);
	}

	@Override
	public long personQuestionnaireEntitiesCount() {
		return personQuestionnaireCrudRepository.count();
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

	@Override
	public void deletePersonQuestionEntityByPersonQuestionnaireId(long id) {
		personQuestionCrudRepository.deletePersonQuestionEntityByPersonQuestionnaireId(id);;
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
	public List<PersonQuestionEntity> getCorrectQuestion(boolean correct) {
		return (List<PersonQuestionEntity>) personQuestionCrudRepository
				.getCorrectQuestion(correct);
	}
	
	@Override
	public List<PersonQuestionEntity> getQuestionByPersonQuestionnaireId(
			long personQuestionnaireId) {
		return (List<PersonQuestionEntity>) personQuestionCrudRepository
				.getQuestionByPersonQuestionnaireId(personQuestionnaireId);
	}
	
	@Override
	public boolean personQuestionEntityExists(long id) {
		return personQuestionCrudRepository.exists(id);
	}

	@Override
	public long personQuestionEntitiesCount() {
		return personQuestionCrudRepository.count();
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
	public void deleteAllPersonAnswerEntity() {
		personAnswerCrudRepository.deleteAll();
	}

	@Override
	public PersonAnswerEntity getPersonAnswerEntity(long id) {
		return personAnswerCrudRepository.findOne(id);
	}

	@Override
	public List<PersonAnswerEntity> getPersonAnswerEntities() {
		return (List<PersonAnswerEntity>) 
				personAnswerCrudRepository.findAll();
	}

	@Override
	public boolean personAnswerEntityExists(long id) {
		return personAnswerCrudRepository.exists(id);
	}
	
	@Override
	public long personAnswerEntitiesCount() {
		return personAnswerCrudRepository.count();
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
	public void deleteAllQuestionnaireEntity() {
		questionnaireCrudRepository.deleteAll();
	}

	@Override
	public QuestionnaireEntity getQuestionnaireEntity(long id) {
		return questionnaireCrudRepository.findOne(id);
	}

	@Override
	public List<QuestionnaireEntity> getQuestionnaireEntities() {
		return (List<QuestionnaireEntity>) questionnaireCrudRepository
				.findAll();
	}
	
	@Override
	public boolean questionnaireEntityExists(long id) {
		return questionnaireCrudRepository.exists(id);
	}

	@Override
	public long questionnaireEntitiesCount() {
		return questionnaireCrudRepository.count();
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
	public void deleteAllQuestionEntity() {
		questionCrudRepository.deleteAll();
	}

	@Override
	public QuestionEntity getQuestionEntity(long id) {
		return questionCrudRepository.findOne(id);
	}

	@Override
	public List<QuestionEntity> getQuestionEntities() {
		return (List<QuestionEntity>) questionCrudRepository.findAll();
	}
	
	@Override
	public boolean questionEntityExists(long id) {
		return questionCrudRepository.exists(id);
	}

	@Override
	public long questionEntitiesCount() {
		return questionCrudRepository.count();
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
	public void deleteAllAnswerEntity() {
		answerCrudRepository.deleteAll();
	}

	@Override
	public AnswerEntity getAnswerEntity(long id) {
		return answerCrudRepository.findOne(id);
	}

	@Override
	public List<AnswerEntity> getAnswerEntities() {
		return (List<AnswerEntity>) answerCrudRepository.findAll();
	}

	@Override
	public List<AnswerEntity> getAnswerByQuestionId(Long questionId) {
		return answerCrudRepository.getAnswerByQuestionId(questionId);
	}

	@Override
	public List<AnswerEntity> getAnswerByQuestionnaireId(Long questionnaireId) {
		return answerCrudRepository.getAnswerByQuestionnaireId(questionnaireId);
	}

	@Override
	public boolean answerEntityExists(long id) {
		return answerCrudRepository.exists(id);
	}

	@Override
	public long answerEntitiesCount() {
		return answerCrudRepository.count();
	}

	

}
