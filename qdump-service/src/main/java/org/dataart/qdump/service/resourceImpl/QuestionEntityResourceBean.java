package org.dataart.qdump.service.resourceImpl;

import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.QuestionEntityResource;
import org.dataart.qdump.service.utils.QuestionnaireChecker;
import org.dataart.qdump.service.utils.WebApplicationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Component
public class QuestionEntityResourceBean implements QuestionEntityResource{

    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;

    public Response add(QuestionEntity questionEntity) {
        QuestionnaireChecker.QuestionChecker.checkPrePersist(questionEntity);
        serviceQdump.addQuestionEntity(questionEntity);
        return Response.status(Status.CREATED).build();
    }

    public Response delete(@PathParam("id") long id) {
        if (!serviceQdump.questionEntityExists(id)) {
            WebApplicationUtils.exceptionCreator(Status.NOT_FOUND, String.format(
                    "Question with id = %d is not exists", id));
        }
        serviceQdump.deleteQuestionEntity(id);
        return WebApplicationUtils.responseCreator(Status.OK, String.format("Question with id = %d was deleted",
                id));
    }

    public void delete() {
        serviceQdump.deleteQuestionEntities();
    }

    public QuestionEntity get(@PathParam("id") long id) {
        if (!serviceQdump.questionEntityExists(id)) {
            WebApplicationUtils.exceptionCreator(Status.NOT_FOUND, String.format(
                    "Question with id = %d is not exists", id));
        }
        return serviceQdump.getQuestionEntity(id);
    }

    public List<QuestionEntity> get() {
        return serviceQdump.getQuestionEntities();
    }
}
