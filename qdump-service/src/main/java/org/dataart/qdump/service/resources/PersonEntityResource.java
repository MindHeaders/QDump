package org.dataart.qdump.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
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
	@Produces(MediaType.APPLICATION_JSON)
	@Path("test")
	public PersonEntity getPersonEntity(@QueryParam("id") Long id) {
		return serviceQdump.getPersonEntity(id);
	}
}
