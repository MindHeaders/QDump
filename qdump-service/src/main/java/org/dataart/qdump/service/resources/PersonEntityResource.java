package org.dataart.qdump.service.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dataart.qdump.entities.enums.PersonGroupEnums;
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
	public String get() {
		return String.format("ServiceQdump is - %s",
				serviceQdump == null ? "null" : "not null");
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("test")
	public List<Object> getPersonEntity(@QueryParam("id") Long id) {
		PersonEntity entity = serviceQdump.getPersonEntity(id);
		List<Object> objects = new ArrayList<Object>();
		objects.add(entity);
		objects.add(entity.getPersonQuestionnaireEntities());
		return objects;
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Path("login")
	public Response login(@FormParam("login") String login, @FormParam("password") String password) {
		PersonEntity entity = serviceQdump.getPersonForAuthByLogin(login);
		if(entity == null) {
			return Response.status(404).entity("Error in login hello or password").build();
		} else if(!BCrypt.checkpw(password, entity.getPassword())) {
			return Response.status(404).entity("Error in login or password").build();
		} else {
			return Response.ok("Welcome").build();
		}
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("registration")
	public void registration(PersonEntity entity) {
		entity.setPersonGroup(PersonGroupEnums.USER);
		serviceQdump.addPersonEntity(entity);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAll")
	public List<PersonEntity> getAll() {
		return serviceQdump.getPersonEntities();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getAllMin")
	public List<PersonEntity> getAllMin() {
		return serviceQdump.getPersonsNameLastname();
	}
}
