package ua.servicedesk.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// describes properties of support request fields that user cannot edit.
// List of such fields is attached to user roles
@Entity
public class RequestReadOnlyField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String field;

    private boolean isForbiddenToFilter = true;

    private boolean isForbiddenToEdit = true;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }


    public boolean isForbiddenToEdit() {
        return isForbiddenToEdit;
    }

    public void setForbiddenToEdit(boolean forbiddenToEdit) {
        isForbiddenToEdit = forbiddenToEdit;
    }

    public boolean isForbiddenToFilter() {
        return isForbiddenToFilter;
    }

    public void setForbiddenToFilter(boolean forbiddenToFilter) {
        isForbiddenToFilter = forbiddenToFilter;
    }

    @Override
    public String toString(){
        return getField() + (isForbiddenToFilter ? " no filter" : "") +
                (isForbiddenToEdit ? " no edit" : "");
    }
}
