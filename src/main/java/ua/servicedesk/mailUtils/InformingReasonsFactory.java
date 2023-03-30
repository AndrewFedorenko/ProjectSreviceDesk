package ua.servicedesk.mailUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ua.servicedesk.dao.RequestsRepository;
import ua.servicedesk.domain.SupportRequest;

import java.util.ArrayList;
import java.util.List;

// used to find and gather reasons of mails sending
@Configuration
public class InformingReasonsFactory {

    private ReasonsService reasonsService;

    public static List<InformingReason> getInforminfReason(SupportRequest supportRequest,
                                                           RequestsRepository repository,
                                                           ReasonsService reasonsService){
        List<InformingReason> reasonList = new ArrayList<>();
        for (InformingReason reason:reasonsService.getReasons()) {
            boolean result = reason.thisReason(supportRequest, repository);
            if(result){
                reasonList.add(reason);
            }

        }
        return reasonList;
    }
    @Autowired
    public void setReasonsService(ReasonsService reasonsService) {
        this.reasonsService = reasonsService;
    }
}
