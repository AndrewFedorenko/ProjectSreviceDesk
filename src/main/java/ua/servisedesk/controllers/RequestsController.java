package ua.servisedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.servisedesk.dao.*;
import ua.servisedesk.domain.*;
import ua.servisedesk.mailUtils.InformingReason;
import ua.servisedesk.mailUtils.InformingReasonsFactory;
import ua.servisedesk.services.FieldsChecker;
import ua.servisedesk.services.MailSender;
import ua.servisedesk.services.RequestFieldsConfig;
import ua.servisedesk.services.RequestsProcessEvents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class RequestsController{
    @Autowired
    RequestsRepository requestsRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MailSender mailSender;
    @Autowired
    RequestMessageRepository messageRepository;
    @Autowired
    FieldsChecker fieldsChecker;

    @RequestMapping(path = "/requests", method = RequestMethod.GET)
    public String requests(Model model) {

        List<SupportRequest> requestsRepositories = requestsRepository.findAllItems();
        SupportRequest filterRequest = new SupportRequest();
        filterRequest.setAuthor(userRepository.getCurrentUser());
        filterRequest.setUser(userRepository.getCurrentUser());
        RequestFieldsConfig.prepareAndFilterRequestModel(fieldsMap(),
                model,
                filterRequest,
                userRepository.getCurrentUser().getRole().getRequestReadOnlyFields(),
                RequestsProcessEvents.FILTER);

        model.addAttribute("requestsRepositories", requestsRepositories);
        model.addAttribute("newrequest", new SupportRequest());
        return "requests";
    }

    @RequestMapping(value = "/requests_filter", method = RequestMethod.POST)//, headers = {"Content-type=application/json"})
    public String filterJson(@RequestParam Map<String, String> paramsMap,
                             Model model){
        List<SupportRequest> requestsRepositories = requestsRepository.findRequestByFilter(paramsMap);

        SupportRequest filterRequest = new SupportRequest();
        filterRequest.setAuthor(userRepository.getCurrentUser());
        filterRequest.setUser(userRepository.getCurrentUser());
        RequestFieldsConfig.prepareAndFilterRequestModel(fieldsMap(),
                model,
                filterRequest,
                userRepository.getCurrentUser().getRole().getRequestReadOnlyFields(),
                RequestsProcessEvents.FILTER);
        model.addAttribute("requestsRepositories", requestsRepositories);

        return "requeststable";
    }

    private Map<String, ArrayList<RequestField>> fieldsMap(){

        Map<String, ArrayList<RequestField>> map = new HashMap<>();

        RequestFieldsConfig.addToMap(map, customerRepository, "customerlist");
        RequestFieldsConfig.addToMap(map, userRepository, "userlist");
        RequestFieldsConfig.addToMap(map, userRepository, "authorlist");
        RequestFieldsConfig.addToMap(map, projectRepository, "projectlist");
        RequestFieldsConfig.addToMap(map, statusRepository, "statuslist");

        return map;
    }

    @RequestMapping(path = "/edit_request", method = RequestMethod.GET)
    public String editRequest(@RequestParam(name = "id") String id,
                              Model model) {
        SupportRequest supportRequest;
        if("0".equals(id)){
            supportRequest =  new SupportRequest();
            supportRequest.setAuthor(userRepository.getCurrentUser());
        }
        else {
            supportRequest = requestsRepository.findRequestById(id);
            model.addAttribute("requestsMessages",messageRepository.findMessagesById(id));
        }

        RequestFieldsConfig.prepareAndFilterRequestModel(fieldsMap(),
                                                        model,
                                                        supportRequest,
                                                        userRepository.getCurrentUser().getRole().getRequestReadOnlyFields(),
                                                        RequestsProcessEvents.EDIT);

        model.addAttribute("requestobject", supportRequest);
        return "request";
    }
    @RequestMapping(value = "/save_request", method = RequestMethod.POST)
    public @ResponseBody String saveRequest(@ModelAttribute("requestobject") SupportRequest request,
                                            @RequestParam Map<String, String> paramsMap) {
        String id="";
        String errorList = fieldsChecker.checkFields("SupportRequest", paramsMap);
        StringBuilder answer = new StringBuilder("{\"errorlist\":\"");

        if(errorList.isEmpty()){
            request.setCustomer(customerRepository.findCustomerById(paramsMap.get("customerid")));
            request.setAuthor(userRepository.findUserById(paramsMap.get("authorid")));
            request.setUser(userRepository.findUserById(paramsMap.get("userid")));
            request.setStatus(statusRepository.findStatusById(paramsMap.get("statusid")));
            request.setProject(projectRepository.findAProjectById(paramsMap.get("projectid")));

            InformingReason reason = InformingReasonsFactory.getInforminfReason(request, requestsRepository);
            request = requestsRepository.updateRequest(request);

            mailSender.send(request,
                    reason);
            id=String.valueOf(request.getId());
        }
        answer.append(errorList).append("\",\"id\":\"").append(id).append("\"}");
        return answer.toString();
    }

    @RequestMapping(value = "/send_request_message", method = RequestMethod.POST)
    public String requestMessages(
                            @RequestParam Map<String, String> paramMap,
                             Model model){

        String MessageValue = paramMap.get("message");

        if(!MessageValue.isEmpty()) {
            String requestId = paramMap.get("id");

            RequestMessage message = new RequestMessage();
            message.setMessageContent(MessageValue);
            message.setUser(userRepository.getCurrentUser());
            message.setSupportRequest(requestsRepository.findRequestById(requestId));
            messageRepository.updateRequest(message);

            model.addAttribute("requestsMessages",messageRepository.findMessagesById(requestId));
        }
        return "requestmessage";
    }
    @RequestMapping(value = "/take_to_work", method = RequestMethod.GET)
    public @ResponseBody String takeToWork(){

        User authUser = userRepository.getCurrentUser();

        if(authUser.getRole().isExecutor()){
            return  "{ \"id\":" + authUser.getId() +
                    ",\"name\":\"" + authUser.getName() + "\" }";
        }
        return "";
    }
    @RequestMapping(value = "/is_enable_navigation", method = RequestMethod.GET)
    public @ResponseBody String isEnableNavigation(){

        User authUser = userRepository.getCurrentUser();

        if(authUser.getRole().isAdmin()){
            return  "";
        }
        return "no";
    }

}
