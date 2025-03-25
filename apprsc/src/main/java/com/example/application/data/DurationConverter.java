package com.example.application.data;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.Duration;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Duration duration) {
        return duration != null ? (int) duration.toMinutes() : null;
    }

    @Override
    public Duration convertToEntityAttribute(Integer minutes) {
        return minutes != null ? Duration.ofMinutes(minutes) : null;
    }
}