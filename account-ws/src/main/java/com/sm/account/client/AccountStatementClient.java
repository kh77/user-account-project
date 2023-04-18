package com.sm.account.client;

import com.sm.common.accountstatement.AccountStatementDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient("account-statement-ws")
public interface AccountStatementClient {

    @GetMapping("/statement")
   // @CircuitBreaker(name = "account-statement-ws", fallbackMethod = "findAllAccountStatementByParamsFallback")
    Map<Long, List<AccountStatementDto>> findAllAccountStatementByParams(@RequestParam(required = false) Long accountId, @RequestParam(required = false) String fromDate, @RequestParam(required = false) String toDate, @RequestParam(required = false) String startAmount, @RequestParam(required = false) String endAmount);

    @GetMapping("/statement/{userId}")
 //   @CircuitBreaker(name = "account-statement-ws", fallbackMethod = "findAccountStatementByUserIdFallback")
    Map<Long, List<AccountStatementDto>> findAccountStatementByUserId(@PathVariable String userId);


//    default List<AccountStatementDto> findAllAccountStatementByParamsFallback(Long accountId, String fromDate, String toDate, String startAmount, String endAmount, Throwable exception) {
//        System.out.println("Exception class=" + exception.getClass().getName());
//        System.out.println("Exception took place: " + exception.getMessage());
//        return new ArrayList<>();
//    }
//
//    default List<AccountStatementDto> findAccountStatementByUserIdFallback(String userId, Throwable exception) {
//        System.out.println("Exception class=" + exception.getClass().getName());
//        System.out.println("Exception took place: " + exception.getMessage());
//        return new ArrayList<>();
//    }
}
