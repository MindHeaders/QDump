package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionnaireCrudRepository extends
		CrudRepository<QuestionnaireEntity, Long> {

	public List<QuestionnaireEntity> findByName(String name);

}
