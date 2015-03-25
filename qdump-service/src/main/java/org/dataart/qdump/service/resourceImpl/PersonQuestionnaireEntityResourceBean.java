package org.dataart.qdump.service.resourceImpl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonQuestionnaireEntityResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Component
public class PersonQuestionnaireEntityResourceBean implements PersonQuestionnaireEntityResource{
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;
    private long startedPersonQuestionnairesCount;
    private long completedPersonQuestionnairesCount;

    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response addPersonQuestionnaire(PersonQuestionnaireEntity entity) {
        if(SecurityUtils.getSubject().getPrincipal() == null) {
            exceptionCreator(Status.UNAUTHORIZED, "User is not authorized");
        }
        if(entity.getId() > 0) {
            exceptionCreator(Status.CONFLICT, "You cannot create Person Questionnaire with id that greater than 0");
        } else if(!entity.checkIdForCreation()) {
            exceptionCreator(Status.FORBIDDEN, "Incorrect persisted data. Persisted Person Questionnaire, Question, Answer shouldn`t contains id greater than 0 and Questionnaire, Question, Answer id should not be equals to 0.");
        }
        PersonEntity personEntity = serviceQdump.getPersonEntity((long) SecurityUtils.getSubject().getPrincipal());
        personEntity.getPersonQuestionnaireEntities().add(entity);
        return Response
                .status(Status.CREATED)
                .build();
    }

    public Response deletePersonQuestionnaireEntity(@PathParam("id") long id) {
        if(!serviceQdump.personQuestionnaireEntityExists(id)) {
            return Response
                    .status(Status.NOT_FOUND)
                    .entity("Person Questionnaire with this id is not exists")
                    .build();
        }
        serviceQdump.deletePersonQuestionnaireEntity(id);
        return Response
                .status(Status.OK)
                .build();
    }

    public Response deleteAllPersonQuestionnaireEntities() {
        serviceQdump.deleteAllPersonQuestionnaireEntities();
        return Response
                .status(Status.OK)
                .build();
    }

    public Response getPersonQuestionnaireEntity(@PathParam("id") long id) {
        if(serviceQdump.personQuestionnaireEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, "Person Questionnaire with this id is not exists");
        }
        userIsAuthorized();
        PersonQuestionnaireEntity entity = serviceQdump.getPersonQuestionnaireEntity(id, (long) SecurityUtils
                .getSubject()
                .getPrincipal());
        return Response
                .status(Status.OK)
                .entity(entity)
                .build();
    }

    public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities() {
        return serviceQdump.getPersonQuestionnaireEntities();
    }

    public Response updatePersonQuestionnaireEntity(
            PersonQuestionnaireEntity source) {
        if (!serviceQdump.personQuestionnaireEntityExists(source.getId())) {
            exceptionCreator(Status.NOT_FOUND, "Person Questionnaire with this id is not exists");
        }
        PersonQuestionnaireEntity target = serviceQdump
                .getPersonQuestionnaireEntity(source.getId());
        target.updateEntity(source);
        return Response
                .status(Status.OK)
                .entity("Person Questionnaire was successfully updated")
                .build();
    }

    public List<PersonQuestionnaireEntity> getCompletedPersonQuestionnairesPagination(int page, int size, String direction, String sort) {
        userIsAuthorized();
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<PersonQuestionnaireEntity> databasePage = serviceQdump.getCompletedPersonQuestionnaireEntities((long) SecurityUtils.getSubject().getPrincipal(), pageable);
        completedPersonQuestionnairesCount = databasePage.getTotalElements();
        return databasePage.getContent();
    }

    public List<PersonQuestionnaireEntity> getStartedPersonQuestionnairesPagination(int page, int size, String direction, String sort) {
        userIsAuthorized();
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<PersonQuestionnaireEntity> databasePage = serviceQdump.getStartedPersonQuestionnaireEntities((long) SecurityUtils.getSubject().getPrincipal(), pageable);
        startedPersonQuestionnairesCount = databasePage.getTotalElements();
        return databasePage.getContent();
    }

    public Response countPersonQuestionnaires(String type) {
        userIsAuthorized();
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        if(type.toUpperCase().equals("STARTED")) {
            if(startedPersonQuestionnairesCount == 0 ) {
                startedPersonQuestionnairesCount = serviceQdump.countStartedPersonQuestionnaireEntities((long) SecurityUtils.getSubject().getPrincipal());
                objectNode.put("count", startedPersonQuestionnairesCount);
            } else {
                objectNode.put("count", startedPersonQuestionnairesCount);
            }
        } else if(type.toUpperCase().equals("COMPLETED")) {
            if(completedPersonQuestionnairesCount == 0) {
                completedPersonQuestionnairesCount = serviceQdump.countCompletedPersonQuestionnaireEntities((long) SecurityUtils.getSubject().getPrincipal());
                objectNode.put("count", completedPersonQuestionnairesCount);
            } else {
                objectNode.put("count", completedPersonQuestionnairesCount);
            }
        }
        return Response.ok(objectNode).build();
    }

    public PersonQuestionnaireEntity getCompletedQuestionnaireEntity(long id) {
        if(SecurityUtils.getSubject().getPrincipal() == null) {
            exceptionCreator(Status.UNAUTHORIZED, "User is not authorized");
        } else if(id == 0) {
            exceptionCreator(Status.NOT_ACCEPTABLE, "Inserted is incorrect");
        }
        PersonQuestionnaireEntity entity = serviceQdump.getPersonQuestionnaireEntity(id, (long) SecurityUtils
                .getSubject
                        ().getPrincipal());
        if(entity == null) {
            exceptionCreator(Status.NOT_FOUND, "There is not person questionnaire with this persisted data");
        }
        return entity;
    }

    private void exceptionCreator(Status status, String message) {
        ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
        objectNode.put("error", message);
        throw new WebApplicationException(Response
                .status(status)
                .entity(objectNode)
                .type(MediaType.APPLICATION_JSON)
                .build());
    }
    private void userIsAuthorized() {
        if(SecurityUtils.getSubject().getPrincipal() == null) {
            exceptionCreator(Status.UNAUTHORIZED, "User is not unauthorized");
        }
    }
}
