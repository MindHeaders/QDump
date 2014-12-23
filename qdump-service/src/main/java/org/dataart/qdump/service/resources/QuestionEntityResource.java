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

import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/question")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuestionEntityResource {

	@Autowired
	private ServiceQdump serviceQdump;

	@POST
	@Path("create")
	public Response addQuestionEntity(QuestionEntity questionEntity) {
		if (questionEntity.getId() > 0) {
			return Response.status(Status.CONFLICT)
					.entity("Question id cannot be greater than 0").build();
		}
		if (!questionEntity.checkIdForCreation()) {
			return Response.status(Status.CONFLICT)
					.entity("Answer ud cannot be greater than 0").build();
		}
		serviceQdump.addQuestionEntity(questionEntity);
		return Response.status(Status.CREATED).build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response deleteQuestionEntity(@PathParam("id") long id) {
		if (!serviceQdump.questionEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format(
							"Question with id = %d is not exists", id)).build();
		} else {
			serviceQdump.deleteQuestionEntity(id);
			return Response
					.status(Status.OK)
					.entity(String.format("Question with id = %d was deleted",
							id)).build();
		}
	}

	@DELETE
	@Path("delete/all")
	public void deleteAllQuestionEntity() {
		serviceQdump.deleteAllQuestionEntity();
	}

	@GET
	@Path("get/{id}")
	public Response getQuestionEntity(@PathParam("id") long id) {
		if (!serviceQdump.questionEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format(
							"Question with id = %d is not exists", id)).build();
		} else {
			return Response.status(Status.OK)
					.entity(serviceQdump.getQuestionEntity(id)).build();
		}
	}

	@GET
	@Path("get/all")
	public List<QuestionEntity> getQuestionEntities() {
		return serviceQdump.getQuestionEntities();
	}
}
