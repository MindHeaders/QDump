package org.dataart.qdump.service.utils;

import com.google.common.base.Preconditions;
import org.dataart.qdump.entities.person.PersonQuestionnaireEntity;
import org.dataart.qdump.entities.questionnaire.BaseEntity;
import org.dataart.qdump.entities.questionnaire.QuestionnaireEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.List;

public class EntitiesUpdater {
    public static <T> void update(T src, T trg) {
        Preconditions.checkNotNull(src, "Target object cannot be null");
        Preconditions.checkNotNull(trg, "Source object cannot be null");
        if(!src.getClass().isInstance(trg)) {
            System.out.println("This object are different");
        } else {
            if(trg instanceof QuestionnaireEntity) {
                ((QuestionnaireEntity) trg).putModifiedDate();
            } else if (trg instanceof PersonQuestionnaireEntity) {
                ((PersonQuestionnaireEntity) trg).putModifiedDate();
            }
            updateEntity(src, trg);
        }
    }
    private static <T extends BaseEntity> void updateList(List<T> src, List<T> trg) {
        for(int i = 0; i < src.size(); i++) {
            if(i < trg.size()) {
                if(trg.get(i).getId() != src.get(i).getId()) {
                    System.out.println(String.format("Error with question source and target order: Target " +
                            "id = %d, Source id = %d", trg.get(i).getId(), src.get(i).getId()));
                    break;
                }
                updateEntity(src.get(i), trg.get(i));
            } else {
                trg.add(src.get(i));
            }
        }
    }
    private static <T> void updateEntity(T source, T target) {
        List<String> updatedFields = getUpdatedFields(target);
        if(updatedFields != null) {
            BeanWrapper trg = new BeanWrapperImpl(target);
            BeanWrapper src = new BeanWrapperImpl(source);
            for (PropertyDescriptor descriptor : BeanUtils
                    .getPropertyDescriptors(source.getClass())) {
                String propName = descriptor.getName();
                if(updatedFields.contains(propName)) {
                    if (trg.getPropertyType(propName).isAssignableFrom(List.class)) {
                        updateList((List) src.getPropertyValue(propName), (List) trg.getPropertyValue(propName));
                    } else if (trg.getPropertyValue(propName) != src
                            .getPropertyValue(propName)) {
                        trg.setPropertyValue(propName, src.getPropertyValue(propName));
                    }
                }
            }
        }
    }
    private static List<String> getUpdatedFields(Object updatedObject) {
        String className = updatedObject.getClass().getSimpleName();
        List<String> updatedFields = null;
        switch (className) {
            case "AnswerEntity":
                updatedFields = Arrays.asList("answer", "correct");
                break;
            case "QuestionEntity":
                updatedFields = Arrays.asList("question", "answerEntities");
                break;
            case "QuestionnaireEntity":
                updatedFields = Arrays.asList("name", "description", "published", "questionEntities");
                break;
            case "PersonAnswerEntity":
                updatedFields = Arrays.asList("personAnswer", "marked");
                break;
            case "PersonQuestionEntity":
                updatedFields = Arrays.asList("correct", "personAnswerEntities");
                break;
            case "PersonQuestionnaireEntity":
                updatedFields = Arrays.asList("status", "personQuestionEntities");
                break;
            case "PersonEntity":
                updatedFields = Arrays.asList("firstname", "lastname", "email", "login", "password",
                        "gender");
                break;
        }
        return updatedFields;
    }
}
