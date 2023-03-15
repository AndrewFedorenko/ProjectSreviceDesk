package ua.servisedesk.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Where(clause = "enabled = 'true'")
public class Customer implements RequestField{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column
    @ColumnDefault("true")
    private boolean enabled;

    public void setId(Long id) {
        this.id = id;
    }


    public Long getId() {
        return id;
    }

    @ManyToMany(mappedBy = "customers", fetch = FetchType.EAGER)
    private List<Project> projects = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean GetEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
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
        if (object == null || object.getClass() != getClass()){
            return false;
        }
        Customer customer = (Customer) object;
        return name.equals(customer.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
