package ua.servicedesk.services.controllerservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.dao.RoleRepository;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.Role;
import ua.servicedesk.domain.requestfields.User;
import ua.servicedesk.services.FieldsChecker;

import java.util.List;
import java.util.Map;

@Service
public class UsersControllerService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private FieldsChecker fieldsChecker;

    private CurrentRoleHolder roleHolder;

    public void processUsers(Model model){
        List<User> users = userRepository.findAll();
        List<Role> roles = roleRepository.findAllItems();

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        CommonModelAttrSetter.setCommonModelAttr(model, roleHolder);
    }

    public void processSetUserRole(String userId, String roleId){
        User user = userRepository.findItemById(userId);
        Role role = roleRepository.findRoleById(roleId);
        user.setRole(role);
        userRepository.updateUser(user);
    }

    public void processEditUser(String userId,
                                Model model){
        User user;
        if("0".equals(userId)){
            user =  new User();
        }
        else {
            user = userRepository.findItemById(userId);
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAllItems());
        model.addAttribute("authuser", SecurityContextHolder.getContext().getAuthentication().getName());
        model.addAttribute("allowedLinks", roleHolder.getRole().getAllowedLinksToGenerateMenu());

    }

    public String processSaveUser(User user,
                                  Map<String, String> paramsMap){
        String id="";
        String errorList = fieldsChecker.checkFields("User", paramsMap);
        StringBuilder answer = new StringBuilder("{\"errorlist\":\"");

        if(errorList.isEmpty()){
            if(!user.getPassword().isEmpty()){
                user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            }

            user.setRole(roleRepository.findRoleById(paramsMap.get("roleid")));
            userRepository.updateUser(user);
            id=String.valueOf(user.getId());
        }

        answer.append(errorList).append("\",\"id\":\"").append(id).append("\"}");
        return answer.toString();

    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Autowired
    public void setFieldsChecker(FieldsChecker fieldsChecker) {
        this.fieldsChecker = fieldsChecker;
    }
    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }
}
