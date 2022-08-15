package com.serhiidiukarev.holiday.repository;

import com.serhiidiukarev.holiday.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
}
