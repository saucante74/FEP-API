package com.glg204.fep.api.LoanAPI;

import com.glg204.fep.application.LoanApplication.LoanRequestDTO;
import com.glg204.fep.application.LoanApplication.LoanService;
import com.glg204.fep.domain.LoanDomain.Loan;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.infrastructure.UserInfrastructure.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody LoanRequestDTO dto,
                                        Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        System.out.println("LENDER = " + user.getEmail());

        Loan loan = loanService.createLoan(dto, user);
        return ResponseEntity.ok(loan);
    }

}
