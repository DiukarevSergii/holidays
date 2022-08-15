package com.serhiidiukarev.holiday.configuration;

import com.serhiidiukarev.holiday.Holiday;
import com.serhiidiukarev.holiday.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
public class HolidayConfig {

    @Autowired
    private HolidaysManager holidaysManager;

    @Bean
    CommandLineRunner commandLineRunner(HolidayRepository holidayRepository) {
        return args -> {
            ArrayList<Holiday> holidays = new ArrayList<>();
            Map<String, List<String>> holidaysMapping = holidaysManager.getHolidaysMapping();
            holidaysMapping.forEach((id, item) -> {
                Holiday holiday = Holiday
                        .builder()
                        .holidayName(item.get(0))
                        .holidayCategory(Holiday.HolidayCategory.valueOf(item.get(1)))
                        .holidayDate(LocalDate.parse(item.get(2)))
                        .build();
                holidays.add(holiday);
            });
            holidayRepository.saveAll(holidays);
        };
    }
}
