package ua.servisedesk.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "requests")
public class SupportRequest{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY) //, cascade = CascadeType.PERSIST, optional = false)
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
