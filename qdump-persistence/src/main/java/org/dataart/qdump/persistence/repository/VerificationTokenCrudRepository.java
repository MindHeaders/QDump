package org.dataart.qdump.persistence.repository;

import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenCrudRepository extends JpaRepository<VerificationTokenEntity, Long> {

    VerificationTokenEntity findByPersonEntityEmail(String email);
    void deleteExpired();
    void deleteVerified();
    boolean exists(String token);
    VerificationTokenEntity findByToken(String token);
    VerificationTokenEntity findByPersonEntityEmailConstructor(String email);
    VerificationTokenEntity findById(long id);
}
