package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.ReportDomain.ReportReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDTO {
    private Long reportedUserId;

    @NotNull(message = "Reason is required")
    private ReportReason reason;
}

