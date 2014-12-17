package org.dataart.qdump.entities.serializer;

import java.io.IOException;

import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class QuestionnairePersonSerializer extends
		JsonSerializer<QuestionnaireEntity> {

	@Override
	public void serialize(QuestionnaireEntity value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("name", value.getName());
		jgen.writeStringField("description", value.getDescription());
		jgen.writeEndObject();
	}

}
