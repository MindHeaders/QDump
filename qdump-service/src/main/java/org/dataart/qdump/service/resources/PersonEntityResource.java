package org.dataart.qdump.service.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
@Path("/person")
public class PersonEntityResource {
	@Autowired
	private ServiceQdump serviceQdump;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get")
	public Response getPersonEntity(@QueryParam("id") Long id) {
		if (!serviceQdump.personEntityExists(id)) {
			return Response.status(Status.NOT_FOUND)
					.entity("There is no person with this id").build();
		} else {
			return Response.status(Status.OK)
					.entity(serviceQdump.getPersonEntity(id)).build();
		}
	}

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("login")
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

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("registration")
	public Response registration(PersonEntity entity) {
		serviceQdump.addPersonEntity(entity);
		return Response.status(Status.CREATED)
				.entity("User was created successful.").build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get/all")
	public List<PersonEntity> getAll() {
		return serviceQdump.getPersonEntities();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("get/allmin")
	public List<PersonEntity> getAllMin() {
		return serviceQdump.getPersonEntitiesForAdminPanel();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("delete")
	public Response deletePersonEntity(long id) {
		if (!serviceQdump.personEntityExists(id)) {
			return Response.status(Status.NOT_FOUND)
					.entity("Person with this id is not exist").build();
		} else {
			serviceQdump.deletePersonEntity(id);
			return Response.status(Status.OK)
					.entity("Person with id = " + id + " was deleted").build();
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("delete/all")
	public Response deleteAllPersonEntities() {
		serviceQdump.deleteAllPersonEntities();
		return Response.status(Status.OK)
				.entity("Was delete all Person Entites").build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("update")
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

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("check/email")
	public Response checkPersonEntityEmail(@QueryParam("email") String email) {
		if (!serviceQdump.personEntityExistsByEmail(email)) {
			return Response.status(Status.FORBIDDEN)
					.entity("This email is alredy exists").build();
		}
		return Response.status(Status.OK).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("check/login")
	public Response checkPersonEntityLogin(@QueryParam("login") String login) {
		if (!serviceQdump.personEntityExistsByLogin(login)) {
			return Response.status(Status.FORBIDDEN)
					.entity("This login is alredy exists").build();
		}
		return Response.status(Status.OK).build();
	}
}
