package ua.servicedesk.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.servicedesk.domain.requestfields.RequestFieldType;

import javax.sql.DataSource;
import java.util.List;

// common abstract class for all repositories
public abstract class AbstractRepository {

    @PersistenceContext
    protected EntityManager entityManager;

    @PersistenceContext
    Session session;

    protected JdbcTemplate jdbcTemplate;
    @Autowired
    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<? extends RequestFieldType> findAll() {
        return null;
    }

    public RequestFieldType findItemById(String id){return null;}
}
