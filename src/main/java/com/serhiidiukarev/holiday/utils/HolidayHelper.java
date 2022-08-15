package com.serhiidiukarev.holiday.utils;

import com.serhiidiukarev.holiday.Holiday;
import com.serhiidiukarev.holiday.validation.ValidationHelper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class HolidayHelper {

    public static int countWorkingDaysBetween(LocalDate startDate, LocalDate endDate, Map<LocalDate, Set<Holiday>> holidays) {
        // Predicate 1: Is a given date is a holiday
        Predicate<LocalDate> isHoliday = holidays::containsKey;

        // Predicate 2: Is a given date is a weekday
        Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY;

        return (int) Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .filter(isWeekend.or(isHoliday).negate())
                .count();
    }

    public static Holiday buildHoliday(LocalDate date) {
        ValidationHelper.validateDate(date);

       return Holiday
                .builder()
                .holidayName(date.toString())
                .holidayCategory(Holiday.HolidayCategory.CUSTOM)
                .holidayDate(date)
                .build();
    }
}
