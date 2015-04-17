package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PersonCrudRepository extends
        JpaRepository<PersonEntity, Long> {

    PersonEntity findByEmail(String email);
    PersonEntity findByLogin(String login);
    List<PersonEntity> findByPersonGroup(
            PersonGroupEnums persongroup);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    boolean enabledByLogin(String login);
    boolean enabledByEmail(String email);
    String findPersonRole(long id);
    @Query(value = "select * from persons p WHERE p.id_person = " +
            "(select maxCount.created_by from " +
            "(select counter.created_by, MAX(counter.createdByCounter) from " +
            "(select q.created_by, count(*) AS createdByCounter from questionnaires q where q.created_by is not null group by q.created_by) counter) maxCount)", nativeQuery = true)
    PersonEntity findMostActivePersonInCreatingQuestionnaires();
    @Query(value = "select * from persons p WHERE p.id_person = " +
            "(select maxCount.id_person from (select counter.id_person, MAX(counter.createdByCounter) from " +
            "(select pq.id_person, count(*) AS createdByCounter from person_questionnaires pq group by pq.id_person) counter) maxCount)", nativeQuery = true)
    PersonEntity findMostActivePersonInPassingQuestionnaires();
    @Query(value = "select * from persons p WHERE p.id_person IN " +
            "(select counter.id_person from " +
            "(select pq.id_person, count(*) AS createdByCounter " +
            "from person_questionnaires pq group by pq.id_person limit 10) counter)", nativeQuery = true)
    List<PersonEntity> findTop10ActivePersonEntities();
    @Query(value = "SELECT p FROM PersonEntity p WHERE p.id IN " +
            "(SELECT p.id FROM PersonQuestionnaireEntity pq WHERE pq " +
            "MEMBER OF p.personQuestionnaireEntities " +
            "GROUP BY pq.id ORDER BY pq.id DESC)", countQuery = "SELECT COUNT(p) FROM PersonEntity p")
    List<PersonEntity> hello(Pageable pageable);
    long countByGender(byte gender);
    long countByEnabled(boolean enabled);
    void deleteByEmail(String email);
    void deleteByLogin(String login);
    PersonEntity findById(long id);

}
