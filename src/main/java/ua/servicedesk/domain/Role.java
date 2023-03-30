package ua.servicedesk.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import ua.servicedesk.domain.requestfields.Status;

import java.util.ArrayList;
import java.util.List;

// describes properties relating to various users permissions:
// - additional filters for DB queries to select permitted support requests, projects and users
// - list of support request read only fields for current user
// - list of statuses used to close current document for edition
// - list of links allowed to view by current role
// - property 'Is executor' to check whether user can take current document to work
@Entity
@SecondaryTable(name="filters")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    // additional dbc filters - for requests
    @Column(table = "filters")
    @NotNull
    private String requestsFilter = "";

    // additional dbc filters - for projects
    @Column(table = "filters")
    private String projectsFilter;

    // additional dbc filters - for users
    @Column(table = "filters")
    private String usersFilter;

    private boolean isExecutor = false;

    private boolean isAdmin = false;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<RequestReadOnlyField> requestReadOnlyFields = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Status> requestReadOnlyStatuses = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    @JsonBackReference
    private List<AllowedLink> allowedLinks = new ArrayList<>();

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

    // get list of support request fields that must be read-only
    // this list can be unique for each role
    public List<String> getStringRequestReadOnlyFields() {
        List<String> stringFielsList = new ArrayList<>();
        requestReadOnlyFields.forEach(x->stringFielsList.add(x.getField()));
        return stringFielsList;
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

    public List<Status> getRequestReadOnlyStatuses() {
        return requestReadOnlyStatuses;
    }

    public void setRequestReadOnlyStatuses(List<Status> requestReadOnlyStatuses) {
        this.requestReadOnlyStatuses = requestReadOnlyStatuses;
    }

    public List<AllowedLink> getAllowedLinks() {
        return allowedLinks;
    }

    // returns list of not empty links
    public List<AllowedLink> getAllowedLinksToGenerateMenu() {

        return allowedLinks.stream().filter(link->!link.getMenuName().isEmpty()).toList();
    }

    public void setAllowedLinks(List<AllowedLink> allowedLinks) {
        this.allowedLinks = allowedLinks;
    }
}
