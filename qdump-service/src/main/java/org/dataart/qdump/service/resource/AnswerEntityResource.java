package org.dataart.qdump.service.resource;

import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by @author vlasovartem
 * Date: 19.01.15
 * Time: 0:18
 */
@Component
@Path("/answers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AnswerEntityResource {

    @POST
    @Path("create")
    public Response addAnswerEntity(AnswerEntity answerEntity);

    @DELETE
    @Path("delete/{id}")
    public Response deleteAnswerEntity(@PathParam("id") long id);

    @DELETE
    @Path("delete")
    public void deleteAllAnswerEntity();

    @GET
    @Path("get/{id}")
    public Response getAnswerEntity(@PathParam("id") long id);

    @GET
    @Path("get")
    public List<AnswerEntity> getAnswerEntities();

    @GET
    public Response get();
}
