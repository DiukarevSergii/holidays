package com.serhiidiukarev.holiday.validation;

import com.serhiidiukarev.holiday.Holiday;
import com.serhiidiukarev.holiday.repository.HolidayRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Reusable utility class which provides a bunch of method for validation support
 */
@Component
public abstract class ValidationHelper {
    static Logger logger = LogManager.getLogger(ValidationHelper.class);
    private static HolidayRepository holidayRepository;

    @Autowired
    protected ValidationHelper(HolidayRepository holidayRepository) {
        ValidationHelper.holidayRepository = holidayRepository;
    }

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

    public static void isHolidayAlreadyExisted(Holiday holiday) {
        validateHoliday(holiday);
        Optional<Holiday> holidayOptional = holidayRepository.findHoliday(
                holiday.getHolidayDate(), holiday.getHolidayName(), holiday.getHolidayCategory());

        if (holidayOptional.isPresent()) {
            throw new IllegalArgumentException("already added");
        }
    }
}
