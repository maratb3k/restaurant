//package com.epam.edp.demo.model.entity;
//
//import com.amazonaws.services.dynamodbv2.datamodeling.*;
//import com.epam.edp.demo.model.dto.LocationMetric;
//import com.epam.edp.demo.model.dto.StaffMetric;
//import com.epam.edp.demo.util.LocalDateConverter;
//import com.epam.edp.demo.util.report.LocationMetricListConverter;
//import com.epam.edp.demo.util.report.StaffMetricListConverter;
//import lombok.*;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Builder
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@DynamoDBTable(tableName = "Reports")
//public class Reports {
//
//    private String id;
//    private String reportType;
//    private LocalDate periodStart;
//    private LocalDate periodEnd;
//    private String locationId;
//    private List<StaffMetric> staffMetrics;
//    private List<LocationMetric> locationMetrics;
//    private String downloadLink;
//
//    @DynamoDBHashKey(attributeName = "reportType")
//    public String getReportType() {
//        return reportType;
//    }
//
//    public void setReportType(String reportType) {
//        this.reportType = reportType;
//    }
//
//    @DynamoDBRangeKey(attributeName = "periodStart")
//    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
//    public LocalDate getPeriodStart() {
//        return periodStart;
//    }
//
//    public void setPeriodStart(LocalDate periodStart) {
//        this.periodStart = periodStart;
//    }
//
//    @DynamoDBAttribute(attributeName = "periodEnd")
//    @DynamoDBTypeConverted(converter = LocalDateConverter.class)
//    public LocalDate getPeriodEnd() {
//        return periodEnd;
//    }
//
//    public void setPeriodEnd(LocalDate periodEnd) {
//        this.periodEnd = periodEnd;
//    }
//
//    @DynamoDBAttribute(attributeName = "locationId")
//    public String getLocationId() {
//        return locationId;
//    }
//
//    public void setLocationId(String locationId) {
//        this.locationId = locationId;
//    }
//
//    @DynamoDBAttribute(attributeName = "staffMetrics")
//    @DynamoDBTypeConverted(converter = StaffMetricListConverter.class)
//    public List<StaffMetric> getStaffMetrics() {
//        return staffMetrics;
//    }
//
//    public void setStaffMetrics(List<StaffMetric> staffMetrics) {
//        this.staffMetrics = staffMetrics;
//    }
//
//    @DynamoDBAttribute(attributeName = "locationMetrics")
//    @DynamoDBTypeConverted(converter = LocationMetricListConverter.class)
//    public List<LocationMetric> getLocationMetrics() {
//        return locationMetrics;
//    }
//
//    public void setLocationMetrics(List<LocationMetric> locationMetrics) {
//        this.locationMetrics = locationMetrics;
//    }
//
//    @DynamoDBAttribute(attributeName = "downloadLink")
//    public String getDownloadLink() {
//        return downloadLink;
//    }
//
//    public void setDownloadLink(String downloadLink) {
//        this.downloadLink = downloadLink;
//    }
//}