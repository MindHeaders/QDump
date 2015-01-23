package org.dataart.qdump.service.resourceImpl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/app")
@Component
public class TestResource {
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
		PersonEntity personEntity = service.getPersonEntity(1l);
		return personEntity;
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
}
