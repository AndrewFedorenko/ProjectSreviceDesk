package ua.servisedesk.mailUtils;

import org.reflections.Reflections;
import ua.servisedesk.dao.RequestsRepository;
import ua.servisedesk.domain.SupportRequest;

import java.lang.reflect.Method;
import java.util.Set;

public class InformingReasonsFactory {
    public static InformingReason getInforminfReason(SupportRequest supportRequest, RequestsRepository repository){
        Reflections reflections = new Reflections("ua.servisedesk");
        Set<Class<? extends InformingReason>> classes = reflections.getSubTypesOf(InformingReason.class);
        for (Class c:classes
             ) {
            Class[] param = new Class[]{SupportRequest.class, RequestsRepository.class};
            try{
                InformingReason reason = (InformingReason)c.getDeclaredConstructor().newInstance();
                Method m = c.getMethod("thisReason", param);
                boolean result = (boolean) m.invoke(reason, supportRequest, repository);
                if(result){
                    return reason;
                }
            } catch (Exception e){
                e.printStackTrace();
            }

        }
        return null;
    }
}
