package com.glg204.fep.api.RefundAPI;

import com.glg204.fep.application.RefundApplication.RefundRequestDTO;
import com.glg204.fep.application.RefundApplication.RefundResponseDTO;
import com.glg204.fep.application.RefundApplication.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<RefundResponseDTO> createRefund(@Valid @RequestBody RefundRequestDTO dto) {
        return ResponseEntity.ok(refundService.createRefund(dto));
    }

    @GetMapping
    public ResponseEntity<List<RefundResponseDTO>> getAllRefunds() {
        return ResponseEntity.ok(refundService.getAllRefunds());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RefundResponseDTO> getRefundById(@PathVariable Long id) {
        return ResponseEntity.ok(refundService.getRefundById(id));
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByLoan(@PathVariable Long loanId) {
        return ResponseEntity.ok(refundService.getRefundsByLoan(loanId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<RefundResponseDTO>> getRefundsByUser() {
        return ResponseEntity.ok(refundService.getRefundsForCurrentUser());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RefundResponseDTO> updateRefund(
            @Valid
            @PathVariable Long id,
            @RequestBody RefundRequestDTO dto
    ) {
        return ResponseEntity.ok(refundService.updateRefund(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRefund(@PathVariable Long id) {
        refundService.deleteRefund(id);
        return ResponseEntity.noContent().build();
    }

}
