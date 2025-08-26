package com.glg204.fep.application.RefundApplication;

import com.glg204.fep.application.notification.MailService;
import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.RefundDomain.Refund;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefundNotificationService {

    private final MailService mailService;

    public void sendRefundNotifications(Refund refund) {
        Loan loan = refund.getLoan();

        String borrowerEmail = loan.getBorrower().getEmail();
        String borrowerName = loan.getBorrower().getFirstName();
        sendRefundRecordedMail(
                borrowerEmail,
                borrowerName,
                loan.getReference(),
                refund.getAmount()
        );

        String lenderEmail = loan.getLender().getEmail();
        String lenderName = loan.getLender().getFirstName();
        sendLenderRefundNotification(
                lenderEmail,
                lenderName,
                loan.getReference(),
                refund.getAmount()
        );
    }

    public void sendUpcomingRefundReminder(String borrowerEmail, String borrowerName, String dueDate, int amount) {
        String subject = "Échéance de remboursement à venir.";
        String body = "Bonjour " + borrowerName + ",\n\n" +
                "Un remboursement de " + amount + "€ est attendu pour la date du " + dueDate + ".\n\n" +
                "Merci de vous assurer que votre compte dispose des fonds nécessaires.\n" +
                "L’équipe FEP.";
        mailService.send(borrowerEmail, subject, body);
    }

    public void sendLateRefundWarning(String borrowerEmail, String borrowerName, String dueDate, int amount, int daysLate) {
        String subject = "Remboursement en retard";
        String body = "Bonjour " + borrowerName + ",\n\n" +
                "Votre remboursement de " + amount + "€, attendu le " + dueDate + ", a " + daysLate + " jours de retard.\n" +
                "Merci de régulariser votre situation rapidement pour éviter des pénalités.\n\n" +
                "L’équipe FEP.";
        mailService.send(borrowerEmail, subject, body);
    }

    private void sendRefundRecordedMail(String borrowerEmail, String borrowerName, String reference, double amount) {
        String subject = "Remboursement enregistré.";
        String body = "Bonjour " + borrowerName + ",\n\n" +
                "Votre remboursement de " + amount + "€ pour le prêt (" + reference + ") a bien été enregistré.\n\n" +
                "Merci de votre ponctualité,\n" +
                "L’équipe FEP.";
        mailService.send(borrowerEmail, subject, body);
    }

    private void sendLenderRefundNotification(String lenderEmail, String lenderName, String reference, double amount) {
        String subject = "Remboursement reçu.";
        String body = "Bonjour " + lenderName + ",\n\n" +
                "Vous avez reçu un remboursement de " + amount + "€ pour le prêt (" + reference + ").\n\n" +
                "L’équipe FEP.";
        mailService.send(lenderEmail, subject, body);
    }
}

