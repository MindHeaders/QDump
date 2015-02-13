package org.dataart.qdump.service.resourceImpl;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.mail.EmailException;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.helper.EntitiesUpdater;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.security.VerificationTokenEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.mail.MailSenderService;
import org.dataart.qdump.service.resource.PersonEntityResource;
import org.dataart.qdump.service.security.utils.VerificationTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Component
public class PersonEntityResourceBean implements PersonEntityResource{
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MailSenderService mailSenderService;

    @RequiresRoles(value = {"USER", "ADMIN"})
    public Response getPersonEntity(@PathParam("id") long id) {
        if (!serviceQdump.personEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, "There is no person with this id");
        }
        return Response.status(Status.OK)
                .entity(serviceQdump.getPersonEntity(id)).build();
    }

    public Response auth(String loginOrEmail, String password, boolean rememberMe) {
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
        if(!isExists) {
            exceptionCreator(Status.NOT_FOUND, "User with this data is not exists");
        } else if(!isVerified) {
            exceptionCreator(Status.FORBIDDEN, "Your account is not verified. Please check your email.");
        }
        UsernamePasswordToken token = new UsernamePasswordToken();
        token.setUsername(loginOrEmail);
        token.setPassword(password.toCharArray());
        token.setRememberMe(rememberMe);
        try {
            SecurityUtils.getSubject().login(token);
        } catch (AuthenticationException e) {
            exceptionCreator(Status.NOT_FOUND, "Error with login, email or password");
        }
        return Response.status(Status.OK).build();
    }

    public Response registration(PersonEntity entity, UriInfo uriInfo) {
        String hashedPassword = BCrypt.hashpw(entity.getPassword(), BCrypt.gensalt());
        entity.setPassword(hashedPassword);
        serviceQdump.addPersonEntity(entity);
        PersonEntity persistedEntity = serviceQdump.getPersonByEmail(entity.getEmail());
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
            exceptionCreator(Status.CONFLICT, e.getMessage());
        }
        return Response.status(Status.CREATED)
                .build();
    }

    public void logout() {
        if(SecurityUtils.getSubject().isAuthenticated() || SecurityUtils.getSubject().isRemembered()) {
            System.out.println("User successfully logout");
            SecurityUtils.getSubject().logout();
        }
    }

    @RequiresRoles("ADMIN")
    public List<PersonEntity> getAll() {
        return serviceQdump.getPersonEntities();
    }

    @RequiresRoles("ADMIN")
    public List<PersonEntity> getAllMin() {
        return serviceQdump.getPersonEntitiesForAdminPanel();
    }

    @RequiresRoles("ADMIN")
    public Response deletePersonEntity(@PathParam("id") long id) {
        if (!serviceQdump.personEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, "Person with this id is not exist");
        }
        serviceQdump.deletePersonEntity(id);
        return Response.status(Status.OK)
                .type(MediaType.TEXT_PLAIN)
                .entity("Person with id = " + id + " was deleted")
                .build();
    }

    @RequiresRoles("ADMIN")
    public Response deleteAllPersonEntities() {
        serviceQdump.deleteAllPersonEntities();
        return Response.status(Status.OK)
                .type(MediaType.TEXT_PLAIN)
                .entity("Was delete all Person Entites")
                .build();
    }

    //If email was changed, account should be disabled, need to generate token and send it to current email
    //make logout().
    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response updatePersonEntity(PersonEntity source) {
        long id = (long) SecurityUtils.getSubject().getPrincipal();
        if (!serviceQdump.personEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, "Person with this id is not exists");
        }
        PersonEntity target = serviceQdump.getPersonEntity(id);
        if (source.checkEqualsForWeb(source)) {
            return Response.status(Status.ACCEPTED)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("There is nothing to change")
                    .build();
        }
        List<String> ignoredFields = Arrays.asList("createdBy", "modifiedBy", "enabled", "personGroup", "personQuestionnaireEntities");
        EntitiesUpdater.updateEntity(source, target, ignoredFields, PersonEntity.class);
        return Response.status(Status.OK)
                .type(MediaType.TEXT_PLAIN)
                .entity("Person was successful updated")
                .build();
    }

    public Response checkPersonEntityEmail(String email){
        boolean isValid = serviceQdump.personEntityExistsByEmail(email);
        String object = String.format("{\"isValid\" : %b }", isValid);
        if (isValid) {
            return Response.status(Status.FORBIDDEN)
                    .entity(object).build();
        }
        return Response.status(Status.OK).entity(object).build();
    }

    public Response checkPersonEntityLogin(String login) {
        boolean isValid = serviceQdump.personEntityExistsByLogin(login);
        String object = String.format("{\"isValid\" : %b }", !isValid);
        if (isValid) {
            return Response.status(Status.FORBIDDEN).entity(object).build();
        }
        return Response.status(Status.OK).entity(object).build();
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response verifyAccount(String token, UriInfo uriInfo) {
        VerificationTokenEntity tokenEntity = VerificationTokenUtils.tokenToBean(token);
        if(serviceQdump.verificationTokenEntityExists(token)) {
            exceptionCreator(Status.NOT_FOUND, "This token is not exists.");
        } else  if(tokenEntity.hasExpired()) {
            exceptionCreator(Status.CONFLICT, "This link has expired. " +
                    "Verification will be resend to your email");
        }
        PersonEntity personEntity = serviceQdump.getPersonByEmail(tokenEntity.getPersonEntity().getEmail());
        tokenEntity = serviceQdump.getTokenByPersonEntityEmail(personEntity.getEmail());
        tokenEntity.setVerified(true);
        personEntity.setEnabled(true);
        try {
            return Response.temporaryRedirect(new URI("/success"))
                    .type(MediaType.TEXT_PLAIN)
                    .entity("You successfully verify your account. Please welcome to our Project.")
                    .build();
        } catch (URISyntaxException e) {
            exceptionCreator(Status.CONFLICT, e.getMessage());
        }
        return Response.status(Status.FORBIDDEN).build();
    }

    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    public Response getEntityForPersonalPage() {
        if(!SecurityUtils.getSubject().isRemembered() && !SecurityUtils.getSubject().isAuthenticated()) {
            exceptionCreator(Status.UNAUTHORIZED, "You need to Authenticate");
        }
        PersonEntity personEntity = serviceQdump.getPersonEntity((long)SecurityUtils.getSubject().getPrincipal());
        return Response.ok(personEntity).build();
    }

    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response resetPersonEntityPassword() {
        long id = (long) SecurityUtils.getSubject().getPrincipal();
        String generatedPassword = RandomStringUtils.randomAlphanumeric(16);
        String hashedPassword = BCrypt.hashpw(generatedPassword, BCrypt.gensalt());
        String msg = String.format("You just reset your password. Your new password is: %s", generatedPassword);
        PersonEntity entity = serviceQdump.getPersonEntity(id);
        entity.setPassword(hashedPassword);
        mailSenderService.sendCustomMail(entity.getEmail(), msg);
        return Response.ok().build();
    }


    private void exceptionCreator(Status status, String message) {
        throw new WebApplicationException(Response
                .status(status)
                .entity(message)
                .type(MediaType.TEXT_PLAIN)
                .build());
    }

}
