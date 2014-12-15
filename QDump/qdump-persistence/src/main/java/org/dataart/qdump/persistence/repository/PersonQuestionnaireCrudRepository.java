package org.dataart.qdump.persistence.repository;

import java.util.List;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link PersonQuestionnaireEntity}
 * 
 * @author Ibrichak
 *
 */

public interface PersonQuestionnaireCrudRepository extends
		CrudRepository<PersonQuestionnaireEntity, Long> {

	/**
	 * This method return PersonQuestionnaire by PersonId.
	 * 
	 * @param ownBy
	 *            {@link PersonQuestionnaireEntity#getOwnBy()}
	 * @return
	 */
	public List<PersonQuestionnaireEntity> getPersonQuestionnaireByPersonId(
			PersonEntity ownBy);

	/**
	 * This method return PersonQuestionnaire by status.
	 * 
	 * @param status
	 *            {@link PersonQuestionnaireEntity#getStatus()}
	 * @return
	 */
	public List<PersonQuestionnaireEntity> getPersonQuestionnaireByStatus(
			String status);

	/**
	 * This method return PersonQuestionnaire by questinnaireName.
	 * 
	 * @param questionnaireName
	 *            name of questionnaire.
	 * 
	 * @return list {@link PersonQuestionnaireEntity}
	 */
	public List<PersonQuestionnaireEntity> getPersonQuestionnaireByQuestinnaireName(
			String questionnaireName);

}
