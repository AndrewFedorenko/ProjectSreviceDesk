package ua.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.servicedesk.domain.requestfields.RequestFieldType;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestsFieldsService {

    private List<RequestFieldType> fieldList;

    public List<RequestFieldType> getFieldList() {
        return fieldList;
    }
    @Autowired
    public void setFieldList(List<RequestFieldType> fieldList) {
        this.fieldList = fieldList;
    }

    public List<String> getStringFieldsList(){
        List<String> resultList  =new ArrayList<>();
        if (fieldList != null) {
            fieldList.forEach(fieldType -> resultList.addAll(fieldType.fieldsList()));
        }
        return resultList;
    }

}
