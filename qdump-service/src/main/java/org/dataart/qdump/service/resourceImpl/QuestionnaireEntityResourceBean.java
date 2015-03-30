package org.dataart.qdump.service.resourceImpl;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.serializer.View;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.QuestionnaireEntityResource;
import org.dataart.qdump.service.utils.EntitiesUpdater;
import org.dataart.qdump.service.utils.QuestionnaireChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static org.dataart.qdump.service.utils.WebApplicationUtils.exceptionCreator;

@Component
public class QuestionnaireEntityResourceBean implements QuestionnaireEntityResource{
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;
    private long publishedQuestionnaireEntitiesCount;
    private long questionnaireEntitiesCount;

    public Response add(
            QuestionnaireEntity questionnaireEntity) {
        QuestionnaireChecker.checkPrePersist(questionnaireEntity);
        if(QuestionnaireChecker.checkCanBePublished(questionnaireEntity)) {
            questionnaireEntity.setPublished(true);
        }
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId((long) SecurityUtils.getSubject().getPrincipal());
        questionnaireEntity.setCreatedBy(personEntity);
        serviceQdump.addQuestionnaireEntity(questionnaireEntity);
        return Response.status(Status.CREATED).build();
    }

    @RequiresRoles("ADMIN")
    public Response delete(@PathParam("id") long id) {
        if (!serviceQdump.questionnaireEntityExists(id)) {
            return Response
                    .status(Status.NOT_FOUND)
                    .entity(String.format(
                            "Questionnaire with id = %d is not exists", id))
                    .build();
        } else {
            serviceQdump.deleteQuestionnaireEntity(id);
            return Response
                    .status(Status.OK)
                    .entity(String.format(
                            "Questionnaire with id = %d was deleted", id))
                    .build();
        }
    }

    @RequiresRoles("ADMIN")
    public void delete() {
        serviceQdump.deleteAllQuestionnaireEntity();
    }

    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public Response get(@PathParam("id") long id) {
        if (!serviceQdump.questionnaireEntityExists(id)) {
            ObjectNode objectNode = JsonNodeFactory.instance.objectNode();
            objectNode.put("error", String.format(
                    "Questionnaire with id = %d is not exists", id));
            return Response
                    .status(Status.NOT_FOUND)
                    .entity(objectNode)
                    .build();
        }
        QuestionnaireEntity entity = serviceQdump.getQuestionnaireEntity(id);
        return Response.status(Status.OK)
                .entity(entity).build();
    }

    public List<QuestionnaireEntity> get() {
        return serviceQdump.getQuestionnaireEntities();
    }

    public List<QuestionnaireEntity> getPublished(int page, int size, String direction, String sort) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<QuestionnaireEntity> databasePage = serviceQdump.getPublishedQuestionnaireEntities(pageable);
        publishedQuestionnaireEntitiesCount = databasePage.getTotalElements();
        return databasePage.getContent();
    }

    public Response countPublished() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if(publishedQuestionnaireEntitiesCount == 0) {
            publishedQuestionnaireEntitiesCount = serviceQdump.countPublishedQuestionnaireEntities();
        }
        node.put("count", publishedQuestionnaireEntitiesCount);
        return Response.ok(node).build();
    }

    @RequiresRoles("ADMIN")
    public List<QuestionnaireEntity> get(int page, int size, String direction, String sort) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<QuestionnaireEntity> databasePage = serviceQdump.getAllQuestionnaireEntities(pageable);
        questionnaireEntitiesCount = databasePage.getTotalElements();
        return databasePage.getContent();
    }

    @RequiresRoles("ADMIN")
    public Response count() {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        if(questionnaireEntitiesCount == 0) {
            questionnaireEntitiesCount = serviceQdump.questionnaireEntitiesCount();
        }
        node.put("count", questionnaireEntitiesCount);
        return Response.ok(node).build();
    }

    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @JsonView(View.Public.class)
    public QuestionnaireEntity getPersonal(long id) {
        return serviceQdump.getQuestionnaireEntity(id);
    }

    @RequiresRoles("ADMIN")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(QuestionnaireEntity source) {
        long sourceId = source.getId();
        if(!serviceQdump.questionnaireEntityExists(sourceId)) {
            exceptionCreator(Status.NOT_FOUND, String.format("Questionnaire with id = %d not found", sourceId));
        }
        QuestionnaireEntity target = serviceQdump.getQuestionnaireEntity(sourceId);
        PersonEntity personEntity = new PersonEntity();
        personEntity.setId((long) SecurityUtils.getSubject().getPrincipal());
        source.setModifiedBy(personEntity);
        EntitiesUpdater.update(source, target);
    }
}
