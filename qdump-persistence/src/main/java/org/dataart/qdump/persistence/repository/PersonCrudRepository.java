package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonCrudRepository extends
        CrudRepository<PersonEntity, Long> {

    PersonEntity findByEmail(String email);
    PersonEntity findByLogin(String login);
    List<PersonEntity> findByPersonGroup(
            PersonGroupEnums persongroup);
    PersonEntity findByLoginForAuth(String login);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean enabledByLogin(String login);
    boolean enabledByEmail(String email);
    String findPersonRole(long id);
    @Query(value = "SELECT p.id, p.login, pq.id, pq.createdDate, pq.modifiedDate, q.id, q.name " +
            "FROM PersonQuestionnaireEntity pq, PersonEntity p, QuestionnaireEntity q " +
            "WHERE pq MEMBER OF p.personQuestionnaireEntities " +
            "AND q.id = pq.questionnaireEntity.id " +
            "AND pq.status = 'in checking process'",
            countQuery = "SELECT COUNT(pq) FROM PersonQuestionnaireEntity pq WHERE pq.status = 'in checking process'")
    Page<PersonEntity> findPersonQuestionnairesInCheckingProcess(Pageable pageable);
    @Query(value = "SELECT NEW org.dataart.qdump.entities.person.PersonEntity(p.id, p.createdDate, p.modifiedDate, p.email, p.login, p.enabled, p.personGroup) FROM PersonEntity p", countQuery = "select count(p) from PersonEntity p")
    Page<PersonEntity> findForAdminPanel(Pageable pageable);
    PersonEntity findById(long id);
}
