package com.serhiidiukarev.holiday.service;

import com.serhiidiukarev.holiday.Holiday;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DefaultHolidayServiceTest {

    public static final String TO_WRITE = "src/test/resources/file-to-write.json";
    @Qualifier("DefaultHolidayService")
    @Autowired
    private HolidayService<LocalDate, String> holidaysService;
    private Map<LocalDate, Set<Holiday>> holidays;


    @BeforeEach
    public void setUp() {
        holidays = holidaysService.getHolidays();
    }

    @AfterEach
    void tearDown() {
        holidaysService.clear();
    }

    @AfterAll
    static void afterAll() {
        new File(TO_WRITE).delete();
    }

    @ParameterizedTest
    @CsvSource({"2000-01-04", "2000-02-04", "2001-01-04"})
    public void testAddHoliday(
            @ConvertWith(LocalDateConverter.class) LocalDate date) {
        holidaysService.addHoliday(date);

        assertEquals(1, holidays.size());

        Set<Holiday> holidaysSet = holidays.get(date);

        assertNotNull(holidaysSet);
        assertEquals(1, holidays.get(date).size());

        Holiday holiday = holidaysSet.iterator().next();
        assertNotNull(holiday);

        assertEquals(date, holiday.getHolidayDate());
        assertEquals(date.toString(), holiday.getHolidayName());
        assertEquals(Holiday.HolidayCategory.CUSTOM, holiday.getHolidayCategory());
    }

    @ParameterizedTest
    @CsvSource({"2000-01-04", "2000-02-04", "2001-01-04"})
    public void addHoliday_OneDate_true(
            @ConvertWith(LocalDateConverter.class) LocalDate date) {
        assertTrue(holidaysService.addHoliday(date));
        assertEquals(1, holidays.size());

        Set<Holiday> holidaysSet = holidays.get(date);

        assertNotNull(holidaysSet);
        assertEquals(1, holidaysSet.size());

        Holiday holiday = holidaysSet.iterator().next();
        assertNotNull(holiday);

        assertEquals(date, holiday.getHolidayDate());
        assertEquals(date.toString(), holiday.getHolidayName());
    }

    @ParameterizedTest
    @CsvSource({"2000-01-04", "2000-02-04", "2001-01-04"})
    public void addHoliday_Twice_false(
            @ConvertWith(LocalDateConverter.class) LocalDate date) {
        holidaysService.addHoliday(date);
        assertFalse(holidaysService.addHoliday(date));
    }

    @ParameterizedTest
    @CsvSource({"2000-01-01,2000-01-03", "2000-02-01,2000-02-03", "2001-01-01,2001-01-03"})
    public void addHolidaysBetween_ThreeDays_Size3(
            @ConvertWith(LocalDateConverter.class) LocalDate startDate,
            @ConvertWith(LocalDateConverter.class) LocalDate endDate) {
        holidaysService.addHolidaysBetween(startDate, endDate);
        assertEquals(3, holidaysService.getHolidays().size());
    }

    @ParameterizedTest
    @CsvSource({"2000-01-01", "2022-07-16", "2022-07-23"})
    public void countWorkingDaysBetween_SATURDAY_0(
            @ConvertWith(LocalDateConverter.class) LocalDate date) {
        assertEquals(DayOfWeek.SATURDAY, date.getDayOfWeek());
        assertEquals(0, holidaysService.countWorkingDaysBetween(date, date));
    }

    @ParameterizedTest
    @CsvSource({"2000-01-02", "2022-07-17", "2022-07-24"})
    public void countWorkingDaysBetween_SUNDAY_0(
            @ConvertWith(LocalDateConverter.class) LocalDate date) {
        assertEquals(DayOfWeek.SUNDAY, date.getDayOfWeek());
        assertEquals(0, holidaysService.countWorkingDaysBetween(date, date));
    }

    @ParameterizedTest
    @CsvSource({"2022-06-27,2022-07-04,2022-07-01", "2022-07-29,2022-08-05,2022-08-02"})
    public void countWorkingDaysBetween_TwoDatesWithAdditionalHoliday_5(
            @ConvertWith(LocalDateConverter.class) LocalDate startDate,
            @ConvertWith(LocalDateConverter.class) LocalDate endDate,
            @ConvertWith(LocalDateConverter.class) LocalDate holidayDate
    ) {
        holidaysService.addHoliday(holidayDate);
        assertEquals(5, holidaysService.countWorkingDaysBetween(startDate, endDate));
    }

    @ParameterizedTest
    @CsvSource({"src/test/resources/data01.json,2", "src/test/resources/data02.json,1096"})
    public void addHolidaysFromJSON_PathToJson_AddObjectsToHolidays(String jsonDestination, int expectedSize) {
        holidaysService.addHolidaysFromJSON(jsonDestination);

        assertEquals(expectedSize, holidays.size());

        Set<Holiday> holidaySet = holidays.get(LocalDate.of(2020, 1, 1));
        assertEquals(2, holidaySet.size());

        assertEquals(1, holidays.get(LocalDate.of(2020, 1, 4)).size());

        holidays.values().forEach(map -> {
            assertNotNull(map);
            map.forEach(holiday -> {
                assertNotNull(holiday);
                assertNotNull(holiday.getHolidayId());
                assertNotNull(holiday.getHolidayName());
                assertNotNull(holiday.getHolidayName());
                assertNotNull(holiday.getHolidayCategory());
            });
        });

        Holiday custom0Test = Holiday
                .builder()
                .holidayDate(LocalDate.of(2020, 1, 1))
                .holidayCategory(Holiday.HolidayCategory.CUSTOM)
                .holidayName("CUSTOM0")
                .build();

        Holiday custom0Version2Test = Holiday
                .builder()
                .holidayDate(LocalDate.of(2020, 1, 1))
                .holidayCategory(Holiday.HolidayCategory.CUSTOM)
                .holidayName("CUSTOM0 - version 2")
                .build();

        Set<Holiday> customSet = Set.of(custom0Test, custom0Version2Test);

        assertEquals(customSet, holidaySet);
    }

    @ParameterizedTest
    @CsvSource({TO_WRITE + ",src/test/resources/file-to-read.json"})
    public void writeHolidaysToJSON_PathToJson_AddAllObjectsToHolidays(String toWrite, String toRead) throws IOException {
        Holiday holiday0 = Holiday
                .builder()
                .holidayDate(LocalDate.of(2020, 1, 1))
                .holidayCategory(Holiday.HolidayCategory.CUSTOM)
                .holidayName("CUSTOM")
                .build();

        Holiday holiday1 = Holiday
                .builder()
                .holidayDate(LocalDate.of(2021, 1, 1))
                .holidayCategory(Holiday.HolidayCategory.OTHER)
                .holidayName("OTHER")
                .build();

        Holiday holiday2 = Holiday
                .builder()
                .holidayDate(LocalDate.of(2020, 1, 1))
                .holidayCategory(Holiday.HolidayCategory.GOVERNMENT)
                .holidayName("GOVERNMENT")
                .build();

        holidaysService.addHoliday(holiday0);
        holidaysService.addHoliday(holiday1);
        holidaysService.addHoliday(holiday2);
        holidaysService.writeHolidaysToJSON(toWrite);

        String actual = Files.readString(Path.of(toWrite));
        String expected = Files.readString(Path.of(toRead));

        assertEquals(expected, actual);
    }

}

