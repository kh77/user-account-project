package com.sm.accountstatement.repository;

import com.sm.accountstatement.entity.AccountStatement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountStatementRepository extends JpaRepository<AccountStatement,Long> {

    List<AccountStatement> findByUser(String userId);

    List<AccountStatement> findByAccountId(Long accountId);
}

