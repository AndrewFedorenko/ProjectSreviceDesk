package ua.servicedesk.mailUtils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

// holds list of all types of reasons to sand mail
@Configuration
public class ReasonsConfiguration {
    @Bean
    public List<InformingReason> setReqsons(){
        List<InformingReason> reasons = new ArrayList<>();
        return reasons;
    }
}
