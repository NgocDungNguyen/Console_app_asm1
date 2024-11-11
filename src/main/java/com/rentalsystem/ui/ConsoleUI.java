package com.rentalsystem.ui;

import com.rentalsystem.manager.RentalManager;
import com.rentalsystem.manager.RentalManagerImpl;
import com.rentalsystem.model.*;
import com.rentalsystem.util.DateUtil;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private RentalManager rentalManager;
    private Scanner scanner;

    public ConsoleUI() {
        this.rentalManager = new RentalManagerImpl();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        rentalManager.loadFromFile();
        boolean running = true;
        while (running) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addRentalAgreement();
                    break;
                case 2:
                    updateRentalAgreement();
                    break;
                case 3:
                    deleteRentalAgreement();
                    break;
                case 4:
                    viewRentalAgreement();
                    break;
                case 5:
                    viewAllRentalAgreements();
                    break;
                case 6:
                    viewSortedRentalAgreements();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        rentalManager.saveToFile();
        System.out.println("Thank you for using the Rental Property Management System!");
    }

    private void displayMenu() {
        System.out.println("\n--- Rental Property Management System ---");
        System.out.println("1. Add Rental Agreement");
        System.out.println("2. Update Rental Agreement");
        System.out.println("3. Delete Rental Agreement");
        System.out.println("4. View Rental Agreement");
        System.out.println("5. View All Rental Agreements");
        System.out.println("6. View Sorted Rental Agreements");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private void addRentalAgreement() {
        System.out.println("\n--- Add Rental Agreement ---");
        String id = "RA" + (rentalManager.getAllRentalAgreements().size() + 1);
        
        System.out.print("Enter tenant name: ");
        String tenantName = scanner.nextLine();
        Tenant tenant = new Tenant("T" + id.substring(2), tenantName, new Date(), "tenant@email.com");
        
        System.out.print("Enter property address: ");
        String propertyAddress = scanner.nextLine();
        System.out.print("Enter property price: ");
        double propertyPrice = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        Property property = new Property("P" + id.substring(2), propertyAddress, propertyPrice, Property.Status.AVAILABLE, "Owner1");
        
        System.out.print("Enter rental period (DAILY/WEEKLY/FORTNIGHTLY/MONTHLY): ");
        RentalAgreement.Period period = RentalAgreement.Period.valueOf(scanner.nextLine().toUpperCase());
        
        System.out.print("Enter renting fee: ");
        double rentingFee = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        
        RentalAgreement newAgreement = new RentalAgreement(
            id, tenant, property, period, new Date(), rentingFee, RentalAgreement.Status.NEW
        );
        rentalManager.addRentalAgreement(newAgreement);
        System.out.println("Rental Agreement added successfully!");
    }

    private void updateRentalAgreement() {
        System.out.println("\n--- Update Rental Agreement ---");
        System.out.print("Enter agreement ID to update: ");
        String id = scanner.nextLine();
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
            System.out.println("Current agreement details: " + agreement);
            System.out.print("Enter new status (NEW/ACTIVE/COMPLETED): ");
            String status = scanner.nextLine().toUpperCase();
            agreement.setStatus(RentalAgreement.Status.valueOf(status));
            
            System.out.print("Enter new renting fee (or press enter to keep current): ");
            String feeInput = scanner.nextLine();
            if (!feeInput.isEmpty()) {
                agreement.setRentingFee(Double.parseDouble(feeInput));
            }
            
            rentalManager.updateRentalAgreement(agreement);
            System.out.println("Rental Agreement updated successfully!");
        } else {
            System.out.println("Agreement not found.");
        }
    }

    private void deleteRentalAgreement() {
        System.out.println("\n--- Delete Rental Agreement ---");
        System.out.print("Enter agreement ID to delete: ");
        String id = scanner.nextLine();
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
            System.out.println("Agreement to delete: " + agreement);
            System.out.print("Are you sure you want to delete this agreement? (y/n): ");
            String confirm = scanner.nextLine().toLowerCase();
            if (confirm.equals("y")) {
                rentalManager.deleteRentalAgreement(id);
                System.out.println("Rental Agreement deleted successfully!");
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            System.out.println("Agreement not found.");
        }
    }

    private void viewRentalAgreement() {
        System.out.println("\n--- View Rental Agreement ---");
        System.out.print("Enter agreement ID to view: ");
        String id = scanner.nextLine();
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
            System.out.println(agreement);
        } else {
            System.out.println("Agreement not found.");
        }
    }

    private void viewAllRentalAgreements() {
        System.out.println("\n--- All Rental Agreements ---");
        List<RentalAgreement> agreements = rentalManager.getAllRentalAgreements();
        if (agreements.isEmpty()) {
            System.out.println("No rental agreements found.");
        } else {
            for (RentalAgreement agreement : agreements) {
                System.out.println(agreement);
            }
        }
    }

    private void viewSortedRentalAgreements() {
        System.out.println("\n--- View Sorted Rental Agreements ---");
        System.out.print("Sort by (id/date/fee): ");
        String sortBy = scanner.nextLine().toLowerCase();
        List<RentalAgreement> sortedAgreements = rentalManager.getSortedRentalAgreements(sortBy);
        if (sortedAgreements.isEmpty()) {
            System.out.println("No rental agreements found.");
        } else {
            for (RentalAgreement agreement : sortedAgreements) {
                System.out.println(agreement);
            }
        }
    }

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}