package ua.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.servicedesk.domain.requestfields.User;
import ua.servicedesk.services.controllerservices.UsersControllerService;

import java.util.Map;

@Controller
@RequestMapping("/")
public class UsersController{

    private UsersControllerService controllerService;

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public String users(Model model) {

        controllerService.processUsers(model);
        return "users";
    }

    @RequestMapping(value = "/set_user_role", method = RequestMethod.POST)
    public String addUserProject(@RequestParam(required = false, name = "userid") String userId,
                                 @RequestParam(required = false, name = "roleid") String roleId)
    {

        controllerService.processSetUserRole(userId, roleId);
        return "redirect:/users";
    }
    @RequestMapping(path = "/edit_user", method = RequestMethod.GET)
    public String editUser(@RequestParam(name = "userid") String userId,
                          Model model) {

        controllerService.processEditUser(userId, model);
        return "user";
    }

    @RequestMapping(value = "/save_user", method = RequestMethod.POST)
    public @ResponseBody String saveCustomer(@ModelAttribute("user") User user,
                               @RequestParam Map<String, String> paramsMap) {

        return controllerService.processSaveUser(user, paramsMap);
    }

    @Autowired
    public void setControllerService(UsersControllerService controllerService) {
        this.controllerService = controllerService;
    }
}
