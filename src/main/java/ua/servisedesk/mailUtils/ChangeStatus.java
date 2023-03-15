package ua.servisedesk.mailUtils;

import ua.servisedesk.dao.RequestsRepository;
import ua.servisedesk.dao.UserRepository;
import ua.servisedesk.domain.SupportRequest;
import ua.servisedesk.domain.User;

public class ChangeStatus implements InformingReason {
    public ChangeStatus(){

    }
    @Override
    public boolean thisReason(SupportRequest supportRequest, RequestsRepository repository) {
        SupportRequest requestFromBase = repository.findRequestById(String.valueOf(supportRequest.getId()));

        return !supportRequest.getStatus().equals(requestFromBase.getStatus());

    }

    @Override
    public String createMailBody(SupportRequest supportRequest) {
        return commonInform(supportRequest) +
                "\nststus was changed." +
                commonBottomInform();
    }

    @Override
    public String createReceiversString(SupportRequest supportRequest, UserRepository userRepository) {
        User currentUser = userRepository.getCurrentUser();

        if(currentUser.equals(supportRequest.getAuthor())){
            return supportRequest.getUser()==null ?
                    RelatedExecutorsAddresses.relatedUsersAddresses(
                            String.valueOf(supportRequest.getProject().getId()),
                            userRepository) :
                    supportRequest.getUser().getEmail();

        } else if (currentUser.equals(supportRequest.getUser())) {

            return supportRequest.getAuthor().getEmail();
        }
        return "";
    }

}

