package com.serhiidiukarev.holiday.service;

import com.serhiidiukarev.holiday.utils.LocalDateAdapter;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

import java.time.LocalDate;

public class LocalDateConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        if (!(source instanceof String)) {
            throw new IllegalArgumentException("The argument should be a string: " + source);
        }
        try {
            return LocalDate.parse((String) source, LocalDateAdapter.formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert", e);
        }
    }
}