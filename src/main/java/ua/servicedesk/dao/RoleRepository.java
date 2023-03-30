package ua.servicedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servicedesk.domain.AllowedLink;
import ua.servicedesk.domain.Role;

import java.util.List;

// repository for DB operations processing for entity Role
@Repository
@Transactional
public class RoleRepository extends AbstractRepository{

    public List<Role> findAllItems() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }

    public List<AllowedLink> findAllLinks() {
        return entityManager.createQuery("select r from AllowedLink r", AllowedLink.class).getResultList();
    }

    public Role findRoleById(String id) {
        return entityManager.createQuery("select r from Role r where r.id=:id",
                Role.class).setParameter("id", id).getSingleResult();
    }
}
