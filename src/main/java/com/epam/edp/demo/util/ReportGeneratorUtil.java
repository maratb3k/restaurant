//package com.epam.edp.demo.util;
//
//
//import com.epam.edp.demo.model.dto.LocationMetric;
//import com.epam.edp.demo.model.dto.StaffMetric;
//import com.epam.edp.demo.model.entity.*;
//import com.epam.edp.demo.repository.*;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true")
//public class ReportGeneratorUtil {
//
//    private final OrderRepository orderRepository;
//    private final ReservationRepository reservationRepository;
//    private final FeedbackRepository feedbackRepository;
//    private final WaiterRepository waiterRepository;
//    private final LocationRepository locationRepository;
//    private final DishRepository dishRepository; // Added DishRepository
//    private final ObjectMapper objectMapper;
//    private final RabbitTemplate rabbitTemplate;
//
//    @Value("${rabbitmq.exchange.name}")
//    private String exchangeName;
//
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;
//
//    // Runs every Sunday at midnight for the previous week
//    @Scheduled(cron = "0 0 0 * * SUN")
//    public void generateWeeklyReports() {
//        try {
//            LocalDate periodEnd = LocalDate.now().minusDays(1); // Saturday
//            LocalDate periodStart = periodEnd.minusDays(6); // Monday
//            LocalDate prevEnd = periodStart.minusDays(1); // Previous Sunday
//            LocalDate prevStart = prevEnd.minusDays(6); // Previous Monday
//
//            List<String> locationIds = locationRepository.findAllIds();
//            log.info("Generating reports for {} locations from {} to {}", locationIds.size(), periodStart, periodEnd);
//
//            for (String locationId : locationIds) {
//                Reports staffMetrics = calculateStaffPerformance(locationId, periodStart, periodEnd, prevStart, prevEnd);
//                sendToRabbitMQ(staffMetrics);
//
//                Reports locationMetrics = calculateLocationPerformance(locationId, periodStart, periodEnd, prevStart, prevEnd);
//                sendToRabbitMQ(locationMetrics);
//            }
//        } catch (Exception e) {
//            log.error("Failed to generate weekly reports", e);
//            throw new RuntimeException("Report generation failed", e);
//        }
//    }
//
//    private Reports calculateStaffPerformance(String locationId, LocalDate periodStart, LocalDate periodEnd, LocalDate prevStart, LocalDate prevEnd) {
//        List<Waiter> waiters = waiterRepository.findAllByLocationId(locationId);
//        List<StaffMetric> staffMetrics = new ArrayList<>();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//
//        for (Waiter waiter : waiters) {
//            // Working hours
//            int workingHours = waiter.getTimeSlots().entrySet().stream()
//                    .filter(e -> {
//                        String dateTimeStr = e.getKey().split("_")[0]; // "2025-06-07 12:00"
//                        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
//                        LocalDate date = dateTime.toLocalDate(); // just the date part
//                        return !date.isBefore(periodStart) && !date.isAfter(periodEnd);
//                    })
//                    .mapToInt(Map.Entry::getValue)
//                    .sum();
//
//            // Orders processed
//            List<Reservation> reservations = reservationRepository.findByAssignedWaiterIdAndDateBetween(waiter.getId(), periodStart, periodEnd);
//            List<Order> orders = orderRepository.findByReservationIdIn(reservations.stream().map(Reservation::getId).collect(Collectors.toList()));
//            int ordersProcessed = orders.size();
//
//            // Delta orders
//            List<Reservation> prevReservations = reservationRepository.findByAssignedWaiterIdAndDateBetween(waiter.getId(), prevStart, prevEnd);
//            List<Order> prevOrders = orderRepository.findByReservationIdIn(prevReservations.stream().map(Reservation::getId).collect(Collectors.toList()));
//            double deltaOrdersPercent = prevOrders.isEmpty() ? 0 : ((ordersProcessed - prevOrders.size()) * 100.0 / prevOrders.size());
//
//            // Average and Minimum Service Feedback
//            List<Feedback> feedbacks = feedbackRepository.findAllByIdIn(reservations.stream().map(Reservation::getFeedbackId).filter(Objects::nonNull).collect(Collectors.toList()));
//            double avgServiceFeedback = feedbacks.stream()
//                    .mapToInt(Feedback::getServiceRating)
//                    .average()
//                    .orElse(0);
//            int minServiceFeedback = feedbacks.stream()
//                    .mapToInt(Feedback::getServiceRating)
//                    .min()
//                    .orElse(0);
//
//            // Delta Minimum Feedback
//            List<Feedback> prevFeedbacks = feedbackRepository.findAllByIdIn(prevReservations.stream().map(Reservation::getFeedbackId).filter(Objects::nonNull).collect(Collectors.toList()));
//            int prevMinFeedback = prevFeedbacks.stream().mapToInt(Feedback::getServiceRating).min().orElse(0);
//            double deltaMinFeedbackPercent = prevMinFeedback == 0 ? 0 : ((minServiceFeedback - prevMinFeedback) * 100.0 / prevMinFeedback);
//
//            staffMetrics.add(StaffMetric.builder()
//                    .waiterId(waiter.getId())
//                    .waiterEmail(waiter.getEmail())
//                    .workingHours(workingHours)
//                    .ordersProcessed(ordersProcessed)
//                    .deltaOrdersPercent(deltaOrdersPercent)
//                    .avgServiceFeedback(avgServiceFeedback)
//                    .minServiceFeedback(minServiceFeedback)
//                    .deltaMinFeedbackPercent(deltaMinFeedbackPercent)
//                    .build());
//        }
//
//        return Reports.builder()
//                .reportType("STAFF_PERFORMANCE")
//                .periodStart(periodStart)
//                .periodEnd(periodEnd)
//                .locationId(locationId)
//                .staffMetrics(staffMetrics)
//                .build();
//    }
//
//    private Reports calculateLocationPerformance(String locationId, LocalDate periodStart, LocalDate periodEnd, LocalDate prevStart, LocalDate prevEnd) {
//        // Orders processed
//        List<Reservation> reservations = reservationRepository.findByLocationIdAndDateBetween(locationId, periodStart, periodEnd);
//        List<Order> orders = orderRepository.findByReservationIdIn(reservations.stream().map(Reservation::getId).collect(Collectors.toList()));
//        int ordersProcessed = orders.size();
//
//        // Delta orders
//        List<Reservation> prevReservations = reservationRepository.findByLocationIdAndDateBetween(locationId, prevStart, prevEnd);
//        List<Order> prevOrders = orderRepository.findByReservationIdIn(prevReservations.stream().map(Reservation::getId).collect(Collectors.toList()));
//        double deltaOrdersPercent = prevOrders.isEmpty() ? 0 : ((ordersProcessed - prevOrders.size()) * 100.0 / prevOrders.size());
//
//        // Average and Minimum Cuisine Feedback
//        List<Feedback> feedbacks = feedbackRepository.findAllByIdIn(reservations.stream().map(Reservation::getFeedbackId).filter(Objects::nonNull).collect(Collectors.toList()));
//        double avgCuisineFeedback = feedbacks.stream()
//                .mapToInt(Feedback::getCuisineRating)
//                .average()
//                .orElse(0);
//        int minCuisineFeedback = feedbacks.stream()
//                .mapToInt(Feedback::getCuisineRating)
//                .min()
//                .orElse(0);
//
//        // Delta Average Feedback
//        List<Feedback> prevFeedbacks = feedbackRepository.findAllByIdIn(prevReservations.stream().map(Reservation::getFeedbackId).filter(Objects::nonNull).collect(Collectors.toList()));
//        double prevAvgFeedback = prevFeedbacks.stream().mapToInt(Feedback::getCuisineRating).average().orElse(0);
//        double deltaAvgFeedbackPercent = prevAvgFeedback == 0 ? 0 : ((avgCuisineFeedback - prevAvgFeedback) * 100.0 / prevAvgFeedback);
//
//        // Fetch all dish IDs from to minimize DynamoDB calls
//        List<String> dishIds = orders.stream()
//                .flatMap(order -> order.getOrders().stream())
//                .map(FoodOrder::getDishId)
//                .distinct()
//                .collect(Collectors.toList());
//        List<Dish> dishes = dishRepository.findByIdIn(dishIds);
//        Map<String, Double> dishPrices = dishes.stream()
//                .collect(Collectors.toMap(Dish::getId, Dish::getPrice, (p1, p2) -> p1));
//
//        // Revenue
//        double revenue = orders.stream()
//                .flatMap(order -> order.getOrders().stream())
//                .mapToDouble(foodOrder -> {
//                    Double price = dishPrices.get(foodOrder.getDishId());
//                    return price != null ? price * foodOrder.getQuantity() : 0.0;
//                })
//                .sum();
//
//        // Fetch dish IDs for previous orders
//        List<String> prevDishIds = prevOrders.stream()
//                .flatMap(order -> order.getOrders().stream())
//                .map(FoodOrder::getDishId)
//                .distinct()
//                .collect(Collectors.toList());
//        List<Dish> prevDishes = dishRepository.findByIdIn(prevDishIds);
//        Map<String, Double> prevDishPrices = prevDishes.stream()
//                .collect(Collectors.toMap(Dish::getId, Dish::getPrice, (p1, p2) -> p1));
//
//        // Delta Revenue
//        double prevRevenue = prevOrders.stream()
//                .flatMap(order -> order.getOrders().stream())
//                .mapToDouble(foodOrder -> {
//                    Double price = prevDishPrices.get(foodOrder.getDishId());
//                    return price != null ? price * foodOrder.getQuantity() : 0.0;
//                })
//                .sum();
//        double deltaRevenuePercent = prevRevenue == 0 ? 0 : ((revenue - prevRevenue) * 100.0 / prevRevenue);
//
//        return Reports.builder()
//                .reportType("LOCATION_COMPARISON")
//                .periodStart(periodStart)
//                .periodEnd(periodEnd)
//                .locationMetrics(List.of(LocationMetric.builder()
//                        .locationId(locationId)
//                        .ordersProcessed(ordersProcessed)
//                        .deltaOrdersPercent(deltaOrdersPercent)
//                        .avgCuisineFeedback(avgCuisineFeedback)
//                        .minCuisineFeedback(minCuisineFeedback)
//                        .deltaAvgFeedbackPercent(deltaAvgFeedbackPercent)
//                        .revenue(revenue)
//                        .deltaRevenuePercent(deltaRevenuePercent)
//                        .build()))
//                .build();
//    }
//
//
//    private void sendToRabbitMQ(Reports metrics) {
//        try {
//            String messageBody = objectMapper.writeValueAsString(metrics);
//            rabbitTemplate.convertAndSend(exchangeName, routingKey, messageBody);
//
//            log.info("Sent {} report for location {} to RabbitMQ.", metrics.getReportType(), metrics.getLocationId());
//        } catch (Exception e) {
//            log.error("Failed to send RabbitMQ message for {} report, location {}",
//                    metrics.getReportType(), metrics.getLocationId(), e);
//            throw new RuntimeException("RabbitMQ send failed", e);
//        }
//    }
//}