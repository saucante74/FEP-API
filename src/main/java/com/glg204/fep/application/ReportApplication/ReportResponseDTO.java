package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.ReportDomain.ReportReason;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReportResponseDTO {
    private Long id;
    private String reason;
    private String reporterEmail;
    private String reportedUserEmail;
    private LocalDateTime reportDate;
    private ReportReason status;
}

