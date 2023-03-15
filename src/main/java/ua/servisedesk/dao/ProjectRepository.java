package ua.servisedesk.dao;

import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servisedesk.domain.Project;
import ua.servisedesk.domain.User;

import java.util.List;

@Repository
@Transactional
public class ProjectRepository extends AbstractRepository{
    @Autowired
    UserRepository userRepository;

    public List<Project> findAll() {

        User authorisedUser = userRepository.getCurrentUser();
        String additionalFilter = authorisedUser.getRole().getProjectsFilter();
        additionalFilter = additionalFilter == null ? "" : additionalFilter;

        Query query = entityManager.createQuery(
                // customer query
                "select p from Project p " + additionalFilter + " order by p.id", Project.class);
        if (!additionalFilter.isEmpty() && additionalFilter.contains(":userid")){
            query.setParameter("userid", authorisedUser.getId());
        }

        return query.getResultList();


    }
    public Project findAProjectById(String id) {
        return entityManager.createQuery("select sr from Project sr where sr.id=:id",
                Project.class).setParameter("id", id).getSingleResult();
    }
    public void update(Project project) {
        entityManager.merge(project);
    }
}
