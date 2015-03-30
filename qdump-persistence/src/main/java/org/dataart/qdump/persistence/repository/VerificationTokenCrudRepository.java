package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenCrudRepository extends CrudRepository<VerificationTokenEntity, Long> {

    VerificationTokenEntity findByPersonEntityEmail(String email);
    void deleteExpired();
    void deleteVerified();
    boolean exists(String token);
    VerificationTokenEntity findByToken(String token);
    VerificationTokenEntity findByPersonEntityEmailConstructor(String email);
}
