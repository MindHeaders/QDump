package org.dataart.qdump.service.resourceImpl;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.AnswerEntityResource;
import org.springframework.beans.factory.annotation.Autowired;

public class AnswerEntityResourceBean implements AnswerEntityResource{

	@Autowired
	private ServiceQdump serviceQdump;

	public Response addAnswerEntity(AnswerEntity answerEntity) {
		if (answerEntity.getId() > 0) {
			return Response.status(Status.CONFLICT)
					.entity("Answer id cannot be greater than 0").build();
		} else {
			serviceQdump.addAnswerEntity(answerEntity);
			return Response.status(Status.CREATED).build();
		}
	}

	public Response deleteAnswerEntity(@PathParam("id") long id) {
		if (!serviceQdump.answerEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format("Answer with id = %d is not exists",
							id)).build();
		} else {
			serviceQdump.deleteAnswerEntity(id);
			return Response
					.status(Status.OK)
					.entity(String
							.format("Answer with id = %d was deleted", id))
					.build();
		}
	}

	public void deleteAllAnswerEntity() {
		serviceQdump.deleteAllAnswerEntity();
	}

	public Response getAnswerEntity(@PathParam("id") long id) {
		if (!serviceQdump.answerEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format("Answer with id = %d is not exists",
							id)).build();
		} else {
			return Response.status(Status.OK)
					.entity(serviceQdump.getAnswerEntity(id)).build();
		}
	}

	public List<AnswerEntity> getAnswerEntities() {
		return serviceQdump.getAnswerEntities();
	}

	public Response get() {
		return Response.ok("Hello wolrd").build();
	}


}
