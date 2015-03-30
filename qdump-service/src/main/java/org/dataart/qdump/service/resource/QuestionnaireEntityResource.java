package org.dataart.qdump.service.resource;

import com.fasterxml.jackson.annotation.JsonView;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.entities.serializer.View;
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
public interface QuestionnaireEntityResource {
    @POST
    @Path("create")
    @RequiresRoles("ADMIN")
    public Response add(
            QuestionnaireEntity questionnaireEntity);

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
    @RequiresRoles("ADMIN")
    public List<QuestionnaireEntity> get();

    @GET
    @Path("get/published")
    public List<QuestionnaireEntity> getPublished(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);
    @GET
    @Path("get/published/count")
    public Response countPublished();

    @GET
    @Path("get/{id}")
    @RequiresRoles("ADMIN")
    public Response get(@PathParam("id") long id);

    @GET
    @Path("get/all")
    @RequiresRoles("ADMIN")
    public List<QuestionnaireEntity> get(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("get/all/count")
    @RequiresRoles("ADMIN")
    public Response count();

    @GET
    @Path("personal/{id}")
    @JsonView(View.Public.class)
    @RequiresRoles(value = {"ADMIN", "USER"}, logical = Logical.OR)
    public QuestionnaireEntity getPersonal(@PathParam("id") long id);

    @PUT
    @Path("update")
    @RequiresRoles("ADMIN")
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
    public void update(QuestionnaireEntity entity);

}
