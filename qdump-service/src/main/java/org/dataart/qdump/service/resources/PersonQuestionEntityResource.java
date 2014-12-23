package org.dataart.qdump.service.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@Path("/person/question")
public class PersonQuestionEntityResource {
	@Autowired
	private ServiceQdump serviceQdump;
	
	@POST
	@Path("create")
	public Response addPersonQuestionEntity(PersonQuestionEntity entity) {
		if(entity.getId() > 0) {
			return Response
					.status(Status.CONFLICT)
					.entity("You cannot create Person Question with id that "
							+ "greater than 0")
					.build();
		} else if(!entity.checkIdForCreation()) {
			return Response
					.status(Status.FORBIDDEN)
					.entity("Incorrect persisted data. Persisted Person Questionnaire, "
							+ "Question, Answer shouldn`t contains id greater than 0 and "
							+ "Questionnaire, Question, Answer id should not be equals to 0.")
					.build();
		} else {
			serviceQdump.addPersonQuestionEntity(entity);
			return Response
					.status(Status.CREATED)
					.build();
		}
	}
	
	@DELETE
	@Path("delete/{id}")
	public Response deletePersonQuestionEntity(@PathParam("id") long id) {
		if(!serviceQdump.personQuestionEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format("Person question with id = %d not found", id))
					.build();
		}
		serviceQdump.deletePersonQuestionEntity(id);
		return Response
				.status(Status.OK)
				.entity(String.format("Person question with id = %d was deleted", id))
				.build();
	}
	
	@DELETE
	@Path("delete/all")
	public void deleteAllPersonQuestionEntity() {
		serviceQdump.deleteAllPersonQuestionEntity();
	}
	
	@GET
	@Path("get/{id}")
	public Response getPersonQuestionEntity(@PathParam("id") long id) {
		if(!serviceQdump.personQuestionEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format("Person question with id = %d, not found", id))
					.build();
		}
		return Response
				.ok(serviceQdump.getPersonQuestionEntity(id)).build();
		
	}
	
	@GET
	@Path("get/all")
	public List<PersonQuestionEntity> getPersonQuestionEntities() {
		return serviceQdump.getPersonQuestionEntities();
	}
}
