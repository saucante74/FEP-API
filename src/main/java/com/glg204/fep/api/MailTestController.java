package com.glg204.fep.api;


import com.glg204.fep.infrastructure.mail.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailTestController {

    private final MailService mailService;

    public MailTestController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/test-mail")
    public String sendTestMail() {
        mailService.send("test@fep.local", "Test Mail", "Ceci est un test d'envoi via MailHog.");
        return "Mail envoyé !";
    }
}

