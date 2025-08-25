package com.glg204.fep.infrastructure.mail;

public interface MailService {
    void send(String to, String subject, String body);
}
