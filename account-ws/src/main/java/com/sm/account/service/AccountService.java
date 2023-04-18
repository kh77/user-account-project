package com.sm.account.service;

import com.sm.account.client.AccountStatementClient;
import com.sm.account.entity.Account;
import com.sm.account.repository.AccountRepository;
import com.sm.common.accountstatement.AccountStatementDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository;
    private final AccountStatementClient accountStatementClient;

    public List<AccountStatementDto> findAllAccountAndStatement(String accountId, String fromDate, String toDate, String startAmount, String endAmount) {
        Long accountIdVal = StringUtils.isBlank(accountId) ? null : Long.valueOf(accountId);
        fromDate = StringUtils.isBlank(fromDate) ? null : fromDate;
        endAmount =  endAmount.equalsIgnoreCase("null") ? null : endAmount;
        startAmount = startAmount.equalsIgnoreCase("null") ? null : startAmount;
        toDate = StringUtils.isBlank(toDate) ? null : toDate;
        Map<Long, List<AccountStatementDto>> mapStatement = accountStatementClient.findAllAccountStatementByParams(accountIdVal, fromDate, toDate, startAmount, endAmount);
        return getUserAccountListStatementDto(mapStatement);
    }

    public List<AccountStatementDto> findAccountAndStatementByUserId(String userId) {
        Map<Long, List<AccountStatementDto>> mapStatement = accountStatementClient.findAccountStatementByUserId(userId);
        return getUserAccountListStatementDto(mapStatement);
    }

    public List<AccountStatementDto> getUserAccountListStatementDto(Map<Long, List<AccountStatementDto>> mapStatement) {
        List<AccountStatementDto> list = new ArrayList<>();
        Map<Long, Account> map = new HashMap<>();
        if (mapStatement != null && !mapStatement.isEmpty()) {

            mapStatement.entrySet().stream().forEach((key) -> {
                Account account;
                if (map.get(key.getKey()) == null) {
                    account = repository.findById(key.getKey()).get();
                    map.put(key.getKey(), account);
                } else {
                    account = map.get(key.getKey());
                }

                // hash account.setAccountNumber();
                list.addAll(key.getValue().stream().map(data -> {
                    AccountStatementDto accountStatementDto = new AccountStatementDto();
                    accountStatementDto.setAccountId(account.getId());
                    accountStatementDto.setAccountNumber(DigestUtils.sha512Hex(account.getAccountNumber()));
                    accountStatementDto.setAccountType(account.getAccountType());
                    accountStatementDto.setId(data.getId());
                    accountStatementDto.setDatefield(data.getDatefield());
                    accountStatementDto.setAmount(data.getAmount());
                    accountStatementDto.setUser(data.getUser());

                    return accountStatementDto;
                }).collect(Collectors.toList()));

            });

            if (!list.isEmpty()) {
                list.sort(Comparator.comparingLong(AccountStatementDto::getId).reversed());
            }
        }
        return list;
    }
}
