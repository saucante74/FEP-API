package com.glg204.fep.api.ReportAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glg204.fep.application.ReportApplication.ReportRequestDTO;
import com.glg204.fep.application.ReportApplication.ReportResponseDTO;
import com.glg204.fep.application.ReportApplication.ReportService;
import com.glg204.fep.domain.ReportDomain.ReportReason;
import com.glg204.fep.domain.UserDomain.User;
import com.glg204.fep.domain.UserDomain.UserRole;
import com.glg204.fep.domain.UserDomain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReportControllerStandaloneTest {

    private MockMvc mockMvc;
    private ReportService reportService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        reportService = Mockito.mock(ReportService.class);
        objectMapper = new ObjectMapper();

        ReportController reportController = new ReportController(reportService);
        mockMvc = MockMvcBuilders.standaloneSetup(reportController).build();
    }

    private User getSampleUser() {
        return User.builder()
                .id(1L)
                .username("jdoe")
                .email("jdoe@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .role(UserRole.USER)
                .status(UserStatus.VALIDATED)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private ReportRequestDTO getSampleReportRequest() {
        ReportRequestDTO dto = new ReportRequestDTO();
        dto.setReportedUserId(2L);
        dto.setReason(ReportReason.FRAUD);
        return dto;
    }

    private ReportResponseDTO getSampleReportResponse() {
        return ReportResponseDTO.builder()
                .id(1L)
                .reason(ReportReason.FRAUD)
                .reporterEmail("jdoe@example.com")
                .reportedUserEmail("asmith@example.com")
                .reportDate(LocalDateTime.of(2025, 9, 1, 10, 0))
                .open(true)
                .build();
    }

    @Test
    void testCreateReport() throws Exception {
        ReportRequestDTO requestDTO = getSampleReportRequest();
        ReportResponseDTO responseDTO = getSampleReportResponse();

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(getSampleUser());
        Mockito.when(reportService.createReport(any(ReportRequestDTO.class), any(User.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/reports")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reporterEmail").value("jdoe@example.com"));
    }

    @Test
    void testGetAllReports() throws Exception {
        List<ReportResponseDTO> reports = List.of(getSampleReportResponse(), getSampleReportResponse());
        Mockito.when(reportService.getAllReports()).thenReturn(reports);

        mockMvc.perform(get("/api/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reporterEmail").value("jdoe@example.com"));
    }

    @Test
    void testGetReportById() throws Exception {
        ReportResponseDTO responseDTO = getSampleReportResponse();
        Mockito.when(reportService.getReportById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/reports/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reporterEmail").value("jdoe@example.com"));
    }

    @Test
    void testGetReportsByUser() throws Exception {
        List<ReportResponseDTO> reports = List.of(getSampleReportResponse());
        Mockito.when(reportService.getReportsByReporter()).thenReturn(reports);

        mockMvc.perform(get("/api/reports/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reporterEmail").value("jdoe@example.com"));
    }

    @Test
    void testUpdateReport() throws Exception {
        ReportRequestDTO requestDTO = getSampleReportRequest();
        ReportResponseDTO responseDTO = getSampleReportResponse();

        Mockito.when(reportService.updateReport(eq(1L), any(ReportRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/reports/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reporterEmail").value("jdoe@example.com"));
    }

    @Test
    void testDeleteReport() throws Exception {
        mockMvc.perform(delete("/api/reports/1"))
                .andExpect(status().isNoContent());
    }
}
