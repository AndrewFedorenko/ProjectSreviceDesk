package ua.servicedesk.domain;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.servicedesk.db.Migrator;

@Component
public class DBInitializer implements InitializingBean {

    private Migrator migrator;


    @Autowired(required = false)
    public void setMigrator(Migrator migrator) {
        this.migrator = migrator;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(migrator != null) {
            migrator.migrate();
        }
    }
}
