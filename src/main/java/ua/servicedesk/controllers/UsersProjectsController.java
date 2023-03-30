package ua.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.servicedesk.domain.requestfields.Project;
import ua.servicedesk.services.controllerservices.UsersProjectsControllerService;

@Controller
@RequestMapping("/")
public class UsersProjectsController {

    private UsersProjectsControllerService controllerService;

    @RequestMapping(path = "/user_projects", method = RequestMethod.GET)
    public String userProjects(Model model) {

        controllerService.prepareModel(model);

        return "userprojects";
    }

    @RequestMapping(value = "/add_user_project", method = RequestMethod.POST)
    public String addUserProject(@RequestParam(required = false, name = "userid") String userId,
                                 @RequestParam(required = false, name = "projectid") String projectId,
                                 Model model)
    {

        controllerService.processAddUserProject(userId, projectId, model);
        return "redirect:/user_projects";
    }
    @RequestMapping(value = "/add_project", method = RequestMethod.POST)
    public String addProject(@ModelAttribute("newproject") Project project, Model model)
    {

        controllerService.processAddProject(project, model);
        return "redirect:/user_projects";
    }
    @RequestMapping(value = "/del_user_project", method = RequestMethod.POST)
    public String delUserProject(@RequestParam(required = false, name = "userid") String userId,
                              @RequestParam(required = false, name = "projectid") String projectId)
    {

        controllerService.processDelUserProject(userId, projectId);
        return "redirect:/user_projects";
    }

    @Autowired
    public void setControllerService(UsersProjectsControllerService controllerService) {
        this.controllerService = controllerService;
    }
}
