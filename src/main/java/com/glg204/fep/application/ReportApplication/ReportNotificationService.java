package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.application.notification.MailService;
import com.glg204.fep.domain.ReportDomain.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportNotificationService {

    private final MailService mailService;

    public void sendReportReceivedNotification(Report report) {
        String subject = "Votre signalement a été reçu";
        String body = "Bonjour " + report.getReporter().getFirstName() + ",\n\n" +
                "Nous avons bien reçu votre signalement concernant l’utilisateur " +
                report.getReportedUser().getFirstName() + " " + report.getReportedUser().getLastName() +
                " pour motif : " + report.getReason() + ".\n\n" +
                "Notre équipe va l’examiner et prendra les mesures nécessaires.\n\n" +
                "L’équipe FEP.";

        mailService.send(report.getReporter().getEmail(), subject, body);
    }

    public void sendReportEscalationNotification(Report report) {
        String subject = "Nouveau signalement à traiter";
        String body = "Un nouveau signalement a été enregistré :\n\n" +
                "- Motif : " + report.getReason() + "\n" +
                "- Reporter : " + report.getReporter().getEmail() + "\n" +
                "- Utilisateur signalé : " + report.getReportedUser().getEmail() + "\n" +
                "- Date : " + report.getReportDate() + "\n\n" +
                "Merci de traiter ce cas rapidement.";

        mailService.send("admin@fep.com", subject, body);
    }

    public void sendReportResolutionNotification(Report report) {
        String subject = "Votre signalement a été traité";
        String body = "Bonjour " + report.getReporter().getFirstName() + ",\n\n" +
                "Votre signalement concernant " + report.getReportedUser().getFirstName() +
                " a été traité par notre équipe.\n\n" +
                "Statut actuel : " + (report.isOpen() ? "Ouvert" : "Fermé") + ".\n\n" +
                "Merci de votre vigilance.\n\n" +
                "L’équipe FEP.";

        mailService.send(report.getReporter().getEmail(), subject, body);
    }
}
