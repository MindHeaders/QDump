package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionCrudRepository extends
        JpaRepository<QuestionEntity, Long> {
    QuestionEntity findById(long id);
}
