package ua.servicedesk.domain.requestfields;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.Role;
import ua.servicedesk.domain.SupportRequest;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(name = "name_uk", columnNames = "name"))
@Component
public class User implements RequestFieldType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String name;

    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<SupportRequest> supportRequests;

    @Column
    @ColumnDefault("true")
    private boolean isEnabled = true;

    private transient UserRepository repository;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
            isEnabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<SupportRequest> getSupportRequests() {
        return supportRequests;
    }

    public void setSupportRequests(List<SupportRequest> supportRequests) {
        this.supportRequests = supportRequests;
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
        if (object == null){// || object.getClass() != getClass()){
            return false;
        }
        User user = (User) object;
        return name.equals(user.getName()) && email.equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return (name + email).hashCode();
    }

    @Override
    public List<String> fieldsList() {
        return List.of("author", "executor");
    }


    public UserRepository getRepository() {
        return repository;
    }
    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }
}
