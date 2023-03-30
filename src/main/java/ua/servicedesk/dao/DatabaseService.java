package ua.servicedesk.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.servicedesk.domain.Database;

import java.io.File;
import java.io.FileInputStream;

@Repository
@Transactional
public class DatabaseService extends AbstractRepository{

    @Transactional(readOnly = true)
    public Database getDatabase() {
        return entityManager.find(Database.class, 1L);
    }


    public void applyVersion(File file) {
        try (FileInputStream fis = new FileInputStream(file)) {
            String sql = new String(fis.readAllBytes());
            jdbcTemplate.execute(sql);
            increaseDatabaseVersion();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private void increaseDatabaseVersion() {
        Database database = getDatabase();
        if (database == null) {
            entityManager.createNativeQuery("insert into database (version) values (0)").executeUpdate();
            return;
        }

        entityManager.createNativeQuery("update database set version = :newVersion where id= :id")
                .setParameter("newVersion", database.getVersion() + 1)
                .setParameter("id", database.getId()).executeUpdate();
    }
}
