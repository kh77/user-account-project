package com.sm.common.accountstatement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountStatementDto {

    private Long id;
    private Long accountId;
    private String accountType;
    private String accountNumber;
    private String datefield;
    private String amount;
    private String user;

    public AccountStatementDto() {
    }

    public AccountStatementDto(Long id, Long accountId, String datefield, String amount, String user) {
        this.id = id;
        this.accountId = accountId;
        this.datefield = datefield;
        this.amount = amount;
        this.user = user;

    }


}