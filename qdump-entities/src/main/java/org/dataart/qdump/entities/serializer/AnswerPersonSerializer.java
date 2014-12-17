package org.dataart.qdump.entities.serializer;

import java.io.IOException;

import org.dataart.qdump.entities.questionnaire.AnswerEntity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class AnswerPersonSerializer extends JsonSerializer<AnswerEntity> {

	@Override
	public void serialize(AnswerEntity value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("answer", value.getAnswer());
		jgen.writeEndObject();
	}

}
