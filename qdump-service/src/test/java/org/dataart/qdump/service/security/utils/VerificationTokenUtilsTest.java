package org.dataart.qdump.service.security.utils;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.dataart.qdump.service.security.utils.VerificationTokenUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by artemvlasov on 07/02/15.
 */
public class VerificationTokenUtilsTest {
    private static VerificationTokenUtils verificationTokenUtils;
    private static VerificationTokenEntity verificationToken;
    private static String encyptedBean;
    @BeforeClass
    public static void setupClass() {
        verificationTokenUtils = new VerificationTokenUtils();
        verificationToken = new VerificationTokenEntity();
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(1);
        personEntity.setEmail("testemail@gmail.com");
        verificationToken.setPersonEntity(personEntity);
        encyptedBean = verificationTokenUtils.beanToToken(verificationToken);
    }
    @Test
    public void createTokenFromBeanSuccess() {
        verificationTokenUtils.beanToToken(verificationToken);
        assertNotNull(verificationToken);
    }
    @Test(expected = RuntimeException.class)
    public void createTokenFromBeanErrorVerificationTokenNull() {
        verificationToken = null;
        verificationTokenUtils.beanToToken(verificationToken);
    }
    @Test(expected = RuntimeException.class)
    public void createTokenFromBeanErrorPersonEntityNull() {
        verificationToken.setPersonEntity(null);
        verificationTokenUtils.beanToToken(verificationToken);
    }
    @Test(expected = RuntimeException.class)
    public void createTokenFromBeanErrorPersonEntityIdZero() {
        verificationToken.getPersonEntity().setId(0);
        verificationTokenUtils.beanToToken(verificationToken);
    }
    @Test
    public void createBeanFromTokenSuccess() {
        VerificationTokenEntity decryptedToken = verificationTokenUtils.tokenToBean(encyptedBean);
        assertNotNull(decryptedToken);
        assertNotNull(decryptedToken.getPersonEntity());
        assertEquals(1l, decryptedToken.getPersonEntity().getId());
        assertEquals(new String("testemail@gmail.com"), decryptedToken.getPersonEntity().getEmail());
        assertNotNull(decryptedToken.getExpireDate());
    }
    @Test(expected = RuntimeException.class)
    public void createBeanFromTokenErrorTokenNull() {
        VerificationTokenEntity decryptedToken = verificationTokenUtils.tokenToBean(null);
        assertNull(decryptedToken);
    }
}
