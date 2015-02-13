package org.dataart.qdump.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.dataart.qdump.entities.person.PersonEntity;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by artemvlasov on 08/02/15.
 */
public class TestClass {
    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = new ObjectNode(JsonNodeFactory.instance);
        objectNode.put("error", "This is error");
        System.out.println("ObjectNode = " + objectNode.toString());
        String jsonObject = objectNode.toString();
        ObjectNode newObjectNode = new ObjectNode(JsonNodeFactory.instance);
        JsonNode jsonNode = objectMapper.readTree(jsonObject);
        List<String> lists = jsonNode.findValuesAsText("error");
        System.out.println(jsonNode.asText());
        for(String data : lists) {
            System.out.println(data);
        }
        String email = new String("vlasov@gmail.com");
        long id = 1;
        LocalDateTime expire_date = LocalDateTime.now();
        List<Object> objects = new ArrayList<>();
        objects.add(email);
        objects.add(id);
        objects.add(expire_date);
        System.out.println(objects.get(2).getClass().getName());
        System.out.println("name = " + expire_date.getClass().getName() + ", Canonical =  " + expire_date.getClass().getCanonicalName() + ", SimpleName " + expire_date.getClass().getSimpleName());
        System.out.println();
        PersonEntity personEntity = new PersonEntity();
        personEntity.setEmail("hello@gsdfsdfs.com");
        personEntity.setId(1l);
        BeanWrapper wrapper = new BeanWrapperImpl(personEntity);
        for(PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()) {
            String propName = descriptor.getName();
            String type = descriptor.getPropertyType().getName();
            System.out.println("Type contains byte = " + type.toLowerCase().contains("byte"));

        }
        TestBean testClass = new TestBean();
        BeanWrapper secondWrapper = new BeanWrapperImpl(testClass);
        for(PropertyDescriptor descriptor : secondWrapper.getPropertyDescriptors()) {
            String propName = descriptor.getName();
            String type = descriptor.getPropertyType().getName();
            System.out.println(type);
            System.out.println("Type contains long = " + type.toLowerCase().contains("long"));
            System.out.println("Is primitive = " + descriptor.getPropertyType().isPrimitive());
        }
//        TestClass testClass1 = new TestClass();
//        String test = String.format(testClass1.getProperties(),"username", "token");
//        System.out.println(test);
    }
    public static class TestBean {
        private long primitiveLong;
        private Long classLong;
        public TestBean() {
        }

        public long getPrimitiveLong() {
            return primitiveLong;
        }

        public void setPrimitiveLong(long primitiveLong) {
            this.primitiveLong = primitiveLong;
        }

        public Long getClassLong() {
            return classLong;
        }

        public void setClassLong(Long classLong) {
            this.classLong = classLong;
        }
    }
    public String getProperties() throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("mail.properties");
        Properties properties = new Properties();
        properties.load(stream);
        return properties.getProperty("email.service.msg");
    }
}
