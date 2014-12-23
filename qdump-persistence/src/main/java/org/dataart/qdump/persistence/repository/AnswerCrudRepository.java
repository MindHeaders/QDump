package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.springframework.data.repository.CrudRepository;

public interface AnswerCrudRepository extends
		CrudRepository<AnswerEntity, Long> {
}
