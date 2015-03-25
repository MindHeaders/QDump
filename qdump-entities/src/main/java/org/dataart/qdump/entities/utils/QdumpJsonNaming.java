package org.dataart.qdump.entities.utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;

/**
 * Created by artemvlasov on 15/03/15.
 */
public class QdumpJsonNaming extends PropertyNamingStrategy {
    @Override
    public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
        return convert(defaultName);
    }

    private String convert(String defaultName) {
        char[] arr = defaultName.toCharArray();
        StringBuilder builder = new StringBuilder();
        if(arr.length != 0) {
            for(char symbol : arr) {
                if(Character.isUpperCase(symbol)) {
                    builder.append("_");
                    builder.append(Character.toLowerCase(symbol));
                }
                builder.append(symbol);
            }
        }
        return new StringBuffer().append(arr).toString();
    }
}
