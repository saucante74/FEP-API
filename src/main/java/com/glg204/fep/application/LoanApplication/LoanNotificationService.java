package com.glg204.fep.application.LoanApplication;

import com.glg204.fep.application.notification.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanNotificationService {

    private final MailService mailService;

    public void sendLoanRequestCreatedMail(String borrowerEmail, String borrowerName, String reference) {
        String subject = "Demande de prêt créée.";
        String body = "Bonjour " + borrowerName + ",\n\n" +
                "Votre demande de prêt (" + reference + ") a bien été enregistrée. " +
                "Elle est en attente de validation.\n\n" +
                "L’équipe FEP.";
        mailService.send(borrowerEmail, subject, body);
    }

    public void sendLoanFundedMail(String lenderEmail, String lenderName, String reference) {
        String subject = "Prêt financé.";
        String body = "Bonjour " + lenderName + ",\n\n" +
                "Vous venez de financer le prêt (" + reference + "). " +
                "Merci de votre confiance !\n\n" +
                "L’équipe FEP.";
        mailService.send(lenderEmail, subject, body);
    }

    public void sendLoanInProgressMail(String borrowerEmail, String borrowerName, String reference) {
        String subject = "Votre prêt est maintenant en cours.";
        String body = "Bonjour " + borrowerName + ",\n\n" +
                "Votre prêt (" + reference + ") est maintenant en cours d’exécution. " +
                "Les remboursements démarreront selon l’échéancier prévu.\n\n" +
                "L’équipe FEP.";
        mailService.send(borrowerEmail, subject, body);
    }

    public void sendLoanFullyRepaidMail(String lenderEmail, String lenderName, String reference) {
        String subject = "Prêt remboursé.";
        String body = "Bonjour " + lenderName + ",\n\n" +
                "Le prêt (" + reference + ") a été intégralement remboursé par l’emprunteur. " +
                "Les fonds vous ont été crédités.\n\n" +
                "L’équipe FEP.";
        mailService.send(lenderEmail, subject, body);
    }

    public void sendLoanRejectedMail(String borrowerEmail, String borrowerName, String reference) {
        String subject = "Votre demande de prêt a été refusée.";
        String body = "Bonjour " + borrowerName + ",\n\n" +
                "Nous sommes désolés de vous informer que votre demande de prêt (" + reference + ") a été refusée ou annulée.\n\n" +
                "L’équipe FEP.";
        mailService.send(borrowerEmail, subject, body);
    }
}
