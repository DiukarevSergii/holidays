package com.serhiidiukarev.holiday.repository;

import com.serhiidiukarev.holiday.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    @Query("SELECT holiday FROM Holiday holiday " +
            "WHERE holiday.holidayDate = ?1 " +
            "AND holiday.holidayName = ?2 " +
            "AND holiday.holidayCategory = ?3") // todo reimplement to criteria
    Optional<Holiday> findHoliday(LocalDate holidayDate, String holidayName, Holiday.HolidayCategory holidayCategory);
}
