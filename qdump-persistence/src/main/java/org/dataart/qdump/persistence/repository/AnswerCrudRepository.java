package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerCrudRepository extends
        JpaRepository<AnswerEntity, Long> {
    AnswerEntity findById(long id);
}
