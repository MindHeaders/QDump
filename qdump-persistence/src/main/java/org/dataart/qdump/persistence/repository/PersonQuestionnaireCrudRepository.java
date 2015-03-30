package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonQuestionnaireCrudRepository extends
        CrudRepository<PersonQuestionnaireEntity, Long> {

	List<PersonQuestionnaireEntity> findByStatus(
            String status);
	List<PersonQuestionnaireEntity> findByQuestionnaireName(
            String questionnaireName);
    PersonQuestionnaireEntity findByPersonQuestionnaireIdAndPersonId(long personQuestionnaireId, long personId);
    @Query(value = "SELECT NEW PersonQuestionnaireEntity(pq.id, pq.status, q.id, q.name, pq.createdDate, pq.modifiedDate) FROM PersonQuestionnaireEntity pq, QuestionnaireEntity q, PersonEntity p WHERE p.id = ?1 AND pq.status NOT IN ('in progress', 'not specified')",
            countQuery = "SELECT count(pq) FROM PersonQuestionnaireEntity pq, PersonEntity p WHERE p.id = ?1 AND pq.status NOT IN ('in progress', 'not specified')")
    Page<PersonQuestionnaireEntity> findCompletedByPersonId(long id, Pageable pageable);
    @Query(value = "SELECT NEW PersonQuestionnaireEntity(pq.id, pq.status, q.id, q.name, pq.createdDate, pq.modifiedDate) FROM PersonQuestionnaireEntity pq, QuestionnaireEntity q, PersonEntity p WHERE p.id = ?1 AND pq.status = 'in progress'",
            countQuery = "SELECT COUNT(pq) FROM PersonQuestionnaireEntity pq, PersonEntity p WHERE p.id = ?1 AND pq.status = 'in progress'")
    Page<PersonQuestionnaireEntity> findStartedByPersonId(long id, Pageable pageable);
    long countCompletedByPersonId(long id);
    long countStartedByPersonId(long id);
    long countByStatus(String status);
}
