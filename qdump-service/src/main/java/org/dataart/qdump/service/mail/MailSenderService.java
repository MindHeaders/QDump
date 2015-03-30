package org.dataart.qdump.service.mail;

import com.google.common.base.Preconditions;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.dataart.qdump.service.config.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by artemvlasov on 07/02/15.
 */
@Service("mailSenderService")
public class MailSenderService {
    private Email email;
    private MailConfig mailConfig;

    public MailSenderService() {
        email = new SimpleEmail();
    }

    public void sendMail(VerificationTokenEntity verificationToken, String host) throws EmailException {
        Preconditions.checkNotNull(verificationToken, "VerificationToken bean cannot be null");
        Preconditions.checkNotNull(verificationToken.getToken(), "Token cannot be null");
        Preconditions.checkNotNull(verificationToken.getPersonEntity(), "Person entity cannot be null");
        Preconditions.checkNotNull(verificationToken.getPersonEntity().getEmail(), "Person email cannot be null");
        String tokenUrl = String.format("%s/rest/persons/verify?token=%s", host, verificationToken.getToken());
        String personLogin = verificationToken.getPersonEntity().getLogin() == null ? "user" : verificationToken.getPersonEntity().getLogin();
        String emailMsg = String.format(mailConfig.getMsg(),personLogin,tokenUrl);
        try {
            prepareMailApi();
            email.setMsg(emailMsg);
            email.addTo(verificationToken.getPersonEntity().getEmail());
            email.send();
        } catch (EmailException e) {
            throw new EmailException(e);
        }
    }
    public void sendCustomMail(String mail, String msg) {
        Preconditions.checkNotNull(mail, "Mail address cannot be null");
        Preconditions.checkNotNull(msg, "Message entity cannot be null");
        try {
            prepareMailApi();
            email.setMsg(msg);
            email.addTo(mail);
            email.send();
        } catch (EmailException e) {
            e.printStackTrace();
        }
    }
    private void prepareMailApi() throws EmailException {
        email.setHostName(mailConfig.getHostName());
        email.setSmtpPort(Integer.parseInt(mailConfig.getSmtpPort()));
        email.setAuthentication(mailConfig.getUsername(), mailConfig.getPassword());
        email.setSSLOnConnect(true);
        try {
            email.setFrom(mailConfig.getFromAddress());
        } catch (EmailException e) {
            throw new EmailException(e);
        }
        email.setSubject(mailConfig.getSubject());
    }

    public MailConfig getMailConfig() {
        return mailConfig;
    }

    @Autowired
    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }
}
