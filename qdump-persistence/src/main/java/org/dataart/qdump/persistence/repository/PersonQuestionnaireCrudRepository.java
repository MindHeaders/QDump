package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonQuestionnaireCrudRepository extends
		CrudRepository<PersonQuestionnaireEntity, Long> {

	List<PersonQuestionnaireEntity> findByOwnById(Long id);
	List<PersonQuestionnaireEntity> getPersonQuestionnaireByStatus(
			String status);
	List<PersonQuestionnaireEntity> getPersonQuestionnaireByQuestionnaireName(
			String questionnaireName);
	void deletePersonQuestionnaireEntityByOwnById(Long id);
}
