package ua.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.servicedesk.domain.FieldsToCheckHolder;
import ua.servicedesk.domain.RequiredField;

import java.util.List;
import java.util.Map;

// check is list of fields filled by user
@Service
public class FieldsChecker {

    private FieldsToCheckHolder fieldsToCheckHolder;

    public String checkFields(String className,
                              Map<String, String> params){
        List<RequiredField> requiredFieldList = fieldsToCheckHolder.getFieldsToCheck(className);
        StringBuilder errorMessage = new StringBuilder();
        for (RequiredField field:requiredFieldList
             ) {
            String val = params.get(field.getFieldName()+"id");
            if(val==null){
                val = params.get(field.getFieldName());
            }
            if (
                    val == null || val.isEmpty() || "0".equals(val)
                    ){
                errorMessage.append(errorMessage.isEmpty() ? "" : ",").append(field.getFieldName());
            }

        }
       return errorMessage.toString();
    }

    @Autowired
    public void setFieldsToCheckHolder(FieldsToCheckHolder fieldsToCheckHolder) {
        this.fieldsToCheckHolder = fieldsToCheckHolder;
    }
}
