
package ua.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.servicedesk.domain.requestfields.Customer;
import ua.servicedesk.services.controllerservices.CustomersProjectsControllerService;

@Controller
@RequestMapping("/")
public class CustomersProjectsController {

    private CustomersProjectsControllerService projectsControllerService;

    @RequestMapping(path = "/customer_projects", method = RequestMethod.GET)
    public String customersProjects(Model model) {

        projectsControllerService.processcustomersProjects(model);
        return "customerprojects";
    }

    @RequestMapping(value = "/add_customer_project", method = RequestMethod.POST)
    public String addCustomerProject(@RequestParam(required = false, name = "customerid") String customerId,
                                 @RequestParam(required = false, name = "projectid") String projectId)
    {
        projectsControllerService.processAddCustomerProject(customerId, projectId);

        return "redirect:/customer_projects";
    }
    @RequestMapping(value = "/del_customer_project", method = RequestMethod.POST)
    public String delCustomerProject(@RequestParam(required = false, name = "customerid") String customerId,
                                 @RequestParam(required = false, name = "projectid") String projectId)
    {
        projectsControllerService.processDelCustomerProject(customerId, projectId);
        return "redirect:/customer_projects";
    }

    @RequestMapping(value = "/add_customer", method = RequestMethod.POST)
    public String addCustomer(@ModelAttribute("newcustomer") Customer customer)
    {
        projectsControllerService.processAddCustomer(customer);
        return "redirect:/customer_projects";
    }
    @Autowired
    public void setProjectsControllerService(CustomersProjectsControllerService projectsControllerService) {
        this.projectsControllerService = projectsControllerService;
    }
}
