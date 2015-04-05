package org.dataart.qdump.service.resourceImpl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.mail.MailSenderService;
import org.dataart.qdump.service.resource.PersonEntityResource;
import org.dataart.qdump.service.security.utils.VerificationTokenUtils;
import org.dataart.qdump.service.utils.EntitiesUpdater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static org.dataart.qdump.service.utils.WebApplicationUtils.exceptionCreator;
import static org.dataart.qdump.service.utils.WebApplicationUtils.responseCreator;

@Component
public class PersonEntityResourceBean implements PersonEntityResource{
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MailSenderService mailSenderService;
    private static final Logger LOG = LoggerFactory.getLogger(PersonEntityResourceBean.class.getName());
    private long personEntitiesCount;

    public Response authentication(String loginOrEmail, String password, boolean rememberMe) {
        boolean isVerified = false;
        boolean isExists;
        if(EmailValidator.getInstance().isValid(loginOrEmail)) {
            isExists = serviceQdump.personEntityExistsByEmail(loginOrEmail);
            if(isExists) {
                isVerified = serviceQdump.personEntityIsEnabledByEmail(loginOrEmail);
            }
        } else {
            isExists = serviceQdump.personEntityExistsByLogin(loginOrEmail);
            if(isExists) {
                isVerified = serviceQdump.personEntityIsEnabledByLogin(loginOrEmail);
            }
        }
        if(!isVerified && isExists) {
            LOG.info(String.format("User credentials %s is not verified", loginOrEmail));
            exceptionCreator(Status.FORBIDDEN, "Your account is not verified. Please check your " +
                    "email.");
        }
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername(loginOrEmail);
        token.setPassword(password.toCharArray());
        token.setRememberMe(rememberMe);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            LOG.info("User input incorrect credentials", e);
            exceptionCreator(Status.NOT_FOUND, "Error with login, email or password");
        }
        return Response.ok().build();
    }

    public Response registration(PersonEntity entity, UriInfo uriInfo) {
        String hashedPassword = BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt());
        entity.setPassword(hashedPassword);
        serviceQdump.addPersonEntity(entity);
        PersonEntity persistedEntity = serviceQdump.getPersonEntityByEmail(entity.getEmail());
        VerificationTokenEntity tokenEntity = new VerificationTokenEntity();
        tokenEntity.setPersonEntity(persistedEntity);
        String token = VerificationTokenUtils.beanToToken(tokenEntity);
        tokenEntity.setToken(token);
        serviceQdump.addVerificationTokenEntity(tokenEntity);
        URI uri = uriInfo.getBaseUri();
        String host = String.format("%s://%s", uri.getScheme(), uri.getAuthority());
        try {
            mailSenderService.sendMail(tokenEntity, host);
        } catch (EmailException e) {
            LOG.error("Mail sender error", e);
            exceptionCreator(Status.CONFLICT, e.getMessage());
        }
        LOG.info("User successfully registered" + entity);
        return Response.status(Status.CREATED)
                .build();
    }

    public void logout() {
        SecurityUtils.getSubject().logout();
    }

    public List<PersonEntity> get() {
        return serviceQdump.getPersonEntities();
    }

    public List<PersonEntity> getForAdminPanel(int page, int size, String direction, String sort) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<PersonEntity> databasePage = serviceQdump.getPersonEntitiesForAdminPanel(pageable);
        personEntitiesCount = databasePage.getTotalElements();
        return databasePage.getContent();
    }

    public Response get(@PathParam("id") long id) {
        if (!serviceQdump.personEntityExists(id)) {
            LOG.info(String.format("User with id %d is not exists", id));
            exceptionCreator(Status.NOT_FOUND, "There is no person with this id");
        }
        return responseCreator(Status.OK, serviceQdump.getPersonEntity(id));
    }

    public Response delete(@PathParam("id") long id) {
        if (!serviceQdump.personEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, "Person with this id is not exist");
        }
        serviceQdump.deletePersonEntity(id);
        return Response.ok().build();
    }

    @RequiresRoles("ADMIN")
    public void delete() {
        serviceQdump.deleteAllPersonEntities();
    }

    public void updatePersonGroup(long id, PersonGroupEnums group) {
        if(!serviceQdump.personEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, "Person entity with this id is not exists");
        }
        PersonEntity personEntity = serviceQdump.getPersonEntity(id);
        personEntity.setModifiedBy(new PersonEntity(null, null, (long) SecurityUtils.getSubject().getPrincipal()));
        personEntity.setPersonGroup(group);
    }

    //If email was changed, account should be disabled, need to generate token and send it to current email
    //make logout().
    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response update(PersonEntity source, UriInfo uriInfo) {
        if(SecurityUtils.getSubject().getPrincipal() == null) {
            exceptionCreator(Status.UNAUTHORIZED, "You need to authorize");
        }
        long id = 0;
        PersonEntity modifiedBy = null;
        if(!SecurityUtils.getSubject().hasRole("ADMIN")) {
            id = (long) SecurityUtils.getSubject().getPrincipal();
            if (!serviceQdump.personEntityExists(id)) {
                exceptionCreator(Status.NOT_FOUND, "Person with this id is not exists");
            }
        } else {
            modifiedBy = new PersonEntity(null, null, (long) SecurityUtils.getSubject().getPrincipal());
        }
        id = source.getId();
        PersonEntity target = serviceQdump.getPersonEntity(id);
        target.setModifiedBy(modifiedBy);
        if (target.equals(source)) {
            return responseCreator(Status.ACCEPTED, "message", "User data is already up to date");
        }
        if(!source.getEmail().equals(target.getEmail())) {
            target.setEnabled(false);
            VerificationTokenEntity tokenEntity = new VerificationTokenEntity();
            tokenEntity.setPersonEntity(target);
            String token = VerificationTokenUtils.beanToToken(tokenEntity);
            tokenEntity.setToken(token);
            serviceQdump.addVerificationTokenEntity(tokenEntity);
            URI uri = uriInfo.getBaseUri();
            String host = String.format("%s://%s", uri.getScheme(), uri.getAuthority());
            try {
                mailSenderService.sendMail(tokenEntity, host);
            } catch (EmailException e) {
                LOG.error("Mail sender error", e);
                exceptionCreator(Status.CONFLICT, e.getMessage());
            }
        }
        EntitiesUpdater.update(source, target);
        return responseCreator(Status.OK, "success", "Person was successful updated");
    }

    public Response checkEmail(String email){
        boolean isExist = serviceQdump.personEntityExistsByEmail(email);
        return !isExist ? Response.ok().build() : Response.status(Status.FORBIDDEN).build();
    }

    public Response checkLogin(String login) {
        boolean isExist = serviceQdump.personEntityExistsByLogin(login);
        return !isExist ? Response.ok().build() : Response.status(Status.FORBIDDEN).build();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response verifyAccount(String token, UriInfo uriInfo) {
        VerificationTokenEntity verificationTokenEntity = serviceQdump.getVerificationTokenEntity(1l);
        System.out.println(verificationTokenEntity.getToken().equals(token));
        VerificationTokenEntity tokenEntity = VerificationTokenUtils.tokenToBean(token);
        if(!serviceQdump.verificationTokenEntityExists(token)) {
            LOG.info(String.format("Verification token is not exists: %s", token));
            exceptionCreator(Status.NOT_FOUND, "This token is not exists.");
        } else  if(tokenEntity.hasExpired()) {
            LOG.info(String.format("Verification token is expired: %s", token));
            exceptionCreator(Status.CONFLICT, "This link has expired. " +
                    "Verification will be resend to your email");
        }
        PersonEntity personEntity = serviceQdump.getPersonEntityByEmail(tokenEntity.getPersonEntity().getEmail());
        tokenEntity = serviceQdump.getVerificationTokenEntity(personEntity.getEmail());
        tokenEntity.setVerified(true);
        personEntity.setEnabled(true);
        try {
            ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
            objectNode.put("success", "You successfully verify your account. Please welcome to our Project.");
            URI uri = uriInfo.getBaseUri();
            return Response.temporaryRedirect(new URI(String.format("%s://%s/%s", uri.getScheme(), uri.getAuthority(), "success")))
                    .type(MediaType.APPLICATION_JSON)
                    .entity(objectNode)
                    .build();
        } catch (URISyntaxException e) {
            LOG.error("Error with uri", e);
            exceptionCreator(Status.CONFLICT, e.getMessage());
        }
        return Response.status(Status.FORBIDDEN).build();
    }


    public Response getEntityForPersonalPage() {
        if(!SecurityUtils.getSubject().isRemembered() && !SecurityUtils.getSubject().isAuthenticated()) {
            exceptionCreator(Status.UNAUTHORIZED, "You need to Authenticate");
        }
        PersonEntity personEntity = serviceQdump.getPersonEntityForAccountPanel((long) SecurityUtils.getSubject().getPrincipal());
        return responseCreator(Status.OK, personEntity);
    }

    public Response getEntityForPersonalPage(long id) {
        if(!serviceQdump.personEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, String.format("Person with id = %d not found", id));
        }
        return responseCreator(Status.OK, serviceQdump.getPersonEntityForAccountPanel(id));
    }

    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response resetPassword() {
        long id = (long) SecurityUtils.getSubject().getPrincipal();
        String generatedPassword = RandomStringUtils.randomAlphanumeric(16);
        String hashedPassword = BCrypt.hashpw(generatedPassword, BCrypt.gensalt());
        String msg = String.format("You just reset your password. Your new password is: %s", generatedPassword);
        PersonEntity entity = serviceQdump.getPersonEntity(id);
        entity.setPassword(hashedPassword);
        mailSenderService.sendCustomMail(entity.getEmail(), msg);
        LOG.info(String.format("User with id - %d successfully reset password.", id));
        return Response.ok().build();
    }

    public Response getAuthorized() {
        if(SecurityUtils.getSubject().getPrincipal() == null) {
            exceptionCreator(Status.UNAUTHORIZED, "User is not authorized");
        }
        return Response.ok().build();
    }

    public Response checkPermission(String role) {
        if(!SecurityUtils.getSubject().hasRole(role.toUpperCase())) {
            exceptionCreator(Status.FORBIDDEN, "User has no permission");
        }
        return Response.ok().build();
    }

    public Response count() {
        if(personEntitiesCount == 0) {
            personEntitiesCount = serviceQdump.personEntitiesCount();
        }
        return responseCreator(Status.OK, "count", personEntitiesCount);
    }
}
