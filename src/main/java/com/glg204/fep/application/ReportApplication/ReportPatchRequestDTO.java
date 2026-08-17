package com.glg204.fep.application.ReportApplication;

import com.glg204.fep.domain.ReportDomain.ReportReason;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportPatchRequestDTO {
    private ReportReason reason;
    private Boolean isOpen;
}
