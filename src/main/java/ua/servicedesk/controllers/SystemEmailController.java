package ua.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.servicedesk.domain.EmailProfile;
import ua.servicedesk.services.controllerservices.SystemEmailControllerService;

import java.util.Map;

@Controller
@RequestMapping("/")
public class SystemEmailController {
    private SystemEmailControllerService controllerService;

    @RequestMapping(path = "/email_profile", method = RequestMethod.GET)
    public String editUser(Model model) {
        controllerService.processEditUser(model);
        return "systemEmailProfile";
    }

    @RequestMapping(value = "/save_email", method = RequestMethod.POST)
    public @ResponseBody String saveEmail(@ModelAttribute("email") EmailProfile profile,
                                             @RequestParam Map<String, String> paramsMap) {

        return controllerService.processSaveEmail(profile, paramsMap);
    }
    @Autowired
    public void setControllerService(SystemEmailControllerService controllerService) {
        this.controllerService = controllerService;
    }
}
