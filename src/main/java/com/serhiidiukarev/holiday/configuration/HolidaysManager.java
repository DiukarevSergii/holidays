package com.serhiidiukarev.holiday.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "app-configuration")
public class HolidaysManager {

    @NotEmpty
    private Map<String, List<String>> holidaysMapping;

    public Map<String, List<String>> getHolidaysMapping() {
        return holidaysMapping;
    }

    public void setHolidaysMapping(Map<String, List<String>> holidaysMapping) {
        this.holidaysMapping = holidaysMapping;
    }
}
