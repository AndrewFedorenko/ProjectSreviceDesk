package ua.servisedesk.dao;

import jakarta.persistence.Query;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servisedesk.domain.User;

import java.util.List;

@Repository
@Transactional
public class UserRepository extends AbstractRepository{

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findUserByName(auth.getName());
    }
    public User findUserByName(String name) {
        try {
            return entityManager.createQuery("select sr from User sr where sr.name=:name",
                    User.class).setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
    public User findUserByEmail(String email) {
        try {
            return entityManager.createQuery("select sr from User sr where sr.email=:email",
                    User.class).setParameter("email", email).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<User> findAll() {

        User authorisedUser = getCurrentUser();
        String additionalFilter = authorisedUser.getRole().getUsersFilter();
        additionalFilter = additionalFilter == null ? "" : additionalFilter;

        Query query = entityManager.createQuery(
                "select us from User us "
                + additionalFilter
                + " order by us.id"
                , User.class);

        if (additionalFilter.contains(":userid")){
            query.setParameter("userid", authorisedUser.getId());
        }

        return query.getResultList();
    }

    public List<User> findExecutorsOnProject(String projectId) {

        return entityManager.createQuery(
                "select u from User u inner join u.projects p where p.id=:projectId and u.role.isExecutor and u.isEnabled",
                        User.class)
                    .setParameter("projectId", projectId)
                    .getResultList();
    }

    public User findUserById(String id) {
        User user = ("0".equals(id)||"".equals(id))  ? null :
                    entityManager.createQuery("select u from User u where u.id=:id and u.isEnabled",
                    User.class).setParameter("id", id)
                    .getSingleResult();
        return user;
    }

    public User saveUser(User user) {

        entityManager.persist(user);

        return user;
    }

    public List<User> findAllClients() {
        return entityManager.createQuery("select u from User u where u.isEnabled order by u.id",
                        User.class)
                        .getResultList();
    }

    public void updateUser(User user) {
        if(user.getId()!=null){
        User userOfBase = findUserById(String.valueOf(user.getId()));
        if(user.getPassword().isEmpty()){
            user.setPassword(userOfBase.getPassword());
        }}
        entityManager.merge(user);
    }
}
