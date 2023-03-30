package ua.servicedesk.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// describes properties of any required field that must be filled before saving.
// used in fields checker
@Entity
public class RequiredField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String entityName;

    private String fieldName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String toString(){
        return getEntityName() + ": " + getFieldName();
    }

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }
        if (object == null || object.getClass() != getClass()){
            return false;
        }
        RequiredField field = (RequiredField) object;
        return entityName.equals(field.getEntityName()) && fieldName.equals(field.getFieldName());
    }

    @Override
    public int hashCode() {
        return (entityName + fieldName).hashCode();
    }
}
