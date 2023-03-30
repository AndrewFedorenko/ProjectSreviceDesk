package ua.servicedesk.dao;

import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servicedesk.domain.CurrentRoleHolder;
import ua.servicedesk.domain.requestfields.Project;

import java.util.List;

// repository for DB operations processing for entity Project
@Repository
@Transactional
public class ProjectRepository extends AbstractRepository{

    private CurrentRoleHolder roleHolder;

    public List<Project> findAll() {

        String additionalFilter = roleHolder.getRole().getProjectsFilter();
        additionalFilter = additionalFilter == null ? "" : additionalFilter;

        Query query = entityManager.createQuery(
                // customer query
                "select p from Project p " + additionalFilter + " order by p.id", Project.class);
        if (!additionalFilter.isEmpty() && additionalFilter.contains(":userid")){
            query.setParameter("userid", roleHolder.getUserId());
        }

        return query.getResultList();


    }
    public Project findItemById(String id) {
        return entityManager.createQuery("select sr from Project sr where sr.id=:id",
                Project.class).setParameter("id", id).getSingleResult();
    }
    public void update(Project project) {
        entityManager.merge(project);
    }

    @Autowired
    public void setRoleHolder(CurrentRoleHolder roleHolder) {
        this.roleHolder = roleHolder;
    }
}
