package org.dataart.qdump.service.resource;

import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by @author vlasovartem
 * Date: 19.01.15
 * Time: 0:19
 */
@Component
@Path("/questionnaires")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface QuestionnaireEntityResource {
    @POST
    @Path("create")
    public Response addQuestionnaireEntity(
            QuestionnaireEntity questionnaireEntity);

    @DELETE
    @Path("delete/{id}")
    public Response deleteQuestionnaireEntity(@PathParam("id") long id);

    @DELETE
    @Path("delete")
    public void deleteAllQuestionnaireEntity();

    @GET
    @Path("get/{id}")
    public Response getQuestionnaireEntity(@PathParam("id") long id);

    @GET
    @Path("get")
    public List<QuestionnaireEntity> getQuestionnaireEntities();
}
