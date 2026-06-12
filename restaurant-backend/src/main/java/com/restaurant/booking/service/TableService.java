package com.restaurant.booking.service;

import com.restaurant.booking.exception.EntityNotFountException;
import com.restaurant.booking.model.dto.TableDto;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.model.entity.Tables;
import com.restaurant.booking.model.entity.TimeSlot;
import com.restaurant.booking.repository.LocationRepository;
import com.restaurant.booking.repository.TableRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableService {

    private final TableRepository tableRepository;
    private final LocationRepository locationRepository;


    public ApiResponse saveTable(TableDto request) throws JsonProcessingException {
        log.info("Received request body: {}", request);

        Integer tableNumber = Integer.valueOf(request.getTableNumber());
        Integer tableCapacity = Integer.valueOf(request.getTableNumber());
        String locationId = request.getLocationId();

        locationRepository.findById(locationId).orElseThrow(
                () -> new EntityNotFountException("Location not found with id: " + locationId)
        );

        Tables tables = Tables.builder()
                .id(UUID.randomUUID().toString())
                .locationId(locationId)
                .tableNumber(tableNumber)
                .tableCapacity(tableCapacity)
                .build();

        List<TimeSlot> timeSlots = createTimeSlotsForTable();

        tableRepository.save(tables);
        log.info("Table saved successfully");

        return ApiResponse.<Void>builder()
                .statusCode(201)
                .success(false)
                .message("Table created successfully with id: " + tables.getId())
                .build();
    }

    private List<TimeSlot> createTimeSlotsForTable() {
        List<TimeSlot> timeSlotList = new ArrayList<>();
        TimeSlot timeSlot1 = TimeSlot.builder()
                .startTime("08:00")
                .endTime("09:30")
                .build();

        TimeSlot timeSlot2 = TimeSlot.builder()
                .startTime("09:45")
                .endTime("11:15")
                .build();

        TimeSlot timeSlot3 = TimeSlot.builder()
                .startTime("11:30")
                .endTime("13:00")
                .build();

        TimeSlot timeSlot4 = TimeSlot.builder()
                .startTime("13:15")
                .endTime("14:45")
                .build();

        TimeSlot timeSlot5 = TimeSlot.builder()
                .startTime("15:00")
                .endTime("16:30")
                .build();

        TimeSlot timeSlot6 = TimeSlot.builder()
                .startTime("16:45")
                .endTime("18:15")
                .build();

        TimeSlot timeSlot7 = TimeSlot.builder()
                .startTime("18:30")
                .endTime("20:00")
                .build();

        timeSlotList.add(timeSlot1);
        timeSlotList.add(timeSlot2);
        timeSlotList.add(timeSlot3);
        timeSlotList.add(timeSlot4);
        timeSlotList.add(timeSlot5);
        timeSlotList.add(timeSlot6);
        timeSlotList.add(timeSlot7);

        return timeSlotList;
    }
}
