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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
	public void deleteAllPersonEntities() {
		personCrudRepository.deleteAll();
	}

	@Override
	public PersonEntity getPersonEntity(long id) {
		return personCrudRepository.findOne(id);
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
	public List<PersonEntity> getPersonEntities() {
		return (List<PersonEntity>) personCrudRepository.findAll();
	}

	@Override
	public PersonEntity getPersonEntityAuthorization(String login) {
		return personCrudRepository.findByLoginForAuth(login);
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
    public String getPersonEntityRole(long id) {
        return personCrudRepository.findPersonRole(id);
    }

    @Override
    public Page<PersonEntity> getPersonQuestionnairesInCheckingProcess(Pageable pageable) {
        return personCrudRepository.findPersonQuestionnairesInCheckingProcess(pageable);
    }

    @Override
    public Page<PersonEntity> getPersonEntitiesForAdminPanel(Pageable pageable) {
        return personCrudRepository.findForAdminPanel(pageable);
    }

    @Override
    public PersonEntity getPersonEntityForAccountPanel(long id) {
        return personCrudRepository.findById(id);
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
	public void deleteAllPersonQuestionnaireEntities() {
		personQuestionnaireCrudRepository.deleteAll();
	}

	@Override
	public PersonQuestionnaireEntity getPersonQuestionnaireEntity(long id) {
		return personQuestionnaireCrudRepository.findOne(id);
	}
	
	@Override
	public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities() {
		return (List<PersonQuestionnaireEntity>) personQuestionnaireCrudRepository.findAll();
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
    public PersonQuestionnaireEntity getPersonQuestionnaireEntity(long personQuestionnaireId, long personId) {
        return personQuestionnaireCrudRepository.findByPersonQuestionnaireIdAndPersonId(personQuestionnaireId, personId);
    }

    @Override
    public Page<PersonQuestionnaireEntity> getCompletedPersonQuestionnaireEntities(long id, Pageable pageable) {
        return personQuestionnaireCrudRepository.findCompletedByPersonId(id, pageable);
    }

    @Override
    public Page<PersonQuestionnaireEntity> getStartedPersonQuestionnaireEntities(long id, Pageable pageable) {
        return personQuestionnaireCrudRepository.findStartedByPersonId(id, pageable);
    }

    @Override
    public long countCompletedPersonQuestionnaireEntities(long id) {
        return personQuestionnaireCrudRepository.countCompletedByPersonId(id);
    }

    @Override
    public long countStartedPersonQuestionnaireEntities(long id) {
        return personQuestionnaireCrudRepository.countStartedByPersonId(id);
    }

    @Override
    public long countPersonQuestionnaireByStatus(String status) {
        return personQuestionnaireCrudRepository.countByStatus(status);
    }

    @Override
    public PersonQuestionnaireEntity getPersonQuestionnaireByPersonQuestion(long id) {
        return personQuestionnaireCrudRepository.findByPersonQuestionId(id);
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
	public boolean personQuestionEntityExists(long id) {
		return personQuestionCrudRepository.exists(id);
	}

	@Override
	public long personQuestionEntitiesCount() {
		return personQuestionCrudRepository.count();
	}

    @Override
    public List<PersonQuestionEntity> getPersonQuestionEntitiesByPersonEntityId(long id) {
        return personQuestionCrudRepository.findByPersonId(id);
    }

    @Override
    public long countNotCheckedPersonQuestionEntities() {
        return personQuestionCrudRepository.countNotCheckedQuestions();
    }

    @Override
    public Page<PersonQuestionEntity> getNotCheckedPersonQuestionEntities(Pageable pageable) {
        return personQuestionCrudRepository.findNotCheckedQuestions(pageable);
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
        return questionnaireCrudRepository.findQuestionnaireById(id);
	}

	@Override
	public List<QuestionnaireEntity> getQuestionnaireEntities() {
		return (List<QuestionnaireEntity>) questionnaireCrudRepository.findAll();
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
    public Page<QuestionnaireEntity> getPublishedQuestionnaireEntities(Pageable pageable) {
        return questionnaireCrudRepository.findPublishedQuestionnaires(pageable);
    }

    @Override
    public long countPublishedQuestionnaireEntities() {
        return questionnaireCrudRepository.countPublishedQuestionnaires();
    }

    @Override
    public QuestionnaireEntity getPublishedQuestionnaireEntities(long id) {
        return questionnaireCrudRepository.findPublishedQuestionnairesById(id);
    }

    @Override
    public Page<QuestionnaireEntity> getAllQuestionnaireEntities(Pageable pageable) {
        return questionnaireCrudRepository.findAllQuestionnaires(pageable);
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

    @Override
    public void deleteQuestionEntity(QuestionEntity questionEntity) {
        questionCrudRepository.delete(questionEntity);
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
	public boolean answerEntityExists(long id) {
		return answerCrudRepository.exists(id);
	}

	@Override
	public long answerEntitiesCount() {
		return answerCrudRepository.count();
	}

    //VerificationTokenEntity


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
    public VerificationTokenEntity getVerificationTokenEntity(long id) {
        return verificationTokenCrudRepository.findOne(id);
    }

    @Override
    public List<VerificationTokenEntity> getVerificationTokenEntities() {
        return (List<VerificationTokenEntity>)verificationTokenCrudRepository.findAll();
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
    public void deleteExpired() {
        verificationTokenCrudRepository.deleteExpired();
    }

    @Override
    public void deleteVerified() {
        verificationTokenCrudRepository.deleteVerified();
    }

    @Override
    public VerificationTokenEntity getVerificationTokenEntityByToken(String token) {
        return verificationTokenCrudRepository.findByToken(token);
    }

    @Override
    public VerificationTokenEntity getVerificationTokenEntityConstructor(String email) {
        return verificationTokenCrudRepository.findByPersonEntityEmailConstructor(email);
    }
}
