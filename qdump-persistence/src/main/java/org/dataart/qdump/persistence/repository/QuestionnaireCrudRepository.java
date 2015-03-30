package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionnaireCrudRepository extends
		CrudRepository<QuestionnaireEntity, Long> {

	List<QuestionnaireEntity> findByName(String name);
    @Query(value = "SELECT NEW org.dataart.qdump.entities.questionnaire.QuestionnaireEntity(q.id, q.name, q.description, q.createdDate, q.modifiedDate) FROM QuestionnaireEntity q WHERE q.published = true",
            countQuery = "SELECT count(q) FROM QuestionnaireEntity q WHERE q.published = true")
    Page<QuestionnaireEntity> findPublishedQuestionnaires(Pageable pageable);
    @Query(value = "SELECT NEW org.dataart.qdump.entities.questionnaire.QuestionnaireEntity(q.id, q.name, q.description, q.createdDate, q.modifiedDate) FROM QuestionnaireEntity q", countQuery = "SELECT count(q) FROM QuestionnaireEntity q")
    Page<QuestionnaireEntity> findAllQuestionnaires(Pageable pageable);
    long countPublishedQuestionnaires();
    QuestionnaireEntity findPublishedQuestionnairesById(long id);
    QuestionnaireEntity findQuestionnaireById(long id);

}
