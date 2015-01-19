package org.dataart.qdump.service.resourceImpl;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonEntityResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class PersonEntityResourceBean implements PersonEntityResource{
	@Autowired
	private ServiceQdump serviceQdump;

	public Response getPersonEntity(@PathParam("id") long id) {
		if (!serviceQdump.personEntityExists(id)) {
			return Response.status(Status.NOT_FOUND)
					.entity("There is no person with this id").build();
		} else {
			return Response.status(Status.OK)
					.entity(serviceQdump.getPersonEntity(id)).build();
		}
	}

	public Response login(@FormParam("login") String login,
			@FormParam("password") String password) {
		if (!serviceQdump.personEntityExistsByLogin(login)) {
			return Response.status(Status.NOT_FOUND)
					.entity("Error in login or password").build();
		} else if (!BCrypt.checkpw(password,
				serviceQdump.getPersonPasswordByLogin(login))) {
			return Response.status(Status.NOT_FOUND)
					.entity("Error in login or password").build();
		} else {
			PersonEntity entity = serviceQdump.getPersonByLogin(login);
			return Response.status(Status.OK).entity(entity).build();
		}
	}

	public Response registration(PersonEntity entity) {
		serviceQdump.addPersonEntity(entity);
		return Response.status(Status.CREATED)
				.entity("User was created successful.").build();
	}

	public List<PersonEntity> getAll() {
		return serviceQdump.getPersonEntities();
	}

	public List<PersonEntity> getAllMin() {
		return serviceQdump.getPersonEntitiesForAdminPanel();
	}

	public Response deletePersonEntity(@PathParam("id") long id) {
		if (!serviceQdump.personEntityExists(id)) {
			return Response.status(Status.NOT_FOUND)
					.entity("Person with this id is not exist").build();
		} else {
			serviceQdump.deletePersonEntity(id);
			return Response.status(Status.OK)
					.entity("Person with id = " + id + " was deleted").build();
		}
	}

	public Response deleteAllPersonEntities() {
		serviceQdump.deleteAllPersonEntities();
		return Response.status(Status.OK)
				.entity("Was delete all Person Entites").build();
	}

	public Response updatePersonEntity(PersonEntity entity) {
		if (!serviceQdump.personEntityExists(entity.getId())) {
			return Response.status(Status.NOT_FOUND)
					.entity("Person with this id is not exists").build();
		}
		PersonEntity source = serviceQdump.getPersonEntity(entity.getId());
		if (source.checkEqualsForWeb(entity)) {
			return Response.status(Status.ACCEPTED)
					.entity("There is nothing to change").build();
		}
		return Response.status(Status.OK)
				.entity("Person was successful updated").build();
	}

	public Response checkPersonEntityEmail(@QueryParam("email") String email) {
		if (!serviceQdump.personEntityExistsByEmail(email)) {
			return Response.status(Status.FORBIDDEN)
					.entity("This email is alredy exists").build();
		}
		return Response.status(Status.OK).build();
	}

	public Response checkPersonEntityLogin(@QueryParam("login") String login) {
		if (!serviceQdump.personEntityExistsByLogin(login)) {
			return Response.status(Status.FORBIDDEN)
					.entity("This login is alredy exists").build();
		}
		return Response.status(Status.OK).build();
	}
}
