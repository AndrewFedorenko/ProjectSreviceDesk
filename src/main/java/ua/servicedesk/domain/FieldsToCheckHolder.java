package ua.servicedesk.domain;

import org.springframework.stereotype.Component;

import java.util.List;

// bean holding fields list to check if user filled it before saving
// used to check empty fields of document Support request, User. system email
@Component
public class FieldsToCheckHolder {
    private List<RequiredField> fieldsToCheck;

    public List<RequiredField> getFieldsToCheck(String className) {
        return fieldsToCheck.stream().filter(field->field.getEntityName().equals(className)).toList();
    }

    public void setFieldsToCheck(List<RequiredField> fieldsToCheck) {
        this.fieldsToCheck = fieldsToCheck;
    }
}
