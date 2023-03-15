package ua.servisedesk.dao;


import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ua.servisedesk.domain.RequestField;

import javax.sql.DataSource;
import java.util.List;

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

    public List<? extends RequestField> findAll() {
        return null;
    }
}
