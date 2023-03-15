package ua.servisedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.servisedesk.dao.EmailRepository;
import ua.servisedesk.dao.RequestsRepository;
import ua.servisedesk.dao.UserRepository;
import ua.servisedesk.domain.EmailProfile;
import ua.servisedesk.domain.SupportRequest;
import ua.servisedesk.mailUtils.InformingReason;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class MailSender {
    @Autowired
    private EmailRepository repository;
    @Autowired
    private UserRepository userRepository;

    public void send(SupportRequest supportRequest, InformingReason reason){
        if(reason==null){
            return;
        }
        EmailProfile emailProfile = repository.findActualProfile();
        if(emailProfile==null){
            return;
        }

        Properties prop = new Properties();
        prop.put("mail.smtp.host", emailProfile.getSmtpHost());
        prop.put("mail.smtp.port", emailProfile.getPort());
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", emailProfile.getPort());
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailProfile.getLogin(),
                                emailProfile.getPassword());
                    }
                });
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailProfile.getAddress()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(reason.createReceiversString(supportRequest, userRepository))
            );
            message.setSubject("Servicedesk message, project: " + supportRequest.getProject().getName());
            message.setText(reason.createMailBody(supportRequest));

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public EmailRepository getRepository() {
        return repository;
    }

    public void setRepository(EmailRepository repository) {
        this.repository = repository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
