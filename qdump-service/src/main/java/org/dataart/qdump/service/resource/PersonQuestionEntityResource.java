package org.dataart.qdump.service.resource;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
@Transactional
public interface PersonQuestionEntityResource {

    @POST
    @RequiresRoles("ADMIN")
    Response add(PersonQuestionEntity entity);

    @DELETE
    @RequiresRoles("ADMIN")
    void delete();

    @DELETE
    @Path("{id : \\d+}")
    @RequiresRoles("ADMIN")
    Response delete(@PathParam("id") long id);

    @GET
    @RequiresRoles("ADMIN")
    List<PersonQuestionEntity> get();

    @GET
    @Path("checking")
    @RequiresRoles("ADMIN")
    List<PersonQuestionEntity> get(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("{id : \\d+}")
    @RequiresRoles("ADMIN")
    Response get(@PathParam("id") long id);

    @GET
    @Path("checking/count")
    @RequiresRoles("ADMIN")
    Response count();

    @PUT
    @Path("verify/{id : \\d+}")
    @RequiresRoles("ADMIN")
    void verify(@PathParam("id") long id, @QueryParam("correct") boolean correct);

    @PUT
    @Path("verify")
    @RequiresRoles("ADMIN")
    void verify(List<PersonQuestionEntity> personQuestionEntities);

}
