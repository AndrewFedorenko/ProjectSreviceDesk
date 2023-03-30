package ua.servicedesk.domain;

import jakarta.persistence.*;
import ua.servicedesk.domain.requestfields.Customer;
import ua.servicedesk.domain.requestfields.Project;
import ua.servicedesk.domain.requestfields.Status;
import ua.servicedesk.domain.requestfields.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// describes properties of document Support request
// contains next fields:
// - date - date and time of current document creating
// - id - used for every document identification
// - customer - customer supported by executors
// - project - specified theme to support
// - author - user who created request
// - executor - user who executed request
// - status - current state of request processing
// - content - describes authors question
// - answer - describes executors answer
@Entity
@Table(name = "requests")
public class SupportRequest{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime date;

    private long version = 0;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    private User executor;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    private Status status;

    @Column(name = "content", length = 2000)
    private String content = "";

    @Column(name = "answer", length = 2000)
    private String answer = "";

    @OneToMany(fetch = FetchType.LAZY)
    private List<RequestMessage> requestMessages = new ArrayList<>();

    public SupportRequest() {
        this.date = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getExecutor() {
        return executor;
    }

    public void setExecutor(User user) {
        this.executor = user;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<RequestMessage> getRequestMessages() {
        return requestMessages;
    }

    public void setRequestMessages(List<RequestMessage> requestMessages) {
        this.requestMessages = requestMessages;
    }

    @Override
    public String toString(){
        return "Request N" + getId() + " (" + getDate() + ")";
    }
    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }
        if (object == null || object.getClass() != getClass()){
            return false;
        }
        SupportRequest request = (SupportRequest) object;
        return id == request.id && date.equals(request.getDate());
    }

    @Override
    public int hashCode() {
        return Long.valueOf(id).hashCode();
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }
}
