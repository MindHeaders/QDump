package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonQuestionnaireCrudRepository extends
        JpaRepository<PersonQuestionnaireEntity, Long> {

	List<PersonQuestionnaireEntity> findByStatus(
            String status);
	List<PersonQuestionnaireEntity> findByQuestionnaireEntityName(
            String questionnaireName);
    PersonQuestionnaireEntity findByPersonQuestionnaireIdAndPersonId(long personQuestionnaireId, long personId);
    @Query(value = "SELECT pq FROM PersonQuestionnaireEntity pq " +
            "WHERE pq.status = 'in checking process'",
            countQuery = "SELECT COUNT(pq) FROM PersonQuestionnaireEntity pq WHERE pq.status = 'in checking process'")
    List<PersonQuestionnaireEntity> findInCheckingProcess(Pageable pageable);
    @Query(value = "SELECT pq FROM PersonQuestionnaireEntity pq, PersonEntity p " +
            "LEFT JOIN pq.questionnaireEntity q " +
            "WHERE pq MEMBER OF p.personQuestionnaireEntities " +
            "AND p.id = ?1 " +
            "AND pq.status NOT IN ('in progress', 'not specified')",
            countQuery = "SELECT count(pq) FROM PersonQuestionnaireEntity pq, PersonEntity p WHERE p.id = ?1 AND pq.status NOT IN ('in progress', 'not specified')")
    List<PersonQuestionnaireEntity> findCompletedByPersonId(long id, Pageable pageable);
    @Query(value = "SELECT pq FROM PersonQuestionnaireEntity pq, PersonEntity p " +
            "LEFT JOIN pq.questionnaireEntity q " +
            "WHERE pq MEMBER OF p.personQuestionnaireEntities " +
            "AND p.id = ?1 " +
            "AND pq.status = 'in progress'",
            countQuery = "SELECT COUNT(pq) FROM PersonQuestionnaireEntity pq, PersonEntity p WHERE p.id = ?1 AND pq.status = 'in progress'")
    List<PersonQuestionnaireEntity> findStartedByPersonId(long id, Pageable pageable);
    long countCompletedByPersonId(long id);
    long countStartedByPersonId(long id);
    long countByStatus(String status);
    PersonQuestionnaireEntity findByPersonQuestionId(long id);
    long countStartedQuestionnaires();
    long countCompletedQuestionnaires();
    PersonQuestionnaireEntity findById(long id);
    void deleteByQuestionnaireEntityId(long id);
}
