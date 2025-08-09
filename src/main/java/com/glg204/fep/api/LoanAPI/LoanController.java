package com.glg204.fep.api.LoanAPI;

import com.glg204.fep.application.LoanApplication.LoanRequestDTO;
import com.glg204.fep.application.LoanApplication.LoanResponseDTO;
import com.glg204.fep.application.LoanApplication.LoanService;
import com.glg204.fep.domain.UserDomain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanRequestDTO dto,
                                                      Authentication authentication) {
        User lender = (User) authentication.getPrincipal();
        return ResponseEntity.ok(loanService.createLoan(dto, lender));
    }

    @GetMapping
    public ResponseEntity<List<LoanResponseDTO>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getLoanById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanResponseDTO> updateLoan(@PathVariable Long id,
                                                      @RequestBody LoanRequestDTO dto) {
        return ResponseEntity.ok(loanService.updateLoan(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }
}
