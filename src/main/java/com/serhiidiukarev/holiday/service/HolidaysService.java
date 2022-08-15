package com.serhiidiukarev.holiday.service;

import com.serhiidiukarev.holiday.Holiday;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/**
 * The root interface in the <i>holidays service hierarchy</i>.  A service
 * represents a list of methods. The methods provide an ability to
 * manipulate dates and {@link Holiday objects}
 *
 * @param <T> the type of dates in this service
 * @param <S> the type of paths to files
 */
public interface HolidaysService<T, S> {

    /**
     * Calculate the number of workdays between two given dates
     *
     * @param startDate Start date
     * @param endDate   End date
     * @return the number of working days
     */
    int countWorkingDaysBetween(final T startDate, final T endDate);

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
}
