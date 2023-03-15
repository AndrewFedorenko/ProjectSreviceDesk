package ua.servisedesk.mailUtils;

import ua.servisedesk.dao.RequestsRepository;
import ua.servisedesk.dao.UserRepository;
import ua.servisedesk.domain.SupportRequest;

public class NewRequest implements InformingReason{

    public NewRequest(){

    }
    @Override
    public boolean thisReason(SupportRequest supportRequest, RequestsRepository repository) {
        return supportRequest.getId()==0;
    }

    @Override
    public String createMailBody(SupportRequest supportRequest) {
        return commonInform(supportRequest) + "\nAuthor: " + supportRequest.getAuthor().getName() +
                "\ncreated new request with next content: " +
                commonBottomInform();
    }

    @Override
    public String createReceiversString(SupportRequest supportRequest, UserRepository userRepository) {
        return RelatedExecutorsAddresses.relatedUsersAddresses(
                String.valueOf(supportRequest.getProject().getId()),
                userRepository);
    }
}
