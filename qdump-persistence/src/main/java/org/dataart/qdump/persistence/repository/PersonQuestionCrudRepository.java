package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonQuestionCrudRepository extends
		CrudRepository<PersonQuestionEntity, Long> {

    List<PersonQuestionEntity> findByPersonId(long id);
    @Query(value = "SELECT pqe FROM PersonQuestionEntity pqe WHERE pqe.questionEntity.type = 'TEXTAREA' AND pqe.checked = false",
            countQuery = "SELECT COUNT(pqe) FROM PersonQuestionEntity pqe WHERE pqe.questionEntity.type = 'TEXTAREA' AND pqe.checked = false")
    Page<PersonQuestionEntity> findNotCheckedQuestions(Pageable pageable);
    long countNotCheckedQuestions();
}
