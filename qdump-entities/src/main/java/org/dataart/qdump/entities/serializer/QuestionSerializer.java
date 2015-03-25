package org.dataart.qdump.entities.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.dataart.qdump.entities.questionnaire.QuestionEntity;

import java.io.IOException;

/**
 * Created by artemvlasov on 15/03/15.
 */
public class QuestionSerializer extends JsonSerializer<QuestionEntity> {
    @Override
    public void serialize(QuestionEntity value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
//        jgen.writeArrayFieldStart("question_entities");
        jgen.writeStartArray();
        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("question", value.getQuestion());
        jgen.writeStringField("question_type", value.getType().name());
        jgen.writeEndObject();
        jgen.writeEndArray();
    }
}
