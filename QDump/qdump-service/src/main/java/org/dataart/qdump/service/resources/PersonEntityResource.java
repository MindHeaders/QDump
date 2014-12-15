package org.dataart.qdump.service.resources;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

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
}
