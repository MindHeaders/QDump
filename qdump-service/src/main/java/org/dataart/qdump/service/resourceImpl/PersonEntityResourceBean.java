package org.dataart.qdump.service.resourceImpl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.JsonGeneratorDelegate;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.dataart.qdump.service.resource.PersonEntityResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.FormParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.List;

@Component
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


	public Response auth(@FormParam("login") String login,
			@FormParam("password") String password) {
		UsernamePasswordToken token = new UsernamePasswordToken();
		token.setUsername(login);
		token.setPassword(password.toCharArray());
		token.setRememberMe(false);
		try {
			SecurityUtils.getSubject().login(token);
		} catch (AuthenticationException e) {
			return Response.status(Status.NOT_FOUND)
					.entity("Error with login, email or password").build();
		}
		return Response.status(Status.OK).build();
	}

	public Response registration(PersonEntity entity) {
		serviceQdump.addPersonEntity(entity);
		return Response.status(Status.CREATED)
				.entity("User was created successful.").build();
	}

    public void logout() {
        if(SecurityUtils.getSubject().isAuthenticated()) {
            System.out.println("User successfully logout");
            SecurityUtils.getSubject().logout();
        } else {
            return;
        }
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

	public Response checkPersonEntityEmail(@QueryParam("email") String email){
        boolean isValid = serviceQdump.personEntityExistsByEmail(email);
        String object = String.format("{\"isValid\" : %b }", isValid);
        if (isValid) {
			return Response.status(Status.FORBIDDEN)
					.entity(object).build();
		}
		return Response.status(Status.OK).entity(object).build();
	}

	public Response checkPersonEntityLogin(@QueryParam("login") String login) {
        boolean isValid = serviceQdump.personEntityExistsByLogin(login);
        String object = String.format("{\"isValid\" : %b }", !isValid);
        if (isValid) {
			return Response.status(Status.FORBIDDEN).entity(object).build();
		}
        return Response.status(Status.OK).entity(object).build();
	}
}
