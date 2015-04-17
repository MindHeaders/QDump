package org.dataart.qdump.service.resource;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.serializer.View;
import org.dataart.qdump.entities.statistics.PersonQuestionnaireEntitiesStatistic;
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
@Transactional
public interface PersonQuestionnaireEntityResource {

    @POST
    @RequiresRoles(value = {"USER", "ADMIN"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    Response add(PersonQuestionnaireEntity entity);

    @DELETE
    @RequiresRoles("ADMIN")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    void delete();

    @DELETE
    @Path("{id : \\d+}")
    @RequiresRoles("ADMIN")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    Response delete(@PathParam("id") long id);

    @GET
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @JsonView(View.User.class)
    List<PersonQuestionnaireEntity> get();

    @GET
    @Path("checking")
    @RequiresRoles("ADMIN")
    List<PersonQuestionnaireEntity> getInCheckingProcess(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("completed")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    List<PersonQuestionnaireEntity> getCompleted(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("started")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    List<PersonQuestionnaireEntity> getStarted(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("{id : \\d+}")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @JsonView(View.User.class)
    PersonQuestionnaireEntity get(@PathParam("id") long id);

    @PUT
    @Path("update")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    Response update(
            PersonQuestionnaireEntity source);

    @PUT
    @Path("check/{id : \\d+}")
    @RequiresRoles("ADMIN")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    Response check(@PathParam("id") long id, @QueryParam("correct") boolean correct);

    @GET
    @Path("completed/{id : \\d+}")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    @JsonView(View.User.class)
    PersonQuestionnaireEntity getCompleted(@PathParam("id") long id);

    @GET
    @Path("{type: \\D+}/count")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    Response count(@PathParam("type") String type);

    @GET
    @Path("statistic")
    @RequiresRoles("ADMIN")
    PersonQuestionnaireEntitiesStatistic getStatistics();

}
