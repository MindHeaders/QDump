package org.dataart.qdump.service.resourceImpl;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.QuestionnaireEntityResource;
import org.springframework.beans.factory.annotation.Autowired;

public class QuestionnaireEntityResourceBean implements QuestionnaireEntityResource{
	@Autowired
	private ServiceQdump serviceQdump;

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

	public void deleteAllQuestionnaireEntity() {
		serviceQdump.deleteAllQuestionnaireEntity();
	}

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

	public List<QuestionnaireEntity> getQuestionnaireEntities() {
		return serviceQdump.getQuestionnaireEntities();
	}
}
