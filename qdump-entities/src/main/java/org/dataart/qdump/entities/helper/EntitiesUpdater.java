package org.dataart.qdump.entities.helper;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

public class EntitiesUpdater {
	public static <T> void updateEntity(T source, T target, List<String> ignoredFields, Class<?> type) {
		BeanWrapper trg = new BeanWrapperImpl(target);
		BeanWrapper src = new BeanWrapperImpl(source);
		List<String> ignoredProperties = Arrays.asList("id", "createdDate",
				"createdBy");
		if(ignoredFields != null) {
			ignoredFields.stream().forEach(entity -> ignoredProperties.add(entity));
		}
		for (PropertyDescriptor descriptor : BeanUtils
				.getPropertyDescriptors(source.getClass())) {
			String propName = descriptor.getName();
			if (trg.getPropertyValue(propName) != src
					.getPropertyValue(propName)
					&& !ignoredProperties.contains(propName)) {
				trg.setPropertyValue(propName, src.getPropertyValue(propName));
			}
		}
	}
	public static <T extends BaseEntity> void updateEntities(List<T> source, List<T> target, Class<?> type) {
		for (int i = 0; i < target.size(); i++) {
			if (target.get(i).entitiesIsEquals(
					source.get(i))) {
				continue;
			} else {
				target.get(i).updateEntity(
						source.get(i));
			}
		}
	}
}
