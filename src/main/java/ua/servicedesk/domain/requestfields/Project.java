package ua.servicedesk.domain.requestfields;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.servicedesk.dao.ProjectRepository;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects", uniqueConstraints = @UniqueConstraint(name = "name_uk", columnNames = "name"))
@Component
public class Project implements RequestFieldType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "user_project",
            joinColumns = @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "project_id_fk")),
            inverseJoinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "user_id_fk"))
    )
    @JsonBackReference
    private List<User> users = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "customer_project",
            joinColumns = @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "project_id_fk")),
            inverseJoinColumns = @JoinColumn(name = "customer_id", foreignKey = @ForeignKey(name = "customer_id_fk"))
    )
    @JsonBackReference
    private List<Customer> customers = new ArrayList<>();

    private transient ProjectRepository repository;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public boolean addUser(User user){
        if(this.users.stream().noneMatch(u -> u.equals(user))){
            this.users.add(user);
            return true;
        }
        return false;
    }
    public boolean removeUser(User user){
        return this.users.remove(user);
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }

    public boolean addCustomer(Customer customer){
        if(this.customers.stream().noneMatch(u -> u.equals(customer))){
            this.customers.add(customer);
            return true;
        }
        return false;
    }
    public boolean removeCustomer(Customer customer){
        return this.customers.remove(customer);
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
        Project project = (Project) object;
        return name.equals(project.getName());
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : super.hashCode();
    }

    @Override
    public List<String> fieldsList() {
        return List.of("project");
    }

    public ProjectRepository getRepository() {
        return repository;
    }
    @Autowired
    public void setRepository(ProjectRepository repository) {
        this.repository = repository;
    }
}
