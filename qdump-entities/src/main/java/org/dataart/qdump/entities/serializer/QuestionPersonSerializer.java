package org.dataart.qdump.entities.serializer;

import java.io.IOException;

import org.dataart.qdump.entities.questionnaire.QuestionEntity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class QuestionPersonSerializer extends JsonSerializer<QuestionEntity> {

	@Override
	public void serialize(QuestionEntity value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeNumberField("id", value.getId());
		jgen.writeStringField("question", value.getQuestion());
		jgen.writeStringField("question_type", value.getType().name());
		jgen.writeEndObject();
	}

}
