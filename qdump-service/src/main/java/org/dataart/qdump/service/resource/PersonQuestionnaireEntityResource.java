package org.dataart.qdump.service.resource;

import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.springframework.stereotype.Component;

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
    public Response addPersonQuestionnaire(PersonQuestionnaireEntity entity);

    @DELETE
    @Path("delete/{id}")
    public Response deletePersonQuestionnaireEntity(@PathParam("id") long id);

    @DELETE
    @Path("delete")
    public Response deleteAllPersonQuestionnaireEntities();

    @GET
    @Path("get/{id}")
    public Response getPersonQuestionnaireEntity(@PathParam("id") long id);

    @GET
    @Path("get")
    public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities();

    @PUT
    @Path("update")
    public Response updatePersonQuestionnaireEntity(
            PersonQuestionnaireEntity source);

    @GET
    @Path("/completed")
    public List<PersonQuestionnaireEntity> getCompletedPersonQuestionnairesPagination(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("/started")
    public List<PersonQuestionnaireEntity> getStartedPersonQuestionnairesPagination(
            @DefaultValue("0") @QueryParam("page") int page,
            @DefaultValue("15") @QueryParam("size") int size,
            @DefaultValue("DESC") @QueryParam("direction") String direction,
            @DefaultValue("createdDate") @QueryParam("sort") String sort);

    @GET
    @Path("/count/{type}")
    public Response countPersonQuestionnaires(@PathParam("type") String type);

    @GET
    @Path("/completed/{id}")
    public PersonQuestionnaireEntity getCompletedQuestionnaireEntity(@PathParam("id") long id);

}
