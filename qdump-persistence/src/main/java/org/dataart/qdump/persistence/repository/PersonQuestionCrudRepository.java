package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.springframework.data.repository.CrudRepository;

public interface PersonQuestionCrudRepository extends
		CrudRepository<PersonQuestionEntity, Long> {

}
