package org.dataart.qdump.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import org.dataart.qdump.entities.person.PersonEntity;
import org.springframework.stereotype.Component;

@Component
public class AutowiredTest {
	@PersistenceContext(unitName = "qdump-persistence")
	private EntityManagerFactory factory;
	
	public void persistObject(PersonEntity entity) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			entityManager.persist(entity);
		} finally {
			entityManager.close();
		}
	}
	public PersonEntity findObject(long id) {
		EntityManager entityManager = factory.createEntityManager();
		try {
			return entityManager.find(PersonEntity.class, id);
		} finally {
			entityManager.close();
		}
	}
	public static void main(String[] args) {
		AutowiredTest autowiredTest = new AutowiredTest();
		PersonEntity entity = new PersonEntity("vlasovartem21@gmail.com", "422617");
		autowiredTest.persistObject(entity);
		System.out.println(autowiredTest.findObject(2l));
	}
}
