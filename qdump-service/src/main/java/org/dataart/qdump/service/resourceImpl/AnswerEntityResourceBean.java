package org.dataart.qdump.service.resourceImpl;

import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.AnswerEntityResource;
import org.dataart.qdump.service.utils.QuestionnaireChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

import static org.dataart.qdump.service.utils.WebApplicationUtils.exceptionCreator;

@Component
public class AnswerEntityResourceBean implements AnswerEntityResource{

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;

    public Response add(AnswerEntity answerEntity) {
        QuestionnaireChecker.QuestionChecker.AnswerChecker.checkPrePersist(answerEntity);
        serviceQdump.addAnswerEntity(answerEntity);
        return Response.status(Status.CREATED).build();
    }

    public Response delete(@PathParam("id") long id) {
        if (!serviceQdump.answerEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, String.format("Answer with id = %d is not exists", id));
        }
        serviceQdump.deleteAnswerEntity(id);
        return Response.ok().build();
    }

    public void delete() {
        serviceQdump.deleteAnswerEntities();
    }

    public AnswerEntity get(@PathParam("id") long id) {
        if (!serviceQdump.answerEntityExists(id)) {
            exceptionCreator(Status.NOT_FOUND, String.format("Answer with id = %d is not exists", id));
        }
        return serviceQdump.getAnswerEntity(id);
    }

    public List<AnswerEntity> get() {
        return serviceQdump.getAnswerEntities();
    }
}
