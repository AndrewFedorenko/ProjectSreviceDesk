package ua.servisedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.servisedesk.dao.RequiredFieldsRepository;
import ua.servisedesk.domain.RequiredField;

import java.util.List;
import java.util.Map;

@Service
public class FieldsChecker {
    @Autowired
    private RequiredFieldsRepository fieldsRepository;
    public String checkFields(String className, Map<String, String> params){
        List<RequiredField> requiredFieldList = fieldsRepository.findAllFields(className);
        StringBuilder errorMessage = new StringBuilder();
        for (RequiredField field:requiredFieldList
             ) {
            String val = params.get(field.getFieldName()+"id");
            if(val==null){
                val = params.get(field.getFieldName());
            }
            if (
                    val!=null &&
                    (val==null ||
                            val.isEmpty() ||
                            "0".equals(val)
                    )){
                errorMessage.append(errorMessage.isEmpty() ? "" : ",").append(field.getFieldName());
            }

        }
       return errorMessage.toString();
    }

    public RequiredFieldsRepository getFieldsRepository() {
        return fieldsRepository;
    }

    public void setFieldsRepository(RequiredFieldsRepository fieldsRepository) {
        this.fieldsRepository = fieldsRepository;
    }
}
