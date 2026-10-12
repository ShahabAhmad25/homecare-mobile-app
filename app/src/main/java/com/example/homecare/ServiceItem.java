package com.example.homecare;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

public class ServiceItem {
    private String category;
    private String name;
    private Object base_price; // Can be String or Double in Firestore
    private Object rating;     // Can be String or Double in Firestore
    private String image_url;

    // Empty constructor needed for Firebase
    public ServiceItem() {}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @PropertyName("base_price")
    public Object getBase_price() {
        return base_price;
    }

    @PropertyName("base_price")
    public void setBase_price(Object base_price) {
        this.base_price = base_price;
    }

    @PropertyName("image_url")
    public String getImage_url() {
        return image_url;
    }

    @PropertyName("image_url")
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    // Convenience methods for UI - Excluded from Firestore mapping
    @Exclude
    public String getPrice() {
        if (base_price == null) return "Rs. 0";
        String priceStr = String.valueOf(base_price);
        // Replace $ with Rs. if present, or prepend Rs. if it's just a number
        if (priceStr.contains("$")) {
            return priceStr.replace("$", "Rs. ");
        } else if (!priceStr.isEmpty() && !priceStr.startsWith("Rs.")) {
            return "Rs. " + priceStr;
        }
        return priceStr;
    }

    @Exclude
    public String getRatingString() {
        return rating != null ? String.valueOf(rating) : "0.0";
    }

    @Exclude
    public String getImageUrl() {
        return image_url;
    }
}
