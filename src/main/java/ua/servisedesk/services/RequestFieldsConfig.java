package ua.servisedesk.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import ua.servisedesk.dao.AbstractRepository;
import ua.servisedesk.domain.RequestField;
import ua.servisedesk.domain.RequestReadOnlyField;
import ua.servisedesk.domain.SupportRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class  RequestFieldsConfig{
    private static <T> void addEmptyEntity (List<T> list, T entity) {
        list.add(entity);
    }

    private static String getUpperCase(String word){
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static void setModelParams(Map<String, ArrayList<RequestField>> map, Model model, List<RequestReadOnlyField> requestReadOnlyFields){
        for (Map.Entry entry:map.entrySet()
        ) {
            model.addAttribute((String) entry.getKey(),entry.getValue());
        }
        model.addAttribute("authuser", SecurityContextHolder.getContext().getAuthentication().getName());

        model.addAttribute("fieldslist", requestReadOnlyFields);
    }
    public static void filterModelParams(Map<String, ArrayList<RequestField>> map,
                                         SupportRequest supportRequest,
                                         List<RequestReadOnlyField> requestReadOnlyFields,
                                         RequestsProcessEvents event){

        for (RequestReadOnlyField field:requestReadOnlyFields
        ) {
            if(event==RequestsProcessEvents.EDIT && field.isForbiddenToEdit() ||
                    event==RequestsProcessEvents.FILTER && field.isForbiddenToFilter()){

                if(map.get(field.getField()+"list")==null){
                    continue;
                }

                RequestField ob = null;

                try{ob = (RequestField)supportRequest.getClass().getMethod("get" + getUpperCase(field.getField()),
                                null).invoke(supportRequest,null);
                } catch (Exception e){
                    e.printStackTrace();
                }
                ArrayList<RequestField> fieldsList = new ArrayList<>();
                fieldsList.add(ob);
                map.put(field.getField()+"list", fieldsList);
            }
        }
    }

    public static void prepareAndFilterRequestModel(Map<String, ArrayList<RequestField>> map,
                                                    Model model,
                                                    SupportRequest supportRequest,
                                                    List<RequestReadOnlyField> requestReadOnlyFields,
                                                    RequestsProcessEvents event) {

        filterModelParams(map, supportRequest, requestReadOnlyFields,event);
        setModelParams(map,model, requestReadOnlyFields);
    }

    public static <T extends AbstractRepository> void addToMap (Map<String, ArrayList<RequestField>> map, T repository, String mapKey){
        ArrayList<RequestField> requestField = (ArrayList<RequestField>) repository.findAll();
        if(requestField.size()!=0){
            addEmptyEntity(requestField, null);
        }
        map.put(mapKey, requestField);
    }

}
