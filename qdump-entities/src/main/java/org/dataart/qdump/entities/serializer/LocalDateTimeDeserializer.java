package org.dataart.qdump.entities.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by artemvlasov on 10/04/15.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime>{
    @Override
    public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return LocalDateTime.parse(jp.getText());
    }
}
