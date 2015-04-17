package org.dataart.qdump.service.resourceImpl;

import org.apache.shiro.SecurityUtils;
import org.dataart.qdump.entities.enums.QuestionnaireStatusEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.statistics.PersonQuestionnaireEntitiesStatistic;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonQuestionnaireEntityResource;
import org.dataart.qdump.service.utils.EntitiesUpdater;
import org.dataart.qdump.service.utils.PersonQuestionnaireChecker;
import org.dataart.qdump.service.utils.WebApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static org.dataart.qdump.service.utils.WebApplicationUtils.exceptionCreator;

@Component
public class PersonQuestionnaireEntityResourceBean implements PersonQuestionnaireEntityResource{
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;

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
        serviceQdump.deletePersonQuestionnaireEntities();
    }

    public Response delete(@PathParam("id") long id) {
        if(!serviceQdump.personQuestionnaireEntityExists(id)) {
            WebApplicationUtils.exceptionCreator(Status.NOT_FOUND, "Person Questionnaire with this id is not exists");
        }
        serviceQdump.deletePersonQuestionnaireEntity(id);
        return Response
                .status(Status.OK)
                .build();
    }

    public List<PersonQuestionnaireEntity> get() {
        List<PersonQuestionnaireEntity> personQuestionnaireEntities = serviceQdump.getPersonQuestionnaireEntities();
        personQuestionnaireEntities.stream().forEach(entity -> entity.getPersonQuestionEntities().size());
        return personQuestionnaireEntities;
    }

    public List<PersonQuestionnaireEntity> getInCheckingProcess(int page, int size, String direction, String sort) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        return serviceQdump.getPersonQuestionnairesInCheckingProcess(pageable);
    }

    public PersonQuestionnaireEntity get(@PathParam("id") long id) {
        if(serviceQdump.personQuestionnaireEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, "Person Questionnaire with this id is not exists");
        }
        userIsAuthorized();
        PersonQuestionnaireEntity entity = serviceQdump.getPersonQuestionnaireEntity(id, (long) SecurityUtils
                .getSubject()
                .getPrincipal());
        entity.getPersonQuestionEntities().size();
        return entity;
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
        return serviceQdump.getCompletedPersonQuestionnaireEntities((long) SecurityUtils.getSubject().getPrincipal(), pageable);
    }

    public List<PersonQuestionnaireEntity> getStarted(int page, int size, String direction, String sort) {
        userIsAuthorized();
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        return serviceQdump.getStartedPersonQuestionnaireEntities((long) SecurityUtils.getSubject().getPrincipal(), pageable);
    }

    public Response check(long id, boolean correct) {
        return null;
    }

    public Response count(String type) {
        userIsAuthorized();
        long count = 0;
        if(type.toUpperCase().equals("STARTED")) {
            count = serviceQdump.startedPersonQuestionnaireEntitiesCount((long) SecurityUtils.getSubject().getPrincipal
                    ());
        } else if(type.toUpperCase().equals("COMPLETED")) {
           count = serviceQdump.completedPersonQuestionnaireEntitiesCount((long) SecurityUtils.getSubject()
                   .getPrincipal());
        } else if (type.toUpperCase().equals("CHECKING")) {
            count = serviceQdump.personQuestionnaireCountByStatus(
                    (QuestionnaireStatusEnums.IN_CHECKING_PROCESS.getName()));
        }
        return WebApplicationUtils.responseCreator(Status.OK, "count", count);
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
        entity.getPersonQuestionEntities().size();
        return entity;
    }

    public PersonQuestionnaireEntitiesStatistic getStatistics() {
        PersonQuestionnaireEntitiesStatistic statistic = new PersonQuestionnaireEntitiesStatistic();
        statistic.setCompletedPersonQuestionnaireEntities(serviceQdump.completedQuestionnaireEntitiesCount());
        statistic.setStartedPersonQuestionnaireEntities(serviceQdump.startedQuestionnaireEntitiesCount());
        statistic.setTotalPersonQuestionnaireEntities(serviceQdump.personQuestionnaireEntitiesCount());
        return statistic;
    }

    private void userIsAuthorized() {
        if(SecurityUtils.getSubject().getPrincipal() == null) {
            exceptionCreator(Status.UNAUTHORIZED, "User is not unauthorized");
        }
    }
}
