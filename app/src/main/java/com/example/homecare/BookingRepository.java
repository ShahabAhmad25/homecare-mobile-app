package com.example.homecare;

import java.util.ArrayList;
import java.util.List;

public class BookingRepository {
    private static BookingRepository instance;
    private List<Booking> bookings;

    private BookingRepository() {
        bookings = new ArrayList<>();
        // Pre-fill with some dummy data
        
        // A completed booking with provider info
        bookings.add(new Booking("1", "AC Repair", "Rs. 2600", "Rs. 2500", "Cash After Service", "20 Oct, 2023", "10:00 AM", "Home", "COMPLETED", "Jack Richards", "4.9", ""));
        
        // An accepted booking with provider info - This will show the "View Profile" link
        bookings.add(new Booking("2", "Home Cleaning", "Rs. 1500", "Rs. 1500", "Cash", "28 Oct, 2023", "02:00 PM", "Apartment 4B", "ACCEPTED", "Sarah Jenkins", "4.7", ""));
        
        // A pending booking - Now with provider info from our ML model
        bookings.add(new Booking("3", "Plumbing", "Rs. 800", "Rs. 800", "Digital Payment", "30 Oct, 2023", "11:30 AM", "Sector F-11", "PENDING", "Zahid Ahmed", "4.6", ""));
    }

    public static synchronized BookingRepository getInstance() {
        if (instance == null) {
            instance = new BookingRepository();
        }
        return instance;
    }

    public void addBooking(Booking booking) {
        bookings.add(0, booking); // Add to the top of the list
    }

    public void removeBooking(String bookingId) {
        bookings.removeIf(b -> b.getId().equals(bookingId));
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
