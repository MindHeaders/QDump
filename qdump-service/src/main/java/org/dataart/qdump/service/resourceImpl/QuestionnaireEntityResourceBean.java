package org.dataart.qdump.service.resourceImpl;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ValueNode;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.QuestionnaireEntityResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.List;

@Component
public class QuestionnaireEntityResourceBean implements QuestionnaireEntityResource{
    @Autowired
    @SuppressWarnings("SpringJavaAutowiringInspection")
    private ServiceQdump serviceQdump;

    @RequiresRoles("ADMIN")
    public Response addQuestionnaireEntity(
            QuestionnaireEntity questionnaireEntity) {
        if (questionnaireEntity.getId() > 0) {
            return Response.status(Status.CONFLICT)
                    .entity("Questionnaire id cannot be greater than 0")
                    .build();
        }
        if (!questionnaireEntity.checkIdForCreation()) {
            return Response.status(Status.CONFLICT)
                    .entity("Answer ud cannot be greater than 0").build();
        }
        if(checkQuestionnaireCanBePublished(questionnaireEntity)) {
            questionnaireEntity.setPublished(true);
        }
        serviceQdump.addQuestionnaireEntity(questionnaireEntity);
        return Response.status(Status.CREATED).build();
    }

    @RequiresRoles("ADMIN")
    public Response deleteQuestionnaireEntity(@PathParam("id") long id) {
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
    public void deleteAllQuestionnaireEntity() {
        serviceQdump.deleteAllQuestionnaireEntity();
    }

    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public Response getQuestionnaireEntity(@PathParam("id") long id) {
        if (!serviceQdump.questionnaireEntityExists(id)) {
            return Response
                    .status(Status.NOT_FOUND)
                    .entity(String.format(
                            "Questionnaire with id = %d is not exists", id))
                    .build();
        } else {
            return Response.status(Status.OK)
                    .entity(serviceQdump.getQuestionnaireEntity(id)).build();
        }
    }

    public List<QuestionnaireEntity> getQuestionnaireEntities() {
        return serviceQdump.getQuestionnaireEntities();
    }

    public List<QuestionnaireEntity> paginationQuestionnaire(int page, int size, String direction, String sort) {
        Pageable pageable = new PageRequest(page, size, Sort.Direction.fromString(direction), sort);
        Page<QuestionnaireEntity> databasePage = serviceQdump.questionnairesPagination(pageable);
        List<QuestionnaireEntity> resultPage = databasePage.getContent();
        return resultPage;
    }

    public Response countPaginationQuestionnaires() {
        Long count = serviceQdump.countPublishedQuestionnaires();
        JsonNodeFactory factory = JsonNodeFactory.instance;
        ValueNode countNode = factory.numberNode(count);
        return Response.ok(countNode).build();
    }

    private boolean checkQuestionnaireCanBePublished(QuestionnaireEntity questionnaireEntity) {
        if(questionnaireEntity.getName() == null) {
            exceptionCreator(Status.NOT_ACCEPTABLE, "Questionnaire name cannot be null");
        }
        boolean questionHasCorrectAnswer = false;
        for(QuestionEntity questionEntity : questionnaireEntity.getQuestionEntities()) {
            if(questionEntity.getQuestion() == null) {
                exceptionCreator(Status.NOT_ACCEPTABLE, "All questionnaire questions should be filled ");
            }
            for(AnswerEntity answerEntity : questionEntity.getAnswerEntities()) {
                if(!questionHasCorrectAnswer && !answerEntity.isCorrect()) {
                    questionHasCorrectAnswer = true;
                }
                if(answerEntity.getAnswer() == null) {
                    exceptionCreator(Status.NOT_ACCEPTABLE, "All question answers should be filled");
                }
            }
        }
        if(!questionHasCorrectAnswer) {
            exceptionCreator(Status.NOT_ACCEPTABLE, "One of the answers should be correct");
        }
        return true;
    }

    private void exceptionCreator(Status status, String message) {
        throw new WebApplicationException(Response
                .status(status)
                .entity(message)
                .type(MediaType.TEXT_PLAIN)
                .build());
    }
}
