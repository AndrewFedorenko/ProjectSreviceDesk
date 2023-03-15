package ua.servisedesk.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@SecondaryTable(name="filters")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    // additional dbc filters - for requests, for projects, for users
    @Column(table = "filters")
    private String requestsFilter;
    @Column(table = "filters")
    private String projectsFilter;
    @Column(table = "filters")
    private String usersFilter;

    private boolean isExecutor = false;

    private boolean isAdmin = false;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RequestReadOnlyField> requestReadOnlyFields = new ArrayList<>();

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

    public String getRequestsFilter() {
        return requestsFilter;
    }

    public void setRequestsFilter(String requestsFilter) {
        this.requestsFilter = requestsFilter;
    }

    public String getProjectsFilter() {
        return projectsFilter;
    }

    public void setProjectsFilter(String projectsFilter) {
        this.projectsFilter = projectsFilter;
    }


    public boolean isExecutor() {
        return isExecutor;
    }

    public void setExecutor(boolean executor) {
        isExecutor = executor;
    }

    public String getUsersFilter() {
        return usersFilter;
    }

    public void setUsersFilter(String usersFilter) {
        this.usersFilter = usersFilter;
    }

    public List<RequestReadOnlyField> getRequestReadOnlyFields() {
        return requestReadOnlyFields;
    }

    public void setRequestReadOnlyFields(List<RequestReadOnlyField> requestReadOnlyFields) {
        this.requestReadOnlyFields = requestReadOnlyFields;
    }

    @Override
    public String toString(){
        return getName();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }
        if (object == null || object.getClass() != getClass()){
            return false;
        }
        Role role = (Role) object;
        return name.equals(role.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
