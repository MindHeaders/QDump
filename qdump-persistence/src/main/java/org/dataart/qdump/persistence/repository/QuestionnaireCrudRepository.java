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
    Page<QuestionnaireEntity> findByPublished(boolean published, Pageable pageable);

    @Query(name = "QuestionnaireEntity.findPublishedQuestionnaires", countName = "QuestionnaireEntity.countPublishedQuestionnaires")
    Page<QuestionnaireEntity> getPublishedQuestionnaires(Pageable pageable);

    long countPublishedQuestionnaires();

}
