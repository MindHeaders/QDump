package org.dataart.qdump.persistence;

import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

public class JpaTest {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("qdump-persistence");
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT NEW PersonQuestionnaireEntity(pq.id, pq.status, q.id, q.name, pq.createdDate, pq.modifiedDate) " +
                    "FROM PersonQuestionnaireEntity pq, PersonEntity p " +
                    "LEFT JOIN pq.questionnaireEntity q " +
                    "WHERE pq MEMBER OF p.personQuestionnaireEntities " +
                    "AND p.id = ?1 " +
                    "AND pq.status NOT IN ('in progress', 'not specified')");
            query.setParameter(1, 1l);
            List<PersonQuestionnaireEntity> personQuestionnaireEntities = query.getResultList();
            System.out.println(personQuestionnaireEntities.size());

//            PersonEntity personEntity = (PersonEntity) em.createQuery("SELECT p FROM PersonEntity p WHERE p.id = 1")
//                    .getSingleResult();
//            for(int i = 0; i < 10; i++) {
//                QuestionnaireEntity questionnaireEntity = new QuestionnaireEntity();
//                PersonQuestionnaireEntity personQuestionnaireEntity = new PersonQuestionnaireEntity();
//                personQuestionnaireEntity.setStatus(QuestionnaireStatusEnums.IN_CHECKING_PROCESS.getName());
//                personQuestionnaireEntity.setQuestionnaireEntity(questionnaireEntity);
//                QuestionEntity questionEntity = new QuestionEntity();
//                questionEntity.setType(QuestionTypeEnums.TEXTAREA);
//                PersonQuestionEntity personQuestionEntity = new PersonQuestionEntity();
//                personQuestionEntity.setQuestionEntity(questionEntity);
//                PersonAnswerEntity personAnswerEntity = new PersonAnswerEntity();
//                personQuestionEntity.setPersonAnswerEntities(Arrays.asList(personAnswerEntity));
//                questionnaireEntity.setName("This is the test questionnaire #" + (i + 1));
//                questionnaireEntity.setPublished(true);
//                questionEntity.setQuestion("This is the test question #" + (i + 1));
//                questionnaireEntity.setQuestionEntities(Arrays.asList(questionEntity));
//                personQuestionEntity.getPersonAnswerEntities().get(0).setPersonAnswer("This is the test person answer" +
//                        " #" + (i + 1));
//                personQuestionnaireEntity.setPersonQuestionEntities(Arrays.asList(personQuestionEntity));
//                em.persist(questionnaireEntity);
//                personEntity.getPersonQuestionnaireEntities().add(personQuestionnaireEntity);
//            }
            em.getTransaction().commit();
        } finally {
            if (em.isOpen()) {
                em.close();
            }
            emf.close();
        }
    }
//	public static void main(String[] args) throws IOException, IntrospectionException, NamingException {
//
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//            em.getTransaction().begin();
//            TestClassB testClassB = new TestClassB();
//            testClassB.setName("This is the first test class b");
//            TestClassB testClassB1 = new TestClassB();
//            testClassB1.setName("This is the second test class b");
//            TestClassA testClassA = new TestClassA();
//            testClassA.setName("This is the first test class a");
//            List<TestClassB> testClassBs = Arrays.asList(testClassB, testClassB1);
//            testClassA.setTestClassBs(testClassBs);
//            em.persist(testClassB);
//            em.persist(testClassB1);
//            em.persist(testClassA);
//            System.out.println(testClassA);


    //            VerificationTokenEntity verificationTokenEntity = (VerificationTokenEntity) query.getSingleResult();
//            BeanWrapper wrapper = new BeanWrapperImpl(verificationTokenEntity);
//            for(PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()) {
//                String propName = descriptor.getName();
//                System.out.println("propName : " + propName + "\nValue : " + wrapper.getPropertyValue(propName));
//            }
//            VerificationTokenEntity tokenEntity2 = em.find(VerificationTokenEntity.class, 2l);
//            wrapper = new BeanWrapperImpl(tokenEntity2);
//            for(PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()) {
//                String propName = descriptor.getName();
//                System.out.println("propName : " + propName + "\nValue : " + wrapper.getPropertyValue(propName));
//            }
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
//			em.getTransaction().commit();
//		} finally {
//			if (em.isOpen()) {
//				em.close();
//			}
//            emf.close();
//		}
//	}
//
//	public static AnswerEntity createAnswerEntity(String answer,
//			boolean isCorrect) {
//		AnswerEntity answerEntity = new AnswerEntity();
//		answerEntity.setAnswer(answer);
//		answerEntity.setCorrect(isCorrect);
//		return answerEntity;
//	}
//
//	public static QuestionEntity createQuestionEntity(
//			List<AnswerEntity> answerEntities, String question,
//			QuestionTypeEnums type) {
//		QuestionEntity questionEntity = new QuestionEntity();
//		questionEntity.setQuestion(question);
//		questionEntity.setType(type);
//		questionEntity.setAnswerEntities(answerEntities);
//		return questionEntity;
//	}
//
//	public static QuestionnaireEntity createQuestionnaireEntity(
//			PersonEntity personEntity, List<QuestionEntity> questionEntities,
//			String description, String name, boolean isPublished) {
//		QuestionnaireEntity questionnaireEntity = new QuestionnaireEntity();
//		questionnaireEntity.setCreatedBy(personEntity);
//		questionnaireEntity.setDescription(description);
//		questionnaireEntity.setModifiedBy(personEntity);
//		questionnaireEntity.setName(name);
//		questionnaireEntity.setPublished(isPublished);
//		questionnaireEntity.setQuestionEntities(questionEntities);
//		return questionnaireEntity;
//	}
//
//	public static PersonEntity createPersonEntity(String email, String login,
//			String password, String surname, String name, boolean isEnabled,
//			byte gender) {
//		PersonEntity personEntity = new PersonEntity();
//		personEntity.setEmail(email);
//		personEntity.setEnabled(isEnabled);
//		personEntity.setGender(gender);
//		personEntity.setLogin(login);
//		personEntity.setFirstname(name);
//		personEntity.setPassword(password);
//		personEntity.setPersonGroup(PersonGroupEnums.USER);
//		personEntity.setLastname(surname);
//		return personEntity;
//	}
//
//	public static PersonAnswerEntity createPersonAnswerEntity(
//			AnswerEntity answerEntity, String personAnswer, boolean marked) {
//		PersonAnswerEntity personAnswerEntity = new PersonAnswerEntity();
//		personAnswerEntity.setAnswerEntity(answerEntity);
//		personAnswerEntity.setPersonAnswer(personAnswer);
//		personAnswerEntity.setMarked(marked);
//		return personAnswerEntity;
//	}
//
//	public static PersonQuestionEntity createPersonQuestionEntity(
//			List<PersonAnswerEntity> personAnswerEntities,
//			QuestionEntity questionEntity) {
//		PersonQuestionEntity personQuestionEntity = new PersonQuestionEntity();
//		personQuestionEntity.setQuestionEntity(questionEntity);
//		personQuestionEntity.setPersonAnswerEntities(personAnswerEntities);
//		return personQuestionEntity;
//	}
//
//	public static PersonQuestionnaireEntity createPersonQuestionnaireEntity(
//			List<PersonQuestionEntity> personQuestionEntities,
//			QuestionnaireEntity questionnaireEntity, PersonEntity ownBy) {
//		PersonQuestionnaireEntity personQuestionnaireEntity = new PersonQuestionnaireEntity();
//		personQuestionnaireEntity.setQuestionnaireEntity(questionnaireEntity);
//		List<PersonQuestionnaireEntity> personQuestionnaireEntities;
//		if (ownBy.getPersonQuestionnaireEntities() != null) {
//			personQuestionnaireEntities = ownBy
//					.getPersonQuestionnaireEntities();
//			personQuestionnaireEntities.add(personQuestionnaireEntity);
//		} else {
//			personQuestionnaireEntities = new ArrayList<PersonQuestionnaireEntity>();
//			personQuestionnaireEntities.add(personQuestionnaireEntity);
//		}
//		ownBy.setPersonQuestionnaireEntities(personQuestionnaireEntities);
//		personQuestionnaireEntity
//				.setPersonQuestionEntities(personQuestionEntities);
//		return personQuestionnaireEntity;
//	}
//
//	public static void persistCollection(List<?> collection, EntityManager em) {
//		for (int i = 0; i < collection.size(); i++) {
//			em.persist(collection.get(i));
//		}
//	}
//    public static void main(String[] args) {
//        AnswerEntity sameAnswer = new AnswerEntity();
//        sameAnswer.setAnswer("Hello");
//        AnswerEntity answerEntity = new AnswerEntity();
//        answerEntity.setAnswer("Name");
//        QuestionEntity questionEntity = new QuestionEntity();
//        questionEntity.setAnswerEntities(Arrays.asList(answerEntity, sameAnswer));
//        QuestionEntity questionEntity1 = new QuestionEntity();
//        AnswerEntity answerEntity1 = new AnswerEntity();
//        answerEntity1.setAnswer("Boo");
//        questionEntity1.setAnswerEntities(Arrays.asList(answerEntity1, sameAnswer));
//        EntitiesUpdater updater = new EntitiesUpdater();
//        updater.update(questionEntity1, questionEntity);
//    }
}
