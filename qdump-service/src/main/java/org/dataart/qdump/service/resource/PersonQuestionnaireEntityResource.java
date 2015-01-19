package org.dataart.qdump.service.resource;

import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
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
    @Path("delete/by/ownby/{id}")
    public Response deletePersonQuestionnaireEntityByOwnById(@PathParam("id") long id);

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
}
