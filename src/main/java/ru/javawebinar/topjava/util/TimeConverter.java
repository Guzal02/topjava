package ru.javawebinar.topjava.util;


import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalTime;

@Component
public class TimeConverter implements Converter<String, LocalTime> {
    @Nullable
    @Override
    public LocalTime convert(@Nullable String time) {
        return StringUtils.hasLength(time) ? LocalTime.parse(time) : null;
    }
}
