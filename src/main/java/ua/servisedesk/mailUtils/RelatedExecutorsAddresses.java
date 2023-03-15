package ua.servisedesk.mailUtils;

import ua.servisedesk.dao.UserRepository;
import ua.servisedesk.domain.User;

import java.util.List;

public class RelatedExecutorsAddresses {
    private static UserRepository userRepository;
    public static String relatedUsersAddresses(String projectId, UserRepository userRepository){
        List<User> receivers = userRepository.findExecutorsOnProject(projectId);
        StringBuilder sb = new StringBuilder();
        for (User receiver:receivers
        ) {
            sb.append(sb.isEmpty() ? "" : ",").append(receiver.getEmail());
        }
        return sb.toString();
    }

}
