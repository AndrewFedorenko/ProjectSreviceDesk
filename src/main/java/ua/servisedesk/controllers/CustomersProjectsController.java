
package ua.servisedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.servisedesk.dao.CustomerRepository;
import ua.servisedesk.dao.ProjectRepository;
import ua.servisedesk.domain.Customer;
import ua.servisedesk.domain.Project;

import java.util.List;

@Controller
@RequestMapping("/")
public class CustomersProjectsController {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    CustomerRepository customerRepository;

    @RequestMapping(path = "/customer_projects", method = RequestMethod.GET)
    public String requests(Model model) {

        List<Project> projects = projectRepository.findAll();
        List<Customer> customers = customerRepository.findAll();

        model.addAttribute("projects", projects);
        model.addAttribute("customers", customers);
        model.addAttribute("newcustomer", new Customer());
        return "customerprojects";
    }

    @RequestMapping(value = "/add_customer_project", method = RequestMethod.POST)
    public String addUserProject(@RequestParam(required = false, name = "customerid") String customerId,
                                 @RequestParam(required = false, name = "projectid") String projectId)
    {
        Customer customer = customerRepository.findCustomerById(customerId);
        Project project = projectRepository.findAProjectById(projectId);
        if(project.addCustomer(customer)) {
            projectRepository.update(project);
        }
        return "redirect:/customer_projects";
    }
    @RequestMapping(value = "/del_customer_project", method = RequestMethod.POST)
    public String delUserProject(@RequestParam(required = false, name = "customerid") String customerId,
                                 @RequestParam(required = false, name = "projectid") String projectId)
    {
        Customer customer = customerRepository.findCustomerById(customerId);
        Project project = projectRepository.findAProjectById(projectId);
        if(project.removeCustomer(customer)) {
            projectRepository.update(project);
        }
        return "redirect:/customer_projects";
    }

    @RequestMapping(value = "/add_customer", method = RequestMethod.POST)
    public String addCustomer(@ModelAttribute("newcustomer") Customer customer, Model model)
    {
        customer.setEnabled(true);
        customerRepository.updateCustomer(customer);

        return "redirect:/customer_projects";
    }

}
