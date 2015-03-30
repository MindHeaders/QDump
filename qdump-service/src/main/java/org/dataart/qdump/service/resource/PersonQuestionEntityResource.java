package org.dataart.qdump.service.resource;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
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
public interface PersonQuestionEntityResource {

    @POST
    @Path("create")
    public Response add(PersonQuestionEntity entity);

    @DELETE
    @Path("delete")
    public void delete();

    @DELETE
    @Path("delete/{id}")
    public Response delete(@PathParam("id") long id);

    @GET
    @Path("get")
    public List<PersonQuestionEntity> get();

    @GET
    @Path("get/checking")
    public List<PersonQuestionEntity> getChecking();

    @GET
    @Path("get/{id}")
    public Response get(@PathParam("id") long id);
}
