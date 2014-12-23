package org.dataart.qdump.service.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/qu")
public class QuestionnaireEntityResource {
	@Autowired
	private ServiceQdump serviceQdump;
	
	@GET
	@Path("q")
	@Produces(MediaType.APPLICATION_JSON)
	public QuestionnaireEntity getQuestionnaireEntity() {
		QuestionnaireEntity entity = serviceQdump.getQuestionnaireEntity(1l);
		return entity;
	}
}
