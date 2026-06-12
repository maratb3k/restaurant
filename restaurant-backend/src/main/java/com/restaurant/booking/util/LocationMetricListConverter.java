package com.restaurant.booking.util;

import com.restaurant.booking.model.dto.LocationMetric;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class LocationMetricListConverter implements AttributeConverter<List<LocationMetric>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<LocationMetric> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize LocationMetric list", e);
        }
    }

    @Override
    public List<LocationMetric> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            return mapper.readValue(dbData, new TypeReference<List<LocationMetric>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize LocationMetric list", e);
        }
    }
}