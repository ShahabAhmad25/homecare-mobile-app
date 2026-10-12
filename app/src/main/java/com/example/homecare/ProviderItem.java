package com.example.homecare;

public class ProviderItem {
    private String id;
    private String name;
    private String category;
    private int experience;
    private String rating;
    private String price;
    private String imageUrl;

    public ProviderItem() {}

    public ProviderItem(String id, String name, String category, int experience, String rating, String price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.experience = experience;
        this.rating = rating;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
