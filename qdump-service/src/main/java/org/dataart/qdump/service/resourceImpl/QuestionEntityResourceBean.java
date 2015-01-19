package org.dataart.qdump.service.resourceImpl;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dataart.qdump.entities.questionnaire.QuestionEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.QuestionEntityResource;
import org.springframework.beans.factory.annotation.Autowired;


public class QuestionEntityResourceBean implements QuestionEntityResource{

	@Autowired
	private ServiceQdump serviceQdump;

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

	public void deleteAllQuestionEntity() {
		serviceQdump.deleteAllQuestionEntity();
	}

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

	public List<QuestionEntity> getQuestionEntities() {
		return serviceQdump.getQuestionEntities();
	}
}
