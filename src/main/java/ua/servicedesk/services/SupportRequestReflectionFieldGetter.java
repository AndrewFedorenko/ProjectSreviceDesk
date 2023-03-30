package ua.servicedesk.services;

import ua.servicedesk.domain.SupportRequest;
import ua.servicedesk.domain.requestfields.RequestFieldType;

public class SupportRequestReflectionFieldGetter {

    private static String getUpperCase(String word){
       return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public static RequestFieldType getField(SupportRequest supportRequest, String field){

        if(field == null || field.isEmpty()) {
            return null;
        }

        RequestFieldType ob = null;

        try{ob = (RequestFieldType)supportRequest.getClass().getMethod("get" + getUpperCase(field),
                null).invoke(supportRequest,null);
        } catch (Exception e){
            e.printStackTrace();
        }

        return ob;

    }
    public static void setField(SupportRequest supportRequest, String field, RequestFieldType value){

        RequestFieldType ob = null;

        try{ob = (RequestFieldType)supportRequest.getClass().
                getMethod("set" + getUpperCase(field),
                        value.getClass()).invoke(supportRequest,value);
        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
