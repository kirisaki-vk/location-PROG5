package vk.kirisaki;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

import vk.kirisaki.models.RentableObject;
import vk.kirisaki.service.RentService;

public class RentalTUI {
    private final RentService rentService;
    private final Scanner scanner;
    private final DateTimeFormatter dateTimeFormatter;

    public RentalTUI() {
        this.rentService = new RentService();
        this.scanner = new Scanner(System.in);
        this.dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    }

    public void start() {
        System.out.println("=== Rental Service Management System ===");
        
        while (true) {
            printMainMenu();
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    listAllObjects();
                    break;
                case "2":
                    checkAvailability();
                    break;
                case "3":
                    rentObject();
                    break;
                case "4":
                    bookObject();
                    break;
                case "5":
                    returnObject();
                    break;
                case "6":
                    System.out.println("Exiting system...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. List all rentable objects");
        System.out.println("2. Check availability");
        System.out.println("3. Rent an object");
        System.out.println("4. Book an object");
        System.out.println("5. Return an object");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private void listAllObjects() {
        System.out.println("\nAll Rentable Objects:");
        List<RentableObject> objects = rentService.getRentableObjects();
        if (objects.isEmpty()) {
            System.out.println("No rentable objects available.");
        } else {
            for (int i = 0; i < objects.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, objects.get(i).getName());
            }
        }
    }

    private void checkAvailability() {
        LocalDateTime dateTime = promptForDateTime();
        if (dateTime == null) return;

        System.out.println("\nAvailable Objects at " + dateTime.format(dateTimeFormatter) + ":");
        List<RentableObject> availableObjects = rentService.getAvailableRentableObjects(dateTime);
        
        if (availableObjects.isEmpty()) {
            System.out.println("No objects available at the specified time.");
        } else {
            for (int i = 0; i < availableObjects.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, availableObjects.get(i).getName());
            }
        }
    }

    private void rentObject() {
        RentableObject object = selectObject("rent");
        if (object == null) return;

        LocalDateTime dateTime = promptForDateTime();
        if (dateTime == null) return;

        try {
            object.rent(dateTime);
            System.out.println("Successfully rented " + object.getName() + " at " + dateTime.format(dateTimeFormatter));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void bookObject() {
        RentableObject object = selectObject("book");
        if (object == null) return;

        LocalDateTime dateTime = promptForDateTime();
        if (dateTime == null) return;

        try {
            object.book(dateTime);
            System.out.println("Successfully booked " + object.getName() + " at " + dateTime.format(dateTimeFormatter));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void returnObject() {
        RentableObject object = selectObject("return");
        if (object == null) return;

        LocalDateTime dateTime = promptForDateTime();
        if (dateTime == null) return;

        try {
            object.returnBack(dateTime);
            System.out.println("Successfully returned " + object.getName() + " at " + dateTime.format(dateTimeFormatter));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private RentableObject selectObject(String action) {
        List<RentableObject> objects = rentService.getRentableObjects();
        if (objects.isEmpty()) {
            System.out.println("No objects available to " + action + ".");
            return null;
        }

        System.out.println("\nSelect an object to " + action + ":");
        for (int i = 0; i < objects.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, objects.get(i).getName());
        }
        System.out.print("Enter object number (0 to cancel): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            if (choice == 0) return null;
            if (choice < 1 || choice > objects.size()) {
                System.out.println("Invalid selection.");
                return null;
            }
            return objects.get(choice - 1);
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return null;
        }
    }

    private LocalDateTime promptForDateTime() {
        while (true) {
            System.out.print("Enter date and time (yyyy-MM-dd HH:mm) or 'now' for current time: ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("now")) {
                return LocalDateTime.now();
            }
            
            try {
                return LocalDateTime.parse(input, dateTimeFormatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Please use yyyy-MM-dd HH:mm (e.g., 2023-12-31 14:30)");
            }
        }
    }

    public static void main(String[] args) {
        new RentalTUI().start();
    }
}