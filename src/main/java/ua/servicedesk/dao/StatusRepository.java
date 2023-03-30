package ua.servicedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servicedesk.domain.requestfields.Status;

import java.util.List;

// repository for DB operations processing for entity Status
@Repository
@Transactional
public class StatusRepository extends AbstractRepository{

    public List<Status> findAll() {
        return entityManager.createQuery("select sr from Status sr", Status.class).getResultList();
    }

    public Status findItemById(String id) {
        return entityManager.createQuery("select sr from Status sr where sr.id=:id",
                Status.class).setParameter("id", id).getSingleResult();
    }
}
