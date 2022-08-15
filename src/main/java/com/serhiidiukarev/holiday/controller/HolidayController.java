package com.serhiidiukarev.holiday.controller;

import com.serhiidiukarev.holiday.Holiday;
import com.serhiidiukarev.holiday.service.HolidayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping(path = "api/v1/holiday")
public class HolidayController {
    private final HolidayService holidaysService;

    @Autowired
    public HolidayController(@Qualifier("DBHolidayService") HolidayService holidaysService) {
        this.holidaysService = holidaysService;
    }

    @GetMapping
    public Map getHolidays() {
        return holidaysService.getHolidays();
    }

    @PostMapping
    public boolean addHoliday(@RequestBody Holiday holiday) {
        return holidaysService.addHoliday(holiday);
    }

    @PutMapping(path = "{holidayId}")
    public Holiday updateHoliday(
            @PathVariable("holidayId") Long holidayId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate holidayDate,
            @RequestParam(required = false) String holidayName,
            @RequestParam(required = false) Holiday.HolidayCategory holidayCategory
    ) {
        return holidaysService.updateHoliday(holidayId, holidayDate, holidayName, holidayCategory);
    }

    @DeleteMapping(path = "{holidayId}")
    public boolean deleteHoliday(@PathVariable("holidayId") Long holidayId) {
        return holidaysService.deleteHoliday(holidayId);
    }
}
