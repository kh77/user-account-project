package com.sm.accountstatement.service;

import com.sm.accountstatement.entity.AccountStatement;
import com.sm.accountstatement.repository.AccountStatementRepository;
import com.sm.common.accountstatement.AccountStatementDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class AccountStatementServiceTest {

    @Mock
    private AccountStatementRepository accountStatementRepository;

    @InjectMocks
    private AccountStatementService accountStatementService;


    @Test
     void testFindAllStatementWhenAccountIdIsNull() {
        String user = "user1";
        List<AccountStatement> mockAccountStatements = new ArrayList<>();
        AccountStatement statement1 = new AccountStatement(1L, 1L, "15.04.2023", "100.00", user);
        AccountStatement statement2 = new AccountStatement(2L, 1L, "15.04.2023", "200.00", user);
        mockAccountStatements.add(statement1);
        mockAccountStatements.add(statement2);
        when(accountStatementRepository.findAll()).thenReturn(mockAccountStatements);

        Long accountId = null;
        String fromDate = "2023-04-15";
        String toDate = "2023-04-15";
        String startAmount = "50.00";
        String endAmount = "250.00";

        Map<Long, List<AccountStatementDto>> expected = new HashMap<>();
        List<AccountStatementDto> statementDtos = new ArrayList<>();
        statementDtos.add(new AccountStatementDto(2L, 1L, "15.04.2023", "200.00", user));
        statementDtos.add(new AccountStatementDto(1L, 1L, "15.04.2023", "100.00", user));
        expected.put(1L, statementDtos);

        Map<Long, List<AccountStatementDto>> result = accountStatementService.findAllStatement(accountId, fromDate, toDate, startAmount, endAmount);
        assertEquals(expected.size(), result.size());
        verify(accountStatementRepository, times(1)).findAll();
    }

    @Test
     void testFindAllStatementWhenAccountIdIsExist() {
        String user = "user1";
        List<AccountStatement> mockAccountStatements = new ArrayList<>();
        AccountStatement statement1 = new AccountStatement(1L, 1L, "15.11.2020", "100.00", user);
        AccountStatement statement2 = new AccountStatement(2L, 1L, "15.11.2020", "200.00", user);
        mockAccountStatements.add(statement1);
        mockAccountStatements.add(statement2);
        when(accountStatementRepository.findByAccountId(1L)).thenReturn(mockAccountStatements);

        Long accountId = 1L;
        String fromDate = "2020-11-15";
        String toDate = "2022-11-15";
        String startAmount = "50.00";
        String endAmount = "250.00";


        Map<Long, List<AccountStatementDto>> expected = new HashMap<>();
        List<AccountStatementDto> statementDtos = new ArrayList<>();
        statementDtos.add(new AccountStatementDto(2L, 1L, "15.11.2020", "200.00", user));
        statementDtos.add(new AccountStatementDto(1L, 1L, "15.11.2021", "100.00", user));
        expected.put(1L, statementDtos);

        Map<Long, List<AccountStatementDto>> result = accountStatementService.findAllStatement(accountId, fromDate, toDate, startAmount, endAmount);
        assertEquals(expected.size(), result.size());
        verify(accountStatementRepository, times(1)).findByAccountId(1L);
    }

    @Test
     void testFindStatementByUserIdWithSameAccount() {
        testFindStatementByUserId(1L, 1L);
    }

    @Test
     void testFindStatementByUserIdWithDifferentAccount() {
        testFindStatementByUserId(1L, 1L);
    }

    @Test
     void testFindStatementByUserIdWhenUserIsNotExist() {
        when(accountStatementRepository.findByUser("user1")).thenReturn(null);
        Map<Long, List<AccountStatementDto>> result = accountStatementService.findStatementByUserId("user1");
        assertEquals(0, result.size());
        verify(accountStatementRepository, times(1)).findByUser("user1");
    }


     void testFindStatementByUserId(Long firstAccountId, Long secondAccountId) {
        String user = "user1";
        List<AccountStatement> mockAccountStatements = new ArrayList<>();
        AccountStatement statement1 = new AccountStatement(1L, firstAccountId, "15.03.2023", "100.00", user);
        AccountStatement statement2 = new AccountStatement(2L, secondAccountId, "16.03.2023", "200.00", user);
        mockAccountStatements.add(statement1);
        mockAccountStatements.add(statement2);
        when(accountStatementRepository.findByUser(user)).thenReturn(mockAccountStatements);

        Long userId = 1L;
        Map<Long, List<AccountStatementDto>> expected = new HashMap<>();
        List<AccountStatementDto> statementDtos = new ArrayList<>();
        statementDtos.add(new AccountStatementDto(2L, firstAccountId, "15.03.2023", "200.00", user));
        statementDtos.add(new AccountStatementDto(1L, secondAccountId, "16.03.2023", "100.00", user));
        expected.put(1L, statementDtos);

        Map<Long, List<AccountStatementDto>> result = accountStatementService.findStatementByUserId(user);
        assertEquals(expected.size(), result.size());
        verify(accountStatementRepository, times(1)).findByUser(user);
    }
}