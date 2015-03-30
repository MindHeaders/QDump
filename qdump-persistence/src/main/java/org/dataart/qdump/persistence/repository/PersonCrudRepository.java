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

    PersonEntity findPersonByEmail(String email);
    PersonEntity findPersonByLogin(String login);
    List<PersonEntity> findPersonByPersonGroup(
            PersonGroupEnums persongroup);
    List<PersonEntity> findPersonsForAdminPanel();
    PersonEntity findPersonByLoginForAuth(String login);
    boolean personExistsByLogin(String login);
    boolean personExistsByEmail(String email);
    String findPersonPasswordByLogin(String login);
    boolean personEnabledByLogin(String login);
    boolean personEnabledByEmail(String email);
    String findPersonRole(long id);
    @Query(value = "SELECT p.id, p.login, pq.id, pq.createdDate, pq.modifiedDate, q.id, q.name " +
            "FROM PersonQuestionnaireEntity pq, PersonEntity p, QuestionnaireEntity q " +
            "WHERE pq MEMBER OF p.personQuestionnaireEntities " +
            "AND q.id = pq.questionnaireEntity.id " +
            "AND pq.status = 'in checking process'",
            countQuery = "SELECT COUNT(pq) FROM PersonQuestionnaireEntity pq WHERE pq.status = 'in checking process'")
    Page<PersonEntity> findPersonQuestionnairesInCheckingProcess(Pageable pageable);
}
