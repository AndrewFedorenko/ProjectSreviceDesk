package ua.servisedesk.db;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.servisedesk.dao.DatabaseService;
import ua.servisedesk.domain.Database;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service("migrator")
public class Migrator {

    private File migrationFolder;

    private DatabaseService databaseService;

    @PostConstruct
    protected void init() {
        try {
            migrationFolder = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource("migrator/db")).toURI());
            if(!migrationFolder.exists() || !migrationFolder.isDirectory()) {
                throw new IllegalArgumentException("Couldn't find migrator folder");
            }
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
    }

    public void migrate() {
        if(migrationFolder == null || migrationFolder.listFiles() == null) {
            throw new IllegalStateException("migration folder is null");
        }

        List<File> files = Arrays.stream(migrationFolder.listFiles()).sorted().toList();

        for(File file : files) {
            String name = file.getName();
            if(name.endsWith(".sql")) {
                String version = name.substring(0, name.indexOf(".sql")).split("_")[1];
                int dbVersion = Integer.parseInt(version);
                Database database = databaseService.getDatabase();
                int currentDBVersion = database == null? -1: database.getVersion();

                if(dbVersion > currentDBVersion) {
                    databaseService.applyVersion(file);
                }
            }
        }
    }

    @Autowired
    public void setDatabaseService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }
}
