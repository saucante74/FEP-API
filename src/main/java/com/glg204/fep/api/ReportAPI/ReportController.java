package com.glg204.fep.api.ReportAPI;

import com.glg204.fep.application.ReportApplication.ReportRequestDTO;
import com.glg204.fep.application.ReportApplication.ReportResponseDTO;
import com.glg204.fep.application.ReportApplication.ReportService;
import com.glg204.fep.domain.UserDomain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponseDTO> createReport(
            @Valid
            @RequestBody ReportRequestDTO dto,
            Authentication authentication
    ) {
        User reporter = (User) authentication.getPrincipal();
        return ResponseEntity.ok(reportService.createReport(dto, reporter));
    }

    @GetMapping
    public ResponseEntity<List<ReportResponseDTO>> getAllReports() {
        return ResponseEntity.ok(reportService.getAllReports());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> getReportById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReportResponseDTO>> getReportsByUser() {
        return ResponseEntity.ok(reportService.getReportsByReporter());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReportResponseDTO> updateReport(
            @Valid
            @PathVariable Long id,
            @RequestBody ReportRequestDTO dto
    ) {
        return ResponseEntity.ok(reportService.updateReport(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReport(@PathVariable Long id) {
        reportService.deleteReport(id);
        return ResponseEntity.noContent().build();
    }
}
