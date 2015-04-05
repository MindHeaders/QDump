package org.dataart.qdump.service.resourceImpl;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonQuestionEntityResource;
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

@Component
public class PersonQuestionEntityResourceBean implements PersonQuestionEntityResource{
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;
    long notCheckedQuestionsCount;

    public Response add(PersonQuestionEntity entity) {
        PersonQuestionnaireChecker.PersonQuestionChecker.checkPrePersist(entity);
        serviceQdump.addPersonQuestionEntity(entity);
        return Response
                .status(Status.CREATED)
                .build();
    }

    public Response delete(@PathParam("id") long id) {
        if(!serviceQdump.personQuestionEntityExists(id)) {
            WebApplicationUtils.exceptionCreator(Status.NOT_FOUND, String.format("Person question with id = %d " +
                    "not found", id));
        }
        serviceQdump.deletePersonQuestionEntity(id);
        return WebApplicationUtils.responseCreator(Status.OK, String.format("Person question with id = %d was " +
                "deleted", id));
    }

    public void delete() {
        serviceQdump.deleteAllPersonQuestionEntity();
    }

    public List<PersonQuestionEntity> get() {
        return serviceQdump.getPersonQuestionEntities();
    }

    public List<PersonQuestionEntity> getChecking(int page, int size, String direction, String sort) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<PersonQuestionEntity> databasePage = serviceQdump.getNotCheckedPersonQuestionEntities(pageable);
        notCheckedQuestionsCount = databasePage.getTotalElements();
        return databasePage.getContent();
    }

    public Response get(@PathParam("id") long id) {
        if(!serviceQdump.personQuestionEntityExists(id)) {
            WebApplicationUtils.exceptionCreator(Status.NOT_FOUND, String.format("Person question with id = %d " +
                    "not found", id));
        }
        return Response
                .ok(serviceQdump.getPersonQuestionEntity(id)).build();
    }

    public Response count() {
        if(notCheckedQuestionsCount != 0) {
            notCheckedQuestionsCount = serviceQdump.countNotCheckedPersonQuestionEntities();
        }
        return WebApplicationUtils.responseCreator(Status.OK, "count", notCheckedQuestionsCount);
    }

    public void verify(long id, boolean correct) {
        if(!serviceQdump.personQuestionEntityExists(id)) {
            WebApplicationUtils.exceptionCreator(Status.NOT_FOUND, String.format("Person question with id = %d " +
                    "not found", id));
        }
        PersonQuestionnaireEntity personQuestionnaireEntity = serviceQdump.getPersonQuestionnaireByPersonQuestion(id);
        PersonQuestionEntity personQuestionEntity = personQuestionnaireEntity.getPersonQuestionEntities()
                .stream()
                .filter(entity -> entity.getId() == id).findFirst().get();
        personQuestionEntity.setChecked(true);
        personQuestionEntity.setCorrect(correct);
        PersonQuestionnaireChecker.checkStatus(personQuestionnaireEntity, personQuestionnaireEntity.getQuestionnaireEntity());
    }

    public void verify(List<PersonQuestionEntity> personQuestionEntities) {
        personQuestionEntities.stream().forEach(e -> verify(e.getId(), e.isCorrect()));
    }
}
