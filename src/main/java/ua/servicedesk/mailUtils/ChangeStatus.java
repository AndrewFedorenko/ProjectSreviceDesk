package ua.servicedesk.mailUtils;

import org.springframework.stereotype.Component;
import ua.servicedesk.dao.RequestsRepository;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.SupportRequest;
import ua.servicedesk.domain.requestfields.User;

import java.util.List;

@Component
public class ChangeStatus implements InformingReason {
    public ChangeStatus(){

    }
    @Override
    public boolean thisReason(SupportRequest supportRequest, RequestsRepository repository) {
        SupportRequest requestFromBase = repository.findRequestById(String.valueOf(supportRequest.getId()));
        if(requestFromBase==null){
            return false;
        }
        return !supportRequest.getStatus().equals(requestFromBase.getStatus());

    }

    @Override
    public String createMailBody(SupportRequest supportRequest) {
        return "\nststus was changed.";
    }

    @Override
    public List<String> createReceiversString(SupportRequest supportRequest, UserRepository userRepository) {
        User currentUser = userRepository.getCurrentUser();

        if(currentUser.equals(supportRequest.getAuthor())){
            return supportRequest.getExecutor()==null ?
                    RelatedExecutorsAddresses.relatedUsersAddresses(
                            String.valueOf(supportRequest.getProject().getId()),
                            userRepository) :
                    List.of(supportRequest.getExecutor().getEmail());

        } else if (currentUser.equals(supportRequest.getExecutor())) {

            return List.of(supportRequest.getAuthor().getEmail());
        }
        return List.of("");
    }

}

