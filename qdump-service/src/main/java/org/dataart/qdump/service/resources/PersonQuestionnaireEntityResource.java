package org.dataart.qdump.service.resources;

import javax.ws.rs.Path;

import org.dataart.qdump.service.ServiceQdump;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("/person/questionnaire")
public class PersonQuestionnaireEntityResource {
	@Autowired
	private ServiceQdump serviceQdump;
	
	/*@POST
	
	void addPersonQuestionnaireEntity(PersonQuestionnaireEntity personQuestionnaireEntity);
	void deletePersonQuestionnaireEntity(long id);
	void deletePersonQuestionnaireEntityByOwnById(long id);
	void deleteAllPersonQuestionnaireEntities();
	PersonQuestionnaireEntity getPersonQuestionnaireEntity(long id);
	List<PersonQuestionnaireEntity> getPersonQuestionnaireEntities();
	boolean personQuestionnaireEntityExists(long id);
	long personQuestionnaireEntitiesCount();*/
}
