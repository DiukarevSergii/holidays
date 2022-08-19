package com.serhiidiukarev.holiday.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.serhiidiukarev.holiday.Holiday;
import com.serhiidiukarev.holiday.repository.HolidayRepository;
import com.serhiidiukarev.holiday.utils.HolidayTreeSetComparator;
import com.serhiidiukarev.holiday.utils.LocalDateAdapter;
import com.serhiidiukarev.holiday.validation.ValidationHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Implementation of the {@link  HolidayService} interface for working
 * with database entity {@link Holiday}.
 *
 * Implements all optional operations, and permits all elements, including
 * {@code null}.  In addition to implementing the {@link HolidayService} interface,
 * this class provides methods to manipulate the order of the set's element
 * by {@link HolidayTreeSetComparator} and has his own implementation to serializer
 * and deserializer {@link LocalDate} for Json by {@link LocalDateAdapter}.
 *
 * @see HolidayService
 */
@Service("DBHolidayService")
public class DBHolidayService implements HolidayService<LocalDate, String> {
    /**
     * DB logger
     */
    Logger logger = LogManager.getLogger(DBHolidayService.class);
    private final HolidayRepository holidayRepository;

    @Autowired
    public DBHolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }

    /**
     * Calculate the number of workdays between two given dates
     *
     * @param startDate Start date
     * @param endDate   End date
     * @return the number of working days
     */
    @Override
    public int countWorkingDaysBetween(LocalDate startDate, LocalDate endDate) {
        ValidationHelper.validateDates(startDate, endDate);

        Map<LocalDate, Set<Holiday>> holidays = getHolidays();

        return countWorkingDaysBetween(startDate, endDate, holidays);
    }

    /**
     * Mark a date as a new holiday
     *
     * @param date date of a new holiday
     * @return {@code true} if the date is already marked as a holiday
     */
    @Override
    public boolean addHoliday(LocalDate date) {
        Holiday holiday = buildHoliday(date);

        return addHoliday(holiday);
    }

    /**
     * Mark a date as a new holiday
     *
     * @param holiday a new holiday item
     * @return {@code true} if the holiday was saved without exception
     * @throws IllegalArgumentException in case the given {@literal entity} is {@literal null}.
     */
    @Override
    public boolean addHoliday(Holiday holiday) {
        ValidationHelper.isHolidayAlreadyExisted(holiday);
        holidayRepository.save(holiday);
        return true;
    }

    /**
     * Mark range of dates as new holidays
     *
     * @param startDate start date of a new holiday
     * @param endDate   end date of a new holiday
     */
    @Override
    public void addHolidaysBetween(LocalDate startDate, LocalDate endDate) {
        ValidationHelper.validateDates(startDate, endDate);

        List<Holiday> holidays = Stream.iterate(startDate, d -> d.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .map(this::buildHoliday)
                .collect(Collectors.toList());

        holidays.forEach(ValidationHelper::isHolidayAlreadyExisted);
        holidayRepository.saveAll(holidays);
    }

    /**
     * Parse JSON file and mark list of dates as new holidays
     *
     * @param jsonDestination path to a file in JSON format
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

            holidayList.forEach(ValidationHelper::isHolidayAlreadyExisted);
            holidayRepository.saveAll(holidayList);
        } catch (FileNotFoundException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Write current list of holidays to file in a JSON format
     *
     * @param jsonDestination path to a file in JSON format
     */
    @Override
    public void writeHolidaysToJSON(String jsonDestination) {
        File file = new File(jsonDestination);
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        try (FileWriter fileWriter = new FileWriter(file)) {
            List<Holiday> holidays = holidayRepository.findAll();
            String holidaysJson = gson.toJson(holidays);
            fileWriter.write(holidaysJson);
        } catch (IOException e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<LocalDate, Set<Holiday>> getHolidays() {
        List<Holiday> all = holidayRepository.findAll();
        Map<LocalDate, Set<Holiday>> holidays = new TreeMap<>();

        all.forEach(holiday -> {
            LocalDate holidayDate = holiday.getHolidayDate();
            if (!holidays.containsKey(holidayDate)) {
                holidays.put(holidayDate, new TreeSet<>(new HolidayTreeSetComparator()));
            }
            Set<Holiday> holidaysSet = holidays.get(holidayDate);
            holidaysSet.add(holiday);
        });

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
        holidayRepository.deleteAll();
    }

    @Override
    public boolean deleteHoliday(Long holidayId) {
        ValidationHelper.isHolidayExist(holidayId);
        holidayRepository.deleteById(holidayId);
        return true;
    }

    @Override
    @Transactional
    public Holiday updateHoliday(Long holidayId,
                                 LocalDate holidayDate,
                                 String holidayName,
                                 Holiday.HolidayCategory holidayCategory) {
        Holiday holiday = holidayRepository.findById(holidayId)
                .orElseThrow(() -> new IllegalArgumentException("holiday with id=" + holidayId + " does not exists"));

        if (holidayDate != null && !Objects.equals(holiday.getHolidayDate(), holidayDate)) {
            holiday.setHolidayDate(holidayDate);
        }
        if (holidayName != null && !Objects.equals(holiday.getHolidayName(), holidayName)) {
            holiday.setHolidayName(holidayName);
        }
        if (holidayCategory != null && !Objects.equals(holiday.getHolidayCategory(), holidayCategory)) {
            holiday.setHolidayCategory(holidayCategory);
        }
        return holiday;
    }
}
