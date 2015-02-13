package org.dataart.qdump.entities;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by artemvlasov on 09/02/15.
 */
public class DateTimeTest {
    public static void main(String[] args) {
        LocalDateTime dateTime = LocalDateTime.now();
        BeanWrapper wrapper = new BeanWrapperImpl(dateTime);
        for(PropertyDescriptor descriptor : wrapper.getPropertyDescriptors()) {
            String propName = descriptor.getName();
            System.out.println("Property name : " + propName);
            System.out.println("Value : " + wrapper.getPropertyValue(propName));
        }
        System.out.println(dateTime);
        Date date = new Date(348723l);
        System.out.println(date);
        date.setTime(dateTime.getMinute());
        System.out.println(date);
        Timestamp timestamp = Timestamp.valueOf(dateTime);
        System.out.println(timestamp);
        dateTime = timestamp.toLocalDateTime();
        System.out.println(dateTime);
    }
}
