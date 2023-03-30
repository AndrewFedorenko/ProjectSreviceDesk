package ua.servicedesk.mailUtils;

import org.springframework.stereotype.Component;
import ua.servicedesk.dao.RequestsRepository;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.SupportRequest;

import java.util.List;

@Component
public class Answer implements InformingReason{
    public Answer(){

    }
    @Override
    public boolean thisReason(SupportRequest supportRequest, RequestsRepository repository) {

        SupportRequest requestFromBase = repository.findRequestById(String.valueOf(supportRequest.getId()));
        if(requestFromBase==null){
            return false;
        }
        return requestFromBase.getAnswer() == null &&
                supportRequest.getAnswer() != null &&
                !supportRequest.getAnswer().isEmpty() ||
                requestFromBase.getAnswer() != null &&
                        !requestFromBase.getAnswer().equals(supportRequest.getAnswer());

    }

    @Override
    public String createMailBody(SupportRequest supportRequest) {
        return "\nExecutor: " +
                supportRequest.getExecutor().getName() +
                "\nanswered request: " +
                "\n" +
                supportRequest.getAnswer();
    }

    @Override
    public List<String> createReceiversString(SupportRequest supportRequest, UserRepository userRepository) {
        return List.of(supportRequest.getAuthor().getEmail());
    }

}
