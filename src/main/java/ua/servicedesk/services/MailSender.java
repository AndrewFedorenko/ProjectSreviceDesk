package ua.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ua.servicedesk.dao.EmailRepository;
import ua.servicedesk.dao.UserRepository;
import ua.servicedesk.domain.EmailProfile;
import ua.servicedesk.domain.SupportRequest;
import ua.servicedesk.mailUtils.InformingReason;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Service
public class MailSender {
    @Autowired
    private EmailRepository repository;
    @Autowired
    private UserRepository userRepository;

    public void send(SupportRequest supportRequest, List<InformingReason> reason){
        if(reason.size()==0){
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
                    InternetAddress.parse(
                            receiversString(reason, supportRequest))
            );
            message.setSubject("Servicedesk message, project: " + supportRequest.getProject().getName());
            message.setText(mailBody(reason, supportRequest));

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String mailBody(List<InformingReason> reasons, SupportRequest supportRequest){
        StringBuilder sb = new StringBuilder();

        reasons.forEach(reason-> sb.append(reason.createMailBody(supportRequest)));
        return
                "HELLO!" +
                        "\nRequest N:" +
                        supportRequest.getId() +
                        " (" + supportRequest.getDate() +
                        ")\n" +
                        SecurityContextHolder.getContext().getAuthentication().getName() +
                        ": \n" +
                        sb +
                        "\n---------------\nVisit support requests page for details.";
    }

    private String receiversString(List<InformingReason> reasons, SupportRequest supportRequest){
        Set<String> resList = new HashSet<>();

        reasons.forEach(reason->
                resList.addAll(reason.createReceiversString(supportRequest, userRepository)));
        return String.join(",", resList);
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
