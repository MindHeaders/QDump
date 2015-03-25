package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonCrudRepository extends
        CrudRepository<PersonEntity, Long> {

    PersonEntity findPersonByEmail(String email);
    PersonEntity findPersonByLogin(String login);
    List<PersonEntity> findPersonByPersonGroup(
            PersonGroupEnums persongroup);
    List<PersonEntity> findPersonsForAdminPanel();
    PersonEntity findPersonByLoginForAuth(String login);
    boolean personExistsByLogin(String login);
    boolean personExistsByEmail(String email);
    String findPersonPasswordByLogin(String login);
    boolean personEnabledByLogin(String login);
    boolean personEnabledByEmail(String email);
    String findPersonRole(long id);
}
