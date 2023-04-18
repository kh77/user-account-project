package com.sm.accountstatement.controller;


import com.sm.accountstatement.service.AccountStatementService;
import com.sm.common.accountstatement.AccountStatementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class AccountStatementController {

    private final AccountStatementService accountStatementService;

    @GetMapping
    public Map<Long, List<AccountStatementDto>> findAllAccountStatementByParams(@RequestParam(required = false) Long accountId, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String startAmount, @RequestParam(required = false) String endAmount) {
        return accountStatementService.findAllStatement(accountId,fromDate, toDate, startAmount, endAmount);
    }

    @GetMapping("/{userId}")
    public Map<Long, List<AccountStatementDto>> findAccountStatementByUserId(@PathVariable String userId) {
        return accountStatementService.findStatementByUserId(userId);
    }
}
