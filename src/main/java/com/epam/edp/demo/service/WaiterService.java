package com.epam.edp.demo.service;

import com.epam.edp.demo.exception.EntityNotFountException;
import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.model.entity.Location;
import com.epam.edp.demo.model.entity.Waiter;
import com.epam.edp.demo.repository.LocationRepository;
import com.epam.edp.demo.repository.WaiterRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WaiterService {

    private final WaiterRepository waiterRepository;
    private final LocationRepository locationRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ApiResponse handleCreateWaiter(String email, String locationId) {
        try {

            if (email == null || locationId == null) {
                return ApiResponse.<Void>builder()
                        .statusCode(400)
                        .success(false)
                        .message("Missing required fields: email or locationId")
                        .build();
            }

            Optional<Location> locationById = locationRepository.findById(locationId);
            if(locationById.isEmpty()){
                throw new EntityNotFountException("Location not found with id: "+ locationById);
            }

            Waiter newWaiter = Waiter.builder()
                    .id(UUID.randomUUID().toString())
                    .email(email)
                    .locationId(locationId)
                    .build();

            waiterRepository.save(newWaiter);
            return ApiResponse.<Void>builder()
                    .statusCode(201)
                    .success(false)
                    .message("Waiter created successfully")
                    .build();

        } catch (Exception e) {
            return ApiResponse.<Void>builder()
                    .statusCode(500)
                    .success(false)
                    .message("Error creating waiter: " + e.getMessage())
                    .build();
        }
    }

}
