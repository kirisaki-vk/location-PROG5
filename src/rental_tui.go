package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
	"time"
)

type RentalTUI struct {
	service *RentService
	reader  *bufio.Reader
}

func NewRentalTUI() *RentalTUI {
	return &RentalTUI{
		service: NewRentService(),
		reader:  bufio.NewReader(os.Stdin),
	}
}

func (tui *RentalTUI) Start() {
	fmt.Println("=== Rental Service Management System ===")

	for {
		tui.printMainMenu()
		choice, _ := tui.reader.ReadString('\n')
		choice = strings.TrimSpace(choice)

		switch choice {
		case "1":
			tui.listAllObjects()
		case "2":
			tui.checkAvailability()
		case "3":
			tui.rentObject()
		case "4":
			tui.bookObject()
		case "5":
			tui.returnObject()
		case "6":
			fmt.Println("Exiting system...")
			return
		default:
			fmt.Println("Invalid option. Please try again.")
		}
	}
}

func (tui *RentalTUI) printMainMenu() {
	fmt.Println("\nMain Menu:")
	fmt.Println("1. List all rentable objects")
	fmt.Println("2. Check availability")
	fmt.Println("3. Rent an object")
	fmt.Println("4. Book an object")
	fmt.Println("5. Return an object")
	fmt.Println("6. Exit")
	fmt.Print("Enter your choice: ")
}

func (tui *RentalTUI) listAllObjects() {
	fmt.Println("\nAll Rentable Objects:")
	for i, obj := range tui.service.RentableObjects {
		fmt.Printf("%d. %s\n", i+1, obj.Name)
	}
}

func (tui *RentalTUI) checkAvailability() {
	dateTime, err := tui.promptForDateTime()
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	fmt.Printf("\nAvailable Objects at %s:\n", dateTime.Format("2006-01-02 15:04"))
	available := tui.service.GetAvailableObjects(dateTime)

	if len(available) == 0 {
		fmt.Println("No objects available at the specified time.")
	} else {
		for i, obj := range available {
			fmt.Printf("%d. %s\n", i+1, obj.Name)
		}
	}
}

func (tui *RentalTUI) rentObject() {
	obj, err := tui.selectObject("rent")
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	dateTime, err := tui.promptForDateTime()
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	if err := obj.Rent(dateTime); err != nil {
		fmt.Println("Error:", err)
	} else {
		fmt.Printf("Successfully rented %s at %s\n", obj.Name, dateTime.Format("2006-01-02 15:04"))
	}
}

func (tui *RentalTUI) bookObject() {
	obj, err := tui.selectObject("book")
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	dateTime, err := tui.promptForDateTime()
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	if err := obj.Book(dateTime); err != nil {
		fmt.Println("Error:", err)
	} else {
		fmt.Printf("Successfully booked %s at %s\n", obj.Name, dateTime.Format("2006-01-02 15:04"))
	}
}

func (tui *RentalTUI) returnObject() {
	obj, err := tui.selectObject("return")
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	dateTime, err := tui.promptForDateTime()
	if err != nil {
		fmt.Println("Error:", err)
		return
	}

	if err := obj.Return(dateTime); err != nil {
		fmt.Println("Error:", err)
	} else {
		fmt.Printf("Successfully returned %s at %s\n", obj.Name, dateTime.Format("2006-01-02 15:04"))
	}
}

func (tui *RentalTUI) selectObject(action string) (*RentableObject, error) {
	if len(tui.service.RentableObjects) == 0 {
		return nil, fmt.Errorf("no objects available to %s", action)
	}

	fmt.Printf("\nSelect an object to %s:\n", action)
	for i, obj := range tui.service.RentableObjects {
		fmt.Printf("%d. %s\n", i+1, obj.Name)
	}
	fmt.Print("Enter object number (0 to cancel): ")

	input, _ := tui.reader.ReadString('\n')
	choice, err := strconv.Atoi(strings.TrimSpace(input))
	if err != nil {
		return nil, fmt.Errorf("please enter a valid number")
	}

	if choice == 0 {
		return nil, fmt.Errorf("operation cancelled")
	}

	if choice < 1 || choice > len(tui.service.RentableObjects) {
		return nil, fmt.Errorf("invalid selection")
	}

	return &tui.service.RentableObjects[choice-1], nil
}

func (tui *RentalTUI) promptForDateTime() (time.Time, error) {
	for {
		fmt.Print("Enter date and time (2006-01-02 15:04) or 'now' for current time: ")
		input, _ := tui.reader.ReadString('\n')
		input = strings.TrimSpace(input)

		if strings.ToLower(input) == "now" {
			return time.Now(), nil
		}

		dateTime, err := time.Parse("2006-01-02 15:04", input)
		if err == nil {
			return dateTime, nil
		}
		fmt.Println("Invalid format. Please use yyyy-MM-dd HH:mm (e.g., 2023-12-31 14:30)")
	}
}
