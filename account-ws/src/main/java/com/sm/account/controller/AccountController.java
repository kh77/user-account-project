package com.sm.account.controller;


import com.sm.account.service.AccountService;
import com.sm.common.accountstatement.AccountStatementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/statement")
    public List<AccountStatementDto> findAllAccount(@RequestParam(required = false) String accountId, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String startAmount, @RequestParam(required = false) String endAmount) {

        return service.findAllAccountAndStatement(accountId, fromDate, toDate, startAmount, endAmount);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{userId}/statement")
    public List<AccountStatementDto> findAccount(@PathVariable String userId) {
        return service.findAccountAndStatementByUserId(userId);
    }
}
