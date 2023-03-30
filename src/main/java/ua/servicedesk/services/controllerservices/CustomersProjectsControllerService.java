package ua.servicedesk.services.controllerservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.servicedesk.dao.CustomerRepository;
import ua.servicedesk.dao.ProjectRepository;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.requestfields.Customer;
import ua.servicedesk.domain.requestfields.Project;

import java.util.List;

@Service
public class CustomersProjectsControllerService {

    private ProjectRepository projectRepository;

    private CustomerRepository customerRepository;

    private CurrentRoleHolder roleHolder;

    public void processcustomersProjects(Model model){
        List<Project> projects = projectRepository.findAll();
        List<Customer> customers = customerRepository.findAll();

        model.addAttribute("projects", projects);
        model.addAttribute("customers", customers);
        model.addAttribute("newcustomer", new Customer());
        CommonModelAttrSetter.setCommonModelAttr(model, roleHolder);
    }

   public void processAddCustomerProject(String customerId, String projectId){
        Customer customer = customerRepository.findItemById(customerId);
        Project project = projectRepository.findItemById(projectId);
        if(project.addCustomer(customer)) {
            projectRepository.update(project);
        }
   }

    public void processDelCustomerProject(String customerId, String projectId){
        Customer customer = customerRepository.findItemById(customerId);
        Project project = projectRepository.findItemById(projectId);
        if(project.removeCustomer(customer)) {
            projectRepository.update(project);
        }
    }

    public void processAddCustomer(Customer customer){
        customer.setEnabled(true);
        customerRepository.updateCustomer(customer);

    }

    @Autowired
    public void setProjectRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }
    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }
}
