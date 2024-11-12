package com.rentalsystem.ui;

import com.rentalsystem.manager.*;
import com.rentalsystem.model.*;
import com.rentalsystem.util.DateUtil;
import com.rentalsystem.util.FileHandler;
import com.rentalsystem.util.AsciiTableGenerator;
import org.jline.reader.*;
import org.jline.terminal.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ConsoleUI {
    private RentalManager rentalManager;
    private TenantManager tenantManager;
    private HostManager hostManager;
    private PropertyManager propertyManager;
    private LineReader reader;
    private FileHandler fileHandler;

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";

    public ConsoleUI() throws IOException {
        this.fileHandler = new FileHandler();
        Map<String, List<?>> loadedData = fileHandler.loadAllData();
       
        List<Tenant> tenants = (List<Tenant>) loadedData.get("tenants");
        this.tenantManager = new TenantManagerImpl(fileHandler, tenants != null ? tenants : new ArrayList<>());
       
        List<Host> hosts = (List<Host>) loadedData.get("hosts");
        this.hostManager = new HostManagerImpl(fileHandler, hosts != null ? hosts : new ArrayList<>());
       
        List<Property> properties = (List<Property>) loadedData.get("properties");
        this.propertyManager = new PropertyManagerImpl(fileHandler, properties != null ? properties : new ArrayList<>());
       
        this.rentalManager = new RentalManagerImpl(fileHandler);

        Terminal terminal = TerminalBuilder.builder().system(true).build();
        this.reader = LineReaderBuilder.builder()
            .terminal(terminal)
            .completer(new MenuCompleter())
            .option(LineReader.Option.AUTO_REMOVE_SLASH, true)
            .build();

        System.out.println("ConsoleUI initialized. Managers created with loaded data.");
    }

    private class MenuCompleter implements Completer {
        @Override
        public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
            candidates.add(new Candidate("1"));
            candidates.add(new Candidate("2"));
            candidates.add(new Candidate("3"));
            candidates.add(new Candidate("4"));
            candidates.add(new Candidate("5"));
            candidates.add(new Candidate("6"));
        }
    }

    public void start() {
        displayLogo();
        System.out.println("Rental agreements in manager: " + rentalManager.getAllRentalAgreements().size());
        boolean running = true;
        while (running) {
            displayMainMenu();
            displayStatusBar("Admin");
            String choice = reader.readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    manageRentalAgreements();
                    break;
                case "2":
                    manageTenants();
                    break;
                case "3":
                    manageHosts();
                    break;
                case "4":
                    manageProperties();
                    break;
                case "5":
                    generateReports();
                    break;
                case "6":
                    running = confirmExit();
                    break;
                default:
                    displayError("Invalid choice. Please try again.");
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
        String border = "╔══════════════════════════════════════╗";
        String footer = "╚══════════════════════════════════════╝";
        System.out.println(ANSI_BLUE + border + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "           MAIN MENU                " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════╣" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 1. Manage Rental Agreements         " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 2. Manage Tenants                   " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 3. Manage Hosts                     " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 4. Manage Properties                " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 5. Generate Reports                 " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 6. Exit                             " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + footer + ANSI_RESET);
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

    private boolean confirmExit() {
        String response = reader.readLine(ANSI_YELLOW + "Are you sure you want to exit? (y/n): " + ANSI_RESET);
        return response.toLowerCase().startsWith("y");
    }

    private void manageRentalAgreements() {
        boolean managing = true;
        while (managing) {
            System.out.println(ANSI_YELLOW + "\n=== Manage Rental Agreements ===" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "1. " + ANSI_RESET + "Add Rental Agreement");
            System.out.println(ANSI_BLUE + "2. " + ANSI_RESET + "Update Rental Agreement");
            System.out.println(ANSI_BLUE + "3. " + ANSI_RESET + "Delete Rental Agreement");
            System.out.println(ANSI_BLUE + "4. " + ANSI_RESET + "View Rental Agreement");
            System.out.println(ANSI_BLUE + "5. " + ANSI_RESET + "View All Rental Agreements");
            System.out.println(ANSI_BLUE + "6. " + ANSI_RESET + "Manage Payments");
            System.out.println(ANSI_BLUE + "7. " + ANSI_RESET + "Return to Main Menu");

            String choice = reader.readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    addRentalAgreement();
                    break;
                case "2":
                    updateRentalAgreement();
                    break;
                case "3":
                    deleteRentalAgreement();
                    break;
                case "4":
                    viewRentalAgreement();
                    break;
                case "5":
                    viewAllRentalAgreements();
                    break;
                case "6":
                    managePayments();
                    break;
                case "7":
                    managing = false;
                    break;
                default:
                    displayError("Invalid choice. Please try again.");
            }
        }
    }

    private void addRentalAgreement() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                   ADD RENTAL AGREEMENT                    " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Agreement ID");
        String tenantId = promptForInput("Tenant ID");
        String propertyId = promptForInput("Property ID");
        String periodInput = promptForInput("Rental Period (DAILY, WEEKLY, FORTNIGHTLY, MONTHLY)");
        String contractDateStr = promptForInput("Contract Date (YYYY-MM-DD)");
        double rentingFee = Double.parseDouble(promptForInput("Renting Fee"));
        String statusInput = promptForInput("Status (NEW, ACTIVE, COMPLETED)");

        try {
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
        } catch (IllegalArgumentException e) {
            displayError("Invalid input: " + e.getMessage());
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }

    private String promptForInput(String prompt) {
        return reader.readLine(ANSI_BLUE + "║ " + ANSI_RESET + prompt + ": ");
    }
    
    private void updateRentalAgreement() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "               UPDATE RENTAL AGREEMENT                   " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Enter agreement ID to update");
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
            String periodInput = promptForInput("Enter new rental period (or press enter to keep current: " + agreement.getPeriod() + ")");
            if (!periodInput.isEmpty()) {
                try {
                    agreement.setPeriod(RentalAgreement.Period.valueOf(periodInput.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    displayError("Invalid period. Keeping current value.");
                }
            }

            String rentingFeeStr = promptForInput("Enter new renting fee (or press enter to keep current: $" + agreement.getRentingFee() + ")");
            if (!rentingFeeStr.isEmpty()) {
                try {
                    double newFee = Double.parseDouble(rentingFeeStr);
                    agreement.setRentingFee(newFee);
                } catch (NumberFormatException e) {
                    displayError("Invalid renting fee. Keeping current value.");
                }
            }

            String statusInput = promptForInput("Enter new status (or press enter to keep current: " + agreement.getStatus() + ")");
            if (!statusInput.isEmpty()) {
                try {
                    agreement.setStatus(RentalAgreement.Status.valueOf(statusInput.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    displayError("Invalid status. Keeping current value.");
                }
            }

            if (rentalManager.updateRentalAgreement(agreement)) {
                displaySuccess("Rental Agreement updated successfully!");
            } else {
                displayError("Failed to update Rental Agreement.");
            }
        } else {
            displayError("Rental Agreement not found.");
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }
    
    private void deleteRentalAgreement() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "               DELETE RENTAL AGREEMENT                   " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Enter agreement ID to delete");
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
            System.out.println("Rental Agreement details:");
            displayRentalAgreementDetails(agreement);
            String confirm = promptForInput("Are you sure you want to delete this rental agreement? (yes/no)");
            if (confirm.equalsIgnoreCase("yes")) {
                if (rentalManager.deleteRentalAgreement(id)) {
                    displaySuccess("Rental Agreement deleted successfully!");
                } else {
                    displayError("Failed to delete Rental Agreement. It may not exist.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            displayError("Rental Agreement not found.");
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }

    private void viewRentalAgreement() {
        String id = reader.readLine("Enter agreement ID to view: ");
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
            displayRentalAgreementDetails(agreement);
        } else {
            displayError("Rental Agreement not found.");
        }
    }

    private void displayRentalAgreementDetails(RentalAgreement agreement) {
        String border = "╔══════════════════════════════════════════════════════════════╗";
        String separator = "╠══════════════════════════════════════════════════════════════╣";
        String footer = "╚══════════════════════════════════════════════════════════════╝";

        System.out.println(ANSI_BLUE + border + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                  RENTAL AGREEMENT DETAILS                 " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + separator + ANSI_RESET);
        
        printDetailRow("Agreement ID", agreement.getId());
        printDetailRow("Main Tenant", agreement.getMainTenant().getFullName());
        printDetailRow("Property", agreement.getProperty().getAddress());
        printDetailRow("Period", agreement.getPeriod().toString());
        printDetailRow("Contract Date", DateUtil.formatDate(agreement.getContractDate()));
        printDetailRow("Renting Fee", String.format("$%.2f", agreement.getRentingFee()));
        printDetailRow("Status", agreement.getStatus().toString());
        printDetailRow("Sub-tenants", String.valueOf(agreement.getSubTenants().size()));

        System.out.println(ANSI_BLUE + footer + ANSI_RESET);
    }

    private void printDetailRow(String label, String value) {
        System.out.printf(ANSI_BLUE + "║" + ANSI_RESET + " %-20s : %-41s " + ANSI_BLUE + "║%n" + ANSI_RESET, label, value);
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

    private void viewTenant() {
        String id = reader.readLine("Enter tenant ID to view: ");
        Tenant tenant = tenantManager.getTenant(id);
        if (tenant != null) {
            displayTenantDetails(tenant);
        } else {
            displayError("Tenant not found.");
        }
    }

    private void displayTenantDetails(Tenant tenant) {
        String border = "╔══════════════════════════════════════════════════════════════╗";
        String separator = "╠══════════════════════════════════════════════════════════════╣";
        String footer = "╚══════════════════════════════════════════════════════════════╝";

        System.out.println(ANSI_BLUE + border + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                     TENANT DETAILS                       " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + separator + ANSI_RESET);
        
        printDetailRow("Tenant ID", tenant.getId());
        printDetailRow("Full Name", tenant.getFullName());
        printDetailRow("Date of Birth", DateUtil.formatDate(tenant.getDateOfBirth()));
        printDetailRow("Contact Info", tenant.getContactInformation());
        printDetailRow("Rental Agreements", String.valueOf(tenant.getRentalAgreements().size()));
        printDetailRow("Payment Transactions", String.valueOf(tenant.getPaymentTransactions().size()));

        System.out.println(ANSI_BLUE + footer + ANSI_RESET);
    }

    private void addTenant() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                       ADD TENANT                         " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Tenant ID");
        String fullName = promptForInput("Full Name");
        String dateOfBirth = promptForInput("Date of Birth (YYYY-MM-DD)");
        String email = promptForInput("Email");
        String phone = promptForInput("Phone Number");
        
        try {
            Tenant newTenant = new Tenant(id, fullName, DateUtil.parseDate(dateOfBirth), email + ", " + phone);
            if (tenantManager.addTenant(newTenant)) {
                displaySuccess("Tenant added successfully!");
            } else {
                displayError("Failed to add tenant. Tenant ID may already exist.");
            }
        } catch (IllegalArgumentException e) {
            displayError("Invalid input: " + e.getMessage());
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }

    private void updateTenant() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                    UPDATE TENANT                        " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Enter tenant ID to update");
        Tenant tenant = tenantManager.getTenant(id);
        if (tenant != null) {
            String fullName = promptForInput("Enter new full name (or press enter to keep current: " + tenant.getFullName() + ")");
            if (!fullName.isEmpty()) tenant.setFullName(fullName);
            
            String dobString = promptForInput("Enter new date of birth (YYYY-MM-DD) (or press enter to keep current: " + DateUtil.formatDate(tenant.getDateOfBirth()) + ")");
            if (!dobString.isEmpty()) {
                try {
                    Date newDob = DateUtil.parseDate(dobString);
                    tenant.setDateOfBirth(newDob);
                } catch (IllegalArgumentException e) {
                    displayError("Invalid date format. Keeping current value.");
                }
            }
            
            String email = promptForInput("Enter new email (or press enter to keep current: " + tenant.getContactInformation().split(", ")[0] + ")");
            String phone = promptForInput("Enter new phone number (or press enter to keep current: " + tenant.getContactInformation().split(", ")[1] + ")");
            if (!email.isEmpty() || !phone.isEmpty()) {
                String[] currentContact = tenant.getContactInformation().split(", ");
                String newEmail = email.isEmpty() ? currentContact[0] : email;
                String newPhone = phone.isEmpty() ? currentContact[1] : phone;
                tenant.setContactInformation(newEmail + ", " + newPhone);
            }
            
            if (tenantManager.updateTenant(tenant)) {
                displaySuccess("Tenant updated successfully!");
            } else {
                displayError("Failed to update tenant.");
            }
        } else {
            displayError("Tenant not found.");
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }
    
    private void deleteTenant() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                    DELETE TENANT                        " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Enter tenant ID to delete");
        Tenant tenant = tenantManager.getTenant(id);
        if (tenant != null) {
            System.out.println("Tenant details:");
            displayTenantDetails(tenant);
            String confirm = promptForInput("Are you sure you want to delete this tenant? (yes/no)");
            if (confirm.equalsIgnoreCase("yes")) {
                if (tenantManager.deleteTenant(id)) {
                    displaySuccess("Tenant deleted successfully!");
                } else {
                    displayError("Failed to delete tenant. Tenant may not exist.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            displayError("Tenant not found.");
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }

    private void updateHost() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                     UPDATE HOST                         " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Enter host ID to update");
        Host host = hostManager.getHost(id);
        if (host != null) {
            String fullName = promptForInput("Enter new full name (or press enter to keep current: " + host.getFullName() + ")");
            if (!fullName.isEmpty()) host.setFullName(fullName);
            
            String dobString = promptForInput("Enter new date of birth (YYYY-MM-DD) (or press enter to keep current: " + DateUtil.formatDate(host.getDateOfBirth()) + ")");
            if (!dobString.isEmpty()) {
                try {
                    Date newDob = DateUtil.parseDate(dobString);
                    host.setDateOfBirth(newDob);
                } catch (IllegalArgumentException e) {
                    displayError("Invalid date format. Keeping current value.");
                }
            }
            
            String email = promptForInput("Enter new email (or press enter to keep current: " + host.getContactInformation().split(", ")[0] + ")");
            String phone = promptForInput("Enter new phone number (or press enter to keep current: " + host.getContactInformation().split(", ")[1] + ")");
            if (!email.isEmpty() || !phone.isEmpty()) {
                String[] currentContact = host.getContactInformation().split(", ");
                String newEmail = email.isEmpty() ? currentContact[0] : email;
                String newPhone = phone.isEmpty() ? currentContact[1] : phone;
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

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }
    
    private void deleteHost() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                     DELETE HOST                         " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Enter host ID to delete");
        Host host = hostManager.getHost(id);
        if (host != null) {
            System.out.println("Host details:");
            displayHostDetails(host);
            String confirm = promptForInput("Are you sure you want to delete this host? (yes/no)");
            if (confirm.equalsIgnoreCase("yes")) {
                if (hostManager.deleteHost(id)) {
                    displaySuccess("Host deleted successfully!");
                } else {
                    displayError("Failed to delete host. Host may not exist.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            displayError("Host not found.");
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }
    
    private void viewHost() {
        String id = reader.readLine("Enter host ID to view: ");
        Host host = hostManager.getHost(id);
        if (host != null) {
            displayHostDetails(host);
        } else {
            displayError("Host not found.");
        }
    }

    private void displayHostDetails(Host host) {
        String border = "╔══════════════════════════════════════════════════════════════╗";
        String separator = "╠══════════════════════════════════════════════════════════════╣";
        String footer = "╚══════════════════════════════════════════════════════════════╝";

        System.out.println(ANSI_BLUE + border + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                      HOST DETAILS                        " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + separator + ANSI_RESET);
        
        printDetailRow("Host ID", host.getId());
        printDetailRow("Full Name", host.getFullName());
        printDetailRow("Date of Birth", DateUtil.formatDate(host.getDateOfBirth()));
        printDetailRow("Contact Info", host.getContactInformation());
        printDetailRow("Managed Properties", String.valueOf(host.getManagedProperties().size()));
        printDetailRow("Cooperating Owners", String.valueOf(host.getCooperatingOwners().size()));
        printDetailRow("Rental Agreements", String.valueOf(host.getRentalAgreements().size()));

        System.out.println(ANSI_BLUE + footer + ANSI_RESET);
    }

    private void addHost() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                        ADD HOST                          " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Host ID");
        String fullName = promptForInput("Full Name");
        String dateOfBirth = promptForInput("Date of Birth (YYYY-MM-DD)");
        String email = promptForInput("Email");
        String phone = promptForInput("Phone Number");
        
        try {
            Host newHost = new Host(id, fullName, DateUtil.parseDate(dateOfBirth), email + ", " + phone);
            if (hostManager.addHost(newHost)) {
                displaySuccess("Host added successfully!");
            } else {
                displayError("Failed to add host. Host ID may already exist.");
            }
        } catch (IllegalArgumentException e) {
            displayError("Invalid input: " + e.getMessage());
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }

    private void addPayment() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                      ADD PAYMENT                        " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Payment ID");
        String rentalAgreementId = promptForInput("Rental Agreement ID");
        double amount = Double.parseDouble(promptForInput("Payment Amount"));
        String dateStr = promptForInput("Payment Date (YYYY-MM-DD)");
        String paymentMethod = promptForInput("Payment Method");

        try {
            Payment payment = new Payment(id, amount, DateUtil.parseDate(dateStr), paymentMethod, rentalAgreementId);
            if (rentalManager.addPayment(payment)) {
                displaySuccess("Payment added successfully!");
            } else {
                displayError("Failed to add payment. Rental agreement may not exist.");
            }
        } catch (IllegalArgumentException e) {
            displayError("Invalid input: " + e.getMessage());
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }

    private void viewPaymentsForRentalAgreement() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "            VIEW PAYMENTS FOR RENTAL AGREEMENT           " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String rentalAgreementId = promptForInput("Enter Rental Agreement ID");
        List<Payment> payments = rentalManager.getPaymentsForRentalAgreement(rentalAgreementId);
        
        if (payments == null || payments.isEmpty()) {
            displayWarning("No payments found for this rental agreement.");
        } else {
            displayPayments(payments);
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }

    private void viewAllPayments() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                    ALL PAYMENTS                        " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        List<Payment> payments = rentalManager.getAllPayments();
        if (payments.isEmpty()) {
            displayWarning("No payments found.");
        } else {
            displayPayments(payments);
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
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
    
    private void updateProperty() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                   UPDATE PROPERTY                       " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Enter property ID to update");
        Property property = propertyManager.getProperty(id);
        if (property != null) {
            String address = promptForInput("Enter new address (or press enter to keep current: " + property.getAddress() + ")");
            if (!address.isEmpty()) property.setAddress(address);
            
            String priceString = promptForInput("Enter new price (or press enter to keep current: $" + property.getPrice() + ")");
            if (!priceString.isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(priceString);
                    property.setPrice(newPrice);
                } catch (NumberFormatException e) {
                    displayError("Invalid price. Keeping current value.");
                }
            }
            
            String statusInput = promptForInput("Enter new status (AVAILABLE, RENTED, UNDER_MAINTENANCE) (or press enter to keep current: " + property.getStatus() + ")");
            if (!statusInput.isEmpty()) {
                try {
                    property.setStatus(Property.Status.valueOf(statusInput.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    displayError("Invalid status. Keeping current value.");
                }
            }
            
            if (property instanceof ResidentialProperty) {
                ResidentialProperty rp = (ResidentialProperty) property;
                String bedroomsString = promptForInput("Enter new number of bedrooms (or press enter to keep current: " + rp.getNumberOfBedrooms() + ")");
                if (!bedroomsString.isEmpty()) {
                    try {
                        int newBedrooms = Integer.parseInt(bedroomsString);
                        rp.setNumberOfBedrooms(newBedrooms);
                    } catch (NumberFormatException e) {
                        displayError("Invalid number of bedrooms. Keeping current value.");
                    }
                }
            } else if (property instanceof CommercialProperty) {
                CommercialProperty cp = (CommercialProperty) property;
                String businessType = promptForInput("Enter new business type (or press enter to keep current: " + cp.getBusinessType() + ")");
                if (!businessType.isEmpty()) cp.setBusinessType(businessType);
            }
            
            if (propertyManager.updateProperty(property)) {
                displaySuccess("Property updated successfully!");
            } else {
                displayError("Failed to update property.");
            }
        } else {
            displayError("Property not found.");
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }
    
    private void deleteProperty() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                   DELETE PROPERTY                       " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Enter property ID to delete");
        Property property = propertyManager.getProperty(id);
        if (property != null) {
            System.out.println("Property details:");
            displayPropertyDetails(property);
            String confirm = promptForInput("Are you sure you want to delete this property? (yes/no)");
            if (confirm.equalsIgnoreCase("yes")) {
                if (propertyManager.deleteProperty(id)) {
                    displaySuccess("Property deleted successfully!");
                } else {
                    displayError("Failed to delete property. Property may not exist.");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        } else {
            displayError("Property not found.");
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }
    
    private void viewProperty() {
        String id = reader.readLine("Enter property ID to view: ");
        Property property = propertyManager.getProperty(id);
        if (property != null) {
            displayPropertyDetails(property);
        } else {
            displayError("Property not found.");
        }
    }

    private void displayPropertyDetails(Property property) {
        String border = "╔══════════════════════════════════════════════════════════════╗";
        String separator = "╠══════════════════════════════════════════════════════════════╣";
        String footer = "╚══════════════════════════════════════════════════════════════╝";

        System.out.println(ANSI_BLUE + border + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                    PROPERTY DETAILS                      " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + separator + ANSI_RESET);
        
        printDetailRow("Property ID", property.getId());
        printDetailRow("Address", property.getAddress());
        printDetailRow("Price", String.format("$%.2f", property.getPrice()));
        printDetailRow("Status", property.getStatus().toString());
        printDetailRow("Owner", property.getOwner());
        printDetailRow("Type", property instanceof ResidentialProperty ? "Residential" : "Commercial");
        
        if (property instanceof ResidentialProperty) {
            ResidentialProperty rp = (ResidentialProperty) property;
            printDetailRow("Bedrooms", String.valueOf(rp.getNumberOfBedrooms()));
            printDetailRow("Has Garden", rp.isHasGarden() ? "Yes" : "No");
            printDetailRow("Pet Friendly", rp.isPetFriendly() ? "Yes" : "No");
        } else if (property instanceof CommercialProperty) {
            CommercialProperty cp = (CommercialProperty) property;
            printDetailRow("Business Type", cp.getBusinessType());
            printDetailRow("Parking Spaces", String.valueOf(cp.getParkingSpaces()));
            printDetailRow("Square Footage", String.format("%.2f", cp.getSquareFootage()));
        }

        System.out.println(ANSI_BLUE + footer + ANSI_RESET);
    }

    private void addProperty() {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                     ADD PROPERTY                         " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String id = promptForInput("Property ID");
        String address = promptForInput("Address");
        double price = Double.parseDouble(promptForInput("Price"));
        String statusInput = promptForInput("Status (AVAILABLE, RENTED, UNDER_MAINTENANCE)");
        Property.Status status = Property.Status.valueOf(statusInput.toUpperCase());
        String owner = promptForInput("Owner ID");
        
        String propertyType = promptForInput("Property Type (RESIDENTIAL or COMMERCIAL)");
        Property newProperty;
        
        try {
            if (propertyType.equalsIgnoreCase("RESIDENTIAL")) {
                int bedrooms = Integer.parseInt(promptForInput("Number of Bedrooms"));
                boolean hasGarden = promptForInput("Has Garden? (yes/no)").equalsIgnoreCase("yes");
                boolean isPetFriendly = promptForInput("Is Pet Friendly? (yes/no)").equalsIgnoreCase("yes");
                newProperty = new ResidentialProperty(id, address, price, status, owner, bedrooms, hasGarden, isPetFriendly);
            } else if (propertyType.equalsIgnoreCase("COMMERCIAL")) {
                String businessType = promptForInput("Business Type");
                int parkingSpaces = Integer.parseInt(promptForInput("Number of Parking Spaces"));
                double squareFootage = Double.parseDouble(promptForInput("Square Footage"));
                newProperty = new CommercialProperty(id, address, price, status, owner, businessType, parkingSpaces, squareFootage);
            } else {
                throw new IllegalArgumentException("Invalid property type. Must be RESIDENTIAL or COMMERCIAL.");
            }
            
            if (propertyManager.addProperty(newProperty)) {
                displaySuccess("Property added successfully!");
            } else {
                displayError("Failed to add property. Property ID may already exist.");
            }
        } catch (IllegalArgumentException e) {
            displayError("Invalid input: " + e.getMessage());
        }

        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);
    }

    private void manageTenants() {
        boolean managing = true;
        while (managing) {
            System.out.println(ANSI_YELLOW + "\n=== Manage Tenants ===" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "1. " + ANSI_RESET + "Add Tenant");
            System.out.println(ANSI_BLUE + "2. " + ANSI_RESET + "Update Tenant");
            System.out.println(ANSI_BLUE + "3. " + ANSI_RESET + "Delete Tenant");
            System.out.println(ANSI_BLUE + "4. " + ANSI_RESET + "View Tenant");
            System.out.println(ANSI_BLUE + "5. " + ANSI_RESET + "View All Tenants");
            System.out.println(ANSI_BLUE + "6. " + ANSI_RESET + "Return to Main Menu");

            String choice = reader.readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    addTenant();
                    break;
                case "2":
                    updateTenant();
                    break;
                case "3":
                    deleteTenant();
                    break;
                case "4":
                    viewTenant();
                    break;
                case "5":
                    viewAllTenants();
                    break;
                case "6":
                    managing = false;
                    break;
                default:
                    displayError("Invalid choice. Please try again.");
            }
        }
    }

    private void manageHosts() {
        boolean managing = true;
        while (managing) {
            System.out.println(ANSI_YELLOW + "\n=== Manage Hosts ===" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "1. " + ANSI_RESET + "Add Host");
            System.out.println(ANSI_BLUE + "2. " + ANSI_RESET + "Update Host");
            System.out.println(ANSI_BLUE + "3. " + ANSI_RESET + "Delete Host");
            System.out.println(ANSI_BLUE + "4. " + ANSI_RESET + "View Host");
            System.out.println(ANSI_BLUE + "5. " + ANSI_RESET + "View All Hosts");
            System.out.println(ANSI_BLUE + "6. " + ANSI_RESET + "Return to Main Menu");

            String choice = reader.readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    addHost();
                    break;
                case "2":
                    updateHost();
                    break;
                case "3":
                    deleteHost();
                    break;
                case "4":
                    viewHost();
                    break;
                case "5":
                    viewAllHosts();
                    break;
                case "6":
                    managing = false;
                    break;
                default:
                    displayError("Invalid choice. Please try again.");
            }
        }
    }

    private void manageProperties() {
        boolean managing = true;
        while (managing) {
            System.out.println(ANSI_YELLOW + "\n=== Manage Properties ===" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "1. " + ANSI_RESET + "Add Property");
            System.out.println(ANSI_BLUE + "2. " + ANSI_RESET + "Update Property");
            System.out.println(ANSI_BLUE + "3. " + ANSI_RESET + "Delete Property");
            System.out.println(ANSI_BLUE + "4. " + ANSI_RESET + "View Property");
            System.out.println(ANSI_BLUE + "5. " + ANSI_RESET + "View All Properties");
            System.out.println(ANSI_BLUE + "6. " + ANSI_RESET + "Return to Main Menu");

            String choice = reader.readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    addProperty();
                    break;
                case "2":
                    updateProperty();
                    break;
                case "3":
                    deleteProperty();
                    break;
                case "4":
                    viewProperty();
                    break;
                case "5":
                    viewAllProperties();
                    break;
                case "6":
                    managing = false;
                    break;
                default:
                    displayError("Invalid choice. Please try again.");
            }
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
            System.out.println(ANSI_YELLOW + "\n=== Generate Reports ===" + ANSI_RESET);
            System.out.println(ANSI_BLUE + "1. " + ANSI_RESET + "Generate Tenant Report");
            System.out.println(ANSI_BLUE + "2. " + ANSI_RESET + "Generate Host Report");
            System.out.println(ANSI_BLUE + "3. " + ANSI_RESET + "Generate Property Report");
            System.out.println(ANSI_BLUE + "4. " + ANSI_RESET + "Generate Rental Agreement Report");
            System.out.println(ANSI_BLUE + "5. " + ANSI_RESET + "Return to Main Menu");

            String choice = reader.readLine("Enter your choice: ");
            switch (choice) {
                case "1":
                    generateTenantReport();
                    break;
                case "2":
                    generateHostReport();
                    break;
                case "3":
                    generatePropertyReport();
                    break;
                case "4":
                    generateRentalAgreementReport();
                    break;
                case "5":
                    generating = false;
                    break;
                default:
                    displayError("Invalid choice. Please try again.");
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

    private void managePayments() {
    boolean managing = true;
    while (managing) {
        System.out.println(ANSI_BLUE + "╔══════════════════════════════════════════════════════════════╗" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_YELLOW + "                   MANAGE PAYMENTS                      " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 1. Add Payment                                         " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 2. View Payments for Rental Agreement                  " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 3. View All Payments                                   " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "║" + ANSI_RESET + " 4. Return to Rental Agreement Menu                     " + ANSI_BLUE + "║" + ANSI_RESET);
        System.out.println(ANSI_BLUE + "╠══════════════════════════════════════════════════════════════╣" + ANSI_RESET);

        String choice = promptForInput("Enter your choice");
        System.out.println(ANSI_BLUE + "╚══════════════════════════════════════════════════════════════╝" + ANSI_RESET);

        switch (choice) {
            case "1":
                addPayment();
                break;
            case "2":
                viewPaymentsForRentalAgreement();
                break;
            case "3":
                viewAllPayments();
                break;
            case "4":
                managing = false;
                break;
            default:
                displayError("Invalid choice. Please try again.");
        }
    }
}

    public static void main(String[] args) {
        try {
            ConsoleUI ui = new ConsoleUI();
            ui.start();
        } catch (IOException e) {
            System.err.println("Error starting UI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}




