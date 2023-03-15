package ua.servisedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.servisedesk.dao.ProjectRepository;
import ua.servisedesk.dao.UserRepository;
import ua.servisedesk.domain.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class UsersProjectsController {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    UserRepository userRepository;

    private void prepareModel(Model model){
        List<Project> projects = projectRepository.findAll();
        List<User> users = userRepository.findAll();

        model.addAttribute("newproject", new Project());

        model.addAttribute("usersprojects_projects", projects);
        model.addAttribute("usersprojects_users", users);
        model.addAttribute("authuser", SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @RequestMapping(path = "/user_projects", method = RequestMethod.GET)
    public String userProjects(Model model) {

        prepareModel(model);

        return "userprojects";
    }

    @RequestMapping(value = "/add_user_project", method = RequestMethod.POST)
    public String addUserProject(@RequestParam(required = false, name = "userid") String userId,
                                 @RequestParam(required = false, name = "projectid") String projectId,
                                 Model model)
    {
        User user = userRepository.findUserById(userId);
        Project project = projectRepository.findAProjectById(projectId);
        if(project.addUser(user)) {
            projectRepository.update(project);
        }

        return "redirect:/user_projects";
    }
    @RequestMapping(value = "/add_project", method = RequestMethod.POST)
    public String addUserProject(@ModelAttribute("newproject") Project project, Model model)
    {
        projectRepository.update(project);
        prepareModel(model);
        return "redirect:/user_projects";
    }
    @RequestMapping(value = "/del_user_project", method = RequestMethod.POST)
    public String delUserProject(@RequestParam(required = false, name = "userid") String userId,
                              @RequestParam(required = false, name = "projectid") String projectId)
    {
        User user = userRepository.findUserById(userId);
        Project project = projectRepository.findAProjectById(projectId);
        if(project.removeUser(user)) {
            projectRepository.update(project);
        }
        return "redirect:/user_projects";
    }


}
