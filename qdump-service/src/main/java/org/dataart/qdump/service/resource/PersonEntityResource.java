package org.dataart.qdump.service.resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.person.PersonEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    public Response get(@PathParam("id") long id);

    @POST
    @Path("authentication")
    public Response authentication(@FormParam("login_or_email") String loginOrEmail,
                                   @FormParam("password") String password,
                                   @FormParam("rememberMe") boolean rememberMe);

    @GET
    @Path("logout")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public void logout();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("registration")
    public Response registration(PersonEntity entity, @Context UriInfo uriInfo);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get")
    @RequiresRoles("ADMIN")
    public List<PersonEntity> get();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("get/min")
    @RequiresRoles("ADMIN")
    public List<PersonEntity> getAllMin();

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete")
    @RequiresRoles("ADMIN")
    public void delete();

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("delete/{id}")
    @RequiresRoles("ADMIN")
    public Response delete(@PathParam("id") long id);

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("update")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response update(PersonEntity source, @Context UriInfo uriInfo);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("check/email")
    public Response checkEmail(@QueryParam("email") String email);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("check/login")
    public Response checkLogin(@QueryParam("login") String login);

    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("verify")
    public Response verifyAccount(@QueryParam("token") String token, @Context UriInfo uriInfo);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("personal")
    public Response getEntityForPersonalPage();

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("reset/password")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public Response resetPassword();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("authorized")
    public Response getAuthorized();

}
