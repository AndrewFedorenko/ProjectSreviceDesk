package ua.servicedesk.services.controllerservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.SupportRequest;
import ua.servicedesk.services.RequestFieldsConfig;
import ua.servicedesk.services.RequestsProcessEvents;
import ua.servicedesk.services.StatisticsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsControllerService {

    private StatisticsBuilder statisticsBuilder;

    private CurrentRoleHolder roleHolder;

    private RequestFieldsConfig requestFieldsConfig;

    private RequestsControllerService requestsControllerService;

    public void processRequestsFilter(Map<String, String> paramsMap,
                                      Model model){

        SupportRequest filterRequest = new SupportRequest();
        requestFieldsConfig.prepareAndFilterRequestModel(
                requestsControllerService.fieldsMap(),
                model,
                filterRequest,
                RequestsProcessEvents.FILTER);

        List<String> analitics = paramsMap.get("fieldslist") != null ? Arrays.stream(
                paramsMap.get("fieldslist").split(",")).toList() :
                new ArrayList<>();
        model.addAttribute("head", analitics);
        model.addAttribute("headtext", paramsMap.get("fieldslist") != null ?
                paramsMap.get("fieldslist").toUpperCase() + ":" : "");
        model.addAttribute("result", statisticsBuilder.buildResult(analitics, paramsMap));

        CommonModelAttrSetter.setCommonModelAttr(model, roleHolder);
    }

    @Autowired
    public void setStatisticsBuilder(StatisticsBuilder statisticsBuilder) {
        this.statisticsBuilder = statisticsBuilder;
    }
    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }
    @Autowired
    public void setRequestFieldsConfig(RequestFieldsConfig requestFieldsConfig) {
        this.requestFieldsConfig = requestFieldsConfig;
    }
    @Autowired
    public void setRequestsControllerService(RequestsControllerService requestsControllerService) {
        this.requestsControllerService = requestsControllerService;
    }
}
