package com.serhiidiukarev.holidays.validation;

import com.serhiidiukarev.holidays.Holiday;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;

/**
 * Reusable utility class which provides a bunch of method for validation support
 */
public abstract class ValidationHelper {
    static Logger logger = LogManager.getLogger(ValidationHelper.class);

    /**
     * @param holiday {@link Holiday} instance
     * @throws IllegalArgumentException if an argument is null
     */
    public static void validateHoliday(Holiday holiday) {
        if (holiday == null) {
            throwNewIllegalArgumentException("Invalid method argument: (holidays=null)");
        }
    }

    /**
     * @param date {@link LocalDate} instance
     * @throws IllegalArgumentException if a date is null
     */
    public static void validateDate(LocalDate date) {
        if (date == null) {
            throwNewIllegalArgumentException("Invalid method argument: (date=null)");
        }
    }

    /**
     * @param startDate {@link LocalDate} instance
     * @param endDate   {@link LocalDate} instance
     * @throws IllegalArgumentException if any date is null
     */
    public static void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throwNewIllegalArgumentException("Invalid method argument(s): (startDate" + startDate + ", endDate" + endDate + ")");
        }
    }

    /**
     * Write an error message to log and throws a new {@link IllegalArgumentException}
     *
     * @param message the message object to log.
     * @throws IllegalArgumentException as an expected result
     */
    private static void throwNewIllegalArgumentException(String message) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException(message);
        logger.error(message, illegalArgumentException);
        throw illegalArgumentException;
    }
}
