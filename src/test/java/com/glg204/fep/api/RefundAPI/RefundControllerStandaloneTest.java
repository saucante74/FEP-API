package com.glg204.fep.api.RefundAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glg204.fep.application.RefundApplication.RefundRequestDTO;
import com.glg204.fep.application.RefundApplication.RefundResponseDTO;
import com.glg204.fep.application.RefundApplication.RefundService;
import com.glg204.fep.domain.RefundDomain.RefundStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Unit Tests
class RefundControllerStandaloneTest {

    private MockMvc mockMvc;
    private RefundService refundService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        refundService = Mockito.mock(RefundService.class);
        objectMapper = new ObjectMapper();

        RefundController refundController = new RefundController(refundService);
        mockMvc = MockMvcBuilders.standaloneSetup(refundController).build();
    }

    private RefundRequestDTO getSampleRefundRequest() {
        RefundRequestDTO dto = new RefundRequestDTO();
        dto.setLoanId(1L);
        dto.setAmount(250.00);
        return dto;
    }

    private RefundResponseDTO getSampleRefundResponse() {
        return RefundResponseDTO.builder()
                .id(1L)
                .loanReference("LN123456")
                .refundDate(LocalDateTime.of(2025, 9, 1, 10, 0))
                .amount(250.00)
                .status(RefundStatus.APPROVED)
                .build();
    }

    @Test
    void testCreateRefund() throws Exception {
        RefundRequestDTO requestDTO = getSampleRefundRequest();
        RefundResponseDTO responseDTO = getSampleRefundResponse();

        Mockito.when(refundService.createRefund(any(RefundRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/refunds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanReference").value("LN123456"));
    }

    @Test
    void testGetAllRefunds() throws Exception {
        List<RefundResponseDTO> refunds = List.of(getSampleRefundResponse(), getSampleRefundResponse());
        Mockito.when(refundService.getAllRefunds()).thenReturn(refunds);

        mockMvc.perform(get("/api/refunds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanReference").value("LN123456"));
    }

    @Test
    void testGetRefundById() throws Exception {
        RefundResponseDTO responseDTO = getSampleRefundResponse();
        Mockito.when(refundService.getRefundById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/refunds/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanReference").value("LN123456"));
    }

    @Test
    void testGetRefundsByLoan() throws Exception {
        List<RefundResponseDTO> refunds = List.of(getSampleRefundResponse());
        Mockito.when(refundService.getRefundsByLoan(1L)).thenReturn(refunds);

        mockMvc.perform(get("/api/refunds/loan/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanReference").value("LN123456"));
    }

    @Test
    void testGetRefundsByUser() throws Exception {
        List<RefundResponseDTO> refunds = List.of(getSampleRefundResponse());
        Mockito.when(refundService.getRefundsForCurrentUser()).thenReturn(refunds);

        mockMvc.perform(get("/api/refunds/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].loanReference").value("LN123456"));
    }

    @Test
    void testUpdateRefund() throws Exception {
        RefundRequestDTO requestDTO = getSampleRefundRequest();
        RefundResponseDTO responseDTO = getSampleRefundResponse();

        Mockito.when(refundService.updateRefund(eq(1L), any(RefundRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/refunds/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.loanReference").value("LN123456"));
    }

    @Test
    void testDeleteRefund() throws Exception {
        mockMvc.perform(delete("/api/refunds/1"))
                .andExpect(status().isNoContent());
    }
}
