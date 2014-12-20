package org.dataart.qdump.persistence.repository;

import java.util.List;

import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link PersonEntity}
 * 
 * @author Ibrichak
 *
 */
@Repository()
public interface PersonCrudRepository extends
		CrudRepository<PersonEntity, Long> {

	/**
	 * This method return Person by email.
	 * 
	 * @param email
	 *            {@link PersonEntity#getEmail()}
	 * @return 
	 */
	public PersonEntity getPersonByEmail(String email);

	/**
	 * This method return Person by login.
	 * 
	 * @param login
	 *            {@link PersonEntity#getLogin()}
	 * @return 
	 */
	public PersonEntity getPersonByLogin(String login);
	
	/**
	 * This method return Person by personGroup.
	 * 
	 * @param personGroup
	 *             {@link PersonEntity#getPersonGroup()}
	 * 
	 * @return 
	 */
	public List<PersonEntity> getPersonByPersonGroup(
			PersonGroupEnums persongroup);
	
	/**
	 * Returns all persons from database only with firstname and lastname
	 * @return
	 */
	public List<PersonEntity> getPersonsNameLastname();
	
	/**
	 * Getter for Authorization
	 * @param login
	 * @return
	 */
	public PersonEntity getPersonForAuthByLogin(String login);
}
