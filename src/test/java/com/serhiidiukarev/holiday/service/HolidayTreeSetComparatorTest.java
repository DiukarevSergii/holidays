package com.serhiidiukarev.holiday.service;

import com.serhiidiukarev.holiday.Holiday;
import com.serhiidiukarev.holiday.utils.HolidayTreeSetComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class HolidayTreeSetComparatorTest {

    private static Holiday holiday1;
    private static Holiday holiday2;

    @BeforeEach
    public void init() {
        holiday1 = Holiday.builder().holidayId(1L).holidayCategory(Holiday.HolidayCategory.GOVERNMENT).holidayName("B").build();
        holiday2 = Holiday.builder().holidayId(1L).holidayCategory(Holiday.HolidayCategory.GOVERNMENT).holidayName("B").build();
    }

    @Test
    public void testCompare() {
        HolidayTreeSetComparator holidayTreeSetComparator = new HolidayTreeSetComparator();
        assertEquals(0, holidayTreeSetComparator.compare(holiday1, holiday2));

        holiday1.setHolidayId(2L);
        assertEquals(0, holidayTreeSetComparator.compare(holiday1, holiday2));

        holiday1.setHolidayId(2L);
        assertEquals(0, holidayTreeSetComparator.compare(holiday1, holiday2));

        holiday1.setHolidayId(0L);
        holiday1.setHolidayName("A");
        assertEquals(-1, holidayTreeSetComparator.compare(holiday1, holiday2));
        assertEquals(1, holidayTreeSetComparator.compare(holiday2, holiday1));

        holiday1.setHolidayId(null);
        holiday2.setHolidayId(null);
        holiday1.setHolidayName("A");
        assertEquals(-1, holidayTreeSetComparator.compare(holiday1, holiday2));
        assertEquals(1, holidayTreeSetComparator.compare(holiday2, holiday1));

        holiday1.setHolidayId(null);
        holiday2.setHolidayId(null);
        holiday1.setHolidayName("B");
        holiday1.setHolidayCategory(Holiday.HolidayCategory.CUSTOM);
        assertEquals(-4, holidayTreeSetComparator.compare(holiday1, holiday2));
        assertEquals(4, holidayTreeSetComparator.compare(holiday2, holiday1));
    }

}
