package vk.kirisaki.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RentableObject implements Rentable {
    private final String name;
    private boolean isCurrentlyRented;
    private LocalDateTime currentRentalEndTime;
    private List<BookingSlot> bookedSlots = new ArrayList<>();

    public RentableObject(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void rent(LocalDateTime dateTime) {
        if (!isAvailable(dateTime)) {
            throw new IllegalStateException(name + " is not available for rent at the requested time");
        }
        this.isCurrentlyRented = true;
        this.currentRentalEndTime = null;
    }

    @Override
    public void book(LocalDateTime dateTime) {
        if (!isAvailable(dateTime)) {
            throw new IllegalStateException(name + " is not available for booking at the requested time");
        }
        bookedSlots.add(new BookingSlot(dateTime, dateTime.plusHours(1)));
    }

    @Override
    public void returnBack(LocalDateTime dateTime) {
        if (!isCurrentlyRented) {
            throw new IllegalStateException(name + "is not currently rented");
        }
        this.isCurrentlyRented = false;
        this.currentRentalEndTime = dateTime;
    }

    @Override
    public boolean isAvailable(LocalDateTime dateTime) {
        if (isCurrentlyRented && (currentRentalEndTime == null || dateTime.isBefore(currentRentalEndTime))) {
            return false;
        }
        
        for (BookingSlot slot : bookedSlots) {
            if (dateTime.isAfter(slot.getStartTime()) && dateTime.isBefore(slot.getEndTime())) {
                return false;
            }
        }
        
        return true;
    }

    private static class BookingSlot {
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public BookingSlot(LocalDateTime startTime, LocalDateTime endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }
    }
}