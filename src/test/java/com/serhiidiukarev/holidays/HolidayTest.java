package com.serhiidiukarev.holidays;

import com.serhiidiukarev.holidays.service.DefaultHolidaysService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HolidayTest {

    private static Holiday holiday1;
    private static Holiday holiday2;

    @BeforeEach
    public void init() {
        holiday1 = Holiday.builder().holidayId(1).holidayCategory(Holiday.HolidayCategory.CUSTOM).holidayName("Holiday Name").build();
        holiday2 = Holiday.builder().holidayId(1).holidayCategory(Holiday.HolidayCategory.CUSTOM).holidayName("Holiday Name").build();
    }

    @Test
    public void equals_EqualsObjects_True() {
        assertEquals(holiday1, holiday2);

        holiday2.setHolidayId(2);

        assertEquals(holiday1, holiday2);

        LocalDate date1 = LocalDate.of(2022, 7, 25);
        LocalDate date2 = LocalDate.of(2022, 7, 25);

        holiday1.setHolidayDate(date1);
        holiday2.setHolidayDate(date2);

        assertEquals(holiday1, holiday2);

        holiday1.setHolidayCategory(Holiday.HolidayCategory.GOVERNMENT);
        holiday2.setHolidayCategory(Holiday.HolidayCategory.GOVERNMENT);

        assertEquals(holiday1, holiday2);

        holiday1.setHolidayCategory(Holiday.HolidayCategory.OTHER);
        holiday2.setHolidayCategory(Holiday.HolidayCategory.OTHER);

        assertEquals(holiday1, holiday2);
    }

    @Test
    public void equals_NotEqualsObjects_False() {
        holiday2.setHolidayName("test name");

        assertNotEquals(holiday1, holiday2);

        holiday2.setHolidayName("Holiday Name");

        LocalDate date1 = LocalDate.of(2022, 7, 27);
        holiday1.setHolidayDate(date1);

        assertNotEquals(holiday1, holiday2);

        LocalDate date2 = LocalDate.of(2022, 7, 21);
        holiday2.setHolidayDate(date2);

        assertNotEquals(holiday1, holiday2);

        holiday1.setHolidayId(1);
        holiday1.setHolidayCategory(Holiday.HolidayCategory.GOVERNMENT);

        assertNotEquals(holiday1, holiday2);
    }

    @Test
    public void hashCode_EqualsObjects_True() {
        assertEquals(holiday1.hashCode(), holiday2.hashCode());

        LocalDate date1 = LocalDate.of(2022, 7, 26);
        LocalDate date2 = LocalDate.of(2022, 7, 26);

        holiday1.setHolidayDate(date1);
        holiday2.setHolidayDate(date2);

        assertEquals(holiday1.hashCode(), holiday2.hashCode());

        holiday1.setHolidayCategory(Holiday.HolidayCategory.GOVERNMENT);
        holiday2.setHolidayCategory(Holiday.HolidayCategory.GOVERNMENT);

        assertEquals(holiday1.hashCode(), holiday2.hashCode());
    }

    @Test
    public void hashCode_NotEqualsObjects_False() {
        holiday2.setHolidayName("test name");

        assertNotEquals(holiday1.hashCode(), holiday2.hashCode());

        holiday2.setHolidayName("Holiday Name");

        LocalDate date1 = LocalDate.of(2022, 7, 20);
        holiday1.setHolidayDate(date1);

        assertNotEquals(holiday1.hashCode(), holiday2.hashCode());

        LocalDate date2 = LocalDate.of(2022, 7, 21);
        holiday2.setHolidayDate(date2);

        assertNotEquals(holiday1.hashCode(), holiday2.hashCode());
    }

    public static class HolidayBuilderTest {
        @ParameterizedTest
        @CsvFileSource(resources = "/data.csv")
        public void builder_initialParameters_instanceShouldKeepTheSameValues(
                int id, String category, String name, String date) {
            Holiday.HolidayCategory holidayCategory = Holiday.HolidayCategory.valueOf(category);
            LocalDate localDate = LocalDate.parse(date, DefaultHolidaysService.LocalDateAdapter.formatter);

            Holiday holiday = Holiday
                    .builder()
                    .holidayId(id)
                    .holidayCategory(holidayCategory)
                    .holidayName(name)
                    .holidayDate(localDate)
                    .build();

            assertEquals(holiday.getHolidayId(), id);
            assertEquals(holiday.getHolidayCategory(), holidayCategory);
            assertEquals(holiday.getHolidayName(), name);
            assertEquals(holiday.getHolidayDate(), localDate);
        }
    }
}
