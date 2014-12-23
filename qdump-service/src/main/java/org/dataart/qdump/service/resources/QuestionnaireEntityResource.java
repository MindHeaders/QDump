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

import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/questionnaire")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class QuestionnaireEntityResource {
	@Autowired
	private ServiceQdump serviceQdump;

	@POST
	@Path("create")
	public Response addQuestionnaireEntity(
			QuestionnaireEntity questionnaireEntity) {
		if (questionnaireEntity.getId() > 0) {
			return Response.status(Status.CONFLICT)
					.entity("Questionnaire id cannot be greater than 0")
					.build();
		}
		if (!questionnaireEntity.checkIdForCreation()) {
			return Response.status(Status.CONFLICT)
					.entity("Answer ud cannot be greater than 0").build();
		}
		serviceQdump.addQuestionnaireEntity(questionnaireEntity);
		return Response.status(Status.CREATED).build();
	}

	@DELETE
	@Path("delete/{id}")
	public Response deleteQuestionnaireEntity(@PathParam("id") long id) {
		if (!serviceQdump.questionnaireEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format(
							"Questionnaire with id = %d is not exists", id))
					.build();
		} else {
			serviceQdump.deleteQuestionnaireEntity(id);
			return Response
					.status(Status.OK)
					.entity(String.format(
							"Questionnaire with id = %d was deleted", id))
					.build();
		}
	}

	@DELETE
	@Path("delete/all")
	public void deleteAllQuestionnaireEntity() {
		serviceQdump.deleteAllQuestionnaireEntity();
	}

	@GET
	@Path("get/{id}")
	public Response getQuestionnaireEntity(@PathParam("id") long id) {
		if (!serviceQdump.questionnaireEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format(
							"Questionnaire with id = %d is not exists", id))
					.build();
		} else {
			return Response.status(Status.OK)
					.entity(serviceQdump.getQuestionnaireEntity(id)).build();
		}
	}

	@GET
	@Path("get/all")
	public List<QuestionnaireEntity> getQuestionnaireEntities() {
		return serviceQdump.getQuestionnaireEntities();
	}
}
