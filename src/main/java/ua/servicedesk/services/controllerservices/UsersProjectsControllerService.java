package ua.servicedesk.services.controllerservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.servicedesk.dao.ProjectRepository;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.requestfields.Project;
import ua.servicedesk.domain.requestfields.User;

import java.util.List;

@Service
public class UsersProjectsControllerService {

    private ProjectRepository projectRepository;

    private UserRepository userRepository;

    private CurrentRoleHolder roleHolder;

    public void prepareModel(Model model){
        List<Project> projects = projectRepository.findAll();
        List<User> users = userRepository.findAll();

        model.addAttribute("newproject", new Project());

        model.addAttribute("usersprojects_projects", projects);
        model.addAttribute("usersprojects_users", users);
        CommonModelAttrSetter.setCommonModelAttr(model, roleHolder);
    }

    public void processAddUserProject(String userId,
                                      String projectId,
                                      Model model){
        User user = userRepository.findItemById(userId);
        Project project = projectRepository.findItemById(projectId);
        if(project.addUser(user)) {
            projectRepository.update(project);
        }

    }

    public void processAddProject(Project project, Model model){
        projectRepository.update(project);
        prepareModel(model);

    }

    public void processDelUserProject(String userId,
                                      String projectId){
        User user = userRepository.findItemById(userId);
        Project project = projectRepository.findItemById(projectId);
        if(project.removeUser(user)) {
            projectRepository.update(project);
        }

    }

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }
}
