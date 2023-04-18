package com.sm.accountstatement.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sm.accountstatement.service.AccountStatementService;
import com.sm.common.accountstatement.AccountStatementDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.config.client.ConfigClientAutoConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc()
@EnableAutoConfiguration(exclude = ConfigClientAutoConfiguration.class)
 class AccountStatementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountStatementService accountStatementService;

    @Test
     void shouldReturnAllAccountStatementsByParams() throws Exception {
        // given
        Map<Long, List<AccountStatementDto>> mockResult = Collections.singletonMap(1L, Collections.singletonList(new AccountStatementDto()));
        when(accountStatementService.findAllStatement(anyLong(), anyString(), anyString(), anyString(), anyString())).thenReturn(mockResult);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/statement").param("accountId", "1").param("fromDate", "2020-01-01").param("toDate", "2020-12-31").param("startAmount", "100").param("endAmount", "1000")).andExpect(status().isOk()).andExpect(jsonPath("$.*.length()").value(1)).andReturn();

        // then
        String responseJson = result.getResponse().getContentAsString();
        Map<Long, List<AccountStatementDto>> response = new ObjectMapper().readValue(responseJson, new TypeReference<>() {
        });
        assertNotNull(response);
    }

    @Test
     void shouldReturnEmptyAllAccountStatementsByParams() throws Exception {
        // given
        when(accountStatementService.findAllStatement(anyLong(), anyString(), anyString(), anyString(), anyString())).thenReturn(new HashMap<Long, List<AccountStatementDto>>());

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/statement").param("accountId", "1").param("fromDate", "2020-01-01").param("toDate", "2020-12-31").param("startAmount", "100").param("endAmount", "1000")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0)).andReturn();

        // then
        String responseJson = result.getResponse().getContentAsString();
        Map<Long, List<AccountStatementDto>> response = new ObjectMapper().readValue(responseJson, new TypeReference<>() {});
        assertNotNull(response);
    }

    @Test
     void shouldReturnAccountStatementsByUserId() throws Exception {
        // given
        Map<Long, List<AccountStatementDto>> mockResult = Collections.singletonMap(1L, Collections.singletonList(new AccountStatementDto()));
        when(accountStatementService.findStatementByUserId("testUser")).thenReturn(mockResult);

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/statement/testUser")).andExpect(status().isOk()).andExpect(jsonPath("$.*.length()").value(1)).andReturn();

        // then
        String responseJson = result.getResponse().getContentAsString();
        Map<Long, List<AccountStatementDto>> response = new ObjectMapper().readValue(responseJson, new TypeReference<Map<Long, List<AccountStatementDto>>>() {
        });
        assertNotNull(response);
    }

    @Test
     void shouldReturnEmptyAccountStatementsByUserId() throws Exception {
        // given
        when(accountStatementService.findStatementByUserId("testUser")).thenReturn(new HashMap<Long, List<AccountStatementDto>>());

        // when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/statement/testUser")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(0)).andReturn();

        // then
        String responseJson = result.getResponse().getContentAsString();
        Map<Long, List<AccountStatementDto>> response = new ObjectMapper().readValue(responseJson, new TypeReference<>() {
        });
        assertNotNull(response);
    }
}
