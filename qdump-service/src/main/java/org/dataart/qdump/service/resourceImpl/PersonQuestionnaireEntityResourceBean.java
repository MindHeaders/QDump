package org.dataart.qdump.service.resourceImpl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.shiro.SecurityUtils;
import org.dataart.qdump.entities.enums.QuestionnaireStatusEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonQuestionnaireEntityResource;
import org.dataart.qdump.service.utils.EntitiesUpdater;
import org.dataart.qdump.service.utils.PersonQuestionnaireChecker;
import org.dataart.qdump.service.utils.WebApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static org.dataart.qdump.service.utils.WebApplicationUtils.exceptionCreator;
import static org.dataart.qdump.service.utils.WebApplicationUtils.responseCreator;

@Component
public class PersonQuestionnaireEntityResourceBean implements PersonQuestionnaireEntityResource{
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;
    private long startedPersonQuestionnairesCount;
    private long completedPersonQuestionnairesCount;
    private long inCheckingProcessCount;

    public Response add(PersonQuestionnaireEntity personQuestionnaireEntity) {
        if(SecurityUtils.getSubject().getPrincipal() == null) {
            exceptionCreator(Status.UNAUTHORIZED, "User is not authorized");
        }
        PersonQuestionnaireChecker.checkPrePersist(personQuestionnaireEntity);
        QuestionnaireEntity questionnaireEntity = serviceQdump.getQuestionnaireEntity(personQuestionnaireEntity
                .getQuestionnaireEntity().getId());
        PersonQuestionnaireChecker.checkStatus(personQuestionnaireEntity, questionnaireEntity);
        PersonEntity personEntity = serviceQdump.getPersonEntity((long) SecurityUtils.getSubject().getPrincipal());
        personEntity.getPersonQuestionnaireEntities().add(personQuestionnaireEntity);
        return Response
                .status(Status.CREATED)
                .build();
    }

    public void delete() {
        serviceQdump.deleteAllPersonQuestionnaireEntities();
    }

    public Response delete(@PathParam("id") long id) {
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

    public List<PersonQuestionnaireEntity> get() {
        return serviceQdump.getPersonQuestionnaireEntities();
    }

    public List<PersonEntity> getInCheckingProcess(int page, int size, String direction, String sort) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<PersonEntity> databasePage = serviceQdump.getPersonQuestionnairesInCheckingProcess(pageable);
        inCheckingProcessCount = databasePage.getTotalElements();
        return databasePage.getContent();
    }

    public Response get(@PathParam("id") long id) {
        if(serviceQdump.personQuestionnaireEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, "Person Questionnaire with this id is not exists");
        }
        userIsAuthorized();
        PersonQuestionnaireEntity entity = serviceQdump.getPersonQuestionnaireEntity(id, (long) SecurityUtils
                .getSubject()
                .getPrincipal());
        return responseCreator(Status.OK, entity);
    }

    public Response update(
            PersonQuestionnaireEntity personQuestionnaireEntity) {
        if (!serviceQdump.personQuestionnaireEntityExists(personQuestionnaireEntity.getId())) {
            exceptionCreator(Status.NOT_FOUND, "Person Questionnaire with this id is not exists");
        }
        QuestionnaireEntity questionnaireEntity = serviceQdump.getQuestionnaireEntity(personQuestionnaireEntity.getQuestionnaireEntity()
                .getId());
        PersonQuestionnaireChecker.checkStatus(personQuestionnaireEntity, questionnaireEntity);
        PersonQuestionnaireEntity target = serviceQdump
                .getPersonQuestionnaireEntity(personQuestionnaireEntity.getId());
        EntitiesUpdater.update(personQuestionnaireEntity, target);
        return Response
                .ok()
                .build();
    }

    public List<PersonQuestionnaireEntity> getCompleted(int page, int size, String direction, String sort) {
        userIsAuthorized();
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<PersonQuestionnaireEntity> databasePage = serviceQdump.getCompletedPersonQuestionnaireEntities((long) SecurityUtils.getSubject().getPrincipal(), pageable);
        completedPersonQuestionnairesCount = databasePage.getTotalElements();
        return databasePage.getContent();
    }

    public List<PersonQuestionnaireEntity> getStarted(int page, int size, String direction, String sort) {
        userIsAuthorized();
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<PersonQuestionnaireEntity> databasePage = serviceQdump.getStartedPersonQuestionnaireEntities((long) SecurityUtils.getSubject().getPrincipal(), pageable);
        startedPersonQuestionnairesCount = databasePage.getTotalElements();
        return databasePage.getContent();
    }

    public Response countInCheckingProcess() {
        if(inCheckingProcessCount == 0) {
            inCheckingProcessCount = serviceQdump.countPersonQuestionnaireByStatus(QuestionnaireStatusEnums.IN_CHECKING_PROCESS
                    .getName());
        }
        return WebApplicationUtils.responseCreator(Status.OK, "count", inCheckingProcessCount);
    }

    public Response check(long id, boolean correct) {
        return null;
    }

    public Response count(String type) {
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

    public PersonQuestionnaireEntity getCompleted(long id) {
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

    private void userIsAuthorized() {
        if(SecurityUtils.getSubject().getPrincipal() == null) {
            exceptionCreator(Status.UNAUTHORIZED, "User is not unauthorized");
        }
    }
}
