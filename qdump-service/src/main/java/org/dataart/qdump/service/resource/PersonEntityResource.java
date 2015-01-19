package org.dataart.qdump.service.resource;

import org.dataart.qdump.entities.person.PersonEntity;
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
@Path("/persons")
public interface PersonEntityResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get/{id}")
    public Response getPersonEntity(@PathParam("id") long id);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("login")
    public Response login(@FormParam("login") String login,
                          @FormParam("password") String password);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registration")
    public Response registration(PersonEntity entity);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    public List<PersonEntity> getAll();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get/min")
    public List<PersonEntity> getAllMin();

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete/{id}")
    public Response deletePersonEntity(@PathParam("id") long id);

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete")
    public Response deleteAllPersonEntities();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("update")
    public Response updatePersonEntity(PersonEntity entity);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("check/email")
    public Response checkPersonEntityEmail(@QueryParam("email") String email);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("check/login")
    public Response checkPersonEntityLogin(@QueryParam("login") String login);
}
