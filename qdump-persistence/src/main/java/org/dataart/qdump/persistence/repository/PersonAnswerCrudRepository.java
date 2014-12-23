package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.springframework.data.repository.CrudRepository;

public interface PersonAnswerCrudRepository extends
		CrudRepository<PersonAnswerEntity, Long> {

}
