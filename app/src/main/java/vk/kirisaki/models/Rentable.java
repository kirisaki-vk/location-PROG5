package vk.kirisaki.models;

import java.time.LocalDateTime;

public interface Rentable {
    public void rent(LocalDateTime dateTime);
    public void book(LocalDateTime dateTime);
    public void returnBack(LocalDateTime dateTime);
    public boolean isAvailable(LocalDateTime dateTime);
}
