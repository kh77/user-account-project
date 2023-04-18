package com.sm.accountstatement.util;

import com.sm.common.accountstatement.AccountStatementDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
 class DateUtilTest {

    @Test
     void testCompareByIdInReverseOrder() {
        AccountStatementDto dto1 = Mockito.mock(AccountStatementDto.class);
        Mockito.when(dto1.getId()).thenReturn(1L);
        AccountStatementDto dto2 = Mockito.mock(AccountStatementDto.class);
        Mockito.when(dto2.getId()).thenReturn(2L);
        AccountStatementDto dto3 = Mockito.mock(AccountStatementDto.class);
        Mockito.when(dto3.getId()).thenReturn(3L);

        List<AccountStatementDto> dtoList = new ArrayList<>();
        dtoList.add(dto3);
        dtoList.add(dto1);
        dtoList.add(dto2);


        Comparator<AccountStatementDto> comparator = DateUtil.compareByIdInReverseOrder();
        dtoList.sort(comparator);


        assertEquals(dto3.getId(), dtoList.get(0).getId());
        assertEquals(dto2.getId(), dtoList.get(1).getId());
        assertEquals(dto1.getId(), dtoList.get(2).getId());
    }

}
