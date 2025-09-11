package ru.mashnin.service;

public interface MailService {
    void sendEmailConfirmation(String toEmail, String confirmationLink);

    void sendWelcomeEmail(String toEmail, String username);
}
