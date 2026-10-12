package com.example.homecare;

public class ProviderOrder {
    private String orderId;
    private String customerName;
    private String serviceName;
    private String dateTime;
    private String price;
    private String status;

    public ProviderOrder(String orderId, String customerName, String serviceName, String dateTime, String price, String status) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.serviceName = serviceName;
        this.dateTime = dateTime;
        this.price = price;
        this.status = status;
    }

    public String getOrderId() { return orderId; }
    public String getCustomerName() { return customerName; }
    public String getServiceName() { return serviceName; }
    public String getDateTime() { return dateTime; }
    public String getPrice() { return price; }
    public String getStatus() { return status; }
}
