package org.dataart.qdump.service.resource;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
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
 * Time: 0:19
 */
@Component
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/persons/questionnaires")
public interface PersonQuestionnaireEntityResource {

    @POST
    @Path("create")
    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response add(PersonQuestionnaireEntity entity);

    @DELETE
    @Path("delete")
    @RequiresRoles("ADMIN")
    public void delete();

    @DELETE
    @Path("delete/{id}")
    @RequiresRoles("ADMIN")
    public Response delete(@PathParam("id") long id);

    @GET
    @Path("get")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public List<PersonQuestionnaireEntity> get();

    @GET
    @Path("get/checking")
    @RequiresRoles("ADMIN")
    public List<PersonEntity> getInCheckingProcess(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("get/{id}")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public Response get(@PathParam("id") long id);

    @PUT
    @Path("update")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response update(
            PersonQuestionnaireEntity source);

    @GET
    @Path("/completed")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public List<PersonQuestionnaireEntity> getCompleted(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("/started")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public List<PersonQuestionnaireEntity> getStarted(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("/count/checking")
    @RequiresRoles("ADMIN")
    public Response countInCheckingProcess();

    @PUT
    @Path("check/{id}")
    @RequiresRoles("ADMIN")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public Response check(@PathParam("id") long id, @QueryParam("correct") boolean correct);

    @GET
    @Path("/count/{type}")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public Response count(@PathParam("type") String type);

    @GET
    @Path("/completed/{id}")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public PersonQuestionnaireEntity getCompleted(@PathParam("id") long id);


}
