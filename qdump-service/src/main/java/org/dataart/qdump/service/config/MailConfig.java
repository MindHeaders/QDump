package org.dataart.qdump.service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by artemvlasov on 08/02/15.
 */
@Configuration
@PropertySource({"classpath:mail.properties"})
public class MailConfig {

    @Autowired
    private Environment environment;
    private static final String MAIL_SERVICE_HOST_NAME = "mail.service.hostName";
    private static final String MAIL_SERVICE_SMTP_PORT = "mail.service.smptPort";
    private static final String MAIL_SERVICE_PASSWORD = "mail.service.password";
    private static final String MAIL_SERVICE_USERNAME = "mail.service.username";
    private static final String MAIL_SERVICE_FROM_ADDRESS = "mail.service.fromAddress";
    private static final String MAIL_SERVICE_MSG = "mail.service.msg";
    private static final String MAIL_SERVICE_SUBJECT = "mail.service.subject";

    public String getHostName() {
        return environment.getProperty(MAIL_SERVICE_HOST_NAME);
    }

    public String getSmtpPort() {
        return environment.getProperty(MAIL_SERVICE_SMTP_PORT);
    }

    public String getPassword() {
        return environment.getProperty(MAIL_SERVICE_PASSWORD);
    }

    public String getUsername() {
        return environment.getProperty(MAIL_SERVICE_USERNAME);
    }

    public String getFromAddress() {
        return environment.getProperty(MAIL_SERVICE_FROM_ADDRESS);
    }

    public String getMsg() {
        return environment.getProperty(MAIL_SERVICE_MSG);
    }

    public String getSubject() {
        return environment.getProperty(MAIL_SERVICE_SUBJECT);
    }
}
