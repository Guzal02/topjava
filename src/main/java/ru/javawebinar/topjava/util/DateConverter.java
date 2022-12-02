package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Component
public class DateConverter implements Converter<String, LocalDate> {
    @Nullable
    @Override
    public LocalDate convert(@Nullable String date) {
        return StringUtils.hasLength(date) ? LocalDate.parse(date) : null;
    }
}
