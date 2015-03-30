package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonQuestionCrudRepository extends
		CrudRepository<PersonQuestionEntity, Long> {

    List<PersonQuestionEntity> findByPersonId(long id);
}
