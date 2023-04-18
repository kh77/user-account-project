package com.sm.accountstatement.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "statement")
public class AccountStatement {

    @Id
    private Long id;
    private Long accountId;
    private String datefield;
    private String amount;
    private String user;
}
