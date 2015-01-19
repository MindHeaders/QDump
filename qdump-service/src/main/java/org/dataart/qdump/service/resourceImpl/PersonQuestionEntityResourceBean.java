package org.dataart.qdump.service.resourceImpl;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dataart.qdump.entities.person.PersonQuestionEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonQuestionEntityResource;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonQuestionEntityResourceBean implements PersonQuestionEntityResource{
	@Autowired
	private ServiceQdump serviceQdump;

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

	public void deleteAllPersonQuestionEntity() {
		serviceQdump.deleteAllPersonQuestionEntity();
	}

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

	public List<PersonQuestionEntity> getPersonQuestionEntities() {
		return serviceQdump.getPersonQuestionEntities();
	}
}
