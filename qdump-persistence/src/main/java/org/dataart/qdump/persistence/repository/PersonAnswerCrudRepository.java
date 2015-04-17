package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonAnswerCrudRepository extends
        JpaRepository<PersonAnswerEntity, Long> {
    PersonAnswerEntity findById(long id);
}
