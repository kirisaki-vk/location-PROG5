package main

import (
	"time"
)

type RentService struct {
	RentableObjects []RentableObject
}

func NewRentService() *RentService {
	return &RentService{
		RentableObjects: []RentableObject{
			{Name: "Car"},
			{Name: "Plates"},
		},
	}
}

func (rs *RentService) GetAvailableObjects(atTime time.Time) []RentableObject {
	var available []RentableObject
	for _, obj := range rs.RentableObjects {
		if obj.IsAvailable(atTime) {
			available = append(available, obj)
		}
	}
	return available
}
