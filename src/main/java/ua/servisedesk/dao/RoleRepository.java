package ua.servisedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servisedesk.domain.Role;

import java.util.List;

@Repository
@Transactional
public class RoleRepository extends AbstractRepository{

    public List<Role> findAllItems() {
        return entityManager.createQuery("select r from Role r", Role.class).getResultList();
    }

    public Role findRoleById(String id) {
        return entityManager.createQuery("select r from Role r where r.id=:id",
                Role.class).setParameter("id", id).getSingleResult();
    }
}
