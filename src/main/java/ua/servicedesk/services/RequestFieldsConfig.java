package ua.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.servicedesk.dao.AbstractRepository;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.requestfields.RequestFieldType;
import ua.servicedesk.domain.RequestReadOnlyField;
import ua.servicedesk.domain.SupportRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// contains utility functions to process view properties and state of document request fields
@Service
public class  RequestFieldsConfig{

    private CurrentRoleHolder roleHolder;

    private <T> void addEmptyEntity (List<T> list, T entity) {
        list.add(entity);
    }

    public void setModelParams(Map<String, ArrayList<RequestFieldType>> map,
                               Model model){

        model.addAttribute("filtermapEntrySet",map.entrySet());
//        model.addAttribute("authuser", SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("fieldslist", roleHolder.getRole().getStringRequestReadOnlyFields());
    }
    public void filterModelParams(Map<String, ArrayList<RequestFieldType>> map,
                                         SupportRequest supportRequest,
                                         RequestsProcessEvents event){
        ArrayList<RequestFieldType> fixedValueForList;
        for (RequestReadOnlyField field:roleHolder.getRole().getRequestReadOnlyFields()
        ) {
            if(event==RequestsProcessEvents.EDIT && field.isForbiddenToEdit() ||
                    event==RequestsProcessEvents.FILTER && field.isForbiddenToFilter()){

                if(map.get(field.getField())==null){
                    continue;
                }

                RequestFieldType ob = SupportRequestReflectionFieldGetter.getField(supportRequest, field.getField());
                fixedValueForList = new ArrayList<>();
                fixedValueForList.add(ob);
                map.put(field.getField(), fixedValueForList);
            }
        }
    }

    public void prepareAndFilterRequestModel(Map<String, ArrayList<RequestFieldType>> fieldsMap,
                                                    Model model,
                                                    SupportRequest supportRequest,
                                                    RequestsProcessEvents event) {

        filterModelParams(fieldsMap, supportRequest,event);
        setModelParams(fieldsMap,model);
//        model.addAttribute("allowedLinks", roleHolder.getRole().getAllowedLinksToGenerateMenu());
    }

    public <T extends AbstractRepository> void addToMap (Map<String,
                                                        ArrayList<RequestFieldType>> map,
                                                         T repository,
                                                         List<String> mapKey){

        ArrayList<RequestFieldType> requestFields = (ArrayList<RequestFieldType>) repository.findAll();

        if(requestFields.size()!=0){
            addEmptyEntity(requestFields, null);
        }
        mapKey.forEach(key->map.put(key, requestFields));

    }

    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }
}
