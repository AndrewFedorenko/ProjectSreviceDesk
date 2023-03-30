package ua.servicedesk.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// entity describes properties of files attached to document Support request
@Entity
@Table(name = "files")
public class AttachedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String fullName;

    private LocalDateTime uploadDateTime;
    private byte[] file;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST, optional = false)
    private SupportRequest supportRequest;

    public byte[] getFile() {
        return file;
    }
    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SupportRequest getSupportRequest() {
        return supportRequest;
    }

    public void setSupportRequest(SupportRequest supportRequest) {
        this.supportRequest = supportRequest;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDateTime getUploadDateTime() {
        return uploadDateTime;
    }

    public void setUploadDateTime(LocalDateTime uploadDateTime) {
        this.uploadDateTime = uploadDateTime;
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
        AttachedFile attachedFile = (AttachedFile) object;
        return name.equals(attachedFile.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
