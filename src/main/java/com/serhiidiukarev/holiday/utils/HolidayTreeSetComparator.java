package com.serhiidiukarev.holiday.utils;

import com.serhiidiukarev.holiday.Holiday;

import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link HolidayTreeSetComparator} implemented to avoid add
 * duplication to {@code holidays} collection and keep on order based
 * on internal field's values in that order: {@code counter},
 * {@link Holiday#getHolidayName()}, {@link Holiday#getHolidayCategory()}
 */
public class HolidayTreeSetComparator implements Comparator<Holiday> {
    /**
     * Compares two {@link Holiday} objects
     *
     * @param h1 the first object to be compared.
     * @param h2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     *         first argument is less than, equal to, or greater than the
     *         second.
     */
    @Override
    public int compare(Holiday h1, Holiday h2) {
        if (h1.hashCode() == h2.hashCode() && h1.equals(h2)) {
            return 0;
        }

        Integer holidayId1 = h1.getHolidayId();
        Integer holidayId2 = h2.getHolidayId();

        if (holidayId1 == null && holidayId2 == null || Objects.equals(holidayId1, holidayId2)) {
            if (h1.getHolidayName().equals(h2.getHolidayName())) {
                return h1.getHolidayCategory().toString().compareTo(h2.getHolidayCategory().toString());
            }
            return h1.getHolidayName().compareTo(h2.getHolidayName());
        }
        if (holidayId1 == null) return -1;
        if (holidayId2 == null) return 1;

        return holidayId1.compareTo(holidayId2);
    }
}