package ua.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.servicedesk.domain.SupportRequest;
import ua.servicedesk.services.controllerservices.RequestsControllerService;

import java.util.Map;

@Controller
@RequestMapping("/")
public class RequestsController{

    private RequestsControllerService controllerService;

    @RequestMapping(path = "/requests", method = RequestMethod.GET)
    public String requests(Model model) {

        controllerService.processRequests(model);

        return "requests";
    }

    @RequestMapping(value = "/requests_filter", method = RequestMethod.POST)//, headers = {"Content-type=application/json"})
    public String filterJson(@RequestParam Map<String, String> paramsMap,
                             Model model){
        controllerService.processRequestsFilter(paramsMap, model);

        return "requeststable";
    }

    @RequestMapping(path = "/edit_request", method = RequestMethod.GET)
    public String editRequest(@RequestParam(name = "id") String id,
                              Model model) {
        controllerService.processRequestEdit(id, model);

        return "request";
    }

    @RequestMapping(path = "/new_request", method = RequestMethod.GET)
    public String newRequest(Model model) {
        controllerService.processRequestEdit("0", model);

        return "request";
    }

    @RequestMapping(value = "/save_request", method = RequestMethod.POST)
    public @ResponseBody String saveRequest(@ModelAttribute("requestobject") SupportRequest request,
                                            @RequestParam Map<String, String> paramsMap) {

        return controllerService.processSaveRequest(request, paramsMap);
    }

    @RequestMapping(value = "/send_request_message", method = RequestMethod.POST)
    public String requestMessages(
                            @RequestParam Map<String, String> paramMap,
                             Model model){

        controllerService.processRequestMessage(paramMap, model);
        return "requestmessage";
    }
    @RequestMapping(value = "/take_to_work", method = RequestMethod.GET)
    public @ResponseBody String takeToWork(){

        return controllerService.processTakeToWork();
    }

    @Autowired
    public void setControllerService(RequestsControllerService controllerService) {
        this.controllerService = controllerService;
    }

}
