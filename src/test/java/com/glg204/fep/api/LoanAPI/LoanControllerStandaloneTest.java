package com.glg204.fep.api.LoanAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.glg204.fep.application.LoanApplication.LoanRequestDTO;
import com.glg204.fep.application.LoanApplication.LoanResponseDTO;
import com.glg204.fep.application.LoanApplication.LoanService;
import com.glg204.fep.domain.LoanDomain.LoanStatus;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class LoanControllerStandaloneTest {

    private MockMvc mockMvc;
    private LoanService loanService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        loanService = Mockito.mock(LoanService.class);
        objectMapper = new ObjectMapper();

        LoanController loanController = new LoanController(loanService);
        mockMvc = MockMvcBuilders.standaloneSetup(loanController).build();
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

    private LoanRequestDTO getSampleLoanRequest() {
        return LoanRequestDTO.builder()
                .amount(new BigDecimal("5000.00"))
                .interestRate(5.5)
                .durationInMonths(12)
                .status(LoanStatus.PENDING)
                .borrowerId(2L)
                .build();
    }

    private LoanResponseDTO getSampleLoanResponse() {
        return LoanResponseDTO.builder()
                .id(1L)
                .reference("LN123456")
                .amount(new BigDecimal("5000.00"))
                .interestRate(5.5)
                .durationInMonths(12)
                .status("PENDING")
                .lender(null)
                .borrower(null)
                .build();
    }

    @Test
    void testCreateLoan() throws Exception {
        LoanRequestDTO requestDTO = getSampleLoanRequest();
        LoanResponseDTO responseDTO = getSampleLoanResponse();

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(getSampleUser());
        Mockito.when(loanService.createLoan(any(LoanRequestDTO.class), any(User.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/loans")
                        .principal(authentication)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value("LN123456"));
    }

    @Test
    void testGetAllLoans() throws Exception {
        List<LoanResponseDTO> loans = List.of(getSampleLoanResponse(), getSampleLoanResponse());
        Mockito.when(loanService.getAllLoans()).thenReturn(loans);

        mockMvc.perform(get("/api/loans"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reference").value("LN123456"));
    }

    @Test
    void testGetLoanById() throws Exception {
        LoanResponseDTO responseDTO = getSampleLoanResponse();
        Mockito.when(loanService.getLoanById(1L)).thenReturn(responseDTO);

        mockMvc.perform(get("/api/loans/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value("LN123456"));
    }

    @Test
    void testGetLoansByUser() throws Exception {
        List<LoanResponseDTO> loans = List.of(getSampleLoanResponse());
        Mockito.when(loanService.getLoansByUser()).thenReturn(loans);

        mockMvc.perform(get("/api/loans/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reference").value("LN123456"));
    }

    @Test
    void testUpdateLoan() throws Exception {
        LoanRequestDTO requestDTO = getSampleLoanRequest();
        LoanResponseDTO responseDTO = getSampleLoanResponse();

        Mockito.when(loanService.updateLoan(eq(1L), any(LoanRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/loans/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reference").value("LN123456"));
    }

    @Test
    void testDeleteLoan() throws Exception {
        mockMvc.perform(delete("/api/loans/1"))
                .andExpect(status().isNoContent());
    }
}
