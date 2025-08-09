package com.glg204.fep.application.ReportApplication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDTO {
    private Long reportedUserId;
    private String reason;
}

