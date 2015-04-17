package org.dataart.qdump.service.resourceImpl;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/app")
@Component
public class TestResource {
	@SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
	private ServiceQdump service;
	
	@GET
	public Response getTest() {
		String value = "hello world"; 
		return Response.status(200).entity(value).build();
	}
	
	@GET
	@Path("service")
	public String getServiceStatus() {
		return String.format("Service is = %s", service == null ? "null" : "not null");
	}
	
	@GET
	@Path("hello/{name}")
	public String getName(@PathParam("name") String name) {
		return "hello " + name;
	}
	
	@GET
	@Path("get/person")
	public PersonEntity getPerson() {
		return service.getPersonEntity(1l);
	}
	
	@GET
	@Path("get/persons")
	public List<PersonEntity> getPersons() {
		List<PersonEntity> entities = new ArrayList<>();
		PersonEntity entity1 = new PersonEntity();
		entity1.setFirstname("firstname1");
		entity1.setLastname("lastname1");
		PersonEntity entity2 = new PersonEntity();
		entity2.setFirstname("firstname2");
		entity2.setLastname("lastname2");
		entities.add(entity1);
		entities.add(entity2);
		return entities;
	}

    @GET
    @Path("data")
    public QuestionnaireEntity getData() {
        return service.getQuestionnaireEntity(6l);
    }

    @GET
    @Path("test/{id}")
    public QuestionnaireEntity test(@PathParam("id") long id) {
        return service.test(id);
    }

    @GET
    @Path("test2/{id}")
    public QuestionnaireEntity test2(@PathParam("id") long id) {
        return service.test2(id);
    }
}
