package vk.kirisaki.service;

import java.time.LocalDateTime;
import java.util.List;

import vk.kirisaki.models.RentableObject;

public class RentService {
    private List<RentableObject> rentableObjects = List.of(
        new RentableObject("Car"),
        new RentableObject("Plates")
    );

    public List<RentableObject> getRentableObjects() {
        return rentableObjects;
    }

    public List<RentableObject> getAvailableRentableObjects(LocalDateTime dateTime) {
        return rentableObjects.stream().filter(o -> o.isAvailable(dateTime)).toList();
    }
}
