package org.dataart.qdump.persistence.repository;

import java.util.List;

import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link AnswerEntity}
 * 
 * @author Ibrichak
 *
 */

public interface AnswerCrudRepository extends
		CrudRepository<AnswerEntity, Long> {

	/**
	 * This method return Answer by questionId.
	 * 
	 * @param questionId
	 *            id of question
	 * @return list  {@link AnswerEntity}
	 */
	public List<AnswerEntity> getAnswerByQuestionId(Long questionId);

	/**
	 * This method return Answer by questionnaireId.
	 * 
	 * @param questionnaireId
	 *            id of questionnaire.
	 * 
	 * @return list  {@link AnswerEntity}
	 */
	public List<AnswerEntity> getAnswerByQuestionnaireId(
			Long questionnaireId);

}
