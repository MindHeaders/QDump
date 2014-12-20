package org.dataart.qdump.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/personpq")
public class PersonQuestionnaireEntityResource {
	@Autowired
	private ServiceQdump serviceQdump;
	
	@GET
	@Path("pq")
	@Produces(MediaType.APPLICATION_JSON)
	public PersonQuestionnaireEntity getPersonQuestionnaireEntity() {
		PersonQuestionnaireEntity entity = serviceQdump.getPersonQuestionnaireEntity(1l);
		return entity;
	}
}
