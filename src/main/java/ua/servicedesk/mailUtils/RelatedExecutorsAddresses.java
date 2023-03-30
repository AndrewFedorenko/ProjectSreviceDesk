package ua.servicedesk.mailUtils;

import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.requestfields.User;

import java.util.ArrayList;
import java.util.List;

public class RelatedExecutorsAddresses {

    public static List<String> relatedUsersAddresses(String projectId, UserRepository userRepository){
        List<String> list = new ArrayList<>();
        List<User> receivers = userRepository.findExecutorsOnProject(projectId);

        receivers.forEach(receiver -> list.add(receiver.getEmail()));

        return list;
    }

}
