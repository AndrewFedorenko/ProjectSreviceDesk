package ua.servicedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servicedesk.domain.AttachedFile;

import java.util.List;

// repository for DB operations processing for entity AttachedFile
@Repository
@Transactional
public class FilesRepository extends AbstractRepository{

    public List<AttachedFile> findByRequestId(String id) {
        return entityManager.createQuery("select f from AttachedFile f where f.supportRequest.id=:id", AttachedFile.class).
                setParameter("id", id).getResultList();
    }
    public AttachedFile findFileById(String id) {
        return entityManager.createQuery("select f from AttachedFile f where f.id=:id",
                AttachedFile.class).setParameter("id", id).getSingleResult();
    }
    public AttachedFile findFileByName(String name) {
        return entityManager.createQuery("select sr from AttachedFile sr where sr.name=:name",
                AttachedFile.class).setParameter("name", name).getSingleResult();
    }
    public void updateFile(AttachedFile file) {
        entityManager.merge(file);
    }
}
