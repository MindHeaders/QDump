package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.converters.LocalDateTimePersistenceConverter;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Convert;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestionnaireCrudRepository extends
        JpaRepository<QuestionnaireEntity, Long> {

    long countByPublished(boolean published);
    @Query(value = "select q.created_date from questionnaires q order by q.created_date desc limit 1",
            nativeQuery = true)
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    LocalDateTime lastCreatedDate();
    @Query(value = "SELECT q FROM QuestionnaireEntity q WHERE q.published = ?1 AND q.id = ?2")
    QuestionnaireEntity findByPublishedAndById(boolean published, long id);
    @Query(value = "select * from questionnaires q where q.id_questionnaire = " +
            "(select maxCount.id_questionnaire from " +
            "(select counter.id_questionnaire, MAX(counter.createdByCounter) from " +
            "(select pq.id_questionnaire, count(*) AS createdByCounter from person_questionnaires pq " +
            "group by pq.id_questionnaire) counter) maxCount)", nativeQuery = true)
    QuestionnaireEntity findPopularQuestionnaireEntity();
    QuestionnaireEntity findByName(String name);
    List<QuestionnaireEntity> findByPublished(boolean published, Pageable pageable);
    QuestionnaireEntity findById(long id);
}
