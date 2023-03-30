package ua.servicedesk.domain;

import jakarta.persistence.*;
import ua.servicedesk.domain.requestfields.User;

import java.time.LocalDateTime;

// describes property of messages that any user can leave for support request
@Entity
public class RequestMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String messageContent;

    private LocalDateTime date;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    private SupportRequest supportRequest;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, optional = false)
    private User user;

    public RequestMessage(){
        this.date = LocalDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public SupportRequest getSupportRequest() {
        return supportRequest;
    }

    public void setSupportRequest(SupportRequest supportRequest) {
        this.supportRequest = supportRequest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public String toString(){
        return messageContent.substring(20);
    }

    @Override
    public boolean equals(Object object){
        if (this == object){
            return true;
        }
        if (object == null || object.getClass() != getClass()){
            return false;
        }
        RequestMessage message = (RequestMessage) object;
        return messageContent.equals(message.getMessageContent()) && date.equals(message.getDate());
    }

    @Override
    public int hashCode() {
        return messageContent.hashCode();
    }
}
