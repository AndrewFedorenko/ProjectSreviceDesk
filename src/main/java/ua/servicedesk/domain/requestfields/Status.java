package ua.servicedesk.domain.requestfields;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.servicedesk.dao.StatusRepository;

import java.util.List;

@Entity
@Table(name = "statuses")
@Component
public class Status implements RequestFieldType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    private transient StatusRepository repository;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return getName();
    }

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }
        if (object == null){
            return false;
        }
        Status status = (Status) object;
        return name.equals(status.getName());
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : super.hashCode();
    }

    @Override
    public List<String> fieldsList(){
        return List.of("status");
    }

    public StatusRepository getRepository() {
        return repository;
    }
    @Autowired
    public void setRepository(StatusRepository repository) {
        this.repository = repository;
    }
}
