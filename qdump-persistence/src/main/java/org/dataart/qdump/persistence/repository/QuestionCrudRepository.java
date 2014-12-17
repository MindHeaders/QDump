package org.dataart.qdump.persistence.repository;

import java.util.List;

import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link QuestionEntity}
 * 
 * @author Ibrichak
 *
 */

public interface QuestionCrudRepository extends
		CrudRepository<QuestionEntity, Long> {

	/**
	 * This method return Question by name.
	 * 
	 * @param name
	 *            {@link QuestionEntity#getName()}
	 * @return
	 */
	public List<QuestionEntity> getQuestionByName(String name);

	/**
	 * This method return Question by questionnaireId.
	 * 
	 * @param questionnaireId
	 *            id of questionnaire.
	 * 
	 * @return list  {@link QuestionEntity}
	 */
	public List<QuestionEntity> getQuestionByQuestionnaireId(
			Long questionnaireId);

}
