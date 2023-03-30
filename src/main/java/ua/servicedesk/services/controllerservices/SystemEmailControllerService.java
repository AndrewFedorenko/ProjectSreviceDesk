package ua.servicedesk.services.controllerservices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import ua.servicedesk.dao.EmailRepository;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.EmailProfile;
import ua.servicedesk.services.FieldsChecker;

import java.util.Map;

@Service
public class SystemEmailControllerService {

    private EmailRepository emailRepository;

    private FieldsChecker fieldsChecker;

    private CurrentRoleHolder roleHolder;

    public void processEditUser(Model model){
        model.addAttribute("email", emailRepository.findEmail());
        CommonModelAttrSetter.setCommonModelAttr(model, roleHolder);
    }

    public String processSaveEmail(EmailProfile profile,
                                          Map<String, String> paramsMap) {

        String id="";
        String errorList = fieldsChecker.checkFields("EmailProfile", paramsMap);
        StringBuilder answer = new StringBuilder("{\"errorlist\":\"");

        if(errorList.isEmpty()){
            emailRepository.updateEmail(profile);
            id=String.valueOf(profile.getId());
        }

        answer.append(errorList).append("\",\"id\":\"").append(id).append("\"}");
        return answer.toString();
    }


    @Autowired
    public void setEmailRepository(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }
    @Autowired
    public void setFieldsChecker(FieldsChecker fieldsChecker) {
        this.fieldsChecker = fieldsChecker;
    }

    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }
}
