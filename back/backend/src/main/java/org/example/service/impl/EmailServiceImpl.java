package org.example.service.impl;

import org.example.model.entity.User;
import org.example.service.EmailService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailServiceImpl implements EmailService {

    @Override
    public void sendVerificationEmail(User user) throws MessagingException {
        String host = "smtp.gmail.com";
        String from = "no-reply@example.com";
        String username = "*";
        String password = "*";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
        message.setSubject("Email Verification");
        message.setText("Please verify your email by clicking the link: http://localhost:8080/users/" + user.getUserName() + "/verify/" + user.getVerificationToken());

        Transport.send(message);
    }
}
