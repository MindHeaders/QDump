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

import org.dataart.qdump.entities.questionnaire.AnswerEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/answer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AnswerEntityResource {

	@Autowired
	private ServiceQdump serviceQdump;

	@POST
	@Path("create")
	public Response addAnswerEntity(AnswerEntity answerEntity) {
		if (answerEntity.getId() > 0) {
			return Response.status(Status.CONFLICT)
					.entity("Answer id cannot be greater than 0").build();
		} else {
			serviceQdump.addAnswerEntity(answerEntity);
			return Response.status(Status.CREATED).build();
		}
	}

	@DELETE
	@Path("delete/{id}")
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

	@DELETE
	@Path("delete/all")
	public void deleteAllAnswerEntity() {
		serviceQdump.deleteAllAnswerEntity();
	}

	@GET
	@Path("get/{id}")
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

	@GET
	@Path("get/all")
	public List<AnswerEntity> getAnswerEntities() {
		return serviceQdump.getAnswerEntities();
	}
}
