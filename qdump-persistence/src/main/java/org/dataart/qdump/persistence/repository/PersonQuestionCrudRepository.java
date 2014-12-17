package org.dataart.qdump.persistence.repository;

import java.util.List;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link PersonQuestionEntity}
 * 
 * @author Ibrichak
 *
 */

public interface PersonQuestionCrudRepository extends
		CrudRepository<PersonQuestionEntity, Long> {

	/**
	 * This method return PersonQuestion by correct.
	 * 
	 * @param correct
	 *            {@link PersonQuestionEntity#isCorrect()}
	 * @return
	 */
	public List<PersonQuestionEntity> getCorrectQuestion(boolean correct);

	/**
	 * This method return PersonQuestion by PersonQuestionnaireId.
	 * 
	 * @param personQuestionnaireId
	 *            id of PersonQuestionnaire
	 * @return list {@link PersonQuestionEntity}
	 */
	public List<PersonQuestionEntity> getQuestionByPersonQuestionnaireId(
			long personQuestionnaireId);

}
