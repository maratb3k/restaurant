package com.epam.edp.demo.service;

import com.epam.edp.demo.exception.EntityNotFountException;
import com.epam.edp.demo.exception.UserNotFoundException;
import com.epam.edp.demo.mapper.DishMapper;
import com.epam.edp.demo.mapper.LocationMapper;
import com.epam.edp.demo.model.constant.FeedbackSortType;
import com.epam.edp.demo.model.constant.FeedbackType;
import com.epam.edp.demo.model.dto.response.DishResponse;
import com.epam.edp.demo.model.dto.response.FeedbackResponse;
import com.epam.edp.demo.model.dto.response.LocationResponse;
import com.epam.edp.demo.model.dto.response.LocationSelectionResponse;
import com.epam.edp.demo.model.dto.request.FeedbacksByLocationRequest;
import com.epam.edp.demo.model.dto.request.LocationCreateRequest;
import com.epam.edp.demo.model.dto.request.LocationUpdateRequest;
import com.epam.edp.demo.model.dto.response.ApiResponse;
import com.epam.edp.demo.model.entity.AuthUser;
import com.epam.edp.demo.model.entity.Dish;
import com.epam.edp.demo.model.entity.Feedback;
import com.epam.edp.demo.model.entity.Location;
import com.epam.edp.demo.repository.AuthUserRepository;
import com.epam.edp.demo.repository.DishRepository;
import com.epam.edp.demo.repository.FeedbackRepository;
import com.epam.edp.demo.repository.LocationRepository;
import com.epam.edp.demo.storage.MinioStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {

    private final LocationRepository locationRepository;
    private final FeedbackRepository feedbackRepository;
    private final AuthUserRepository authUserRepository;
    private final MinioStorageService minioStorageService;
    private final DishRepository dishRepository;
    private final LocationMapper locationMapper;
    private final DishMapper dishMapper;
    private final TableService tableService;


    public ApiResponse saveLocation(LocationCreateRequest request, MultipartFile image) throws IOException {
        log.info("Creating location: {}", request);
        // saving location image
        byte[] bytes = image.getBytes();
        String imageKey = "location:" + UUID.randomUUID() + ":" + LocalDateTime.now();
        minioStorageService.saveImage(imageKey, bytes, "image/png");

        Location location = locationMapper.toEntity(request);
        location.setImageKey(imageKey);
        locationRepository.save(location);

        //todo tables should be created automatically

        return ApiResponse.builder()
                .success(true)
                .statusCode(201)
                .message("Location created successfully!")
                .build();
    }

    public ApiResponse getLocationById(String locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFountException("Location with id : %s not found" .formatted(locationId)));

        return ApiResponse.builder()
                .statusCode(200)
                .success(true)
                .data(locationMapper.toResponse(location))
                .build();
    }

    public ApiResponse getSpecialDishesByLocation(String locationId){
        log.info("Retrieving speciality dishes by location : {}", locationId);

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFountException("Location with id : %s not found".formatted(locationId)));

        List<String> specialDishesId = location.getSpecialDishesId();

        if (specialDishesId == null || specialDishesId.isEmpty()) {
            return ApiResponse.builder()
                    .statusCode(404)
                    .success(false)
                    .message("Special dishes not found")
                    .build();
        }

        List<DishResponse> dishResponses = new ArrayList<>();

        for (String dishId : specialDishesId) {
            Dish dish = dishRepository.findById(dishId)
                    .orElseThrow(() -> new EntityNotFountException("Dish not found with id: %s".formatted(dishId)));

            DishResponse response = dishMapper.toResponse(dish);
            dishResponses.add(response);
        }

        return ApiResponse.builder()
                .statusCode(200)
                .success(true)
                .data(dishResponses)
                .build();
    }

    public ApiResponse updateLocation(LocationUpdateRequest request){
        log.info("Updating location with id: {}", request.getLocationId());
        log.info("Updating location: {}", request);

        Location location = locationRepository.findById(request.getLocationId())
                .orElseThrow(() -> new EntityNotFountException("Location with id: %s not found".formatted(request.getLocationId())));

        //Save the url to database
        if (request.getSpecialDishesId() != null) {
            location.setSpecialDishesId(request.getSpecialDishesId());
        }

        locationRepository.save(location);

        return ApiResponse.<Void>builder()
                .statusCode(200)
                .success(true)
                .message("Updated successfully")
                .build();
    }

    public ApiResponse getFeedbacksByLocation(FeedbacksByLocationRequest request, String locationId) throws Exception {
        log.info("Retrieving feedbacks of chosen location with id: {}", locationId);

        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<Feedback> page = feedbackRepository.findByLocationId(locationId, pageable);

        List<Feedback> allFeedbacks = new ArrayList<>(page.getContent());

        Comparator<Feedback> comparator = getComparator(request);

        if (comparator != null) {
            allFeedbacks.sort(request.getAscending() ? comparator : comparator.reversed()); // feedbacks are sorted
        }

        int pageFromIdx = request.getPage() * request.getSize();
        int pageToIdx = Math.min(pageFromIdx + request.getSize(), allFeedbacks.size());
        int totalElements = allFeedbacks.size();
        int totalPages = totalElements / request.getSize() + 1;

        List<Feedback> feedbacks = pageFromIdx < allFeedbacks.size() ? allFeedbacks.subList(pageFromIdx, pageToIdx) : Collections.emptyList();

        List<FeedbackResponse> feedbackResponses = mapToFeedbackResponse(feedbacks, request.getFeedbackType()); // this will be returned to frontend

        Map<String, Object> result = new HashMap<>();
        result.put("totalPages", totalPages);
        result.put("totalElements", totalElements);
        result.put("size", result.size());
        result.put("content", feedbackResponses);

        return ApiResponse.<List<FeedbackResponse>>builder()
                .statusCode(200)
                .success(true)
                .data(feedbackResponses)
                .build();
    }

    public ApiResponse getLocationsForSelection() {
        log.info("Fetching locations for selection...");
        List<Location> locations = locationRepository.findAll();

        if (locations.isEmpty()) {
            return ApiResponse.<List<LocationSelectionResponse>>builder()
                    .message("Location not found")
                    .success(false)
                    .statusCode(404)
                    .build();
        }

        List<LocationSelectionResponse> locationResponses = locationMapper.toLocationSelectionResponse(locations);
        return ApiResponse.<List<LocationSelectionResponse>>builder()
                .statusCode(200)
                .success(true)
                .data(locationResponses)
                .build();
    }

    public ApiResponse getLocations() throws Exception {
        log.info("Retrieving locations...");

        List<Location> locations = locationRepository.findAll();
        if (locations.isEmpty()) {
            return ApiResponse.<List<LocationResponse>>builder()
                    .message("No locations found")
                    .success(false)
                    .statusCode(404)
                    .build();
        }

        List<LocationResponse> locationResponses = locationMapper.toResponses(locations);
        return ApiResponse.<List<LocationResponse>>builder()
                .statusCode(200)
                .success(true)
                .data(locationResponses)
                .build();
    }

    private List<FeedbackResponse> mapToFeedbackResponse(List<Feedback> feedbacks, FeedbackType feedbackType) {

        List<FeedbackResponse> feedbackResponses = new ArrayList<>();

        for (Feedback feedback : feedbacks) {
            AuthUser authUser = null;
            String imageUrl = null;
            String feedbackGiverEmail = feedback.getFeedbackGiverEmail();

            if (!feedbackGiverEmail.contains("@visitor.temp")) {
                authUser = authUserRepository.findByEmail(feedbackGiverEmail)
                        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + feedback.getFeedbackGiverEmail()));

                Long preSignedUrlExpirationTimeHours = 1L;
//                imageUrl = authUser.getImageKey() != null ?
//                        s3Service.generatePreSignedUrl(authUser.getImageKey(), preSignedUrlExpirationTimeHours) : null;
//
            }

            feedbackResponses.add(FeedbackResponse.builder()
                    .id(feedback.getId())
                    .rate(String.valueOf(getRatingByType(feedback, feedbackType)))
                    .comment(getCommentByType(feedback, feedbackType))
                    .userName(authUser == null ? "Anonymous" : authUser.getFirstName())
                    .date(String.valueOf(feedback.getDate()))
                    .locationId(feedback.getLocationId())
                    .userAvatarUrl(imageUrl)
                    .build());
        }

        return feedbackResponses;
    }

    private int getRatingByType(Feedback feedback, FeedbackType feedbackType) {
        return feedbackType == FeedbackType.CUISINE ? feedback.getCuisineRating() : feedback.getServiceRating();
    }

    private String getCommentByType(Feedback feedback, FeedbackType feedbackType) {
        return feedbackType == FeedbackType.CUISINE ? feedback.getCuisineComment() : feedback.getServiceComment();
    }

    private Comparator<Feedback> getComparator(FeedbacksByLocationRequest request) {
        Comparator<Feedback> comparator = null;

        if (request.getSortBy() == FeedbackSortType.TOP_RATED || request.getSortBy() == FeedbackSortType.LOWEST_RATED) {
            boolean isCuisine = request.getFeedbackType() == FeedbackType.CUISINE;
            comparator = Comparator.comparing(fb -> Optional.ofNullable(
                    isCuisine ? fb.getCuisineRating() : fb.getServiceRating()
            ).orElse(0));
            if (request.getSortBy() == FeedbackSortType.TOP_RATED) {
                comparator = comparator.reversed();
            }
        } else if (request.getSortBy() == FeedbackSortType.NEWEST) {
            comparator = Comparator.comparing(Feedback::getDate).reversed();
        } else if (request.getSortBy() == FeedbackSortType.OLDEST) {
            comparator = Comparator.comparing(Feedback::getDate);
        }
        return comparator;
    }
}