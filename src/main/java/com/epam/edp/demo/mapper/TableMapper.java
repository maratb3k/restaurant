package com.epam.edp.demo.mapper;

import com.epam.edp.demo.model.dto.TableDto;
import com.epam.edp.demo.model.entity.Location;
import com.epam.edp.demo.model.entity.Tables;
import com.epam.edp.demo.model.entity.TimeSlot;
import com.epam.edp.demo.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TableMapper {

    private final LocationRepository locationRepository;


    public TableDto mapToDto(Tables table, String locationAddress) {
        return TableDto.builder()
                .tableNumber(String.valueOf(table.getTableNumber()))
                .capacity(String.valueOf(table.getTableCapacity()))
                .locationId(table.getLocationId())
                .locationAddress(locationAddress)
                .availableSlots(getAvailableTimeSlots(table.getTimeSlots()))
                .build();
    }

    public List<TableDto> mapToDtoList(List<Tables> tables, String address) {
        return tables.stream().map(t -> mapToDto(t, address)).toList();
    }

    private String getLocationAddress(String locationId) {
        Optional<String> location = locationRepository.findById(locationId)
                .map(Location::getAddress);
        return location.orElse("Unknown Location");
    }

    private List<String> getAvailableTimeSlots(List<TimeSlot> reservedSlots) {
        LocalTime openingTime = LocalTime.of(8, 0);
        LocalTime closingTime = LocalTime.of(20, 0);

        if (reservedSlots == null || reservedSlots.isEmpty()) {
            return List.of(openingTime + " - " + closingTime);
        }

        // Sort reserved slots by start time
        reservedSlots = reservedSlots.stream()
                .filter(TimeSlot::isReserved)
                .sorted((a, b) -> a.getStartTime().compareTo(b.getStartTime()))
                .collect(Collectors.toList());

        List<String> availableSlots = new ArrayList<>();
        LocalTime freeStart = openingTime;

        for (TimeSlot slot : reservedSlots) {
            LocalTime bookedStart = LocalTime.parse(slot.getStartTime());
            LocalTime bookedEnd = LocalTime.parse(slot.getEndTime());

            if (freeStart.isBefore(bookedStart)) {
                availableSlots.add(freeStart + " - " + bookedStart);
            }
            freeStart = bookedEnd;
        }

        if (freeStart.isBefore(closingTime)) {
            availableSlots.add(freeStart + " - " + closingTime);
        }

        return availableSlots;
    }
}
