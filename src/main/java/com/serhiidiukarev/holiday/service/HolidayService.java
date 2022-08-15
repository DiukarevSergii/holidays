package com.serhiidiukarev.holiday.service;

import com.serhiidiukarev.holiday.Holiday;
import com.serhiidiukarev.holiday.validation.ValidationHelper;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * The root interface in the <i>holidays service hierarchy</i>.  A service
 * represents a list of methods. The methods provide an ability to
 * manipulate dates and {@link Holiday objects}
 *
 * @param <T> the type of dates in this service
 * @param <S> the type of paths to files
 */
public interface HolidayService<T, S> {

    /**
     * Calculate the number of workdays between two given dates
     *
     * @param startDate Start date
     * @param endDate   End date
     * @return the number of working days
     */
    int countWorkingDaysBetween(final T startDate, final T endDate);

    /**
     * Calculate the number of workdays between two given dates
     *
     * @param startDate Start date
     * @param endDate   End date
     * @param holidays List of holidays
     * @return  the number of working days
     */
    default int countWorkingDaysBetween(LocalDate startDate, LocalDate endDate, Map<LocalDate, Set<Holiday>> holidays) {
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

    /**
     * @param date a new holiday
     * @return a new instance of Holiday class
     */
    default Holiday buildHoliday(LocalDate date) {
        ValidationHelper.validateDate(date);

        return Holiday
                .builder()
                .holidayName(date.toString())
                .holidayCategory(Holiday.HolidayCategory.CUSTOM)
                .holidayDate(date)
                .build();
    }

    /**
     * Mark a date as a new holiday
     *
     * @param date date of a new holiday
     * @return {@code true} if the date is already marked as a holiday
     */
    boolean addHoliday(final T date);

    /**
     * Mark a date as a new holiday
     *
     * @param holiday a new holiday item
     * @return {@code true} if the holiday is already taken into account
     */
    boolean addHoliday(final Holiday holiday);

    /**
     * Mark range of dates as new holidays
     *
     * @param startDate start date of a new holiday
     * @param endDate end date of a new holiday
     */
    void addHolidaysBetween(final T startDate, final T endDate);

    /**
     * Parse JSON file and mark list of dates as new holidays
     *
     * @param jsonDestination path to a file in JSON format
     */
    void addHolidaysFromJSON(S jsonDestination);

    /**
     * Write current list of holidays to file in a JSON format
     *
     * @param jsonDestination path to a file in JSON format
     */
    void writeHolidaysToJSON(S jsonDestination);

    /**
     * Provides all current holidays
     *
     * @return map with sets of current holidays
     * (where key is a date and values is a set of holidays)
     */
    Map<LocalDate, Set<Holiday>> getHolidays();

    /**
     * Removes all the elements from a holidays' collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *         is not supported by this collection
     */
    void clear();

    boolean deleteHoliday(Long holidayId);

    Holiday updateHoliday(Long holidayId, LocalDate holidayDate, String holidayName, Holiday.HolidayCategory holidayCategory);
}
