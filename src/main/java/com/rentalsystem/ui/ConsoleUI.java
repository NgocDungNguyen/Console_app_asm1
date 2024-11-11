package com.rentalsystem.ui;

import com.rentalsystem.manager.*;
import com.rentalsystem.model.*;
import com.rentalsystem.util.DateUtil;
import com.rentalsystem.util.FileHandler;
import com.rentalsystem.util.InputValidator;
import com.rentalsystem.util.AsciiTableGenerator;
import com.rentalsystem.ui.MenuBuilder;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class ConsoleUI {
    private RentalManager rentalManager;
    private TenantManager tenantManager;
    private HostManager hostManager;
    private PropertyManager propertyManager;
    private Scanner scanner;
    private MenuBuilder menuBuilder;
    private FileHandler fileHandler;

    // ANSI color codes
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public ConsoleUI() {
        this.fileHandler = new FileHandler();
        Map<String, List<?>> loadedData = fileHandler.loadAllData();
       
        List<Tenant> tenants = (List<Tenant>) loadedData.get("tenants");
        this.tenantManager = new TenantManagerImpl(fileHandler, tenants != null ? tenants : new ArrayList<>());
       
        List<Host> hosts = (List<Host>) loadedData.get("hosts");
        this.hostManager = new HostManagerImpl(fileHandler, hosts != null ? hosts : new ArrayList<>());
       
        List<Property> properties = (List<Property>) loadedData.get("properties");
        this.propertyManager = new PropertyManagerImpl(fileHandler, properties != null ? properties : new ArrayList<>());
       
        this.rentalManager = new RentalManagerImpl(fileHandler);
       
        this.scanner = new Scanner(System.in);
        this.menuBuilder = new MenuBuilder(); // Initialize it here

        System.out.println("ConsoleUI initialized. Managers created with loaded data.");
    }

    public void start() {
    displayLogo();
    System.out.println("Rental agreements in manager: " + rentalManager.getAllRentalAgreements().size());
    boolean running = true;
    while (running) {
            displayMainMenu();
            displayStatusBar("Admin");
            int choice = InputValidator.getIntInput(scanner, "Enter your choice: ", 1, 6);
            switch (choice) {
                case 1:
                    manageRentalAgreements();
                    break;
                case 2:
                    manageTenants();
                    break;
                case 3:
                    manageHosts();
                    break;
                case 4:
                    manageProperties();
                    break;
                case 5:
                    generateReports();
                    break;
                case 6:
                    running = false;
                    break;
            }
        }
        saveAllData();
        displaySuccess("Thank you for using the Rental Property Management System!");
    }

    private void displayLogo() {
        System.out.println(ANSI_BLUE +
            "  ____            _        _   ____                           _         \n" +
            " |  _ \\ ___ _ __ | |_ __ _| | |  _ \\ _ __ ___  _ __   ___ _ __| |_ _   _ \n" +
            " | |_) / _ \\ '_ \\| __/ _` | | | |_) | '__/ _ \\| '_ \\ / _ \\ '__| __| | | |\n" +
            " |  _ <  __/ | | | || (_| | | |  __/| | | (_) | |_) |  __/ |  | |_| |_| |\n" +
            " |_| \\_\\___|_| |_|\\__\\__,_|_| |_|   |_|  \\___/| .__/ \\___|_|   \\__|\\__, |\n" +
            "                                               |_|                  |___/ \n" +
            "  __  __                                                   _   \n" +
            " |  \\/  | __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ \n" +
            " | |\\/| |/ _` | '_ \\ / _` |/ _` |/ _ \\ '_ ` _ \\ / _ \\ '_ \\| __|\n" +
            " | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ \n" +
            " |_|  |_|\\__,_|_| |_|\\__,_|\\__, |\\___|_| |_| |_|\\___|_| |_|\\__|\n" +
            "                           |___/                               \n" +
            ANSI_RESET);
    }

    private void displayMainMenu() {
        menuBuilder.clear()
                   .addOption("Manage Rental Agreements")
                   .addOption("Manage Tenants")
                   .addOption("Manage Hosts")
                   .addOption("Manage Properties")
                   .addOption("Generate Reports")
                   .addOption("Exit")
                   .display("Rental Property Management System");
    }

    private void displayStatusBar(String username) {
        String date = java.time.LocalDate.now().toString();
        String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
        System.out.println(ANSI_BLUE + "[Current User: " + username + "] [Date: " + date + "] [Time: " + time + "]" + ANSI_RESET);
    }

    private void displaySuccess(String message) {
        System.out.println(ANSI_GREEN + "✔ " + message + ANSI_RESET);
    }

    private void displayError(String message) {
        System.out.println(ANSI_RED + "✘ " + message + ANSI_RESET);
    }

    private void displayWarning(String message) {
        System.out.println(ANSI_YELLOW + "⚠ " + message + ANSI_RESET);
    }

    private void loadAllData() {
        try {
            displayLoading("Loading data");
            fileHandler.loadAllData();
            displaySuccess("Data loaded successfully!");
        } catch (Exception e) {
            displayError("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void saveAllData() {
        try {
            displayLoading("Saving data");
            fileHandler.saveAllData();
            displaySuccess("Data saved successfully!");
        } catch (Exception e) {
            displayError("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayLoading(String message) {
        String[] animation = {"|", "/", "-", "\\"};
        for (int i = 0; i < 20; i++) {
            System.out.print("\r" + message + " " + animation[i % 4]);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        System.out.println();
    }

    private void manageRentalAgreements() {
        boolean managing = true;
        while (managing) {
            menuBuilder.clear()
                       .addOption("Add Rental Agreement")
                       .addOption("Update Rental Agreement")
                       .addOption("Delete Rental Agreement")
                       .addOption("View Rental Agreement")
                       .addOption("View All Rental Agreements")
                       .addOption("Manage Payments")
                       .addOption("Return to Main Menu")
                       .display("Manage Rental Agreements");

            int choice = InputValidator.getIntInput(scanner, "Enter your choice: ", 1, 7);
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
                    managePayments();
                    break;
                case 7:
                    managing = false;
                    break;
            }
        }
    }

    private void managePayments() {
        boolean managing = true;
        while (managing) {
            menuBuilder.clear()
                       .addOption("Add Payment")
                       .addOption("View Payments for Rental Agreement")
                       .addOption("View All Payments")
                       .addOption("Return to Rental Agreement Menu")
                       .display("Manage Payments");

            int choice = InputValidator.getIntInput(scanner, "Enter your choice: ", 1, 4);
            switch (choice) {
                case 1:
                    addPayment();
                    break;
                case 2:
                    viewPaymentsForRentalAgreement();
                    break;
                case 3:
                    viewAllPayments();
                    break;
                case 4:
                    managing = false;
                    break;
            }
        }
    }

    private void manageTenants() {
        boolean managing = true;
        while (managing) {
            menuBuilder.clear()
                       .addOption("Add Tenant")
                       .addOption("Update Tenant")
                       .addOption("Delete Tenant")
                       .addOption("View Tenant")
                       .addOption("View All Tenants")
                       .addOption("Return to Main Menu")
                       .display("Manage Tenants");

            int choice = InputValidator.getIntInput(scanner, "Enter your choice: ", 1, 6);
            switch (choice) {
                case 1:
                    addTenant();
                    break;
                case 2:
                    updateTenant();
                    break;
                case 3:
                    deleteTenant();
                    break;
                case 4:
                    viewTenant();
                    break;
                case 5:
                    viewAllTenants();
                    break;
                case 6:
                    managing = false;
                    break;
            }
        }
    }

    private void manageHosts() {
        boolean managing = true;
        while (managing) {
            menuBuilder.clear()
                       .addOption("Add Host")
                       .addOption("Update Host")
                       .addOption("Delete Host")
                       .addOption("View Host")
                       .addOption("View All Hosts")
                       .addOption("Return to Main Menu")
                       .display("Manage Hosts");

            int choice = InputValidator.getIntInput(scanner, "Enter your choice: ", 1, 6);
            switch (choice) {
                case 1:
                    addHost();
                    break;
                case 2:
                    updateHost();
                    break;
                case 3:
                    deleteHost();
                    break;
                case 4:
                    viewHost();
                    break;
                case 5:
                    viewAllHosts();
                    break;
                case 6:
                    managing = false;
                    break;
            }
        }
    }

    private void manageProperties() {
        boolean managing = true;
        while (managing) {
            menuBuilder.clear()
                       .addOption("Add Property")
                       .addOption("Update Property")
                       .addOption("Delete Property")
                       .addOption("View Property")
                       .addOption("View All Properties")
                       .addOption("Return to Main Menu")
                       .display("Manage Properties");

            int choice = InputValidator.getIntInput(scanner, "Enter your choice: ", 1, 6);
            switch (choice) {
                case 1:
                    addProperty();
                    break;
                case 2:
                    updateProperty();
                    break;
                case 3:
                    deleteProperty();
                    break;
                case 4:
                    viewProperty();
                    break;
                case 5:
                    viewAllProperties();
                    break;
                case 6:
                    managing = false;
                    break;
            }
        }
    }

    private void addRentalAgreement() {
        System.out.println("\n--- Add Rental Agreement ---");
        String id = InputValidator.getStringInput(scanner, "Enter agreement ID: ");
        String tenantId = InputValidator.getStringInput(scanner, "Enter tenant ID: ");
        String propertyId = InputValidator.getStringInput(scanner, "Enter property ID: ");
        String periodInput = InputValidator.getStringInput(scanner, "Enter rental period (DAILY, WEEKLY, FORTNIGHTLY, MONTHLY): ");
        String contractDateStr = InputValidator.getDateInput(scanner, "Enter contract date (YYYY-MM-DD): ");
        double rentingFee = InputValidator.getDoubleInput(scanner, "Enter renting fee: ");
        String statusInput = InputValidator.getStringInput(scanner, "Enter status (NEW, ACTIVE, COMPLETED): ");
    
        Tenant tenant = tenantManager.getTenant(tenantId);
        Property property = propertyManager.getProperty(propertyId);
        
        if (tenant != null && property != null) {
            RentalAgreement agreement = new RentalAgreement(
                id, tenant, property,
                RentalAgreement.Period.valueOf(periodInput.toUpperCase()),
                DateUtil.parseDate(contractDateStr),
                rentingFee,
                RentalAgreement.Status.valueOf(statusInput.toUpperCase())
            );
            if (rentalManager.addRentalAgreement(agreement)) {
                displaySuccess("Rental Agreement added successfully!");
            } else {
                displayError("Failed to add Rental Agreement. It may already exist.");
            }
        } else {
            displayError("Invalid tenant or property ID.");
        }
    }
    
    private void updateRentalAgreement() {
    System.out.println("\n--- Update Rental Agreement ---");
    String id = InputValidator.getStringInput(scanner, "Enter agreement ID to update: ");
    RentalAgreement agreement = rentalManager.getRentalAgreement(id);
    if (agreement != null) {
        String periodInput = InputValidator.getStringInput(scanner, "Enter new rental period (DAILY, WEEKLY, FORTNIGHTLY, MONTHLY) or press enter to keep current: ");
        if (!periodInput.isEmpty()) {
            agreement.setPeriod(RentalAgreement.Period.valueOf(periodInput.toUpperCase()));
        }

        String rentingFeeStr = InputValidator.getStringInput(scanner, "Enter new renting fee or press enter to keep current: ");
        if (!rentingFeeStr.isEmpty()) {
            agreement.setRentingFee(Double.parseDouble(rentingFeeStr));
        }

        String statusInput = InputValidator.getStringInput(scanner, "Enter new status (NEW, ACTIVE, COMPLETED) or press enter to keep current: ");
        if (!statusInput.isEmpty()) {
            agreement.setStatus(RentalAgreement.Status.valueOf(statusInput.toUpperCase()));
        }

        if (rentalManager.updateRentalAgreement(agreement)) {
            displaySuccess("Rental Agreement updated successfully!");
        } else {
            displayError("Failed to update Rental Agreement.");
        }
    } else {
        displayError("Rental Agreement not found.");
    }
}
    
    private void deleteRentalAgreement() {
        System.out.println("\n--- Delete Rental Agreement ---");
        String id = InputValidator.getStringInput(scanner, "Enter agreement ID to delete: ");
        if (rentalManager.deleteRentalAgreement(id)) {
            displaySuccess("Rental Agreement deleted successfully!");
        } else {
            displayError("Failed to delete Rental Agreement. It may not exist.");
        }
    }

    private void viewRentalAgreement() {
        System.out.println("\n--- View Rental Agreement ---");
        String id = InputValidator.getStringInput(scanner, "Enter agreement ID to view: ");
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
            System.out.println(agreement);
        } else {
            displayError("Rental Agreement not found.");
        }
    }

    private void viewAllRentalAgreements() {
    System.out.println("\n--- All Rental Agreements ---");
    List<RentalAgreement> agreements = rentalManager.getAllRentalAgreements();
    System.out.println("Number of agreements found in RentalManager: " + agreements.size());
    
    if (agreements.isEmpty()) {
        displayWarning("No rental agreements found in the system.");
        System.out.println("Attempting to read raw data from file:");
        try (BufferedReader reader = new BufferedReader(new FileReader("resources/rental_agreements.txt"))) {
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                lineCount++;
            }
            System.out.println("Total lines read from file: " + lineCount);
        } catch (IOException e) {
            System.err.println("Error reading rental agreements file: " + e.getMessage());
        }
    } else {
        String[] headers = {"ID", "Tenant", "Property", "Period", "Contract Date", "Renting Fee", "Status"};
        List<String[]> data = new ArrayList<>();
        for (RentalAgreement agreement : agreements) {
            data.add(new String[]{
                agreement.getId(),
                agreement.getMainTenant().getFullName(),
                agreement.getProperty().getAddress(),
                agreement.getPeriod().toString(),
                DateUtil.formatDate(agreement.getContractDate()),
                String.format("$%.2f", agreement.getRentingFee()),
                agreement.getStatus().toString()
            });
        }
        System.out.println(AsciiTableGenerator.generateTable(headers, data));
    }
}

    private void addTenant() {
        System.out.println("\n--- Add Tenant ---");
        String id = InputValidator.getStringInput(scanner, "Enter tenant ID: ");
        String fullName = InputValidator.getStringInput(scanner, "Enter full name: ");
        String dateOfBirth = InputValidator.getDateInput(scanner, "Enter date of birth (YYYY-MM-DD): ");
        String email = InputValidator.getEmailInput(scanner, "Enter email: ");
        String phone = InputValidator.getPhoneInput(scanner, "Enter phone number (10 digits): ");
        
        Tenant newTenant = new Tenant(id, fullName, DateUtil.parseDate(dateOfBirth), email + ", " + phone);
        if (tenantManager.addTenant(newTenant)) {
            displaySuccess("Tenant added successfully!");
        } else {
            displayError("Failed to add tenant. Tenant ID may already exist.");
        }
    }
    
    private void updateTenant() {
    System.out.println("\n--- Update Tenant ---");
    String id = InputValidator.getStringInput(scanner, "Enter tenant ID to update: ");
    Tenant tenant = tenantManager.getTenant(id);
    if (tenant != null) {
        String fullName = InputValidator.getStringInput(scanner, "Enter new full name (or press enter to keep current): ");
        if (!fullName.isEmpty()) tenant.setFullName(fullName);
        
        String dobString = InputValidator.getDateInput(scanner, "Enter new date of birth (YYYY-MM-DD) (or press enter to keep current): ");
        if (!dobString.isEmpty()) tenant.setDateOfBirth(DateUtil.parseDate(dobString));
        
        String email = InputValidator.getEmailInput(scanner, "Enter new email (or press enter to keep current): ");
        String phone = InputValidator.getPhoneInput(scanner, "Enter new phone number (10 digits) (or press enter to keep current): ");
        
        if (!email.isEmpty() || !phone.isEmpty()) {
            String[] currentContact = tenant.getContactInformation().split(", ");
            String newEmail = email.isEmpty() ? currentContact[0] : email;
            String newPhone = phone.isEmpty() ? (currentContact.length > 1 ? currentContact[1] : "") : phone;
            tenant.setContactInformation(newEmail + (newPhone.isEmpty() ? "" : ", " + newPhone));
        }
        
        if (tenantManager.updateTenant(tenant)) {
            displaySuccess("Tenant updated successfully!");
        } else {
            displayError("Failed to update tenant.");
        }
    } else {
        displayError("Tenant not found.");
    }
}
    
    private void deleteTenant() {
        System.out.println("\n--- Delete Tenant ---");
        String id = InputValidator.getStringInput(scanner, "Enter tenant ID to delete: ");
        if (tenantManager.deleteTenant(id)) {
            displaySuccess("Tenant deleted successfully!");
        } else {
            displayError("Failed to delete tenant. Tenant may not exist.");
        }
    }

    private void viewTenant() {
        System.out.println("\n--- View Tenant ---");
        String id = InputValidator.getStringInput(scanner, "Enter tenant ID to view: ");
        Tenant tenant = tenantManager.getTenant(id);
        if (tenant != null) {
            System.out.println(tenant);
        } else {
            displayError("Tenant not found.");
        }
    }

    private void viewAllTenants() {
        System.out.println("\n--- All Tenants ---");
        List<Tenant> tenants = tenantManager.getAllTenants();
        if (tenants.isEmpty()) {
            displayWarning("No tenants found.");
        } else {
            String[] headers = {"ID", "Name", "Date of Birth", "Contact Info", "Rental Agreements"};
            List<String[]> data = new ArrayList<>();
            for (Tenant tenant : tenants) {
                data.add(new String[]{
                    tenant.getId(),
                    tenant.getFullName(),
                    DateUtil.formatDate(tenant.getDateOfBirth()),
                    tenant.getContactInformation(),
                    String.valueOf(tenant.getRentalAgreements().size())
                });
            }
            System.out.println(AsciiTableGenerator.generateTable(headers, data));
        }
    }

    private void addHost() {
        System.out.println("\n--- Add Host ---");
        String id = InputValidator.getStringInput(scanner, "Enter host ID: ");
        String fullName = InputValidator.getStringInput(scanner, "Enter full name: ");
        Date dateOfBirth = DateUtil.parseDate(InputValidator.getDateInput(scanner, "Enter date of birth (YYYY-MM-DD): "));
        String email = InputValidator.getEmailInput(scanner, "Enter email: ");
        String phone = InputValidator.getPhoneInput(scanner, "Enter phone number (10 digits): ");
        
        Host newHost = new Host(id, fullName, dateOfBirth, email + ", " + phone);
        if (hostManager.addHost(newHost)) {
            displaySuccess("Host added successfully!");
        } else {
            displayError("Failed to add host. Host ID may already exist.");
        }
    }
    
    private void updateHost() {
        System.out.println("\n--- Update Host ---");
        String id = InputValidator.getStringInput(scanner, "Enter host ID to update: ");
        Host host = hostManager.getHost(id);
        if (host != null) {
            String fullName = InputValidator.getStringInput(scanner, "Enter new full name (or press enter to keep current): ");
            if (!fullName.isEmpty()) host.setFullName(fullName);
            
            String dobString = InputValidator.getDateInput(scanner, "Enter new date of birth (YYYY-MM-DD) (or press enter to keep current): ");
            if (!dobString.isEmpty()) host.setDateOfBirth(DateUtil.parseDate(dobString));
            
            String email = InputValidator.getEmailInput(scanner, "Enter new email (or press enter to keep current): ");
            String phone = InputValidator.getPhoneInput(scanner, "Enter new phone number (10 digits) (or press enter to keep current): ");
            if (!email.isEmpty() || !phone.isEmpty()) {
                String currentContact = host.getContactInformation();
                String[] contactParts = currentContact.split(", ");
                String newEmail = email.isEmpty() ? contactParts[0] : email;
                String newPhone = phone.isEmpty() ? contactParts[1] : phone;
                host.setContactInformation(newEmail + ", " + newPhone);
            }
            
            if (hostManager.updateHost(host)) {
                displaySuccess("Host updated successfully!");
            } else {
                displayError("Failed to update host.");
            }
        } else {
            displayError("Host not found.");
        }
    }
    
    private void deleteHost() {
        System.out.println("\n--- Delete Host ---");
        String id = InputValidator.getStringInput(scanner, "Enter host ID to delete: ");
        if (hostManager.deleteHost(id)) {
            displaySuccess("Host deleted successfully!");
        } else {
            displayError("Failed to delete host. Host may not exist.");
        }
    }
    
    private void viewHost() {
        System.out.println("\n--- View Host ---");
        String id = InputValidator.getStringInput(scanner, "Enter host ID to view: ");
        Host host = hostManager.getHost(id);
        if (host != null) {
            System.out.println(host);
        } else {
            displayError("Host not found.");
        }
    }
    
    private void viewAllHosts() {
        System.out.println("\n--- All Hosts ---");
        List<Host> hosts = hostManager.getAllHosts();
        if (hosts.isEmpty()) {
            displayWarning("No hosts found.");
        } else {
            String[] headers = {"ID", "Name", "Date of Birth", "Contact Info", "Managed Properties"};
            List<String[]> data = new ArrayList<>();
            for (Host host : hosts) {
                data.add(new String[]{
                    host.getId(),
                    host.getFullName(),
                    DateUtil.formatDate(host.getDateOfBirth()),
                    host.getContactInformation(),
                    String.valueOf(host.getManagedProperties().size())
                });
            }
            System.out.println(AsciiTableGenerator.generateTable(headers, data));
        }
    }

    private void addPayment() {
        System.out.println("\n--- Add Payment ---");
        String id = InputValidator.getStringInput(scanner, "Enter payment ID: ");
        String rentalAgreementId = InputValidator.getStringInput(scanner, "Enter rental agreement ID: ");
        double amount = InputValidator.getDoubleInput(scanner, "Enter payment amount: ");
        String dateStr = InputValidator.getDateInput(scanner, "Enter payment date (YYYY-MM-DD): ");
        String paymentMethod = InputValidator.getStringInput(scanner, "Enter payment method: ");

        Payment payment = new Payment(id, amount, DateUtil.parseDate(dateStr), paymentMethod, rentalAgreementId);
        if (rentalManager.addPayment(payment)) {
            displaySuccess("Payment added successfully!");
        } else {
            displayError("Failed to add payment. Rental agreement may not exist.");
        }
    }

    private void viewPaymentsForRentalAgreement() {
        System.out.println("\n--- View Payments for Rental Agreement ---");
        String rentalAgreementId = InputValidator.getStringInput(scanner, "Enter rental agreement ID: ");
        List<Payment> payments = rentalManager.getPaymentsForRentalAgreement(rentalAgreementId);
        if (payments.isEmpty()) {
            displayWarning("No payments found for this rental agreement.");
        } else {
            displayPayments(payments);
        }
    }

    private void viewAllPayments() {
        System.out.println("\n--- All Payments ---");
        List<Payment> payments = rentalManager.getAllPayments();
        if (payments.isEmpty()) {
            displayWarning("No payments found.");
        } else {
            displayPayments(payments);
        }
    }

    private void displayPayments(List<Payment> payments) {
        String[] headers = {"ID", "Amount", "Date", "Method", "Rental Agreement ID"};
        List<String[]> data = new ArrayList<>();
        for (Payment payment : payments) {
            data.add(new String[]{
                payment.getId(),
                String.format("$%.2f", payment.getAmount()),
                DateUtil.formatDate(payment.getPaymentDate()),
                payment.getPaymentMethod(),
                payment.getRentalAgreementId()
            });
        }
        System.out.println(AsciiTableGenerator.generateTable(headers, data));
    }
    
    private void addProperty() {
        System.out.println("\n--- Add Property ---");
        String id = InputValidator.getStringInput(scanner, "Enter property ID: ");
        String address = InputValidator.getStringInput(scanner, "Enter address: ");
        double price = InputValidator.getDoubleInput(scanner, "Enter price: ");
        String statusInput = InputValidator.getStringInput(scanner, "Enter status (AVAILABLE, RENTED, UNDER_MAINTENANCE): ");
        Property.Status status = Property.Status.valueOf(statusInput.toUpperCase());
        String owner = InputValidator.getStringInput(scanner, "Enter owner ID: ");
        
        String propertyType = InputValidator.getStringInput(scanner, "Enter property type (RESIDENTIAL or COMMERCIAL): ");
        Property newProperty;
        
        if (propertyType.equalsIgnoreCase("RESIDENTIAL")) {
            int bedrooms = InputValidator.getIntInput(scanner, "Enter number of bedrooms: ", 0, Integer.MAX_VALUE);
            boolean hasGarden = InputValidator.getStringInput(scanner, "Has garden? (yes/no): ").equalsIgnoreCase("yes");
            boolean isPetFriendly = InputValidator.getStringInput(scanner, "Is pet friendly? (yes/no): ").equalsIgnoreCase("yes");
            newProperty = new ResidentialProperty(id, address, price, status, owner, bedrooms, hasGarden, isPetFriendly);
        } else {
            String businessType = InputValidator.getStringInput(scanner, "Enter business type: ");
            int parkingSpaces = InputValidator.getIntInput(scanner, "Enter number of parking spaces: ", 0, Integer.MAX_VALUE);
            double squareFootage = InputValidator.getDoubleInput(scanner, "Enter square footage: ");
            newProperty = new CommercialProperty(id, address, price, status, owner, businessType, parkingSpaces, squareFootage);
        }
        
        if (propertyManager.addProperty(newProperty)) {
            displaySuccess("Property added successfully!");
        } else {
            displayError("Failed to add property. Property ID may already exist.");
        }
    }
    
    private void updateProperty() {
        System.out.println("\n--- Update Property ---");
        String id = InputValidator.getStringInput(scanner, "Enter property ID to update: ");
        Property property = propertyManager.getProperty(id);
        if (property != null) {
            String address = InputValidator.getStringInput(scanner, "Enter new address (or press enter to keep current): ");
            if (!address.isEmpty()) property.setAddress(address);
            
            String priceString = InputValidator.getStringInput(scanner, "Enter new price (or press enter to keep current): ");
            if (!priceString.isEmpty()) property.setPrice(Double.parseDouble(priceString));
            
            String statusInput = InputValidator.getStringInput(scanner, "Enter new status (AVAILABLE, RENTED, UNDER_MAINTENANCE) (or press enter to keep current): ");
            if (!statusInput.isEmpty()) property.setStatus(Property.Status.valueOf(statusInput.toUpperCase()));
            
            if (propertyManager.updateProperty(property)) {
                displaySuccess("Property updated successfully!");
            } else {
                displayError("Failed to update property.");
            }
        } else {
            displayError("Property not found.");
        }
    }
    
    private void deleteProperty() {
        System.out.println("\n--- Delete Property ---");
        String id = InputValidator.getStringInput(scanner, "Enter property ID to delete: ");
        if (propertyManager.deleteProperty(id)) {
            displaySuccess("Property deleted successfully!");
        } else {
            displayError("Failed to delete property. Property may not exist.");
        }
    }
    
    private void viewProperty() {
        System.out.println("\n--- View Property ---");
        String id = InputValidator.getStringInput(scanner, "Enter property ID to view: ");
        Property property = propertyManager.getProperty(id);
        if (property != null) {
            System.out.println(property);
        } else {
            displayError("Property not found.");
        }
    }
    
    private void viewAllProperties() {
        System.out.println("\n--- All Properties ---");
        List<Property> properties = propertyManager.getAllProperties();
        if (properties.isEmpty()) {
            displayWarning("No properties found.");
        } else {
            String[] headers = {"ID", "Address", "Price", "Status", "Owner", "Type", "Details"};
            List<String[]> data = new ArrayList<>();
            for (Property property : properties) {
                String type = property instanceof ResidentialProperty ? "Residential" : "Commercial";
                String details = property instanceof ResidentialProperty ?
                    "Bedrooms: " + ((ResidentialProperty) property).getNumberOfBedrooms() :
                    "Business Type: " + ((CommercialProperty) property).getBusinessType();
                data.add(new String[]{
                    property.getId(),
                    property.getAddress(),
                    String.format("$%.2f", property.getPrice()),
                    property.getStatus().toString(),
                    property.getOwner(),
                    type,
                    details
                });
            }
            System.out.println(AsciiTableGenerator.generateTable(headers, data));
        }
    }

    private void generateReports() {
        boolean generating = true;
        while (generating) {
            menuBuilder.clear()
                       .addOption("Generate Tenant Report")
                       .addOption("Generate Host Report")
                       .addOption("Generate Property Report")
                       .addOption("Generate Rental Agreement Report")
                       .addOption("Return to Main Menu")
                       .display("Generate Reports");

            int choice = InputValidator.getIntInput(scanner, "Enter your choice: ", 1, 5);
            switch (choice) {
                case 1:
                    generateTenantReport();
                    break;
                case 2:
                    generateHostReport();
                    break;
                case 3:
                    generatePropertyReport();
                    break;
                case 4:
                    generateRentalAgreementReport();
                    break;
                case 5:
                    generating = false;
                    break;
            }
        }
    }

    private void generateTenantReport() {
        System.out.println("\n--- Tenant Report ---");
        List<Tenant> tenants = tenantManager.getAllTenants();
        if (tenants.isEmpty()) {
            displayWarning("No tenants found.");
        } else {
            for (Tenant tenant : tenants) {
                System.out.println(tenant);
                System.out.println("Rental Agreements: " + tenant.getRentalAgreements().size());
                System.out.println("Payment Transactions: " + tenant.getPaymentTransactions().size());
                System.out.println("--------------------");
            }
        }
    }

    private void generateHostReport() {
        System.out.println("\n--- Host Report ---");
        List<Host> hosts = hostManager.getAllHosts();
        if (hosts.isEmpty()) {
            displayWarning("No hosts found.");
        } else {
            for (Host host : hosts) {
                System.out.println(host);
                System.out.println("Managed Properties: " + host.getManagedProperties().size());
                System.out.println("Cooperating Owners: " + host.getCooperatingOwners().size());
                System.out.println("Rental Agreements: " + host.getRentalAgreements().size());
                System.out.println("--------------------");
            }
        }
    }

    private void generatePropertyReport() {
        System.out.println("\n--- Property Report ---");
        List<Property> properties = propertyManager.getAllProperties();
        if (properties.isEmpty()) {
            displayWarning("No properties found.");
        } else {
            for (Property property : properties) {
                System.out.println(property);
                System.out.println("Hosts: " + property.getHosts().size());
                System.out.println("--------------------");
            }
        }
    }

    private void generateRentalAgreementReport() {
        System.out.println("\n--- Rental Agreement Report ---");
        List<RentalAgreement> agreements = rentalManager.getAllRentalAgreements();
        if (agreements.isEmpty()) {
            displayWarning("No rental agreements found.");
        } else {
            for (RentalAgreement agreement : agreements) {
                System.out.println(agreement);
                System.out.println("Main Tenant: " + agreement.getMainTenant().getFullName());
                System.out.println("Property: " + agreement.getProperty().getAddress());
                System.out.println("--------------------");
            }
        }
    }

    public static void main(String[] args) {
        ConsoleUI ui = new ConsoleUI();
        ui.start();
    }
}