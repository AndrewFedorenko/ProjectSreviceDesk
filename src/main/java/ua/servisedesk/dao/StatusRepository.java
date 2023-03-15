package ua.servisedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servisedesk.domain.Status;

import java.util.List;

@Repository
@Transactional
public class StatusRepository extends AbstractRepository{
    public List<Status> findAll() {
        return entityManager.createQuery("select sr from Status sr", Status.class).getResultList();
    }
    public Status findStatusById(String id) {
        return entityManager.createQuery("select sr from Status sr where sr.id=:id",
                Status.class).setParameter("id", id).getSingleResult();
    }
}
