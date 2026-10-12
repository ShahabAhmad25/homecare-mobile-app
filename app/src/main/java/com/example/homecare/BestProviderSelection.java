package com.example.homecare;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A Linear Scoring Algorithm for selecting the best service provider.
 * This implements a Weighted Sum Model, which is a fundamental algorithm in 
 * multi-criteria decision making and the basis for linear regression.
 */
public class BestProviderSelection {

    // Weights for our "Model" (Total = 1.0)
    private static final double WEIGHT_RATING = 0.3;
    private static final double WEIGHT_EXPERIENCE = 0.5;
    private static final double WEIGHT_PRICE = 0.2;

    /**
     * Calculates a 'Match Score' for a provider item based on its features.
     */
    public static double calculateProviderScore(ProviderItem provider) {
        double normalizedRating = 0.0;
        try {
            normalizedRating = Double.parseDouble(provider.getRating()) / 5.0;
        } catch (Exception ignored) {}

        double normalizedExperience = Math.min(provider.getExperience() / 15.0, 1.0);

        double normalizedPrice = 0.5;
        try {
            String priceStr = provider.getPrice().replaceAll("[^0-9]", "");
            if (!priceStr.isEmpty()) {
                double priceVal = Double.parseDouble(priceStr);
                normalizedPrice = 1.0 - Math.min(priceVal / 3000.0, 1.0);
            }
        } catch (Exception ignored) {}

        return (WEIGHT_RATING * normalizedRating) +
               (WEIGHT_EXPERIENCE * normalizedExperience) +
               (WEIGHT_PRICE * normalizedPrice);
    }

    /**
     * Sorts the list of providers using our ranking algorithm.
     */
    public static void sortProvidersByBestMatch(List<ProviderItem> providers) {
        Collections.sort(providers, (p1, p2) -> Double.compare(calculateProviderScore(p2), calculateProviderScore(p1)));
    }

    /**
     * Calculates a 'Match Score' for a service item based on its features.
     * Higher score means a better match.
     */
    public static double calculateMatchScore(ServiceItem service) {
        // 1. Normalize Rating (0 to 5) to a 0-1 scale
        double normalizedRating = 0.0;
        try {
            normalizedRating = Double.parseDouble(service.getRatingString()) / 5.0;
        } catch (Exception ignored) {}

        // 2. Normalize Experience (Assuming max 15 years for this project)
        double normalizedExperience = 0.5; // Default if experience is not in ServiceItem

        // 3. Normalize Price (Lower price is better)
        double normalizedPrice = 0.5; // Default middle value
        try {
            String priceStr = service.getPrice().replaceAll("[^0-9]", "");
            if (!priceStr.isEmpty()) {
                double priceVal = Double.parseDouble(priceStr);
                normalizedPrice = 1.0 - Math.min(priceVal / 3000.0, 1.0);
            }
        } catch (Exception ignored) {}

        return (WEIGHT_RATING * normalizedRating) +
               (WEIGHT_EXPERIENCE * normalizedExperience) +
               (WEIGHT_PRICE * normalizedPrice);
    }

    /**
     * Sorts the list of services using our ranking algorithm.
     */
    public static void sortServicesByBestMatch(List<ServiceItem> services) {
        Collections.sort(services, (s1, s2) -> Double.compare(calculateMatchScore(s2), calculateMatchScore(s1)));
    }
}
