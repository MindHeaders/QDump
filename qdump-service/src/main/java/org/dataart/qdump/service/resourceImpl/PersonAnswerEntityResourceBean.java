package org.dataart.qdump.service.resourceImpl;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonAnswerEntityResource;
import org.dataart.qdump.service.utils.PersonQuestionnaireChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static org.dataart.qdump.service.utils.WebApplicationUtils.exceptionCreator;

@Component
public class PersonAnswerEntityResourceBean implements PersonAnswerEntityResource{

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;

    public Response add(PersonAnswerEntity entity) {
        PersonQuestionnaireChecker.PersonQuestionChecker.PersonAnswerChecker.checkPrePersist(entity);
        serviceQdump.addPersonAnswerEntity(entity);
        return Response
                .status(Status.CREATED)
                .build();
    }

    public Response delete(@PathParam("id") long id) {
        if(!serviceQdump.personAnswerEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, String.format("Person answer with id = %d not found", id));
        }
        serviceQdump.deletePersonAnswerEntity(id);
        return Response.ok().build();
    }

    public void delete() {
        serviceQdump.deleteAllPersonAnswerEntity();
    }

    public Response get(@PathParam("id") long id) {
        if(!serviceQdump.personAnswerEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, String.format("Person answer with id = %d not found", id));
        }
        return Response
                .ok(serviceQdump.getPersonAnswerEntity(id)).build();
    }

    public List<PersonAnswerEntity> get() {
        return serviceQdump.getPersonAnswerEntities();
    }
}
