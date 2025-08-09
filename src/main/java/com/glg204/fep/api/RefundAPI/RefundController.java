package com.glg204.fep.api.RefundAPI;

import com.glg204.fep.application.RefundApplication.RefundRequestDTO;
import com.glg204.fep.application.RefundApplication.RefundResponseDTO;
import com.glg204.fep.application.RefundApplication.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refunds")
@RequiredArgsConstructor
public class RefundController {

    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<?> createRefund(@RequestBody RefundRequestDTO dto) {
        RefundResponseDTO refund = refundService.createRefund(dto);
        return ResponseEntity.ok(refund);
    }
}

