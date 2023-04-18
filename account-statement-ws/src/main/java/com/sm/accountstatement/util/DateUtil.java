package com.sm.accountstatement.util;

import com.sm.common.accountstatement.AccountStatementDto;
import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

@UtilityClass
public class DateUtil {
    
    public static DateTimeFormatter formatterWithPeriod = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static Comparator<AccountStatementDto> compareByIdInReverseOrder() {
        return Comparator.comparingLong(AccountStatementDto::getId).reversed();
    }

}
