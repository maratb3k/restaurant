//package com.epam.edp.demo.service;
//
//
//import com.epam.edp.demo.model.constant.PerformanceType;
//import com.epam.edp.demo.model.dto.LocationMetric;
//import com.epam.edp.demo.model.dto.StaffMetric;
//import com.epam.edp.demo.model.entity.*;
//import com.epam.edp.demo.repository.*;
//import com.epam.edp.demo.storage.S3Service;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.io.StringWriter;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
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
//public class ReportService {
//
//    private final OrderRepository orderRepository;
//    private final ReservationRepository reservationRepository;
//    private final FeedbackRepository feedbackRepository;
//    private final WaiterRepository waiterRepository;
//    private final LocationRepository locationRepository;
//    private final DishRepository dishRepository;
//    private final ReportRepository reportRepository;
//    private final S3Service s3Service;
//    private final ObjectMapper objectMapper;
//    private final RabbitTemplate rabbitTemplate;
//
//    @Value("${rabbitmq.exchange.name}")
//    private String exchangeName;
//
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;
//
//    public Reports generateReports(String performanceType, String startTime, String endTime, String locationId) {
//        try {
//            LocalDate periodEnd = LocalDate.parse(endTime);
//            LocalDate periodStart = LocalDate.parse(startTime);
//            LocalDate prevEnd = periodStart.minusDays(1);
//            LocalDate prevStart = prevEnd.minusDays(6);
//
//            log.info("Generating reports for locationId {}  from {} to {}", locationId, periodStart, periodEnd);
//            Reports reportMetric;
//            if (PerformanceType.STAFF.equals(PerformanceType.valueOf(performanceType.toUpperCase()))) {
//                reportMetric = calculateStaffPerformance(locationId, periodStart, periodEnd, prevStart, prevEnd);
//            } else if (PerformanceType.LOCATION.equals(PerformanceType.valueOf(performanceType.toUpperCase()))) {
//                reportMetric = calculateLocationPerformance(periodStart, periodEnd, prevStart, prevEnd);
//            } else {
//                throw new IllegalArgumentException("Performance type is not correct");
//            }
//
//            reportRepository.save(reportMetric);
//            String csvContent = generateCsvContent(reportMetric);
//            String fileName = String.format("%s_%s_%s.csv",
//                    reportMetric.getReportType().toLowerCase(),
//                    reportMetric.getPeriodStart(),
//                    reportMetric.getLocationId());
//
//            // Use S3Service to upload the CSV file to S3 and get a download link
//            String link = s3Service.uploadCsvAndGetDownloadUrl(fileName, csvContent);
//            reportMetric.setDownloadLink(link);
////            sendToRabbitMQ(reportMetric);
//            return reportMetric;
//        } catch (Exception e) {
//            log.error("Failed to generate report: type={}, locationId={}, from={} to={}",
//                    performanceType, locationId, startTime, endTime, e);
//            throw new RuntimeException("Report generation failed", e);
//        }
//    }
//
//
//    private String generateCsvContent(Reports reportMetrics) {
//        StringWriter writer = new StringWriter();
//
//        if ("STAFF_PERFORMANCE".equals(reportMetrics.getReportType())) {
//            // Write the header for staff performance report
//            writer.write("waiterId,waiterEmail,workingHours,ordersProcessed,deltaOrdersPercent,avgServiceFeedback,minServiceFeedback,deltaMinFeedbackPercent,periodStart,periodEnd,locationId\n");
//
//            // Write each staff metric as a row in the CSV
//            for (StaffMetric staffMetric : reportMetrics.getStaffMetrics()) {
//                writer.write(String.format("%s,%s,%d,%d,%.2f,%.2f,%d,%.2f,%s,%s,%s\n",
//                        staffMetric.getWaiterId(),
//                        staffMetric.getWaiterEmail(),
//                        staffMetric.getWorkingHours(),
//                        staffMetric.getOrdersProcessed(),
//                        staffMetric.getDeltaOrdersPercent(),
//                        staffMetric.getAvgServiceFeedback(),
//                        staffMetric.getMinServiceFeedback(),
//                        staffMetric.getDeltaMinFeedbackPercent(),
//                        reportMetrics.getPeriodStart(),
//                        reportMetrics.getPeriodEnd(),
//                        reportMetrics.getLocationId()));
//            }
//        } else if ("LOCATION_COMPARISON".equals(reportMetrics.getReportType())) {
//            // Write the header for location comparison report
//            writer.write("locationId,ordersProcessed,deltaOrdersPercent,avgCuisineFeedback,minCuisineFeedback,deltaAvgFeedbackPercent,revenue,deltaRevenuePercent,periodStart,periodEnd\n");
//
//            // Write each location metric as a row in the CSV
//            for (LocationMetric locationMetric : reportMetrics.getLocationMetrics()) {
//                writer.write(String.format("%s,%d,%.2f,%.2f,%d,%.2f,%.2f,%.2f,%s,%s\n",
//                        locationMetric.getLocationId(),
//                        locationMetric.getOrdersProcessed(),
//                        locationMetric.getDeltaOrdersPercent(),
//                        locationMetric.getAvgCuisineFeedback(),
//                        locationMetric.getMinCuisineFeedback(),
//                        locationMetric.getDeltaAvgFeedbackPercent(),
//                        locationMetric.getRevenue(),
//                        locationMetric.getDeltaRevenuePercent(),
//                        reportMetrics.getPeriodStart(),
//                        reportMetrics.getPeriodEnd()));
//            }
//        }
//        return writer.toString();
//    }
//
//
//    public Reports calculateStaffPerformance(String locationId, LocalDate periodStart, LocalDate periodEnd, LocalDate prevStart, LocalDate prevEnd) {
//        log.info("Calculating staff performance for locationId: {}, period: {} to {}", locationId, periodStart, periodEnd);
//
//        List<Waiter> waiters = waiterRepository.findAllByLocationId(locationId);
//        log.info("Fetched {} waiters from DB for locationId={}", waiters.size(), locationId);
//        waiters.forEach(waiter -> log.debug("Waiter: id={}, email={}, timeSlots={}", waiter.getId(), waiter.getEmail(), waiter.getTimeSlots()));
//
//        List<StaffMetric> staffMetrics = new ArrayList<>();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//
//        for (Waiter waiter : waiters) {
//            // Working hours
//            int workingHours = waiter.getTimeSlots().entrySet().stream()
//                    .filter(entry -> {
//                        try {
//                            String[] parts = entry.getKey().split(" ");
//                            if (parts.length < 3) return false;
//
//                            String startStr = parts[0] + " " + parts[1];
//                            String endStr = parts[0] + " " + parts[2];
//                            LocalDateTime startTime = LocalDateTime.parse(startStr, formatter);
//                            LocalDateTime endTime = LocalDateTime.parse(endStr, formatter);
//
//                            LocalDateTime periodStartDateTime = periodStart.atStartOfDay();
//                            LocalDateTime periodEndDateTime = periodEnd.atTime(LocalTime.MAX);
//
//                            return !(endTime.isBefore(periodStartDateTime) || startTime.isAfter(periodEndDateTime));
//                        } catch (Exception e) {
//                            log.warn("Invalid time format in waiter {} timeSlots: {}", waiter.getId(), entry.getKey());
//                            return false;
//                        }
//                    })
//                    .mapToInt(Map.Entry::getValue)
//                    .sum();
//
//            log.debug("Waiter {} workingHours in period: {}", waiter.getId(), workingHours);
//
//            // Current reservations and orders
//            List<Reservation> reservations = reservationRepository.findByAssignedWaiterIdAndDateBetween(waiter.getId(), periodStart, periodEnd);
//            log.debug("Waiter {} has {} reservations in current period", waiter.getId(), reservations.size());
//            List<Order> orders = orderRepository.findByReservationIdIn(
//                    reservations.stream().map(Reservation::getId).collect(Collectors.toList()));
//            log.info("Waiter {} has {} orders in current period", waiter.getId(), orders.size());
//
//            int ordersProcessed = orders.size();
//
//            // Previous period reservations and orders
//            List<Reservation> prevReservations = reservationRepository.findByAssignedWaiterIdAndDateBetween(waiter.getId(), prevStart, prevEnd);
//            log.info("Waiter {} has {} reservations in previous period", waiter.getId(), prevReservations.size());
//            List<Order> prevOrders = orderRepository.findByReservationIdIn(
//                    prevReservations.stream().map(Reservation::getId).collect(Collectors.toList()));
//            log.info("Waiter {} has {} orders in previous period", waiter.getId(), prevOrders.size());
//
//            double deltaOrdersPercent = prevOrders.isEmpty() ? 0 : ((ordersProcessed - prevOrders.size()) * 100.0 / prevOrders.size());
//
//            // Feedbacks (current)
//            List<Feedback> feedbacks = feedbackRepository.findAllById(
//                    reservations.stream().map(Reservation::getFeedbackId).filter(Objects::nonNull).collect(Collectors.toList()));
//            log.info("Waiter {} has {} feedbacks in current period", waiter.getId(), feedbacks.size());
//
//            double avgServiceFeedback = feedbacks.stream().mapToInt(Feedback::getServiceRating).average().orElse(0);
//            int minServiceFeedback = feedbacks.stream().mapToInt(Feedback::getServiceRating).min().orElse(0);
//
//            // Feedbacks (previous)
//            List<Feedback> prevFeedbacks = feedbackRepository.findAllById(
//                    prevReservations.stream().map(Reservation::getFeedbackId).filter(Objects::nonNull).collect(Collectors.toList()));
//            log.info("Waiter {} has {} feedbacks in previous period", waiter.getId(), prevFeedbacks.size());
//
//            int prevMinFeedback = prevFeedbacks.stream().mapToInt(Feedback::getServiceRating).min().orElse(0);
//            double deltaMinFeedbackPercent = prevMinFeedback == 0 ? 0 : ((minServiceFeedback - prevMinFeedback) * 100.0 / prevMinFeedback);
//
//            log.info("Waiter Performance: id={}, ordersProcessed={}, deltaOrdersPercent={}, avgFeedback={}, minFeedback={}, deltaMinFeedback={}",
//                    waiter.getId(), ordersProcessed, deltaOrdersPercent, avgServiceFeedback, minServiceFeedback, deltaMinFeedbackPercent);
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
//        Reports report = Reports.builder()
//                .reportType("STAFF_PERFORMANCE")
//                .periodStart(periodStart)
//                .periodEnd(periodEnd)
//                .locationId(locationId)
//                .staffMetrics(staffMetrics)
//                .build();
//
//        log.info("Generated staff performance report with {} metrics", staffMetrics.size());
//        return report;
//    }
//
//    private Reports calculateLocationPerformance(LocalDate periodStart, LocalDate periodEnd, LocalDate prevStart, LocalDate prevEnd) {
//        List<LocationMetric> locationMetrics = new ArrayList<>();
//
//        List<String> locationsIds = locationRepository.findAllIds();
//        for (String locationId : locationsIds) {
//            // Orders processed
//            List<Reservation> reservations = reservationRepository.findByLocationIdAndDateBetween(locationId, periodStart, periodEnd);
//            List<Order> orders = orderRepository.findByReservationIdIn(reservations.stream().map(Reservation::getId).collect(Collectors.toList()));
//            int ordersProcessed = orders.size();
//
//            // Delta orders
//            List<Reservation> prevReservations = reservationRepository.findByLocationIdAndDateBetween(locationId, prevStart, prevEnd);
//            List<Order> prevOrders = orderRepository.findByReservationIdIn(prevReservations.stream().map(Reservation::getId).collect(Collectors.toList()));
//            double deltaOrdersPercent = prevOrders.isEmpty() ? 0 : ((ordersProcessed - prevOrders.size()) * 100.0 / prevOrders.size());
//
//            // Average and Minimum Cuisine Feedback
//            List<Feedback> feedbacks = feedbackRepository.findAllByIdIn(reservations.stream().map(Reservation::getFeedbackId).filter(Objects::nonNull).collect(Collectors.toList()));
//            double avgCuisineFeedback = feedbacks.stream()
//                    .mapToInt(Feedback::getCuisineRating)
//                    .average()
//                    .orElse(0);
//            int minCuisineFeedback = feedbacks.stream()
//                    .mapToInt(Feedback::getCuisineRating)
//                    .min()
//                    .orElse(0);
//
//            // Delta Average Feedback
//            List<Feedback> prevFeedbacks = feedbackRepository.findAllByIdIn(prevReservations.stream().map(Reservation::getFeedbackId).filter(Objects::nonNull).collect(Collectors.toList()));
//            double prevAvgFeedback = prevFeedbacks.stream().mapToInt(Feedback::getCuisineRating).average().orElse(0);
//            double deltaAvgFeedbackPercent = prevAvgFeedback == 0 ? 0 : ((avgCuisineFeedback - prevAvgFeedback) * 100.0 / prevAvgFeedback);
//
//            // Fetch all dish IDs from to minimize DynamoDB calls
//            List<String> dishIds = orders.stream()
//                    .flatMap(order -> order.getOrders().stream())
//                    .map(FoodOrder::getDishId)
//                    .distinct()
//                    .collect(Collectors.toList());
//            List<Dish> dishes = dishRepository.findByIdIn(dishIds);
//            Map<String, Double> dishPrices = dishes.stream()
//                    .collect(Collectors.toMap(Dish::getId, Dish::getPrice, (p1, p2) -> p1));
//
//            // Revenue
//            double revenue = orders.stream()
//                    .flatMap(order -> order.getOrders().stream())
//                    .mapToDouble(foodOrder -> {
//                        Double price = dishPrices.get(foodOrder.getDishId());
//                        return price != null ? price * foodOrder.getQuantity() : 0.0;
//                    })
//                    .sum();
//
//            // Fetch dish IDs for previous orders
//            List<String> prevDishIds = prevOrders.stream()
//                    .flatMap(order -> order.getOrders().stream())
//                    .map(FoodOrder::getDishId)
//                    .distinct()
//                    .collect(Collectors.toList());
//            List<Dish> prevDishes = dishRepository.findByIdIn(prevDishIds);
//            Map<String, Double> prevDishPrices = prevDishes.stream()
//                    .collect(Collectors.toMap(Dish::getId, Dish::getPrice, (p1, p2) -> p1));
//
//            // Delta Revenue
//            double prevRevenue = prevOrders.stream()
//                    .flatMap(order -> order.getOrders().stream())
//                    .mapToDouble(foodOrder -> {
//                        Double price = prevDishPrices.get(foodOrder.getDishId());
//                        return price != null ? price * foodOrder.getQuantity() : 0.0;
//                    })
//                    .sum();
//            double deltaRevenuePercent = prevRevenue == 0 ? 0 : ((revenue - prevRevenue) * 100.0 / prevRevenue);
//
//            locationMetrics.add(LocationMetric.builder()
//                    .locationId(locationId)
//                    .ordersProcessed(ordersProcessed)
//                    .deltaOrdersPercent(deltaOrdersPercent)
//                    .avgCuisineFeedback(avgCuisineFeedback)
//                    .minCuisineFeedback(minCuisineFeedback)
//                    .deltaAvgFeedbackPercent(deltaAvgFeedbackPercent)
//                    .revenue(revenue)
//                    .deltaRevenuePercent(deltaRevenuePercent)
//                    .build());
//        }
//        return Reports.builder()
//                .reportType("LOCATION_COMPARISON")
//                .periodStart(periodStart)
//                .periodEnd(periodEnd)
//                .locationMetrics(locationMetrics)
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