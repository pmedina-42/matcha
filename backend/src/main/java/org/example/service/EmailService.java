package org.example.service;

import org.example.model.entity.User;

import javax.mail.MessagingException;

public interface EmailService {

    public void sendVerificationEmail(User user) throws MessagingException;

}
