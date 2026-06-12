package com.restaurant.booking.mapper;

import com.restaurant.booking.model.dto.ReservationDto;
import com.restaurant.booking.model.entity.Reservation;
import com.restaurant.booking.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

    private final LocationRepository locationRepository;


    public List<ReservationDto> mapToDtoList(List<Reservation> reservations){
        return reservations.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ReservationDto mapToDto(Reservation reservation){
        return ReservationDto.builder()
                .id(reservation.getId())
                .feedbackId(reservation.getFeedbackId() == null ? null : reservation.getFeedbackId())
                .guestNumber(reservation.getGuestNumber().toString())
                .date(reservation.getDate().toString())
                .locationAddress(locationRepository.findById(reservation.getLocationId()).get().getAddress())
                .preOrder("PRE_ORDER_INFO") //
                .status(reservation.getStatus().toString())
                .tableNumber(reservation.getTableNumber().toString())
                .timeSlot(reservation.getStartTime() + " - "+ reservation.getEndTime())
                .build();
    }
}
