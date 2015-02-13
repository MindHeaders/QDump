package org.dataart.qdump.entities.security;

import org.dataart.qdump.entities.converters.LocalDateTimePersistenceConverter;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.questionnaire.BaseEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 06/02/15.
 */
@Entity
@Table(name="verification_token")
@AttributeOverride(name = "id", column = @Column(name = "id_token", insertable = false, updatable = false))
@NamedQueries({
        @NamedQuery(name = "VerificationTokenEntity.findByPersonEntityEmailConstructor",
                query = "SELECT NEW org.dataart.qdump.entities.security.VerificationTokenEntity" +
                        "(v.token, v.verified) FROM VerificationTokenEntity v WHERE v.personEntity.email = ?1"),
        @NamedQuery(name = "VerificationTokenEntity.findByPersonEntityEmail",
                query = "FROM VerificationTokenEntity v WHERE v.personEntity.email = ?1"),
        @NamedQuery(name = "VerificationTokenEntity.deleteExpired",
                query = "DELETE FROM VerificationTokenEntity v WHERE TIMESTAMPDIFF(MINUTE, v.expireDate, NOW()) > 1440"),
        @NamedQuery(name = "VerificationTokenEntity.deleteVerified",
                query = "DELETE FROM VerificationTokenEntity v WHERE v.verified = 1"),
        @NamedQuery(name = "VerificationTokenEntity.exists",
                query = "SELECT CASE WHEN (COUNT(v) > 0) THEN true ELSE false END " +
                        "FROM VerificationTokenEntity v WHERE v.token = ?1"),
        @NamedQuery(name = "VerificationEntity.findByToken",
                query = "FROM VerificationTokenEntity v WHERE v.token = ?1")
})
public class VerificationTokenEntity extends BaseEntity {
    private static final int DEFAULT_EXPIRE_TIME_IN_MINS = 60 * 24;
    @Column(length = 255)
    private String token;
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime expireDate;
    private boolean verified;
    private PersonEntity personEntity;

    public VerificationTokenEntity() {
        super();
        this.expireDate = calculateExpiryDate(DEFAULT_EXPIRE_TIME_IN_MINS);
    }
    public VerificationTokenEntity(String token, boolean verified) {
        super();
        this.token = token;
        this.verified = verified;
    }

    public VerificationTokenEntity(PersonEntity personEntity, int expirationTimeInMinutes) {
        this();
        this.personEntity = personEntity;
        this.expireDate = calculateExpiryDate(expirationTimeInMinutes);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_person", referencedColumnName = "id_person")
    public PersonEntity getPersonEntity() {
        return personEntity;
    }

    public void setPersonEntity(PersonEntity personEntity) {
        this.personEntity = personEntity;
    }

    private LocalDateTime calculateExpiryDate(int expiryDateInMins) {
        return LocalDateTime.now().plusMinutes(expiryDateInMins);
    }

    public boolean hasExpired() {
        LocalDateTime now = LocalDateTime.now();
        return Duration.between(now, expireDate).isNegative();
    }
}
