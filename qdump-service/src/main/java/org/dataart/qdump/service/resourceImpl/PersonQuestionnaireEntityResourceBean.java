package org.dataart.qdump.service.resourceImpl;

import java.util.List;

import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonQuestionnaireEntityResource;
import org.springframework.beans.factory.annotation.Autowired;

public class PersonQuestionnaireEntityResourceBean implements PersonQuestionnaireEntityResource{
	@Autowired
	private ServiceQdump serviceQdump;

	public Response addPersonQuestionnaire(PersonQuestionnaireEntity entity) {
		if(!serviceQdump.personEntityExists(entity.getOwnBy().getId())) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("Person who fill this questionnaire is not exists")
					.build();
		} else if(entity.getId() > 0) {
			return Response
					.status(Status.CONFLICT)
					.entity("You cannot create Person Questionnaire with id that "
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
			serviceQdump.addPersonQuestionnaireEntity(entity);
			return Response
					.status(Status.CREATED)
					.build();
		}
	}

	public Response deletePersonQuestionnaireEntity(@PathParam("id") long id) {
		if(!serviceQdump.personQuestionnaireEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("Person Questionnaire with this id is not exists")
					.build();
		}
		serviceQdump.deletePersonQuestionnaireEntity(id);
		return Response
				.status(Status.OK)
				.build();
	}

	public Response deletePersonQuestionnaireEntityByOwnById(@PathParam("id") long id) {
		if(!serviceQdump.personEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("Person with this id is not exists")
					.build();
		}
		serviceQdump.deletePersonQuestionnaireEntityByOwnById(id);
		return Response.status(Status.OK).build();
	}

	public Response deleteAllPersonQuestionnaireEntities() {
		serviceQdump.deleteAllPersonQuestionnaireEntities();
		return Response
				.status(Status.OK)
				.build();
	}

	public Response getPersonQuestionnaireEntity(@PathParam("id") long id) {
		if(serviceQdump.personQuestionnaireEntityExists(id)) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("Person Questionnaire with this id is not exists")
					.build();
		}
		serviceQdump.getPersonQuestionnaireEntity(id);
		return Response
				.status(Status.OK)
				.build();
	}

	public List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities() {
		return serviceQdump.getPersonQuestionnaireEntities();
	}

	public Response updatePersonQuestionnaireEntity(
			PersonQuestionnaireEntity source) {
		if (!serviceQdump.personQuestionnaireEntityExists(source.getId())) {
			return Response
					.status(Status.NOT_FOUND)
					.entity("Person Questionnaire with this id is not exists")
					.build();
		} else {
			PersonQuestionnaireEntity target = serviceQdump
					.getPersonQuestionnaireEntity(source.getId());
			target.updateEntity(source);
			return Response
					.status(Status.OK)
					.entity("Person Questionnaire was successfully updated")
					.build();
		}
	}
}
