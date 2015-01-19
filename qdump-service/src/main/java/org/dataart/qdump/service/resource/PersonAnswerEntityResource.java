package org.dataart.qdump.service.resource;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
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
@Path("/persons/answers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PersonAnswerEntityResource {
    @POST
    @Path("create")
    public Response addPersonAnswerEntity(PersonAnswerEntity entity);

    @DELETE
    @Path("delete/{id}")
    public Response deletePersonAnswerEntity(@PathParam("id") long id);

    @DELETE
    @Path("delete/all")
    public void deleteAllPersonAnswerEntity();

    @GET
    @Path("get/{id}")
    public Response getPersonAnswerEntity(@PathParam("id") long id);

    @GET
    @Path("get/all")
    public List<PersonAnswerEntity> getPersonAnswerEntities();
}
