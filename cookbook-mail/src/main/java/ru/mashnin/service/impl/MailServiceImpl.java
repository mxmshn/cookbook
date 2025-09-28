package ru.mashnin.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.mashnin.factory.EmailContent;
import ru.mashnin.service.MailService;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.application.name}")
    private String appName;

    public void sendMessage(EmailContent emailContent) {
        log.info("Попытка отправки письма на почту '{}'", emailContent.getMailTo());
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");
            mimeMessageHelper.setSubject(emailContent.getSubject());
            mimeMessageHelper.setText(emailContent.getText());
            mimeMessageHelper.setTo(emailContent.getMailTo());
            mimeMessageHelper.setFrom(fromEmail, appName);

            mailSender.send(message);
            log.info("Письмо отправлено на: {}", emailContent.getMailTo());
        } catch (MessagingException | UnsupportedEncodingException messagingException) {
            log.warn("Ошибка отправки письма");
            throw new MailSendException("Не удалось отправить письмо", messagingException);
        }
    }
}
