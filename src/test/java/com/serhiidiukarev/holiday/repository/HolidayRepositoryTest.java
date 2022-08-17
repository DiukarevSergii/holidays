package com.serhiidiukarev.holiday.repository;

import com.serhiidiukarev.holiday.Holiday;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class HolidayRepositoryTest {

    @Autowired
    private HolidayRepository holidayRepository;

    @AfterEach
    void tearDown() {
        holidayRepository.deleteAll();
    }

    @Test
    void itShouldCheckWhenHolidayExists() {
        //given
        LocalDate date = LocalDate.parse("2020-02-02");
        String name = "Holiday Name";
        Holiday.HolidayCategory category = Holiday.HolidayCategory.GOVERNMENT;
        Holiday holiday = Holiday.builder()
                .holidayDate(date)
                .holidayCategory(category)
                .holidayName(name)
                .build();
        holidayRepository.save(holiday);

        //when
        Optional<Holiday> holidayOptional = holidayRepository.findHoliday(date, name, category);
        //then
        assertTrue(holidayOptional.isPresent());
        assertEquals(holidayOptional.get().getHolidayName(), name);
        assertEquals(holidayOptional.get().getHolidayDate(), date);
        assertEquals(holidayOptional.get().getHolidayCategory(), category);
    }

    @Test
    void itShouldCheckWhenHolidayDoesNotExists() {
        //given
        LocalDate date = LocalDate.parse("2020-02-02");
        String name = "Holiday Name";
        Holiday.HolidayCategory category = Holiday.HolidayCategory.GOVERNMENT;
        Holiday holiday = Holiday.builder()
                .holidayDate(date)
                .holidayCategory(category)
                .holidayName(name)
                .build();

        //when
        Optional<Holiday> holidayOptional = holidayRepository.findHoliday(date, name, category);
        //then
        assertFalse(holidayOptional.isPresent());
    }
}