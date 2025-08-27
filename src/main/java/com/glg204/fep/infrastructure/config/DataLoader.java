package com.glg204.fep.infrastructure.config;

import com.glg204.fep.domain.UserDomain.*;
import com.glg204.fep.domain.LoanDomain.*;
import com.glg204.fep.domain.RefundDomain.*;
import com.glg204.fep.domain.ReportDomain.*;
import com.glg204.fep.infrastructure.LoanInfrastructure.LoanRepository;
import com.glg204.fep.infrastructure.RefundInfrastructure.RefundRepository;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import com.glg204.fep.infrastructure.ReportInfrastructure.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Component
@Profile({"local", "docker"})
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final RefundRepository refundRepository;
    private final ReportRepository reportRepository;
    private final PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("⚠️ Fixtures already exists ⚠️");
            return;
        }

        User alice = userRepository.save(User.builder()
                .firstName("Alice").lastName("Dupont")
                .username("alice47")
                .email("alice@example.com").password(passwordEncoder.encode("secret"))
                .role(UserRole.LENDER).status(UserStatus.PENDING_VALIDATION)
                .createdAt(LocalDateTime.now().minusMonths(6)).build());

        User bob = userRepository.save(User.builder()
                .firstName("Bob").lastName("Martin")
                .username("Bob78")
                .email("bob@example.com").password(passwordEncoder.encode("secret"))
                .role(UserRole.BORROWER).status(UserStatus.PENDING_VALIDATION)
                .createdAt(LocalDateTime.now().minusMonths(3)).build());

        User claire = userRepository.save(User.builder()
                .firstName("Claire").lastName("Durand")
                .username("Claire14")
                .email("claire@example.com").password(passwordEncoder.encode("secret"))
                .role(UserRole.BORROWER).status(UserStatus.PENDING_VALIDATION)
                .createdAt(LocalDateTime.now().minusMonths(2)).build());

        User david = userRepository.save(User.builder()
                .firstName("David").lastName("Moreau")
                .username("David85")
                .email("david@example.com").password(passwordEncoder.encode("secret"))
                .role(UserRole.LENDER).status(UserStatus.PENDING_VALIDATION)
                .createdAt(LocalDateTime.now().minusMonths(1)).build());

        for (int i = 1; i <= 15; i++) {
            User lender = (i % 2 == 0) ? alice : david;
            User borrower = (i % 3 == 0) ? bob : claire;

            Loan loan = loanRepository.save(Loan.builder()
                    .reference("LN-20250825-092347282-" + String.format("%04d", i))
                    .amount(BigDecimal.valueOf(2000 + random.nextInt(20000)))
                    .interestRate(3.0 + random.nextDouble() * 5)
                    .durationInMonths(6 + random.nextInt(36))
                    .startDate(LocalDate.now().minusMonths(random.nextInt(12)))
                    .status((i % 5 == 0) ? LoanStatus.PENDING : LoanStatus.IN_PROGRESS)
                    .lender(lender)
                    .borrower(borrower)
                    .build());

            int refundsCount = 3 + random.nextInt(5);
            for (int r = 1; r <= refundsCount; r++) {
                refundRepository.save(Refund.builder()
                        .loan(loan)
                        .refundDate(LocalDate.now().plusDays(r * 15).atStartOfDay())
                        .amount(200 + random.nextInt(2000))
                        .status(randomRefundStatus())
                        .build());
            }
        }

        reportRepository.saveAll(List.of(
                Report.builder()
                        .reason(ReportReason.FRAUD)
                        .reporter(alice)
                        .reportedUser(bob)
                        .reportDate(LocalDateTime.now().minusDays(10))
                        .isOpen(true)
                        .build(),

                Report.builder()
                        .reason(ReportReason.SPAM)
                        .reporter(claire)
                        .reportedUser(alice)
                        .reportDate(LocalDateTime.now().minusDays(8))
                        .isOpen(true)
                        .build(),

                Report.builder()
                        .reason(ReportReason.ABUSE)
                        .reporter(bob)
                        .reportedUser(david)
                        .reportDate(LocalDateTime.now().minusDays(7))
                        .isOpen(false)
                        .build(),

                Report.builder()
                        .reason(ReportReason.FRAUD)
                        .reporter(david)
                        .reportedUser(claire)
                        .reportDate(LocalDateTime.now().minusDays(6))
                        .isOpen(true)
                        .build(),

                Report.builder()
                        .reason(ReportReason.SPAM)
                        .reporter(alice)
                        .reportedUser(claire)
                        .reportDate(LocalDateTime.now().minusDays(5))
                        .isOpen(true)
                        .build(),

                Report.builder()
                        .reason(ReportReason.ABUSE)
                        .reporter(bob)
                        .reportedUser(alice)
                        .reportDate(LocalDateTime.now().minusDays(4))
                        .isOpen(false)
                        .build(),

                Report.builder()
                        .reason(ReportReason.FRAUD)
                        .reporter(claire)
                        .reportedUser(bob)
                        .reportDate(LocalDateTime.now().minusDays(3))
                        .isOpen(true)
                        .build(),

                Report.builder()
                        .reason(ReportReason.SPAM)
                        .reporter(david)
                        .reportedUser(alice)
                        .reportDate(LocalDateTime.now().minusDays(2))
                        .isOpen(false)
                        .build(),

                Report.builder()
                        .reason(ReportReason.ABUSE)
                        .reporter(alice)
                        .reportedUser(david)
                        .reportDate(LocalDateTime.now().minusDays(1))
                        .isOpen(true)
                        .build()
        ));


        System.out.println("✅ Fixtures OK ✅");
    }

    private RefundStatus randomRefundStatus() {
        RefundStatus[] values = RefundStatus.values();
        return values[random.nextInt(values.length)];
    }
}
