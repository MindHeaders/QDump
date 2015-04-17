package org.dataart.qdump.service.mail;

import org.apache.commons.mail.EmailException;
import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.dataart.qdump.service.config.MailConfig;
import org.dataart.qdump.service.security.utils.VerificationTokenUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 08/02/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MailConfig.class, MailSenderService.class})
public class MailSenderServiceTest {
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private MailSenderService mailSenderService;
    private static VerificationTokenEntity verificationTokenEntity;
    private static PersonEntity personEntity;
    private static VerificationTokenUtils verificationTokenUtils;
    private static final String HOST = "http://localhost:8080";
    @Before
    public void setup() {
        personEntity.setEmail("vlasovartem21@gmail.com");
        personEntity.setId(1);
        personEntity.setPassword("password");
        personEntity.setPersonGroup(PersonGroupEnums.ADMIN);
        personEntity.setEnabled(true);
        personEntity.setFirstname("Artem");
        personEntity.setGender((byte) 1);
        personEntity.setLastname("Vlasov");
        personEntity.setLogin("vlasovartem");
        personEntity.setCreatedDate(LocalDateTime.now());
        verificationTokenEntity.setPersonEntity(personEntity);
        String token = verificationTokenUtils.beanToToken(verificationTokenEntity);
        verificationTokenEntity.setToken(token);
    }

    @BeforeClass
    public static void beforeClass() {
        verificationTokenEntity = new VerificationTokenEntity();
        personEntity = new PersonEntity();
        verificationTokenUtils = new VerificationTokenUtils();
    }

    @Test(expected = RuntimeException.class)
    public void sendMailErrorVerifictionEntityNull() throws EmailException {
        mailSenderService.sendMail(null, HOST);
    }
    @Test(expected = RuntimeException.class)
    public void sendMailErrorPersonEntityNull() throws EmailException {
        verificationTokenEntity.setPersonEntity(null);
        mailSenderService.sendMail(verificationTokenEntity, HOST);
    }
    @Test(expected = RuntimeException.class)
    public void sendMailErrorTokenNull() throws EmailException {
        verificationTokenEntity.setToken(null);
        mailSenderService.sendMail(verificationTokenEntity, HOST);
    }
    @Test(expected = RuntimeException.class)
    public void sendMailErrorPersonEntityEmailNull() throws EmailException {
        verificationTokenEntity.getPersonEntity().setEmail(null);
        mailSenderService.sendMail(verificationTokenEntity, HOST);
    }
    @Test(expected = EmailException.class)
    public void sendMailErrorIncorrectEmail() throws EmailException {
        verificationTokenEntity.getPersonEntity().setEmail("hello");
        try {
            mailSenderService.sendMail(verificationTokenEntity, HOST);
        } catch (EmailException e) {
            throw new EmailException(e);
        }
    }

}
