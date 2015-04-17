package org.dataart.qdump.service.resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.statistics.PersonEntitiesStatistic;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Path("/persons")
@Transactional
public interface PersonEntityResource {

    @POST
    @Path("authentication")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    Response authentication(@FormParam("login_or_email") String loginOrEmail,
                                   @FormParam("password") String password,
                                   @FormParam("rememberMe") boolean rememberMe);

    @GET
    @Path("logout")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    void logout();

    @POST
    @Path("registration")
    Response registration(PersonEntity entity, @Context UriInfo uriInfo);

    @GET
    @RequiresRoles("ADMIN")
    List<PersonEntity> get();

    @GET
    @RequiresRoles("ADMIN")
    List<PersonEntity> get(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("{id : \\d+}")
    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    PersonEntity get(@PathParam("id") long id);

    @GET
    @Path("personal")
    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    PersonEntity getPersonal();

    @DELETE
    @RequiresRoles("ADMIN")
    void delete();

    @DELETE
    @Path("{id : \\d+}")
    @RequiresRoles("ADMIN")
    Response delete(@PathParam("id") long id);

    @PUT
    @Path("{id : \\d+}")
    @RequiresRoles("ADMIN")
    void updatePersonGroup(@PathParam("id") long id, @DefaultValue("USER") @QueryParam("group") PersonGroupEnums
            group);

    @PUT
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    Response update(PersonEntity source, @Context UriInfo uriInfo);

    @GET
    @Path("check/email")
    Response checkEmail(@QueryParam("email") String email);

    @GET
    @Path("check/login")
    Response checkLogin(@QueryParam("login") String login);

    @GET
    @Path("verify")
    Response verifyAccount(@QueryParam("token") String token, @Context UriInfo uriInfo);

    @PUT
    @Path("reset/password")
    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    Response resetPassword();

    @GET
    @Path("authorized")
    Response getAuthorized();

    @GET
    @Path("check/permission/{role : \\D+}")
    Response checkPermission(@PathParam("role") String role);

    @GET
    @Path("count")
    Response count();

    @GET
    @Path("statistic")
    @RequiresRoles("ADMIN")
    PersonEntitiesStatistic getStatistics();
}
