package com.sm.account.service;


import com.sm.account.client.AccountStatementClient;
import com.sm.account.entity.Account;
import com.sm.account.repository.AccountRepository;
import com.sm.common.accountstatement.AccountStatementDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
 class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountStatementClient accountStatementClient;

    @Test
     void testFindAllAccountAndStatement() {
        Long accountId = 1L;
        String fromDate = "2022-01-01";
        String toDate = "2022-12-31";
        String startAmount = "100";
        String endAmount = "1000";

        Map<Long, List<AccountStatementDto>> mapStatement = new HashMap<>();
        List<AccountStatementDto> accountStatementDtoList = new ArrayList<>();
        AccountStatementDto accountStatementDto = new AccountStatementDto();
        accountStatementDto.setAccountId(accountId);
        accountStatementDto.setAmount("500");
        accountStatementDtoList.add(accountStatementDto);
        mapStatement.put(accountId, accountStatementDtoList);
        when(accountStatementClient.findAllAccountStatementByParams(accountId, fromDate, toDate, startAmount, endAmount)).thenReturn(mapStatement);

        // Mock AccountRepository response
        Account account = new Account();
        account.setId(accountId);
        account.setAccountNumber("123456789");
        when(accountRepository.findById(accountId)).thenReturn(java.util.Optional.of(account));

        List<AccountStatementDto> result = accountService.findAllAccountAndStatement(accountId.toString(), fromDate, toDate, startAmount, endAmount);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAccountId());
        assertEquals("500", result.get(0).getAmount());

    }

    @Test
     void testFindAccountAndStatementByUserId() {
        // Mock input parameters
        String userId = "1L";
        Long accountId = 1L;

        // Mock AccountStatementClient response
        Map<Long, List<AccountStatementDto>> mapStatement = new HashMap<>();
        List<AccountStatementDto> accountStatementDtoList = new ArrayList<>();
        AccountStatementDto accountStatementDto = new AccountStatementDto();
        accountStatementDto.setAccountId(accountId);
        accountStatementDto.setAmount("500");
        accountStatementDtoList.add(accountStatementDto);
        mapStatement.put(1L, accountStatementDtoList);
        when(accountStatementClient.findAccountStatementByUserId(userId)).thenReturn(mapStatement);

        // Mock AccountRepository response
        Account account = new Account();
        account.setId(accountId);
        account.setAccountNumber("123456789");
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));


        List<AccountStatementDto> result = accountService.findAccountAndStatementByUserId(userId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAccountId());
        assertEquals("500", result.get(0).getAmount());

    }

    @Test
     void testFindAccountAndStatementByUserIdWhenStatementIsNull() {
        Map<Long, List<AccountStatementDto>> mapStatement = new HashMap<>();
        List<AccountStatementDto> accountStatementDtoList = new ArrayList<>();
        AccountStatementDto accountStatementDto = new AccountStatementDto();
        accountStatementDto.setAccountId(2L);
        accountStatementDto.setAmount("500");
        accountStatementDtoList.add(accountStatementDto);
        mapStatement.put(1L, accountStatementDtoList);
        when(accountStatementClient.findAccountStatementByUserId("1L")).thenReturn(null);
        assertNotNull(accountService.findAccountAndStatementByUserId("1L"));

    }
}

