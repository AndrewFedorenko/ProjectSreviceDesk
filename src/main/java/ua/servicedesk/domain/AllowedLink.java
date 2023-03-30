package ua.servicedesk.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// entity for links allowed to view by various roles
// also used to create top menu panel
@Entity
public class AllowedLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String link;

    private String menuName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="allowed_link_roles",
            joinColumns = @JoinColumn(name="allowed_link_id", referencedColumnName="id"),
            inverseJoinColumns = @JoinColumn(name="roles_id", referencedColumnName="id"))
    private List<Role> roles = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public String getSubLink() {
        return link.substring(1);
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
