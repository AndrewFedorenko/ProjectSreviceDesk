package ua.servicedesk.mailUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReasonsService {

    private List<InformingReason> reasons;

    public List<InformingReason> getReasons() {
        return reasons;
    }
    @Autowired
    public void setReasons(List<InformingReason> reasons) {
        this.reasons = reasons;
    }
}
