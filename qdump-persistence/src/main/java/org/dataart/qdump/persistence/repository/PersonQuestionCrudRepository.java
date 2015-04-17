package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonQuestionCrudRepository extends
        JpaRepository<PersonQuestionEntity, Long> {

    List<PersonQuestionEntity> findTextareaQuestionByPersonId(long id);
    @Query(value = "SELECT pqe FROM PersonQuestionEntity pqe WHERE pqe.questionEntity.type = 'TEXTAREA' AND pqe.checked = false",
            countQuery = "SELECT COUNT(pqe) FROM PersonQuestionEntity pqe WHERE pqe.questionEntity.type = 'TEXTAREA' AND pqe.checked = false")
    List<PersonQuestionEntity> findNotCheckedQuestions(Pageable pageable);
    long countNotCheckedQuestions();
    PersonQuestionEntity findById(long id);
}
