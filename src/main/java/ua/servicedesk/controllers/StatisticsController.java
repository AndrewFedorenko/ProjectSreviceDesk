package ua.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ua.servicedesk.services.controllerservices.StatisticsControllerService;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class StatisticsController {

    private StatisticsControllerService controllerService;

    @RequestMapping(path = "/statistics", method = RequestMethod.GET)
    public String statistics(Model model) {

        controllerService.processRequestsFilter(new HashMap<>(), model);

        return "statistics";
    }
    @RequestMapping(value = "/statistics_filter", method = RequestMethod.POST)
    public String filterJson(@RequestParam Map<String, String> paramsMap,
                             Model model){

        controllerService.processRequestsFilter(paramsMap, model);

        return "statisticsBody";
    }
    @Autowired
    public void setControllerService(StatisticsControllerService controllerService) {
        this.controllerService = controllerService;
    }
}
