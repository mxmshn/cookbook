package ru.mashnin.factory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmailContentFactoryImpl implements EmailContentFactory {

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public EmailContent createConfirmationEmail(String mailTo, String confirmationLink) {
        String subject = "Подтверждение email";
        String text = "Для подтверждения email перейдите по ссылке: " + confirmationLink;
        return new EmailContent(mailTo, subject, text);
    }

    @Override
    public EmailContent createWelcomeEmail(String mailTo, String username) {
        String subject = "Добро пожаловать!";
        String text = "Добро пожаловать, " + username + "! Ваш аккаунт успешно активирован.";
        return new EmailContent(mailTo, subject, text);
    }

    @Override
    public EmailContent createPasswordResetEmail(String mailTo, String resetLink) {
        String subject = "Сброс пароля";
        String text = "Чтобы сбросить пароль, перейдите по ссылке: " + resetLink;
        return new EmailContent(mailTo, subject, text);
    }
}