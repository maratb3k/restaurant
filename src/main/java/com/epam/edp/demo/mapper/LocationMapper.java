package com.epam.edp.demo.mapper;

import com.epam.edp.demo.model.dto.response.LocationResponse;
import com.epam.edp.demo.model.dto.response.LocationSelectionResponse;
import com.epam.edp.demo.model.dto.request.LocationCreateRequest;
import com.epam.edp.demo.model.entity.Location;
import com.epam.edp.demo.storage.MinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LocationMapper {

    private final MinioStorageService minioStorageService;

    @Value("${preSignedUrlExpirationTimeHours}")
    private Long preSignedUrlExpirationTimeHours;

    public Location toEntity(LocationCreateRequest dto) {
        return Location.builder()
                .id(UUID.randomUUID().toString())
                .address(dto.getAddress())
                .description(dto.getDescription())
                .averageOccupancy(dto.getAverageOccupancy())
                .totalCapacity(dto.getTotalCapacity())
                .build();
    }

    public LocationResponse toResponse(Location entity) {
        return LocationResponse.builder()
                .id(entity.getId())
                .address(entity.getAddress())
                .description(entity.getDescription())
                .totalCapacity(String.valueOf(entity.getTotalCapacity()))
                .averageOccupancy(String.valueOf(entity.getAverageOccupancy()))
                .rating(String.valueOf(entity.getRating()))
                .imageUrl(entity.getImageKey() != null ?
                        minioStorageService.generatePreSignedUrl(entity.getImageKey(), preSignedUrlExpirationTimeHours) : "")
                .build();
    }

    public List<LocationResponse> toResponses(List<Location> locations) {
        return locations.stream().map(this::toResponse).toList();
    }

    public List<LocationSelectionResponse> toLocationSelectionResponse(List<Location> locations) {
        return locations.stream().map(location -> LocationSelectionResponse.builder()
                .id(location.getId())
                .address(location.getAddress())
                .build()).toList();
    }
}
