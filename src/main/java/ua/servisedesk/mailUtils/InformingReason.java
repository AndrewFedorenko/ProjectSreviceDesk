package ua.servisedesk.mailUtils;

import ua.servisedesk.dao.RequestsRepository;
import ua.servisedesk.dao.UserRepository;
import ua.servisedesk.domain.SupportRequest;

public interface InformingReason {
    boolean thisReason(SupportRequest supportRequest, RequestsRepository repository);
    String createMailBody(SupportRequest supportRequest);
    String createReceiversString(SupportRequest supportRequest, UserRepository userRepository);
    default String commonInform(SupportRequest supportRequest){
        return "HELLO!" +
                "\nRequest N:" +
                supportRequest.getId() +
                " (" + supportRequest.getDate() +
                ")";
    }
    default String commonBottomInform(){
        return "\n---------------\nVisit support requests page for details.";
    }


}
