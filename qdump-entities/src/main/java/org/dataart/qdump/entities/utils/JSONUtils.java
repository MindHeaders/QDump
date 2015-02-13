package org.dataart.qdump.entities.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.List;

/**
 * Created by artemvlasov on 08/02/15.
 */
public class JSONUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode createObjectNode(String fieldName, String fieldValue) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put(fieldName, fieldValue);
        return objectNode;
    }
    public <T> ObjectNode createObjectNode(T source, Class<T> type, List<String> parsedFields) {
        ObjectNode objectNode = objectMapper.createObjectNode();
        BeanWrapper src = new BeanWrapperImpl(source);
        for(PropertyDescriptor descriptor : BeanUtils.getPropertyDescriptors(type)) {
            String promName = descriptor.getName();
            if(parsedFields.contains(promName)) {

            }
        }
        return objectNode;
    }

    private <T> void pesistData(ObjectNode objectNode, T t, String propertyTypeName) {
        switch (propertyTypeName) {
        }
    }
}
