package org.dataart.qdump.persistence;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.dataart.qdump.entities.enums.PersonGroupEnums;
import org.dataart.qdump.entities.person.PersonEntity;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonConverter {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		PersonEntity entity = new PersonEntity();
		entity.setCreatedDate(new Date());
		entity.setEmail("test@gmail.com");
		entity.setEnabled(true);
		entity.setGender((byte) 1);
		entity.setLogin("login");
		entity.setModifiedDate(new Date(System.currentTimeMillis() + 100000));
		entity.setFirstname("name");
		entity.setPassword("password");
		entity.setPersonGroup(PersonGroupEnums.USER);
		entity.setLastname("surname");
		ObjectMapper objectMapper = new ObjectMapper();
		PersonQuestionnaireEntity questionnaireEntity = objectMapper.readValue(new File("/Users/artemvlasov/Documents/PersonQuestionnaire.txt"), PersonQuestionnaireEntity.class);
		System.out.println(questionnaireEntity.getId());
	}
}
