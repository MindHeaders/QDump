package org.dataart.qdump.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.enums.QuestionTypeEnums;
import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JpaTest {
	public static void main(String[] args) throws IOException, IntrospectionException, NamingException {
		
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("qdump-persistence");
		EntityManager em = emf.createEntityManager();
		ObjectMapper mapper = new ObjectMapper();
		try {
			
			em.getTransaction().begin();
			PersonEntity entity = new PersonEntity();
			entity.setEmail("testadminemail@mail.com");
			entity.setEnabled(true);
			entity.setFirstname("testadminfirstname");
			entity.setGender((byte) 1);
			entity.setLastname("testadminlastname");
			entity.setLogin("testadminlogin");
			entity.setPassword(BCrypt.hashpw("testadminpassword", BCrypt.gensalt()));
			entity.setPersonGroup(PersonGroupEnums.ADMIN);
			em.persist(entity);
			Query query = em.createNamedQuery("PersonEntity.getPersonByLogin");
			query.setParameter(1, "testadminlogin");
			PersonEntity entity2 = (PersonEntity) query.getSingleResult();
			System.out.println(BCrypt.checkpw("testadminpassword", entity2.getPassword()));
			/*List<PersonEntity> entities = em.createNamedQuery(
					"PersonEntity.getPersonsNameLastname", PersonEntity.class)
					.getResultList();
			entities.stream().forEach(
					entity -> System.out.println(String.format(
							"Firstname = %s, Lastname = %s, id = %d",
							entity.getFirstname(), entity.getLastname(), entity.getId())));
			entities.stream().forEach(
					entity -> System.out.println(entity.toString()));*/
			/*TypedQuery<AnswerEntity> query = em.createNamedQuery("AnswerEntity.getAnswerByQuestionId", AnswerEntity.class);
			query.setParameter(1, 13l);
			List<AnswerEntity> answerEntities = query.getResultList();
			answerEntities.stream().forEach(entity -> System.out.println(entity.toString()));*/
			em.getTransaction().commit();
		} finally {
			if (em.isOpen()) {
				em.close();
			}
			emf.close();
		}
	}

	public static AnswerEntity createAnswerEntity(String answer,
			boolean isCorrect) {
		AnswerEntity answerEntity = new AnswerEntity();
		answerEntity.setAnswer(answer);
		answerEntity.setCorrect(isCorrect);
		return answerEntity;
	}

	public static QuestionEntity createQuestionEntity(
			List<AnswerEntity> answerEntities, String question,
			QuestionTypeEnums type) {
		QuestionEntity questionEntity = new QuestionEntity();
		questionEntity.setQuestion(question);
		questionEntity.setType(type);
		for (AnswerEntity answerEntity : answerEntities) {
			answerEntity.setQuestionEntity(questionEntity);
		}
		questionEntity.setAnswerEntities(answerEntities);
		return questionEntity;
	}

	public static QuestionnaireEntity createQuestionnaireEntity(
			PersonEntity personEntity, List<QuestionEntity> questionEntities,
			String description, String name, boolean isPublished) {
		QuestionnaireEntity questionnaireEntity = new QuestionnaireEntity();
		questionnaireEntity.setCreatedBy(personEntity);
		questionnaireEntity.setDescription(description);
		questionnaireEntity.setModifiedBy(personEntity);
		questionnaireEntity.setName(name);
		questionnaireEntity.setPublished(isPublished);
		for (QuestionEntity questionEntity : questionEntities) {
			questionEntity.setQuestionnaireEntity(questionnaireEntity);
		}
		questionnaireEntity.setQuestionEntities(questionEntities);
		return questionnaireEntity;
	}

	public static PersonEntity createPersonEntity(String email, String login,
			String password, String surname, String name, boolean isEnabled,
			byte gender) {
		PersonEntity personEntity = new PersonEntity();
		personEntity.setEmail(email);
		personEntity.setEnabled(isEnabled);
		personEntity.setGender(gender);
		personEntity.setLogin(login);
		personEntity.setFirstname(name);
		personEntity.setPassword(password);
		personEntity.setPersonGroup(PersonGroupEnums.USER);
		personEntity.setLastname(surname);
		return personEntity;
	}

	public static PersonAnswerEntity createPersonAnswerEntity(
			AnswerEntity answerEntity, String personAnswer, boolean marked) {
		PersonAnswerEntity personAnswerEntity = new PersonAnswerEntity();
		personAnswerEntity.setAnswerEntity(answerEntity);
		personAnswerEntity.setPersonAnswer(personAnswer);
		personAnswerEntity.setMarked(marked);
		return personAnswerEntity;
	}

	public static PersonQuestionEntity createPersonQuestionEntity(
			List<PersonAnswerEntity> personAnswerEntities,
			QuestionEntity questionEntity) {
		PersonQuestionEntity personQuestionEntity = new PersonQuestionEntity();
		personQuestionEntity.setQuestionEntity(questionEntity);
		personQuestionEntity.setPersonAnswerEntities(personAnswerEntities);
		return personQuestionEntity;
	}

	public static PersonQuestionnaireEntity createPersonQuestionnaireEntity(
			List<PersonQuestionEntity> personQuestionEntities,
			QuestionnaireEntity questionnaireEntity, PersonEntity ownBy) {
		PersonQuestionnaireEntity personQuestionnaireEntity = new PersonQuestionnaireEntity();
		personQuestionnaireEntity.setQuestionnaireEntity(questionnaireEntity);
		List<PersonQuestionnaireEntity> personQuestionnaireEntities;
		if (ownBy.getPersonQuestionnaireEntities() != null) {
			personQuestionnaireEntities = ownBy
					.getPersonQuestionnaireEntities();
			personQuestionnaireEntities.add(personQuestionnaireEntity);
		} else {
			personQuestionnaireEntities = new ArrayList<PersonQuestionnaireEntity>();
			personQuestionnaireEntities.add(personQuestionnaireEntity);
		}
		ownBy.setPersonQuestionnaireEntities(personQuestionnaireEntities);
		personQuestionnaireEntity.setOwnBy(ownBy);
		for (PersonQuestionEntity personQuestionEntity : personQuestionEntities) {
			personQuestionEntity
					.setPersonQuestionnaireEntity(personQuestionnaireEntity);
		}
		personQuestionnaireEntity
				.setPersonQuestionEntities(personQuestionEntities);
		return personQuestionnaireEntity;
	}

	public static void persistCollection(List<?> collection, EntityManager em) {
		for (int i = 0; i < collection.size(); i++) {
			em.persist(collection.get(i));
		}
	}
}
