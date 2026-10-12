package com.example.homecare;

public class Booking {
    private String id;
    private String serviceName;
    private String servicePrice;
    private String expectedPrice;
    private String paymentMethod;
    private String date;
    private String time;
    private String address;
    private String status; // PENDING, ACCEPTED, DECLINED, COMPLETED
    private String providerName;
    private String providerRating;
    private String providerImage;

    public Booking(String id, String serviceName, String servicePrice, String expectedPrice, String paymentMethod, String date, String time, String address, String status) {
        this.id = id;
        this.serviceName = serviceName;
        this.servicePrice = servicePrice;
        this.expectedPrice = expectedPrice;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.time = time;
        this.address = address;
        this.status = status;
    }

    // New constructor with provider info
    public Booking(String id, String serviceName, String servicePrice, String expectedPrice, String paymentMethod, String date, String time, String address, String status, String providerName, String providerRating, String providerImage) {
        this(id, serviceName, servicePrice, expectedPrice, paymentMethod, date, time, address, status);
        this.providerName = providerName;
        this.providerRating = providerRating;
        this.providerImage = providerImage;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getServiceName() { return serviceName; }
    public String getServicePrice() { return servicePrice; }
    public String getExpectedPrice() { return expectedPrice; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getAddress() { return address; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getProviderName() { return providerName; }
    public void setProviderName(String providerName) { this.providerName = providerName; }
    public String getProviderRating() { return providerRating; }
    public void setProviderRating(String providerRating) { this.providerRating = providerRating; }
    public String getProviderImage() { return providerImage; }
    public void setProviderImage(String providerImage) { this.providerImage = providerImage; }
}
