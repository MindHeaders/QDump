package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCrudRepository extends
		CrudRepository<QuestionEntity, Long> {
}
