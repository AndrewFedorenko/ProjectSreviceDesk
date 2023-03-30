package ua.servicedesk.services;

import ua.servicedesk.domain.CurrentRoleHolder;

// returns boolean values if user can view some buttons
public class ButtonsAccsessProcessor {

    public static boolean buttonNewRequestAvailable(CurrentRoleHolder roleHolder){
        return roleHolder.getRole().getAllowedLinks()
                .stream().filter(link->link.getSubLink().equals("new_request")).count()>0;

    }
    public static boolean buttonSaveAvailable(CurrentRoleHolder roleHolder){
        return roleHolder.getRole().getAllowedLinks()
                .stream().filter(link->link.getSubLink().equals("save_request")).count()>0;

    }
    public static boolean buttonTakeToWorkAvailable(CurrentRoleHolder roleHolder){
        return roleHolder.getRole().isExecutor();
    }
}
