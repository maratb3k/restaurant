package com.restaurant.booking.util;

import com.restaurant.booking.model.dto.StaffMetric;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class StaffMetricListConverter implements AttributeConverter<List<StaffMetric>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<StaffMetric> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return null;
        }
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize StaffMetric list", e);
        }
    }

    @Override
    public List<StaffMetric> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        try {
            return mapper.readValue(dbData, new TypeReference<List<StaffMetric>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize StaffMetric list", e);
        }
    }
}
