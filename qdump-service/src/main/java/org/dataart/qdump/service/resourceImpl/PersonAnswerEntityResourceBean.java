package org.dataart.qdump.service.resourceImpl;

import java.util.List;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dataart.qdump.entities.person.PersonAnswerEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonAnswerEntityResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PersonAnswerEntityResourceBean implements PersonAnswerEntityResource{
	
	@Autowired
	private ServiceQdump serviceQdump;

	public Response addPersonAnswerEntity(PersonAnswerEntity entity) {
		if(entity.getId() > 0) {
			return Response
					.status(Status.CONFLICT)
					.entity("You cannot create Person Answer with id that "
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
			serviceQdump.addPersonAnswerEntity(entity);
			return Response
					.status(Status.CREATED)
					.build();
		}
	}

	public Response deletePersonAnswerEntity(@PathParam("id") long id) {
		if(!serviceQdump.personAnswerEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format("Person answer with id = %d not found", id))
					.build();
		}
		serviceQdump.deletePersonAnswerEntity(id);
		return Response
				.status(Status.OK)
				.entity(String.format("Person answer with id = %d was deleted", id))
				.build();
	}

	public void deleteAllPersonAnswerEntity() {
		serviceQdump.deleteAllPersonAnswerEntity();
	}

	public Response getPersonAnswerEntity(@PathParam("id") long id) {
		if(!serviceQdump.personAnswerEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity(String.format("Person answer with id = %d, not found", id))
					.build();
		}
		return Response
				.ok(serviceQdump.getPersonAnswerEntity(id)).build();
	}

	public List<PersonAnswerEntity> getPersonAnswerEntities() {
		return serviceQdump.getPersonAnswerEntities();
	}
}
