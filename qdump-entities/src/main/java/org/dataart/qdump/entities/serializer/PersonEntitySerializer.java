package org.dataart.qdump.entities.serializer;

import java.io.IOException;

import org.dataart.qdump.entities.person.PersonEntity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PersonEntitySerializer extends JsonSerializer<PersonEntity> {

	@Override
	public void serialize(PersonEntity value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("login", value.getLogin());
		jgen.writeEndObject();
	}

}
