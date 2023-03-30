package ua.servicedesk.services.controllerservices;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import ua.servicedesk.domain.CurrentRoleHolder;


public class CommonModelAttrSetter {

    public static void setCommonModelAttr(Model model, CurrentRoleHolder roleHolder){

        model.addAttribute("allowedLinks",
                roleHolder.getRole().getAllowedLinksToGenerateMenu());
        model.addAttribute("authuser", SecurityContextHolder.getContext()
                .getAuthentication().getName());

    }
}
