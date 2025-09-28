package ru.mashnin.factory;

public interface EmailContentFactory {
    EmailContent createConfirmationEmail(String mailTo, String confirmationLink);
    EmailContent createWelcomeEmail(String mailTo, String username);
    EmailContent createPasswordResetEmail(String mailTo, String resetLink); // позже добавим
}
