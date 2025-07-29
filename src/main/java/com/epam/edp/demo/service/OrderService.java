package com.epam.edp.demo.service;

import com.epam.edp.demo.exception.EntityNotFountException;
import com.epam.edp.demo.mapper.OrderMapper;
import com.epam.edp.demo.model.dto.request.OrderUpdateRequest;
import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.model.entity.*;
import com.epam.edp.demo.repository.OrderRepository;
import com.epam.edp.demo.repository.ReservationRepository;
import com.epam.edp.demo.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ReservationRepository reservationRepository;

    public ApiResponse getOrdersByUserId(String userId) {
        log.info("Retrieving orders with user id: {}", userId);

        List<Order> order = orderRepository.findAllByUserId(userId);

        if (order.isEmpty()) {
            return ResponseBuilder.build(404, true, "Orders not found");
        }

        return ResponseBuilder.build(200, true, orderMapper.toResponses(order));
    }

    public ApiResponse cancelPreOrder(String preOrderId) {
        log.info("Cancelling pre order with id: {}", preOrderId);

        Order order = orderRepository.findById(preOrderId)
                .orElseThrow(() -> new EntityNotFountException("Order not found with id: %s".formatted(preOrderId)));

        Reservation reservation = reservationRepository.findById(order.getReservationId())
                .orElseThrow(() -> new EntityNotFountException("Reservation of pre order not found"));

        // check if user is cancelling pre order 30min before, if not invalid request
        LocalTime reservationStartTime = reservation.getStartTime();
        if (!LocalTime.now().plusMinutes(30).isBefore(reservationStartTime)){
            return ResponseBuilder.build(400, false, "You can only cancel pre-order 30 min before reservation start time");
        }

        orderRepository.delete(order);
        return ResponseBuilder.build(200, true, "Pre-order cancelled successfully!");
    }

    public ApiResponse editPreOrder(OrderUpdateRequest request) {
        log.info("Editing pre order with id: {}", request.getOrderId());

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new EntityNotFountException("Order with id: %s not found".formatted(request.getOrderId())));

        Reservation reservation = reservationRepository.findById(order.getReservationId())
                .orElseThrow(() -> new EntityNotFountException("Reservation of pre order not found"));

        // check if user is editing pre-order 30min before, if not invalid request
        LocalTime reservationStartTime = reservation.getStartTime();
        if (!LocalTime.now().plusMinutes(30).isBefore(reservationStartTime)){
            return ResponseBuilder.build(400, false, "You can only edit pre-order 30 min before reservation start time");
        }

        List<FoodOrder> updatedOrders = request.getFoodOrders();
        if (updatedOrders.isEmpty()) {
            return ResponseBuilder.build(400, false, "Invalid request, foodOrders is empty");
        }

        order.setOrders(updatedOrders);

        orderRepository.save(order);
        return ResponseBuilder.build(200, true, "Pre order updated successfully");
    }
}
