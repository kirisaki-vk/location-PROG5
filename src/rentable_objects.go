package main

import (
	"fmt"
	"time"
)

type BookingSlot struct {
	StartTime time.Time
	EndTime   time.Time
}

type RentableObject struct {
	Name              string
	IsCurrentlyRented bool
	BookedSlots       []BookingSlot
}

func (ro *RentableObject) IsAvailable(atTime time.Time) bool {
	if ro.IsCurrentlyRented {
		return false
	}

	for _, slot := range ro.BookedSlots {
		if !atTime.Before(slot.StartTime) && atTime.Before(slot.EndTime) {
			return false
		}
	}
	return true
}

func (ro *RentableObject) Rent(atTime time.Time) error {
	if !ro.IsAvailable(atTime) {
		return fmt.Errorf("object is not available for rent at the requested time")
	}
	ro.IsCurrentlyRented = true
	return nil
}

func (ro *RentableObject) Book(atTime time.Time) error {
	if !ro.IsAvailable(atTime) {
		return fmt.Errorf("object is not available for booking at the requested time")
	}

	endTime := atTime.Add(time.Hour) // Default 1-hour booking
	ro.BookedSlots = append(ro.BookedSlots, BookingSlot{
		StartTime: atTime,
		EndTime:   endTime,
	})
	return nil
}

func (ro *RentableObject) Return(atTime time.Time) error {
	if !ro.IsCurrentlyRented {
		return fmt.Errorf("object is not currently rented")
	}
	ro.IsCurrentlyRented = false
	return nil
}
