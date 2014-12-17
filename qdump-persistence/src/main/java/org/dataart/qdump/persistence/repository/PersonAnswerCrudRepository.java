package org.dataart.qdump.persistence.repository;

import java.util.List;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link PersonAnswerEntity}
 * 
 * @author Ibrichak
 *
 */

public interface PersonAnswerCrudRepository extends
		CrudRepository<PersonAnswerEntity, Long> {

	/**
	 * This method return PersonAnswer by personQuestionId.
	 * 
	 * @param personQuestionId
	 *            id of personQuestion
	 * @return list  {@link PersonAnswerEntity}
	 */
	public List<PersonAnswerEntity> getPersonAnswerByPersonQuestionId(Long personQuestionId);

	

}
