package org.dataart.qdump.service.resource;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.serializer.View;
import org.dataart.qdump.entities.statistics.QuestionnaireEntitiesStatistic;
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
@Path("/questionnaires")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Transactional
public interface QuestionnaireEntityResource {
    @POST
    @RequiresRoles("ADMIN")
    Response add(
            QuestionnaireEntity questionnaireEntity);

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
    @RequiresRoles("ADMIN")
    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    List<QuestionnaireEntity> get();

    @GET
    @RequiresRoles("ADMIN")
    @JsonView(View.Page.class)
    List<QuestionnaireEntity> get(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("count")
    @RequiresRoles("ADMIN")
    Response count();

    @GET
    @Path("published")
    @JsonView(View.Page.class)
    List<QuestionnaireEntity> getPublished(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);
    @GET
    @Path("published/count")
    Response countPublished();

    @GET
    @Path("{id : \\d+}")
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    Response get(@PathParam("id") long id);

    @GET
    @Path("personal/{id : \\d+}")
    @JsonView(View.User.class)
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    QuestionnaireEntity getPersonal(@PathParam("id") long id);

    @PUT
    @RequiresRoles("ADMIN")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    void update(QuestionnaireEntity entity);

    @GET
    @Path("statistic")
    @RequiresRoles("ADMIN")
    @JsonView(View.Page.class)
    QuestionnaireEntitiesStatistic getStatistics();

}
