package ua.servicedesk.services.controllerservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.servicedesk.dao.*;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.requestfields.RequestFieldType;
import ua.servicedesk.domain.RequestMessage;
import ua.servicedesk.domain.SupportRequest;
import ua.servicedesk.mailUtils.InformingReason;
import ua.servicedesk.mailUtils.InformingReasonsFactory;
import ua.servicedesk.mailUtils.ReasonsService;
import ua.servicedesk.services.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestsControllerService {

    private RequestsRepository requestsRepository;

    private UserRepository userRepository;

    private CurrentRoleHolder roleHolder;

    private RequestMessageRepository messageRepository;

    private FieldsChecker fieldsChecker;

    private ReasonsService reasonsService;

    private MailSender mailSender;

    private RequestFieldsConfig requestFieldsConfig;

    private RequestsFieldsService requestsFieldsService;

    public void processRequests(Model model){

        List<SupportRequest> requestsRepositories = requestsRepository.findRequestByFilter(new HashMap<>());
        SupportRequest filterRequest = new SupportRequest();
        requestFieldsConfig.prepareAndFilterRequestModel(fieldsMap(),
                model,
                filterRequest,
                RequestsProcessEvents.FILTER);

        model.addAttribute("requestsRepositories", requestsRepositories);
        model.addAttribute("newrequest", new SupportRequest());
        CommonModelAttrSetter.setCommonModelAttr(model, roleHolder);

        model.addAttribute("newrequestbutton",
                ButtonsAccsessProcessor.buttonNewRequestAvailable(roleHolder));
    }

    public void processRequestsFilter(Map<String, String> paramsMap,
                                      Model model){
        List<SupportRequest> requestsRepositories = requestsRepository.findRequestByFilter(paramsMap);

        SupportRequest filterRequest = new SupportRequest();
        requestFieldsConfig.prepareAndFilterRequestModel(fieldsMap(),
                model,
                filterRequest,
                RequestsProcessEvents.FILTER);
        model.addAttribute("requestsRepositories", requestsRepositories);

    }

    public void processRequestEdit(String id,
                                   Model model){
        SupportRequest supportRequest;
        if("0".equals(id)){
            supportRequest =  new SupportRequest();
            supportRequest.setAuthor(userRepository.getCurrentUser());
        }
        else {
            supportRequest = requestsRepository.findRequestById(id);
            model.addAttribute("requestsMessages",messageRepository.findMessagesById(id));
        }

        Map<String, ArrayList<RequestFieldType>> fieldsMap = fieldsMap();
        Map<String, RequestFieldType> valuesMap = new HashMap<>();
        fieldsMap.forEach((key,value)->
                valuesMap.put(key.replace("list",""),
                        SupportRequestReflectionFieldGetter.getField(supportRequest, key.replace("list",""))));

        requestFieldsConfig.prepareAndFilterRequestModel(fieldsMap(),
                model,
                supportRequest,
                RequestsProcessEvents.EDIT);

        model.addAttribute("valuesMap", valuesMap);

        model.addAttribute("readonly", roleHolder.getRole().getRequestReadOnlyStatuses().contains(supportRequest.getStatus()));
        model.addAttribute("requestobject", supportRequest);
        model.addAttribute("taketoworkbutton", ButtonsAccsessProcessor.buttonTakeToWorkAvailable(roleHolder));
        model.addAttribute("savebutton", ButtonsAccsessProcessor.buttonSaveAvailable(roleHolder));

        CommonModelAttrSetter.setCommonModelAttr(model, roleHolder);
    }

    public void SetRequestFields(SupportRequest request,
                                        Map<String, String> paramsMap){
        for (RequestFieldType fieldType:requestsFieldsService.getFieldList()
        ) {
            for (String field:fieldType.fieldsList()
            ) {
                RequestFieldType val = fieldType.getRepository()
                        .findItemById(paramsMap.get(field + "id"));
                SupportRequestReflectionFieldGetter.setField(
                        request, field, val);
            }
        }
    }

    public String processSaveRequest(SupportRequest request,
                                     Map<String, String> paramsMap){

        String id="";
        String version="";
        String errorList = fieldsChecker.checkFields("SupportRequest", paramsMap);
        StringBuilder answer = new StringBuilder("{\"errorlist\":\"");

        if(errorList.isEmpty()){

            // fill list of instance fields of document
            SetRequestFields(request, paramsMap);

            // find reasons before saving document request
            List<InformingReason> reasons = InformingReasonsFactory.getInforminfReason(request,
                    requestsRepository,
                    reasonsService);

            request = requestsRepository.updateRequest(request);

            if(request==null){
                answer.append("Request has already modified by earlier user! You cannot save it(")
                        .append("\",\"id\":\"").append(id)
                        .append("\",\"version\":\"").append(version)
                        .append("\"}");
                return answer.toString();
            }

            mailSender.send(request,
                    reasons);
            id=String.valueOf(request.getId());
            version=String.valueOf(request.getVersion());

        }
        answer.append(errorList.isEmpty() ? "" : "Before saving you have to fill: " + errorList)
                .append("\",\"id\":\"").append(id)
                .append("\",\"version\":\"").append(version)
                .append("\"}");

        return answer.toString();

    }
    public Map<String, ArrayList<RequestFieldType>> fieldsMap(){

        Map<String, ArrayList<RequestFieldType>> map = new HashMap<>();

        requestsFieldsService.getFieldList().forEach(fieldType->
                requestFieldsConfig.addToMap(map, fieldType.getRepository(), fieldType.fieldsList()));

        return map;
    }

    public void processRequestMessage(Map<String, String> paramMap,
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

    }

    public String processTakeToWork(){

        if(roleHolder.getRole().isExecutor()){
            return  "{ \"id\":" + roleHolder.getUserId() +
                    ",\"name\":\"" + roleHolder.getUserName() + "\" }";
        }
        return "";

    }

    @Autowired
    public void setRequestsRepository(RequestsRepository requestsRepository) {
        this.requestsRepository = requestsRepository;
    }

    public RequestsRepository getRequestsRepository() {
        return this.requestsRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }

    public CurrentRoleHolder getRoleHolder(){
        return this.roleHolder;
    }

    @Autowired
    public void setMessageRepository(RequestMessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }
    @Autowired
    public void setFieldsChecker(FieldsChecker fieldsChecker) {
        this.fieldsChecker = fieldsChecker;
    }

    @Autowired
    public void setReasonsService(ReasonsService reasonsService) {
        this.reasonsService = reasonsService;
    }

    public ReasonsService getReasonsService() {
        return this.reasonsService;
    }

    @Autowired
    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Autowired
    public void setRequestFieldsConfig(RequestFieldsConfig requestFieldsConfig) {
        this.requestFieldsConfig = requestFieldsConfig;
    }

    public RequestFieldsConfig getRequestFieldsConfig() {
        return this.requestFieldsConfig;
    }
    @Autowired
    public void setRequestsFieldsService(RequestsFieldsService requestsFieldsService) {
        this.requestsFieldsService = requestsFieldsService;
    }
}
