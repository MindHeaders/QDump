package org.dataart.qdump.service.resource;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.person.PersonEntity;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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
    @Path("auth")
    public Response auth(@FormParam("login_or_email") String loginOrEmail,
                          @FormParam("password") String password,
                          @FormParam("rememberMe") boolean rememberMe);

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registration")
    public Response registration(PersonEntity entity, @Context UriInfo uriInfo);

    @GET
    @Path("logout")
    public void logout();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    @RequiresRoles("ADMIN")
    public List<PersonEntity> getAll();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get/min")
    @RequiresRoles("ADMIN")
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
    public Response updatePersonEntity(PersonEntity source);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("check/email")
    public Response checkPersonEntityEmail(@QueryParam("email") String email);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("check/login")
    public Response checkPersonEntityLogin(@QueryParam("login") String login);

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("verify")
    public Response verifyAccount(@QueryParam("token") String token, @Context UriInfo uriInfo);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/personal")
    public Response getEntityForPersonalPage();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reset/password")
    public Response resetPersonEntityPassword();

}
