package com.restaurant.booking.service;

import com.restaurant.booking.exception.EntityNotFountException;
import com.restaurant.booking.exception.UserNotFoundException;
import com.restaurant.booking.mapper.ReservationMapper;
import com.restaurant.booking.mapper.TableMapper;
import com.restaurant.booking.model.constant.ReservationStatus;
import com.restaurant.booking.model.dto.ReservationDto;
import com.restaurant.booking.model.dto.TableDto;
import com.restaurant.booking.model.dto.request.BookingByClientRequest;
import com.restaurant.booking.model.dto.request.BookingByWaiterRequest;
import com.restaurant.booking.model.dto.response.ApiResponse;
import com.restaurant.booking.model.entity.*;
import com.restaurant.booking.repository.*;
import com.restaurant.booking.util.ResponseBuilder;
import com.restaurant.booking.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {

    private final ReservationRepository reservationRepository;
    private final WaiterRepository waiterRepository;
    private final TableRepository tableRepository;
    private final ReservationMapper reservationMapper;
    private final TableMapper tableMapper;
    private final AuthUserRepository userRepository;
    private final LocationRepository locationRepository;
    private final CartRepository cartRepository;

    public ApiResponse bookingTableByClient(BookingByClientRequest request) {
        log.info("Processing booking request: {}", request);
        String userId = Utils.getCurrentUser().getId();

        log.info("Checking table availability: Location = {}, Table = {}, Date = {}, TimeFrom = {}, TimeTo = {}",
                request.getLocationId(), request.getTableNumber(), request.getDate(), request.getTimeFrom(), request.getTimeTo());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDate date = LocalDate.parse(request.getDate());
        String from = date + " " + request.getTimeFrom(); // example: "2025-04-20 14:00"
        String to = date + " " + request.getTimeTo();     // example: "2025-04-20 17:00"

        LocalDateTime startTime = LocalDateTime.parse(from, formatter);
        LocalDateTime endTime = LocalDateTime.parse(to, formatter);


        if (startTime.isBefore(LocalDateTime.now()) ) {
            return createResponse(400, false, "Date is not valid, please try again");
        }

        Optional<Tables> tableOptional = tableRepository.findByTableNumberAndLocationId(Integer.parseInt(request.getTableNumber()), request.getLocationId());

        if (tableOptional.isEmpty()) {
            log.error("Table not found with number {}", request.getTableNumber());
            throw new EntityNotFountException("Table not found");
        }

        log.info("Found tables by location and number: {}", tableOptional.get());

        Tables table = tableOptional.get();
        if (!isTableAvailable(table, date.toString(), request.getTimeFrom(), request.getTimeTo())) {
            log.error("Table is already booked: {}", request.getTableNumber());
            return createResponse(400, false, "Table is already booked");
        }

        Waiter leastBusyWaiter = findLeastBusyWaiter(request.getLocationId());
        log.info("Assigned waiter: {}", leastBusyWaiter.getId());

        String timeTo = LocalTime.parse(request.getTimeTo()).plusMinutes(15).toString();

        table.getTimeSlots().add(new TimeSlot(date.toString(), request.getTimeFrom(), timeTo, true));
        tableRepository.save(table);

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .locationId(request.getLocationId())
                .tableNumber(Integer.valueOf(request.getTableNumber()))
                .date(date)
                .startTime(LocalTime.parse(request.getTimeFrom()))
                .endTime(LocalTime.parse(request.getTimeTo()).plusMinutes(15))
                .guestNumber(Integer.valueOf(request.getGuestsNumber()))
                .userId(userId)
                .status(ReservationStatus.RESERVED)
                .assignedWaiterId(leastBusyWaiter.getId())
                .build();

        reservationRepository.save(reservation);
        log.info("Reservation saved: {}", reservation.getId());

        Cart cart = Cart.builder()
                .id(UUID.randomUUID().toString())
                .reservationId(reservation.getId())
                .userId(userId)
                .build();
        cartRepository.save(cart);

        ReservationDto reservationDto = reservationMapper.mapToDto(reservation);


        int hours = (int) Duration.between(startTime, endTime).toHours();
        leastBusyWaiter.getTimeSlots().put(from + " " + request.getTimeTo(), hours);

        waiterRepository.save(leastBusyWaiter);
        log.info("Updated waiter time slots.");

        return createResponse(201, true, reservationDto);
    }

    private Waiter findLeastBusyWaiter(String locationId) {
        List<Waiter> waiters = waiterRepository.findAllByLocationId(locationId);
        if (waiters.isEmpty()) {
            throw new EntityNotFountException("Waiter not found with Location Id: " + locationId);
        }

        return waiters.stream()
                .min(Comparator.comparingInt(waiter ->
                        waiter.getTimeSlots().values().stream()
                                .mapToInt(Integer::intValue)
                                .sum()))
                .orElse(null);
    }

    public ApiResponse bookingByWaiter(BookingByWaiterRequest request) {
        log.info("Processing booking request: {}", request);

        String locationId = request.getLocationId();
        String customerEmail = request.getCustomerEmail();
        String clientType = request.getClientType();

        LocalDate date;
        try {
            date = LocalDate.parse(request.getDate());
        } catch (DateTimeParseException e) {
            return ApiResponse.builder()
                    .statusCode(400)
                    .success(false)
                    .message("Invalid date format. Expected yyyy-MM-dd")
                    .build();
        }

        LocalTime timeFrom;
        LocalTime timeTo;
        try {
            timeFrom = LocalTime.parse(request.getTimeFrom());
            timeTo = LocalTime.parse(request.getTimeTo()).plusMinutes(15);
        } catch (DateTimeParseException e) {
            return ApiResponse.builder()
                    .statusCode(400)
                    .success(false)
                    .message("Invalid time format. Expected HH:mm (e.g. 15:00)")
                    .build();
        }

        int tableNumber;
        try {
            tableNumber = Integer.parseInt(request.getTableNumber());
        } catch (NumberFormatException e) {
            return ApiResponse.builder()
                    .statusCode(400)
                    .success(false)
                    .message("Table number must be a valid number")
                    .build();
        }

        int guests;
        try {
            guests = Integer.parseInt(request.getGuestsNumber());
            if (guests <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            return ApiResponse.builder()
                    .statusCode(400)
                    .success(false)
                    .message("Number of guests must be a positive number")
                    .build();
        }

        String userEmail = Utils.getCurrentUser().getEmail();
        log.info("Waiter email : {}", userEmail);

        Optional<Waiter> waiterOptional = waiterRepository.findByEmail(userEmail);
        if (waiterOptional.isEmpty()) {
            throw new EntityNotFountException("You don't have access, you are not waiter");
        }
        Waiter waiter = waiterOptional.get();

        String userId = null;
        if (clientType.equalsIgnoreCase("CUSTOMER")) {
            Optional<AuthUser> authUserOptional = userRepository.findByEmail(customerEmail);
            if (authUserOptional.isEmpty()) {
                throw new UserNotFoundException("User not found with this customer name"+ customerEmail);
            }
            AuthUser user = authUserOptional.get();
            userId = user.getId();
        }

        if (date.isBefore(LocalDate.now())) {
            return ApiResponse.builder()
                    .statusCode(400)
                    .success(false)
                    .message("Date is not valid, please try again")
                    .build();
        }

        log.info("Checking table availability: Location = {}, Table = {}, Date = {}, TimeFrom = {}, TimeTo = {}",
                locationId, tableNumber, date, timeFrom, timeTo);


        Optional<Tables> tableOptional = tableRepository.findByTableNumberAndLocationId(tableNumber, locationId);

        if (tableOptional.isEmpty()) {
            log.info("Table not found: {}", tableNumber);
            throw new EntityNotFountException("Table not found");
        }

        log.info("Found tables by location and number: {}", tableOptional.get());

        Tables table = tableOptional.get();
        if (!isTableAvailable(table, date.toString(), timeFrom.toString(), timeTo.toString())) {
            log.info("Table is already booked: {}", tableNumber);
            return ApiResponse.builder()
                    .statusCode(400)
                    .success(false)
                    .message("Table is already booked for this timeslot or not exist.")
                    .build();
        }


        table.getTimeSlots().add(new TimeSlot(date.toString(), timeFrom.toString(), timeTo.toString(), true));
        tableRepository.save(table);

        Reservation reservation = Reservation.builder()
                .id(UUID.randomUUID().toString())
                .locationId(locationId)
                .tableNumber(tableNumber)
                .date(date)
                .startTime(timeFrom)
                .endTime(timeTo)
                .guestNumber(Integer.valueOf(request.getGuestsNumber()))
                .status(ReservationStatus.RESERVED)
                .assignedWaiterId(waiter.getId())
                .userId(userId)
                .build();

        reservationRepository.save(reservation);
        log.info("Reservation saved: {}", reservation.getId());

        ReservationDto reservationDto = reservationMapper.mapToDto(reservation);
        reservationDto.setUserInfo(clientType + " " + customerEmail);

        waiter.getTimeSlots().put(date + " " + timeFrom, waiter.getTimeSlots().getOrDefault(date + " " + timeFrom, 0) + 1);
        waiterRepository.save(waiter);
        log.info("Updated waiter time slots.");

        return ApiResponse.<ReservationDto>builder()
                .statusCode(200)
                .success(true)
                .data(reservationDto)
                .build();

    }

    public ApiResponse getTables(String locationId, String date, String time, Integer guestsNumber) {
        log.info("Fetching available tables.");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDate localDate = LocalDate.parse(date);
        String from = localDate + " " + time; // example: "2025-04-20 14:00"


        LocalDateTime startTime = LocalDateTime.parse(from, formatter);

        if (startTime.isBefore(LocalDateTime.now()) ) {
            return createResponse(400, false, "Date is not valid, please try again");
        }


        log.info("Search parameters - Location: {}, Guests: {}, Date: {}, Time: {}", locationId, guestsNumber, date, time);

        if (locationId == null || locationId.isBlank()) {
            return ResponseBuilder.build(400, false, "Location ID cannot be null or empty");
        }
        if (date == null || date.isBlank()) {
            return ResponseBuilder.build(400, false, "Date must not be null or empty");
        }
        if (time == null || time.isBlank()) {
            return ResponseBuilder.build(400, false, "Time must not be null or empty");
        }
        if (guestsNumber == null || guestsNumber <= 0) {
            return ResponseBuilder.build(400, false, "Number of guests must be a positive number");
        }

        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return ResponseBuilder.build(400, false, "Date format must be yyyy-MM-dd");
        }

        try {
            LocalTime.parse(time);
        } catch (DateTimeParseException e) {
            return ResponseBuilder.build(400, false, "Time format must be HH:mm (e.g. 15:00)");
        }

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFountException("Location not found with id %s".formatted(locationId)));

        List<Tables> tables = findAvailableTables(locationId, date, time, guestsNumber);
        List<TableDto> tableResponse = tableMapper.mapToDtoList(tables, location.getAddress());

        tableResponse.forEach(tableDto -> tableDto.setDate(date));
        log.info("Found {} available tables.", tableResponse.size());

        return ApiResponse.builder()
                .statusCode(200)
                .success(true)
                .data(tableResponse)
                .build();
    }

    private boolean isTableAvailable(Tables table, String date, String timeFrom, String timeTo) {
        return table.getTimeSlots().stream()
                .noneMatch(slot -> slot.getDate().equals(date) &&
                        slot.isReserved() &&
                        slot.getEndTime().compareTo(timeFrom) > 0 &&
                        slot.getStartTime().compareTo(timeTo) < 0);
    }

    private boolean checkAvailableTables(Tables table, String date, String time) {
        List<TimeSlot> slots = table.getTimeSlots();

        boolean noBookingsOnDate = slots.stream().noneMatch(slot -> slot.getDate().equals(date));

        boolean hasAvailableSlot = slots.stream()
                .anyMatch(slot -> slot.getDate().equals(date)
                        && (time == null || LocalTime.parse(slot.getStartTime()).isAfter(LocalTime.parse(time))));

        return noBookingsOnDate || hasAvailableSlot;
    }

    public List<Tables> findAvailableTables(String locationId, String date, String time, Integer guests) {
        List<Tables> tables = tableRepository.findAllByLocationId(locationId);
        log.info("All Found tables based on: "+ locationId + " and "+ date +" :" + tables.size());
        if (date != null && !date.isEmpty()) {
            tables = filterTablesByAvailability(tables, date, time, guests);
        }

        return tables;
    }

    private List<Tables> filterTablesByAvailability(List<Tables> tables, String date, String time, Integer guests) {
        return tables.stream()
                .filter(table -> checkAvailableTables(table, date, time))
                .filter(table -> table.getTableCapacity() >= guests)
                .collect(Collectors.toList());
    }




    private ApiResponse createResponse(int statusCode, boolean isSuccess, Object data) {
        return ApiResponse.builder()
                .statusCode(statusCode)
                .success(isSuccess)
                .message("")
                .data(data)
                .build();
    }

    private ApiResponse createResponse(int statusCode, boolean isSuccess, String message) {
        return ApiResponse.builder()
                .statusCode(statusCode)
                .success(isSuccess)
                .message(message)
                .data(null)
                .build();
    }
}
