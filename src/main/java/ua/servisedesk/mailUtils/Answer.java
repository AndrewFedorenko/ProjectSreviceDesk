package ua.servisedesk.mailUtils;

import ua.servisedesk.dao.RequestsRepository;
import ua.servisedesk.dao.UserRepository;
import ua.servisedesk.domain.SupportRequest;

public class Answer implements InformingReason{
    public Answer(){

    }
    @Override
    public boolean thisReason(SupportRequest supportRequest, RequestsRepository repository) {
        SupportRequest requestFromBase = repository.findRequestById(String.valueOf(supportRequest.getId()));

        return requestFromBase.getAnswer() == null &&
                supportRequest.getAnswer() != null &&
                !supportRequest.getAnswer().isEmpty() ||
                requestFromBase.getAnswer() != null &&
                        !requestFromBase.getAnswer().equals(supportRequest.getAnswer());

    }

    @Override
    public String createMailBody(SupportRequest supportRequest) {
        return commonInform(supportRequest) +
                "\nExecutor: " +
                supportRequest.getAuthor().getName() +
                "\nanswered request: " +
                "\n" +
                supportRequest.getAnswer() +
                commonBottomInform();
    }

    @Override
    public String createReceiversString(SupportRequest supportRequest, UserRepository userRepository) {
        return supportRequest.getAuthor().getEmail();
    }

}
