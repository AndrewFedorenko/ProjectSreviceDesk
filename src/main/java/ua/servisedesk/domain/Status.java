package ua.servisedesk.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "statuses")
public class Status implements RequestField{
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        if (object == null || object.getClass() != getClass()){
            return false;
        }
        Status status = (Status) object;
        return name.equals(status.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
