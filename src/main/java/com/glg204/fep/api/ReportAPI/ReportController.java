package com.glg204.fep.api.ReportAPI;

import com.glg204.fep.application.ReportApplication.ReportRequestDTO;
import com.glg204.fep.application.ReportApplication.ReportResponseDTO;
import com.glg204.fep.application.ReportApplication.ReportService;
import com.glg204.fep.domain.UserDomain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(@RequestBody ReportRequestDTO dto,
                                                          Authentication authentication) {
        User reporter = (User) authentication.getPrincipal();
        ReportResponseDTO response = reportService.createReport(dto, reporter);
        return ResponseEntity.ok(response);
    }
}
