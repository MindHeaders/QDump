package org.dataart.qdump.service.resource;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
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
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@Path("/persons/questions")
public interface PersonQuestionEntityResource {

    @POST
    @Path("create")
    public Response addPersonQuestionEntity(PersonQuestionEntity entity);

    @DELETE
    @Path("delete/{id}")
    public Response deletePersonQuestionEntity(@PathParam("id") long id);

    @DELETE
    @Path("delete")
    public void deleteAllPersonQuestionEntity();

    @GET
    @Path("get/{id}")
    public Response getPersonQuestionEntity(@PathParam("id") long id);

    @GET
    @Path("get")
    public List<PersonQuestionEntity> getPersonQuestionEntities();
}
