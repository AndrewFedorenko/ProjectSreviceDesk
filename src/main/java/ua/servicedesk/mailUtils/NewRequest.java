package ua.servicedesk.mailUtils;

import org.springframework.stereotype.Component;
import ua.servicedesk.dao.RequestsRepository;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.SupportRequest;

import java.util.List;

@Component
public class NewRequest implements InformingReason{

    public NewRequest(){

    }
    @Override
    public boolean thisReason(SupportRequest supportRequest, RequestsRepository repository) {
        return supportRequest==null || supportRequest.getId()==0;
    }

    @Override
    public String createMailBody(SupportRequest supportRequest) {
        return "\nAuthor: " + supportRequest.getAuthor().getName() +
                " created new request with next content: \n" +
                supportRequest.getContent();
    }

    @Override
    public List<String> createReceiversString(SupportRequest supportRequest, UserRepository userRepository) {
        return RelatedExecutorsAddresses.relatedUsersAddresses(
                String.valueOf(supportRequest.getProject().getId()),
                userRepository);
    }
}
