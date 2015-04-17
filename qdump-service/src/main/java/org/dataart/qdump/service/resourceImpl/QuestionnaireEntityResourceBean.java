package org.dataart.qdump.service.resourceImpl;

import org.apache.shiro.SecurityUtils;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.statistics.QuestionnaireEntitiesStatistic;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.QuestionnaireEntityResource;
import org.dataart.qdump.service.utils.EntitiesUpdater;
import org.dataart.qdump.service.utils.QuestionnaireChecker;
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
public class QuestionnaireEntityResourceBean implements QuestionnaireEntityResource{
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;

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

    public Response delete(@PathParam("id") long id) {
        if (!serviceQdump.questionnaireEntityExists(id)) {
            WebApplicationUtils.exceptionCreator(Status.NOT_FOUND, String.format(
                    "Questionnaire with id = %d is not exists", id));
        }
        serviceQdump.deletePersonQuestionnaireEntities(id);
        serviceQdump.deleteQuestionnaireEntity(id);
        return Response.ok().build();
    }

    public void delete() {
        serviceQdump.deleteQuestionnaireEntities();
    }

    public Response get(@PathParam("id") long id) {
        if (!serviceQdump.questionnaireEntityExists(id)) {
            WebApplicationUtils.exceptionCreator(Status.NOT_FOUND, String.format(
                    "Questionnaire with id = %d is not exists", id));
        }
        QuestionnaireEntity entity = serviceQdump.getQuestionnaireEntity(id);
        return Response.status(Status.OK)
                .entity(entity).build();
    }

    public List<QuestionnaireEntity> get() {
        List<QuestionnaireEntity> questionnaireEntities = serviceQdump.getQuestionnaireEntities();
        questionnaireEntities.stream().forEach(entity -> entity.getQuestionEntities().size());
        return questionnaireEntities;
    }

    public List<QuestionnaireEntity> getPublished(int page, int size, String direction, String sort) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        return serviceQdump.getQuestionnaireEntities(true, pageable);
    }

    public Response countPublished() {
        return WebApplicationUtils.responseCreator(Status.OK, "count", serviceQdump
                .publishedQuestionnaireEntitiesCount(true));
    }

    public List<QuestionnaireEntity> get(int page, int size, String direction, String sort) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        return serviceQdump.getQuestionnaireEntities(pageable);
    }

    public Response count() {
        return WebApplicationUtils.responseCreator(Status.OK, "count", serviceQdump
                .questionnaireEntitiesCount());
    }

    public QuestionnaireEntity getPersonal(long id) {
        return serviceQdump.getQuestionnaireEntity(id);
    }

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

    public QuestionnaireEntitiesStatistic getStatistics() {
        QuestionnaireEntitiesStatistic statistics = new QuestionnaireEntitiesStatistic();
        statistics.setDateOfLastCreatedQuestionnaireEntity(serviceQdump.getLastQuestionnaireEntityCreatedDate());
        statistics.setPopularQuestionnaireEntity(serviceQdump.getPopularQuestionnaireEntity());
        statistics.setPublishedQuestionnaireEntities(serviceQdump.publishedQuestionnaireEntitiesCount(true));
        statistics.setTotalQuestionnaireEntities(serviceQdump.questionnaireEntitiesCount());
        return statistics;
    }
}
