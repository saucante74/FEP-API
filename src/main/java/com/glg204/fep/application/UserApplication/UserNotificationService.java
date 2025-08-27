package com.glg204.fep.application.UserApplication;

import com.glg204.fep.application.notification.MailService;
import com.glg204.fep.domain.UserDomain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private final MailService mailService;

    public void sendRegistrationMail(User user) {
        String subject = "Bienvenue " + user.getFirstName();
        String body = "Bonjour " + user.getFirstName() + ",\n\n" +
                "Merci de vous être inscrit sur notre plateforme. " +
                "Votre compte est en attente de validation.";

        mailService.send(user.getEmail(), subject, body);
    }

    public void sendAccountValidatedMail(User user) {
        String subject = "Votre compte est validé.";
        String body = "Bonjour " + user.getFirstName() + ",\n\n" +
                "Bonne nouvelle ! Votre compte vient d’être validé par notre équipe. " +
                "Vous pouvez maintenant emprunter ou prêter via la plateforme.\n\n" +
                "L’équipe FEP.";
        mailService.send(user.getEmail(), subject, body);
    }

    public void sendPasswordResetMail(User user, String resetLink) {
        String subject = "Réinitialisation de votre mot de passe";
        String body = "Bonjour " + user.getFirstName() + ",\n\n" +
                "Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetLink;

        mailService.send(user.getEmail(), subject, body);
    }

    public void sendAccountBlockedMail(User user) {
        String subject = "Votre compte a été suspendu.";
        String body = "Bonjour " + user.getFirstName() + ",\n\n" +
                "Nous vous informons que votre compte a été suspendu. " +
                "Merci de contacter notre support si vous pensez qu’il s’agit d’une erreur.\n\n" +
                "L’équipe FEP.";
        mailService.send(user.getEmail(), subject, body);
    }
}


