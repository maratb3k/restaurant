package com.restaurant.booking.storage;

import com.restaurant.booking.exception.ImageUploadException;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MinioStorageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Value("${profile-picture-folder}")
    private String profilePictureFolder;

    @Value("${location-picture-folder}")
    private String locationPictureFolder;

    @Value("${dish-picture-folder}")
    private String dishPictureFolder;

    @Value("${report-folder}")
    private String reportFolder;

    private static final int EXPIRATION_HOURS = 1;

    public void saveImage(String fileName, byte[] image, String contentType) throws ImageUploadException {
        if (fileName == null || fileName.isBlank()) {
            throw new ImageUploadException("File name cannot be null or empty");
        }
        if (image == null || image.length == 0) {
            throw new ImageUploadException("Image data cannot be null or empty");
        }

        String folder = resolveFolder(fileName);
        String objectName = folder + fileName;

        try {
            ensureBucketExists();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(image), image.length, -1)
                            .contentType(contentType)
                            .build()
            );

            log.info("Image uploaded to MinIO: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("Failed to upload image to MinIO", e);
            throw new ImageUploadException("Error uploading image to storage.");
        }
    }

    public String generatePreSignedUrl(String fileName, long expirationInHours) throws ImageUploadException {
        if (fileName == null || fileName.isBlank()) {
            throw new ImageUploadException("File name cannot be null or empty");
        }

        String folder = resolveFolder(fileName);
        String objectName = folder + fileName;

        try {
            ensureBucketExists();

            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(objectName)
                            .expiry((int) TimeUnit.HOURS.toSeconds(expirationInHours))
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to generate pre-signed URL for MinIO", e);
            throw new ImageUploadException("Error generating pre-signed URL.");
        }
    }

    public String uploadCsvAndGetDownloadUrl(String fileName, String csvContent) {
        if (!fileName.startsWith("report_")) {
            fileName = "report_" + fileName;
        }

        String objectName = reportFolder + fileName;

        try {
            ensureBucketExists();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(new ByteArrayInputStream(csvContent.getBytes()), csvContent.length(), -1)
                            .contentType("text/csv")
                            .build()
            );

            return generatePreSignedUrl(fileName, EXPIRATION_HOURS);
        } catch (Exception e) {
            log.error("Failed to upload CSV to MinIO", e);
            throw new RuntimeException("CSV upload failed", e);
        }
    }

    private String resolveFolder(String fileName) {
        if (fileName.startsWith("location")) return locationPictureFolder;
        if (fileName.startsWith("dish")) return dishPictureFolder;
        if (fileName.startsWith("report")) return reportFolder;
        return profilePictureFolder;
    }

    private void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("Created MinIO bucket: {}", bucketName);
        }
    }
}
