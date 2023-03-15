package ua.servisedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.servisedesk.dao.RoleRepository;
import ua.servisedesk.dao.UserRepository;
import ua.servisedesk.domain.Role;
import ua.servisedesk.domain.SupportRequest;
import ua.servisedesk.domain.User;
import ua.servisedesk.services.FieldsChecker;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UsersController{
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    FieldsChecker fieldsChecker;

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public String users(Model model) {

        List<User> users = userRepository.findAll();
        List<Role> roles = roleRepository.findAllItems();

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        model.addAttribute("authuser", SecurityContextHolder.getContext().getAuthentication().getName());

        return "users";
    }

    @RequestMapping(value = "/set_user_role", method = RequestMethod.POST)
    public String addUserProject(@RequestParam(required = false, name = "userid") String userId,
                                 @RequestParam(required = false, name = "roleid") String roleId)
    {
        User user = userRepository.findUserById(userId);
        Role role = roleRepository.findRoleById(roleId);
        user.setRole(role);
        userRepository.updateUser(user);

        return "redirect:/users";
    }
    @RequestMapping(path = "/edit_user", method = RequestMethod.GET)
    public String editUser(@RequestParam(name = "userid") String userId,
                          Model model) {
        User user;
        if("0".equals(userId)){
            user =  new User();
        }
        else {
            user = userRepository.findUserById(userId);
        }

        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepository.findAllItems());
        model.addAttribute("authuser", SecurityContextHolder.getContext().getAuthentication().getName());
        return "user";
    }

    @RequestMapping(value = "/save_user", method = RequestMethod.POST)
    public @ResponseBody String saveCustomer(@ModelAttribute("user") User user,
                               @RequestParam Map<String, String> paramsMap) {

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
}
