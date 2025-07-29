package com.epam.edp.demo.service;

import com.epam.edp.demo.exception.EntityNotFountException;
import com.epam.edp.demo.mapper.ReservationMapper;
import com.epam.edp.demo.model.constant.ReservationStatus;
import com.epam.edp.demo.model.dto.ReservationDto;
import com.epam.edp.demo.model.dto.request.ReservationPostponeRequest;
import com.epam.edp.demo.model.dto.request.ReservationUpdateRequest;
import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.model.entity.*;
import com.epam.edp.demo.repository.*;
import com.epam.edp.demo.util.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationMapper reservationMapper;
    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final AuthUserRepository userRepository;
    private final WaiterRepository waiterRepository;

    public ApiResponse getReservation() {
        String userEmail = Objects.requireNonNull(Utils.getCurrentUser()).getEmail();
        Optional<AuthUser> userOptional = userRepository.findByEmail(userEmail);
        Optional<Waiter> waiter = waiterRepository.findByEmail(userEmail);
        String userId;
        List<Reservation> reservations;
        if (waiter.isPresent()) {
            userId = waiter.get().getId();
            reservations = reservationRepository.findAllByAssignedWaiterId(userId);
        } else if (userOptional.isPresent()) {
            userId = userOptional.get().getId();
            reservations = reservationRepository.findAllByUserId(userId);
        } else {
            return ApiResponse.builder()
                    .statusCode(404)
                    .success(false)
                    .message("User not found with this email: " + userEmail)
                    .build();
        }


        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.RESERVED) {
                LocalDateTime reservationStart = LocalDateTime.of(reservation.getDate(), reservation.getStartTime());
                LocalDateTime reservationEnd = LocalDateTime.of(reservation.getDate(), reservation.getEndTime());
                log.info("Current time (UTC): {}", now);
                log.info("Start date time: {}", reservationStart);
                log.info("End date time: {}", reservationEnd);
                if (!now.isBefore(reservationStart) && !now.isAfter(reservationEnd)) {
                    reservation.setStatus(ReservationStatus.IN_PROGRESS);
                    reservationRepository.save(reservation);
                    log.info("Updated reservation {} to IN_PROGRESS", reservation.getId());
                }
            }
        }

        List<ReservationDto> reservationDtos = reservationMapper.mapToDtoList(reservations);

        return ApiResponse.builder()
                .statusCode(200)
                .success(true)
                .data(reservationDtos)
                .build();
    }

    public ApiResponse deleteReservation(String reservationId) {

        String userEmail = Objects.requireNonNull(Utils.getCurrentUser()).getEmail();

        Optional<AuthUser> userOpt = userRepository.findByEmail(userEmail);
        if (userOpt.isEmpty()) {
            return ApiResponse.<Void>builder()
                    .statusCode(404)
                    .success(false)
                    .message("User not found with email: " + userEmail)
                    .build();
        }

        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return ApiResponse.<Void>builder()
                    .statusCode(404)
                    .success(false)
                    .message("Reservation not found")
                    .build();
        }

        Reservation reservation = reservationOpt.get();
        Optional<Tables> tableOpt = tableRepository.findByTableNumberAndLocationId(reservation.getTableNumber(), reservation.getLocationId());
        if (tableOpt.isEmpty()) {
            return ApiResponse.<Void>builder()
                    .statusCode(404)
                    .success(false)
                    .message("Table not found while deleting reservation")
                    .build();
        }

        Optional<Waiter> waiterOpt = waiterRepository.findById(reservation.getAssignedWaiterId());
        if (waiterOpt.isEmpty()) {
            return ApiResponse.builder()
                    .statusCode(404)
                    .success(false)
                    .message("Waiter not found with id: " + reservation.getAssignedWaiterId())
                    .build();
        }

        if (!isAuthorizedUser(userOpt.get(), waiterOpt.get(), userEmail)) {
            return ApiResponse.<Void>builder()
                    .statusCode(403)
                    .success(false)
                    .message("Unauthorized: User does not have permission to delete reservation")
                    .build();
        }

        if (!canCancelReservation(reservation.getDate(), reservation.getStartTime())) {
            return ApiResponse.<Void>builder()
                    .statusCode(403)
                    .success(false)
                    .message("Sorry, you cannot cancel this reservation now")
                    .build();
        }

        deleteReservationAndTimeSlot(reservation, tableOpt.get(), waiterOpt.get());

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .success(true)
                .message("Reservation and corresponding TimeSlot deleted successfully")
                .build();
    }

    public ApiResponse editReservation(ReservationUpdateRequest request, String id) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isEmpty()) {
            throw new EntityNotFountException("Reservation not found with id: " + id);
        }

        Reservation reservation = reservationOptional.get();

        if (!canCancelReservation(LocalDate.parse(request.getDate()), reservation.getStartTime())) {
            return ApiResponse.<Void>builder()
                    .statusCode(403)
                    .success(false)
                    .message("Sorry, you cannot cancel this reservation now")
                    .build();
        }

        if (reservation.getDate().isBefore(LocalDate.now())) {
            buildResponse(400, false, "Reservation date cannot be in the past");
        }
        Optional<Tables> tableOptional = tableRepository.findByTableNumberAndLocationId(Integer.parseInt(request.getTableNumber()), request.getLocationId());
        if (tableOptional.isEmpty()) {
            log.error("Table not found with number {}", request.getTableNumber());
            throw new EntityNotFountException("Table not found");
        }

        Tables table = tableOptional.get();
        if (!isTableAvailable(table, request.getDate(), request.getTimeFrom(), request.getTimeTo())) {
            log.error("Table is already booked: {}", request.getTableNumber());
            return buildResponse(400, false, "Table is already booked");
        }

        table.getTimeSlots().add(new TimeSlot(request.getDate(), request.getTimeFrom(), request.getTimeFrom(), true));
        tableRepository.save(table);

        reservation.setDate(LocalDate.parse(request.getDate()));
        reservation.setLocationId(request.getLocationId());
        reservation.setStartTime(LocalTime.parse(request.getTimeFrom()));
        reservation.setEndTime(LocalTime.parse(request.getTimeTo()));
        reservation.setTableNumber(Integer.parseInt(request.getTableNumber()));
        reservation.setGuestNumber(Integer.parseInt(request.getGuestNumber()));

        reservationRepository.save(reservation);
        log.info("Reservation saved: {}", reservation.getId());
        return buildResponse(200, true, "Reservation edited successfully");
    }

    public ApiResponse postponeByWaiter(ReservationPostponeRequest request, String id){
        String userEmail = Utils.getCurrentUser().getEmail();
        log.info("Waiter email : {}", userEmail);

        Optional<Waiter> waiterOptional = waiterRepository.findByEmail(userEmail);
        if (waiterOptional.isEmpty()) {
            throw new EntityNotFountException("You don't have access, you are not waiter");
        }

        Optional<Reservation> reservationOptional = reservationRepository.findById(id);
        if (reservationOptional.isEmpty()) {
            throw new EntityNotFountException("Reservation not found with id: " + id);
        }

        Reservation reservation = reservationOptional.get();

        if (!canCancelReservation(LocalDate.parse(request.getDate()), reservation.getStartTime())) {
            return ApiResponse.<Void>builder()
                    .statusCode(403)
                    .success(false)
                    .message("Sorry, you cannot postpone this reservation now")
                    .build();
        }

        if (reservation.getDate().isBefore(LocalDate.now())) {
            return buildResponse(400, false, "Reservation date is in the past and cannot be changed");
        }
        Optional<Tables> tableOptional = tableRepository.findByTableNumberAndLocationId(reservation.getTableNumber(), reservation.getLocationId());
        if (tableOptional.isEmpty()) {
            log.error("Table not found with number {}", reservation.getTableNumber());
            throw new EntityNotFountException("Table not found");
        }

        Tables table = tableOptional.get();
        if (!isTableAvailable(table, request.getDate(), request.getTimeFrom(), request.getTimeTo())) {
            log.error("Table is already booked: {}", reservation.getTableNumber());
            return buildResponse(400, false, "Table is is not available for this time");
        }

        table.getTimeSlots().add(new TimeSlot(request.getDate(), request.getTimeFrom(), request.getTimeFrom(), true));
        tableRepository.save(table);

        reservation.setDate(LocalDate.parse(request.getDate()));
        reservation.setStartTime(LocalTime.parse(request.getTimeFrom()));
        reservation.setEndTime(LocalTime.parse(request.getTimeTo()));


        reservationRepository.save(reservation);
        log.info("Reservation saved: {}", reservation.getId());
        return buildResponse(200, true, "Reservation edited successfully");
    }

//    public ApiResponse orderDish(String reservationId, String dishId){
//        // 1. Find reservation
//        Optional<Reservation> optionalReservation = reservationRepository.findReservationById(reservationId);
//        if (optionalReservation.isEmpty()) {
//            return buildResponse(404, false, "Reservation not found");
//
//        }
//
//        // Get cart
//        Reservation reservation = optionalReservation.get();
//        log.info("reservation: " + reservation.getId());
//
//        String cartId = reservation.getCartId();
//        if (cartId == null) {
//            return buildResponse(400,  false,"Reservation has no cart");
//        }
//
//        // 2. Load cart
//        Optional<Cart> optionalCart = cartRepository.findCartById(cartId);
//        if (optionalCart.isEmpty()) {
//
//            return buildResponse(404, false,"Cart not found");
//        }
//        Cart cart = optionalCart.get();
//        List<FoodOrder> orders = cart.getOrders();
//        if (orders == null) {
//            orders = new ArrayList<>();
//        }
//
//        // 3. Check if dish is already in cart
//        Optional<FoodOrder> existing = orders.stream()
//                .filter(order -> dishId.equals(order.getDishId()))
//                .findFirst();
//        if (existing.isPresent()) {
//            existing.get().setQuantity(existing.get().getQuantity() + 1);
//        } else {
//            FoodOrder newOrder = new FoodOrder();
//            newOrder.setDishId(dishId);
//            newOrder.setQuantity(1);
//            orders.add(newOrder);
//        }
//
//        cart.setOrders(orders);
//        cartRepository.save(cart);
//
//        return buildResponse(200, true, "Dish has been place to cart");
//    }


    private boolean isAuthorizedUser(AuthUser user, Waiter waiter, String userEmail) {
        return user.getEmail().equals(userEmail) || waiter.getEmail().equals(userEmail);
    }

    private boolean canCancelReservation(LocalDate date, LocalTime startTime) {
        if(date.isEqual(LocalDate.now())){
            return startTime.isAfter(LocalTime.now().plusMinutes(30));
        }
        return true;
    }

    private void deleteReservationAndTimeSlot(Reservation reservation, Tables table, Waiter waiter) {
        LocalDate date = reservation.getDate();
        LocalTime startTime = reservation.getStartTime();

        table.getTimeSlots().removeIf(slot ->
                slot.isReserved() &&
                        slot.getDate().equals(date.toString()) &&
                        slot.getStartTime().equals(startTime.toString()));

        String waiterTimeSlot = date + " " + startTime;
        waiter.getTimeSlots().remove(waiterTimeSlot);
        waiterRepository.save(waiter);

        reservationRepository.deleteById(reservation.getId());

        tableRepository.save(table);
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
                        && (time == null || LocalTime.parse(slot.getStartTime()).isAfter(LocalTime.parse(time)))
                        && !slot.isReserved());

        return noBookingsOnDate || hasAvailableSlot;
    }

//    public List<Tables> findAvailableTables(String locationId, String date, String time, Integer guests) {
//        List<Tables> tables = tableRepository.findTablesByLocationIdAndTableCapacity(locationId, guests);
//
//        if (date != null && !date.isEmpty()) {
//            tables = filterTablesByAvailability(tables, date, time);
//        }
//
//        return tables;
//    }

    private List<Tables> filterTablesByAvailability(List<Tables> tables, String date, String time) {
        return tables.stream()
                .filter(table -> checkAvailableTables(table, date, time))
                .collect(Collectors.toList());
    }

    private ApiResponse buildResponse(int statusCode, boolean isSuccess, String message, Object data) {
        return ApiResponse.builder()
                .statusCode(statusCode)
                .success(isSuccess)
                .message(message)
                .data(data)
                .build();
    }

    private ApiResponse buildResponse(int statusCode, boolean isSuccess, String message) {
        return ApiResponse.builder()
                .statusCode(statusCode)
                .success(isSuccess)
                .message(message)
                .data(null)
                .build();
    }

}
