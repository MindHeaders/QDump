package org.dataart.qdump.service.resource;

import org.dataart.qdump.entities.questionnaire.QuestionEntity;
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
@Path("/questions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface QuestionEntityResource {
    @POST
    @Path("create")
    public Response addQuestionEntity(QuestionEntity questionEntity);

    @DELETE
    @Path("delete/{id}")
    public Response deleteQuestionEntity(@PathParam("id") long id);

    @DELETE
    @Path("delete")
    public void deleteAllQuestionEntity();

    @GET
    @Path("get/{id}")
    public Response getQuestionEntity(@PathParam("id") long id);

    @GET
    @Path("get")
    public List<QuestionEntity> getQuestionEntities();
}
