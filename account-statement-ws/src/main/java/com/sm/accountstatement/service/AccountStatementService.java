package com.sm.accountstatement.service;

import com.sm.accountstatement.entity.AccountStatement;
import com.sm.accountstatement.repository.AccountStatementRepository;
import com.sm.accountstatement.util.DateUtil;

import com.sm.common.accountstatement.AccountStatementDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountStatementService {

    private final AccountStatementRepository accountStatementRepository;

    public Map<Long, List<AccountStatementDto>> findAllStatement(Long accountId, String fromDate, String toDate, String startAmount, String endAmount) {

        List<AccountStatement> listAccountStatement;
        if (accountId != null) {
            listAccountStatement = accountStatementRepository.findByAccountId(accountId);
        } else {
            listAccountStatement = accountStatementRepository.findAll();
        }

        Map<Long, List<AccountStatementDto>> accountStatementDtoList = new HashMap<>();

        if (listAccountStatement != null && listAccountStatement.size() > 0) {
            Comparator<AccountStatementDto> sortByIdDesc = DateUtil.compareByIdInReverseOrder();

            Predicate<AccountStatement> predicate = searchWithParameters(fromDate, toDate, startAmount, endAmount);
            if (predicate != null) {
                accountStatementDtoList = listAccountStatement.stream().filter(predicate).map(data -> new AccountStatementDto(data.getId(), data.getAccountId(), data.getDatefield(), data.getAmount(), data.getUser())).sorted(sortByIdDesc).sorted(sortByIdDesc).collect(Collectors.groupingBy(AccountStatementDto::getAccountId, Collectors.toList()));
            } else {
                accountStatementDtoList = getLastThreeMonthStatement(listAccountStatement);
            }
        }
        return accountStatementDtoList;
    }

    public Predicate<AccountStatement> searchWithParameters(String fromDate, String toDate, String startAmount, String endAmount) {
        List<Predicate<AccountStatement>> list = new ArrayList<>();
        if (StringUtils.isNotBlank(fromDate)) {
            list.add(findByStartDate(fromDate));
        }
        if (StringUtils.isNotBlank(toDate)) {
            list.add(findByEndDate(toDate));
        }
        if (StringUtils.isNotBlank(startAmount)) {
            list.add(findByStartAmount(startAmount));
        }
        if (StringUtils.isNotBlank(endAmount)) {
            list.add(findByEndAmount(endAmount));
        }
        if (!list.isEmpty()) {
            return list.stream().reduce(w -> true, Predicate::and);
        }

        return null;
    }

    public Predicate<AccountStatement> findByStartDate(String fromDate) {
        Predicate<AccountStatement> predicate = accountStatement -> (LocalDate.parse(accountStatement.getDatefield(), DateUtil.formatterWithPeriod).compareTo(LocalDate.parse(fromDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))) >= 0);
        return predicate;
    }

    public Predicate<AccountStatement> findByEndDate(String toDate) {
        Predicate<AccountStatement> predicate = accountStatement -> (LocalDate.parse(accountStatement.getDatefield(), DateUtil.formatterWithPeriod).compareTo(LocalDate.parse(toDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"))) <= 0);
        return predicate;
    }

    public Predicate<AccountStatement> findByStartAmount(String startAmount) {
        Predicate<AccountStatement> predicate = accountStatement -> Double.valueOf(accountStatement.getAmount()) >= Double.valueOf(startAmount);
        return predicate;
    }

    public Predicate<AccountStatement> findByEndAmount(String endAmount) {
        Predicate<AccountStatement> predicate = accountStatement -> Double.valueOf(accountStatement.getAmount()) <= Double.valueOf(endAmount);
        return predicate;
    }

    public Map<Long, List<AccountStatementDto>> findStatementByUserId(String userId) {

        List<AccountStatement> listAccountStatement = accountStatementRepository.findByUser(userId);
        Map<Long, List<AccountStatementDto>> accountStatementDtoList = new HashMap<>();

        if (listAccountStatement != null && !listAccountStatement.isEmpty()) {
            accountStatementDtoList = getLastThreeMonthStatement(listAccountStatement);

        }
        return accountStatementDtoList;
    }

    private Map<Long, List<AccountStatementDto>> getLastThreeMonthStatement(List<AccountStatement> listAccountStatement) {
        Map<Long, List<AccountStatementDto>> accountStatementDtoList;
        Comparator<AccountStatementDto> sortByIdDesc = DateUtil.compareByIdInReverseOrder();
        LocalDate startDateFrom = LocalDate.now().minusMonths(3);
        LocalDate endDateToday = LocalDate.now();

        accountStatementDtoList = listAccountStatement.stream().filter(accountStatement -> (LocalDate.parse(accountStatement.getDatefield(), DateUtil.formatterWithPeriod).compareTo(startDateFrom) >= 0) && (LocalDate.parse(accountStatement.getDatefield(), DateUtil.formatterWithPeriod).compareTo(endDateToday) <= 0)).map(data -> new AccountStatementDto(data.getId(), data.getAccountId(), data.getDatefield(), data.getAmount(), data.getUser())).sorted(sortByIdDesc).collect(Collectors.groupingBy(AccountStatementDto::getAccountId, Collectors.toList()));
        return accountStatementDtoList;
    }
}
