package ru.mashnin.service;

import ru.mashnin.factory.EmailContent;

public interface MailService {
    void sendMessage(EmailContent emailContent);
}
