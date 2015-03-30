package org.dataart.qdump.service.resourceImpl;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonQuestionEntityResource;
import org.dataart.qdump.service.utils.PersonQuestionnaireChecker;
import org.dataart.qdump.service.utils.WebApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<PersonQuestionEntity> getChecking() {
        return null;
    }

    public Response get(@PathParam("id") long id) {
        if(!serviceQdump.personQuestionEntityExists(id)) {
            WebApplicationUtils.exceptionCreator(Status.NOT_FOUND, String.format("Person question with id = %d " +
                    "not found", id));
        }
        return Response
                .ok(serviceQdump.getPersonQuestionEntity(id)).build();

    }
}
