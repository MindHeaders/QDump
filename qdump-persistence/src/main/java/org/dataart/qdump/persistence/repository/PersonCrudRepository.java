package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PersonCrudRepository extends
        CrudRepository<PersonEntity, Long> {

    PersonEntity getPersonByEmail(String email);
    PersonEntity getPersonByLogin(String login);
    List<PersonEntity> getPersonByPersonGroup(
            PersonGroupEnums persongroup);
    List<PersonEntity> getPersonEntitiesForAdminPanel();
    PersonEntity getPersonByLoginForAuth(String login);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    String getPersonPasswordByLogin(String login);
    boolean isEnabledByLogin(String login);
    boolean isEnabledByEmail(String email);
}
