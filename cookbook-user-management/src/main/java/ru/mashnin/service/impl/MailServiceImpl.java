package ru.mashnin.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.mashnin.service.MailService;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void sendEmailConfirmation(String toEmail, String confirmationLink) {
        log.info("Попытка отправки письма подтверждения email на почту '{}'", toEmail);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);

            helper.setSubject("Подтверждение email");
            helper.setText("Для подтверждения email перейдите по ссылке: " + confirmationLink);
            helper.setFrom(fromEmail, appName);

            mailSender.send(message);
            log.info("Письмо для подтверждения email отправлено на: {}", toEmail);
        } catch (Exception e) {
            log.warn("Письмо подтверждения не удалось отправить на email '{}'", toEmail);
        }
    }

    @Override
    public void sendWelcomeEmail(String toEmail, String username) {
        log.info("Попытка отправки приветственного письма на почту '{}'", toEmail);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Добро пожаловать!");
            helper.setText("Добро пожаловать, " + username + "! Ваш аккаунт успешно активирован.");
            helper.setFrom(fromEmail, appName);

            mailSender.send(message);
            log.info("Приветственное письмо отправлено на: {}", toEmail);
        } catch (Exception e) {
            log.warn("Приветственное письмо не удалось отправить на email '{}'", toEmail);
        }
    }
}
