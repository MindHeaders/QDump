package org.dataart.qdump.persistence.repository;

import java.util.List;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.springframework.data.repository.CrudRepository;

public interface PersonQuestionCrudRepository extends
		CrudRepository<PersonQuestionEntity, Long> {

	List<PersonQuestionEntity> getCorrectQuestion(boolean correct);
	List<PersonQuestionEntity> getQuestionByPersonQuestionnaireId(
			long personQuestionnaireId);
	void deletePersonQuestionEntityByPersonQuestionnaireId(long id);

}
