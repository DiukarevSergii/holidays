package com.serhiidiukarev.holiday.service;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.serhiidiukarev.holiday.Holiday;
import com.serhiidiukarev.holiday.validation.ValidationHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Resizable-map implementation of the {@link  HolidaysService} interface.  Implements
 * all optional operations, and permits all elements, including
 * {@code null}.  In addition to implementing the {@code HolidaysService} interface,
 * this class provides methods to manipulate the order of the set's element
 * by {@link HolidayTreeSetComparator} and has his own implementation to serializer
 * and deserializer {@link LocalDate} for Json by {@link LocalDateAdapter}. This class also keeps
 * an id {@code counter} which is incremented after if successful extend of collection
 *
 * @see HolidaysService
 */
@Service
public class DefaultHolidayService implements HolidaysService<LocalDate, String> {

    /**
     * Default logger
     */
    Logger logger = LogManager.getLogger(DefaultHolidayService.class);
    /**
     * Default map which keeps dates associated with sets of {@link Holiday}
     * For sorting uses {@link HolidayTreeSetComparator}
     */
    private final Map<LocalDate, Set<Holiday>> holidays = new TreeMap<>();

    /**
     * Default counter which represent an index for each successfully added element
     */
    private int counter = 0;

    /**
     * Calculate the number of workdays between two given dates (inclusive)
     *
     * @param startDate Start date
     * @param endDate   End date
     * @return the number of working days (inclusive)
     */
    @Override
    public int countWorkingDaysBetween(LocalDate startDate, LocalDate endDate) {
        ValidationHelper.validateDates(startDate, endDate);

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
     * Ensures that {@code holidays}  collection contains the specified element
     * (optional operation).  Returns {@code true} if this collection changed as a
     * result of the call.  (Returns {@code false} if this collection does
     * not permit duplicates and already contains the specified element.)<p>
     *
     * @param date date of a new holiday
     * @return {@code true} if this collection changed as a result of the call
     * (as specified by {@link HolidaysService#addHoliday(Object)})
     */
    @Override
    public boolean addHoliday(LocalDate date) {
        ValidationHelper.validateDate(date);

        Holiday holiday = Holiday
                .builder()
                .holidayName(date.toString())
                .holidayCategory(Holiday.HolidayCategory.CUSTOM)
                .holidayDate(date)
                .build();

        return addHoliday(holiday);
    }

    /**
     * Ensures that {@code holidays}  collection contains the specified element
     * (optional operation).  Returns {@code true} if this collection changed as a
     * result of the call.  (Returns {@code false} if this collection does
     * not permit duplicates and already contains the specified element.)<p>
     *
     * @param holiday a new holiday
     * @return {@code true} if this collection changed as a result of the call
     * (as specified by {@link HolidaysService#addHoliday(Object)})
     */
    @Override
    public boolean addHoliday(Holiday holiday) {
        ValidationHelper.validateHoliday(holiday);

        LocalDate date = holiday.getHolidayDate();
        boolean containsDate = holidays.containsKey(date);
        Set<Holiday> holidaysValue = holidays.get(date);

        if (containsDate
                && holidaysValue.contains(holiday)) {
            return false;
        }
        Set<Holiday> holidaysSet = containsDate
                ? holidaysValue
                : new TreeSet<>(new HolidayTreeSetComparator());

        holiday.setHolidayId(counter++);
        holidaysSet.add(holiday);
        holidays.put(date, holidaysSet);

        return true;
    }

    /**
     * Adds range of dates as new holidays
     * to {@code holidays} collection (inclusive)
     *
     * @param startDate start date of a new holiday
     * @param endDate   end date of a new holiday
     */
    @Override
    public void addHolidaysBetween(LocalDate startDate, LocalDate endDate) {
        ValidationHelper.validateDates(startDate, endDate);

        Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .forEach(this::addHoliday);
    }

    /**
     * Parse JSON file and add list of dates as new holidays
     * to {@code holidays} collection (inclusive)
     *
     * @param jsonDestination path to a file in JSON format
     * @throws RuntimeException if {@link FileNotFoundException} is happened
     *                          during the read operation from file
     */
    @Override
    public void addHolidaysFromJSON(String jsonDestination) {
        File file = new File(jsonDestination);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        try {
            FileReader fileReader = new FileReader(file);
            Type holidayListType = new TypeToken<ArrayList<Holiday>>() {
            }.getType();
            List<Holiday> holidayList = gson.fromJson(fileReader, holidayListType);

            holidayList.forEach(this::addHoliday);
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Write current list of holidays to file in a JSON format
     *
     * @param jsonDestination path to a file in JSON format
     * @throws RuntimeException if {@link IOException} is happened
     *                          during the writing operation to file
     */
    @Override
    public void writeHolidaysToJSON(String jsonDestination) {
        File file = new File(jsonDestination);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        try (FileWriter fileWriter = new FileWriter(file)) {
            String holidaysJson = gson.toJson(holidays);
            fileWriter.write(holidaysJson);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Return a mpa with all current {@link Holiday}
     *
     * @return map with sets of current holidays
     * (where key is a date and values is a set of holidays)
     */
    @Override
    public Map<LocalDate, Set<Holiday>> getHolidays() {
        return holidays;
    }

    /**
     * Removes all the elements from a holidays' collection (optional operation).
     * The collection will be empty after this method returns.
     *
     * @throws UnsupportedOperationException if the {@code clear} operation
     *                                       is not supported by this collection
     */
    @Override
    public void clear() {
        holidays.clear();
        counter = 0;
    }

    /**
     * A {@link HolidayTreeSetComparator} implemented to avoid add
     * duplication to {@code holidays} collection and keep on order based
     * on internal field's values in that order: {@code counter},
     * {@link Holiday#getHolidayName()}, {@link Holiday#getHolidayCategory()}
     */
    static class HolidayTreeSetComparator implements Comparator<Holiday> {
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

    /**
     * A {@link LocalDateAdapter} implemented to serializer and
     * deserializer {@link LocalDate} for Json base on interval
     * representation of {@link DateTimeFormatter}
     */
    public static class LocalDateAdapter implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {
        public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        /**
         * Serialize date to Json
         *
         * @param localDate the object that needs to be converted to Json.
         * @param srcType   the actual type (fully genericized version) of the source object.
         * @return a tree of {@link JsonElement}s corresponding to the serialized form of {@code src}.
         */
        @Override
        public JsonElement serialize(LocalDate localDate, Type srcType, JsonSerializationContext context) {
            return new JsonPrimitive(formatter.format(localDate));
        }

        /**
         * SDeserialize date from Json
         *
         * @param json    The Json data being deserialized
         * @param typeOfT The type of the Object to deserialize to
         * @return a deserialized object of the specified type typeOfT which is a subclass of {@code T}
         * @throws JsonParseException if json is not in the expected format of {@code typeofT}
         */
        @Override
        public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return LocalDate.parse(json.getAsString(), formatter);
        }
    }
}
