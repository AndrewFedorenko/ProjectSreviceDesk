package ua.servicedesk.mailUtils;

import ua.servicedesk.dao.RequestsRepository;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.SupportRequest;

import java.util.List;

// common interface for list of possible reasons of mail sending
// possible reasons:
// - author created new request
// - executor answered
// - status of current document request was changed
// .....
// used to get users - receivers addresses and text body of mail
public interface InformingReason {

    boolean thisReason(SupportRequest supportRequest, RequestsRepository repository);

    String createMailBody(SupportRequest supportRequest);

    List<String> createReceiversString(SupportRequest supportRequest, UserRepository userRepository);

}
