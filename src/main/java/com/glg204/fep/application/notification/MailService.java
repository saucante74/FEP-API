package com.glg204.fep.application.notification;

public interface MailService {
    void send(String to, String subject, String body);
}
