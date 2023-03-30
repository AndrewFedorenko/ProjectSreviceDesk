package ua.servicedesk.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.servicedesk.domain.FieldsToCheckHolder;
import ua.servicedesk.dao.RequiredFieldsRepository;

// creates bean holding list of fields to check before saving
@Configuration
public class FieldsToCheckConfiguration {
    private RequiredFieldsRepository fieldsRepository;
    private FieldsToCheckHolder fieldsToCheckHolder;

    @Bean
    public void setRequiredFields(){
        fieldsToCheckHolder.setFieldsToCheck(fieldsRepository.findAllFields());
    }
    public RequiredFieldsRepository getFieldsRepository() {
        return fieldsRepository;
    }
    @Autowired
    public void setFieldsRepository(RequiredFieldsRepository fieldsRepository) {
        this.fieldsRepository = fieldsRepository;
    }
    @Autowired
    public void setFieldsToCheckHolder(FieldsToCheckHolder fieldsToCheckHolder) {
        this.fieldsToCheckHolder = fieldsToCheckHolder;
    }
}
