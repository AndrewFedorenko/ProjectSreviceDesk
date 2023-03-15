package ua.servisedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.servisedesk.dao.EmailRepository;
import ua.servisedesk.domain.EmailProfile;
import ua.servisedesk.services.FieldsChecker;

import java.util.Map;

@Controller
@RequestMapping("/")
public class SystemEmailController {
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    FieldsChecker fieldsChecker;

    @RequestMapping(path = "/email_profile", method = RequestMethod.GET)
    public String editUser(Model model) {

        model.addAttribute("email", emailRepository.findEmail());
        model.addAttribute("authuser", SecurityContextHolder.getContext().getAuthentication().getName());
        return "systemEmailProfile";
    }

    @RequestMapping(value = "/save_email", method = RequestMethod.POST)
    public @ResponseBody String saveCustomer(@ModelAttribute("email") EmailProfile profile,
                                             @RequestParam Map<String, String> paramsMap) {

        String id="";
        String errorList = fieldsChecker.checkFields("EmailProfile", paramsMap);
        StringBuilder answer = new StringBuilder("{\"errorlist\":\"");

        if(errorList.isEmpty()){
            emailRepository.updateEmail(profile);
            id=String.valueOf(profile.getId());
        }

        answer.append(errorList).append("\",\"id\":\"").append(id).append("\"}");
        return answer.toString();
    }

}
