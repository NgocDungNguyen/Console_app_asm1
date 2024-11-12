package com.rentalsystem.ui;

import com.rentalsystem.manager.*;
import com.rentalsystem.model.*;
import com.rentalsystem.util.DateUtil;
import com.rentalsystem.util.FileHandler;
import com.rentalsystem.util.AsciiTableGenerator;
import org.jline.reader.*;
import org.jline.terminal.*;
import java.util.*;
import java.util.Arrays;
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
                if (confirmExit()) {
                    running = false;
                }
                break;
            default:
                displayError("Invalid choice. Please try again.");
        }
    }
    saveAllData();
    displayExitMessage();
    System.exit(0);  // This will ensure the program exits
}

private boolean confirmExit() {
    String response = reader.readLine(ANSI_YELLOW + "Are you sure you want to exit? (y/n): " + ANSI_RESET);
    return response.toLowerCase().startsWith("y");
}

    private void displayLogo() {
    System.out.println(ANSI_BLUE +
        "╔═══════════════════════════════════════════════════════════════════════════╗\n" +
        "║                                                                           ║\n" +
        "║           ██████╗ ███████╗███╗   ██╗████████╗ █████╗ ██╗                  ║\n" +
        "║           ██╔══██╗██╔════╝████╗  ██║╚══██╔══╝██╔══██╗██║                  ║\n" +
        "║           ██████╔╝█████╗  ██╔██╗ ██║   ██║   ███████║██║                  ║\n" +
        "║           ██╔══██╗██╔══╝  ██║╚██╗██║   ██║   ██╔══██║██║                  ║\n" +
        "║           ██║  ██║███████╗██║ ╚████║   ██║   ██║  ██║███████╗             ║\n" +
        "║           ╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝   ╚═╝   ╚═╝  ╚═╝╚══════╝             ║\n" +
        "║                                                                           ║\n" +
        "║           ███╗   ███╗ █████╗ ███╗   ██╗ █████╗  ██████╗ ███████╗          ║\n" +
        "║           ████╗ ████║██╔══██╗████╗  ██║██╔══██╗██╔════╝ ██╔════╝          ║\n" +
        "║           ██╔████╔██║███████║██╔██╗ ██║███████║██║  ███╗█████╗            ║\n" +
        "║           ██║╚██╔╝██║██╔══██║██║╚██╗██║██╔══██║██║   ██║██╔══╝            ║\n" +
        "║           ██║ ╚═╝ ██║██║  ██║██║ ╚████║██║  ██║╚██████╔╝███████╗          ║\n" +
        "║           ╚═╝     ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝          ║\n" +
        "║                                                                           ║\n" +
        "║           ███████╗██╗   ██╗███████╗████████╗███████╗███╗   ███╗           ║\n" +
        "║           ██╔════╝╚██╗ ██╔╝██╔════╝╚══██╔══╝██╔════╝████╗ ████║           ║\n" +
        "║           ███████╗ ╚████╔╝ ███████╗   ██║   █████╗  ██╔████╔██║           ║\n" +
        "║           ╚════██║  ╚██╔╝  ╚════██║   ██║   ██╔══╝  ██║╚██╔╝██║           ║\n" +
        "║           ███████║   ██║   ███████║   ██║   ███████╗██║ ╚═╝ ██║           ║\n" +
        "║           ╚══════╝   ╚═╝   ╚══════╝   ╚═╝   ╚══════╝╚═╝     ╚═╝           ║\n" +
        "║                                                                           ║\n" +
        "║                 Welcome to the Rental Management System                   ║\n" +
        "║                                                                           ║\n" +
        "╚═══════════════════════════════════════════════════════════════════════════╝" +
        ANSI_RESET);
}

private void displayExitMessage() {
    System.out.println(ANSI_GREEN +
        "╔═══════════════════════════════════════════════════════════════════════════╗\n" +
        "║                                                                           ║\n" +
        "║ ████████╗██╗  ██╗ █████╗ ███╗   ██╗██╗  ██╗    ██╗   ██╗ ██████╗ ██╗   ██╗║\n" +
        "║ ╚══██╔══╝██║  ██║██╔══██╗████╗  ██║██║ ██╔╝    ╚██╗ ██╔╝██╔═══██╗██║   ██║║\n" +
        "║    ██║   ███████║███████║██╔██╗ ██║█████╔╝      ╚████╔╝ ██║   ██║██║   ██║║\n" +
        "║    ██║   ██╔══██║██╔══██║██║╚██╗██║██╔═██╗       ╚██╔╝  ██║   ██║██║   ██║║\n" +
        "║    ██║   ██║  ██║██║  ██║██║ ╚████║██║  ██╗       ██║   ╚██████╔╝╚██████╔╝║\n" +
        "║    ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝╚═╝  ╚═╝       ╚═╝    ╚═════╝  ╚═════╝ ║\n" +
        "║                                                                           ║\n" +
        "║                  for using the Rental Management System                   ║\n" +
        "║                                                                           ║\n" +
        "║                      We hope to see you again soon!                       ║\n" +
        "║                                                                           ║\n" +
        "╚═══════════════════════════════════════════════════════════════════════════╝" +
        ANSI_RESET);
}
    private void displayMainMenu() {
    System.out.println(ANSI_YELLOW +
        "╔════════════════════════════════════╗\n" +
        "║             MAIN MENU              ║\n" +
        "╠════════════════════════════════════╣\n" +
        "║ 1. Manage Rental Agreements        ║\n" +
        "║ 2. Manage Tenants                  ║\n" +
        "║ 3. Manage Hosts                    ║\n" +
        "║ 4. Manage Properties               ║\n" +
        "║ 5. Generate Reports                ║\n" +
        "║ 6. Exit                            ║\n" +
        "╚════════════════════════════════════╝" +
        ANSI_RESET);
}

   private void displayStatusBar(String username) {
    String date = java.time.LocalDate.now().toString();
    String time = java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));
    System.out.println(ANSI_BLUE +
        "╔══════════════╦════════════╦══════════╗\n" +
        "║ Current User ║ Date       ║ Time     ║\n" +
        "╠══════════════╬════════════╬══════════╣\n" +
        String.format("║ %-12s ║ %-10s ║ %-8s ║%n", username, date, time) +
        "╚══════════════╩════════════╩══════════╝" +
        ANSI_RESET);
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

    private void manageRentalAgreements() {
        boolean managing = true;
        while (managing) {
            String[] options = {
                "1. Add Rental Agreement",
                "2. Update Rental Agreement",
                "3. Delete Rental Agreement",
                "4. View Rental Agreement",
                "5. View All Rental Agreements",
                "6. Manage Payments",
                "7. Return to Main Menu"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateMenuTable("MANAGE RENTAL AGREEMENTS", options) + ANSI_RESET);

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
        String[] prompts = {
            "Agreement ID",
            "Tenant ID",
            "Property ID",
            "Rental Period (DAILY, WEEKLY, FORTNIGHTLY, MONTHLY)",
            "Contract Date (YYYY-MM-DD)",
            "Renting Fee",
            "Status (NEW, ACTIVE, COMPLETED)"
        };
        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateInputPromptTable("ADD RENTAL AGREEMENT", prompts) + ANSI_RESET);

        String id = promptForInput("Agreement ID");
        String tenantId = promptForInput("Tenant ID");
        String propertyId = promptForInput("Property ID");
        String periodInput = promptForInput("Rental Period");
        String contractDateStr = promptForInput("Contract Date");
        double rentingFee = Double.parseDouble(promptForInput("Renting Fee"));
        String statusInput = promptForInput("Status");

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
    }

    private String promptForInput(String prompt) {
        return reader.readLine(ANSI_BLUE + prompt + ": " + ANSI_RESET);
    }

    private void updateRentalAgreement() {
        String id = promptForInput("Enter agreement ID to update");
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
            String[] prompts = {
                "New rental period (current: " + agreement.getPeriod() + ")",
                "New renting fee (current: $" + agreement.getRentingFee() + ")",
                "New status (current: " + agreement.getStatus() + ")"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateInputPromptTable("UPDATE RENTAL AGREEMENT", prompts) + ANSI_RESET);

            String periodInput = promptForInput("Enter new rental period");
            if (!periodInput.isEmpty()) {
                try {
                    agreement.setPeriod(RentalAgreement.Period.valueOf(periodInput.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    displayError("Invalid period. Keeping current value.");
                }
            }

            String rentingFeeStr = promptForInput("Enter new renting fee");
            if (!rentingFeeStr.isEmpty()) {
                try {
                    double newFee = Double.parseDouble(rentingFeeStr);
                    agreement.setRentingFee(newFee);
                } catch (NumberFormatException e) {
                    displayError("Invalid renting fee. Keeping current value.");
                }
            }

            String statusInput = promptForInput("Enter new status");
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
    }

    private void deleteRentalAgreement() {
        String id = promptForInput("Enter agreement ID to delete");
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
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
    }

    private void viewRentalAgreement() {
        String id = promptForInput("Enter agreement ID to view");
        RentalAgreement agreement = rentalManager.getRentalAgreement(id);
        if (agreement != null) {
            displayRentalAgreementDetails(agreement);
        } else {
            displayError("Rental Agreement not found.");
        }
    }

    private void displayRentalAgreementDetails(RentalAgreement agreement) {
        String[] headers = {"Field", "Value"};
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Agreement ID", agreement.getId()});
        data.add(new String[]{"Main Tenant", agreement.getMainTenant().getFullName()});
        data.add(new String[]{"Property", agreement.getProperty().getAddress()});
        data.add(new String[]{"Period", agreement.getPeriod().toString()});
        data.add(new String[]{"Contract Date", DateUtil.formatDate(agreement.getContractDate())});
        data.add(new String[]{"Renting Fee", String.format("$%.2f", agreement.getRentingFee())});
        data.add(new String[]{"Status", agreement.getStatus().toString()});
        data.add(new String[]{"Sub-tenants", String.valueOf(agreement.getSubTenants().size())});

        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
    }

    private void viewAllRentalAgreements() {
        List<RentalAgreement> agreements = rentalManager.getAllRentalAgreements();
        if (agreements.isEmpty()) {
            displayWarning("No rental agreements found in the system.");
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
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
        }
    }

    private void manageTenants() {
        boolean managing = true;
        while (managing) {
            String[] options = {
                "1. Add Tenant",
                "2. Update Tenant",
                "3. Delete Tenant",
                "4. View Tenant",
                "5. View All Tenants",
                "6. Return to Main Menu"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateMenuTable("MANAGE TENANTS", options) + ANSI_RESET);

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

    private void addTenant() {
        String[] prompts = {
            "Tenant ID",
            "Full Name",
            "Date of Birth (YYYY-MM-DD)",
            "Email",
            "Phone Number"
        };
        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateInputPromptTable("ADD TENANT", prompts) + ANSI_RESET);

        String id = promptForInput("Tenant ID");
        String fullName = promptForInput("Full Name");
        String dateOfBirth = promptForInput("Date of Birth");
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
    }

    private void updateTenant() {
        String id = promptForInput("Enter tenant ID to update");
        Tenant tenant = tenantManager.getTenant(id);
        if (tenant != null) {
            String[] prompts = {
                "New full name (current: " + tenant.getFullName() + ")",
                "New date of birth (current: " + DateUtil.formatDate(tenant.getDateOfBirth()) + ")",
                "New email (current: " + tenant.getContactInformation().split(", ")[0] + ")",
                "New phone number (current: " + tenant.getContactInformation().split(", ")[1] + ")"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateInputPromptTable("UPDATE TENANT", prompts) + ANSI_RESET);

            String fullName = promptForInput("Enter new full name");
            if (!fullName.isEmpty()) tenant.setFullName(fullName);
            
            String dobString = promptForInput("Enter new date of birth");
            if (!dobString.isEmpty()) {
                try {
                    Date newDob = DateUtil.parseDate(dobString);
                    tenant.setDateOfBirth(newDob);
                } catch (IllegalArgumentException e) {
                    displayError("Invalid date format. Keeping current value.");
                }
            }
            
            String email = promptForInput("Enter new email");
            String phone = promptForInput("Enter new phone number");
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
    }

    private void deleteTenant() {
        String id = promptForInput("Enter tenant ID to delete");
        Tenant tenant = tenantManager.getTenant(id);
        if (tenant != null) {
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
    }

    private void viewTenant() {
        String id = promptForInput("Enter tenant ID to view");
        Tenant tenant = tenantManager.getTenant(id);
        if (tenant != null) {
            displayTenantDetails(tenant);
        } else {
            displayError("Tenant not found.");
        }
    }

    private void displayTenantDetails(Tenant tenant) {
        String[] headers = {"Field", "Value"};
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Tenant ID", tenant.getId()});
        data.add(new String[]{"Full Name", tenant.getFullName()});
        data.add(new String[]{"Date of Birth", DateUtil.formatDate(tenant.getDateOfBirth())});
        data.add(new String[]{"Contact Info", tenant.getContactInformation()});
        data.add(new String[]{"Rental Agreements", String.valueOf(tenant.getRentalAgreements().size())});
        data.add(new String[]{"Payment Transactions", String.valueOf(tenant.getPaymentTransactions().size())});

        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
    }

    private void viewAllTenants() {
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
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
        }
    }

    private void manageHosts() {
        boolean managing = true;
        while (managing) {
            String[] options = {
                "1. Add Host",
                "2. Update Host",
                "3. Delete Host",
                "4. View Host",
                "5. View All Hosts",
                "6. Return to Main Menu"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateMenuTable("MANAGE HOSTS", options) + ANSI_RESET);

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

    private void addHost() {
        String[] prompts = {
            "Host ID",
            "Full Name",
            "Date of Birth (YYYY-MM-DD)",
            "Email",
            "Phone Number"
        };
        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateInputPromptTable("ADD HOST", prompts) + ANSI_RESET);

        String id = promptForInput("Host ID");
        String fullName = promptForInput("Full Name");
        String dateOfBirth = promptForInput("Date of Birth");
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
    }

    private void updateHost() {
        String id = promptForInput("Enter host ID to update");
        Host host = hostManager.getHost(id);
        if (host != null) {
            String[] prompts = {
                "New full name (current: " + host.getFullName() + ")",
                "New date of birth (current: " + DateUtil.formatDate(host.getDateOfBirth()) + ")",
                "New email (current: " + host.getContactInformation().split(", ")[0] + ")",
                "New phone number (current: " + host.getContactInformation().split(", ")[1] + ")"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateInputPromptTable("UPDATE HOST", prompts) + ANSI_RESET);

            String fullName = promptForInput("Enter new full name");
            if (!fullName.isEmpty()) host.setFullName(fullName);
            
            String dobString = promptForInput("Enter new date of birth");
            if (!dobString.isEmpty()) {
                try {
                    Date newDob = DateUtil.parseDate(dobString);
                    host.setDateOfBirth(newDob);
                } catch (IllegalArgumentException e) {
                    displayError("Invalid date format. Keeping current value.");
                }
            }
            
            String email = promptForInput("Enter new email");
            String phone = promptForInput("Enter new phone number");
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
    }

    private void deleteHost() {
        String id = promptForInput("Enter host ID to delete");
        Host host = hostManager.getHost(id);
        if (host != null) {
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
    }

    private void viewHost() {
        String id = promptForInput("Enter host ID to view");
        Host host = hostManager.getHost(id);
        if (host != null) {
            displayHostDetails(host);
        } else {
            displayError("Host not found.");
        }
    }

    private void displayHostDetails(Host host) {
        String[] headers = {"Field", "Value"};
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Host ID", host.getId()});
        data.add(new String[]{"Full Name", host.getFullName()});
        data.add(new String[]{"Date of Birth", DateUtil.formatDate(host.getDateOfBirth())});
        data.add(new String[]{"Contact Info", host.getContactInformation()});
        data.add(new String[]{"Managed Properties", String.valueOf(host.getManagedProperties().size())});
        data.add(new String[]{"Cooperating Owners", String.valueOf(host.getCooperatingOwners().size())});
        data.add(new String[]{"Rental Agreements", String.valueOf(host.getRentalAgreements().size())});

        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
    }

    private void viewAllHosts() {
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
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
        }
    }

    private void manageProperties() {
        boolean managing = true;
        while (managing) {
            String[] options = {
                "1. Add Property",
                "2. Update Property",
                "3. Delete Property",
                "4. View Property",
                "5. View All Properties",
                "6. Return to Main Menu"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateMenuTable("MANAGE PROPERTIES", options) + ANSI_RESET);

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

    private void addProperty() {
        String[] prompts = {
            "Property ID",
            "Address",
            "Price",
            "Status (AVAILABLE, RENTED, UNDER_MAINTENANCE)",
            "Owner ID",
            "Property Type (RESIDENTIAL or COMMERCIAL)"
        };
        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateInputPromptTable("ADD PROPERTY", prompts) + ANSI_RESET);

        String id = promptForInput("Property ID");
        String address = promptForInput("Address");
        double price = Double.parseDouble(promptForInput("Price"));
        String statusInput = promptForInput("Status");
        Property.Status status = Property.Status.valueOf(statusInput.toUpperCase());
        String owner = promptForInput("Owner ID");
        String propertyType = promptForInput("Property Type");
        
        try {
            Property newProperty;
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
    }

    private void updateProperty() {
        String id = promptForInput("Enter property ID to update");
        Property property = propertyManager.getProperty(id);
        if (property != null) {
            String[] prompts = {
                "New address (current: " + property.getAddress() + ")",
                "New price (current: $" + property.getPrice() + ")",
                "New status (current: " + property.getStatus() + ")"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateInputPromptTable("UPDATE PROPERTY", prompts) + ANSI_RESET);

            String address = promptForInput("Enter new address");
            if (!address.isEmpty()) property.setAddress(address);
            
            String priceString = promptForInput("Enter new price");
            if (!priceString.isEmpty()) {
                try {
                    double newPrice = Double.parseDouble(priceString);
                    property.setPrice(newPrice);
                } catch (NumberFormatException e) {
                    displayError("Invalid price. Keeping current value.");
                }
            }
            
            String statusInput = promptForInput("Enter new status");
            if (!statusInput.isEmpty()) {
                try {
                    property.setStatus(Property.Status.valueOf(statusInput.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    displayError("Invalid status. Keeping current value.");
                }
            }
            
            if (property instanceof ResidentialProperty) {
                ResidentialProperty rp = (ResidentialProperty) property;
                String bedroomsString = promptForInput("Enter new number of bedrooms (current: " + rp.getNumberOfBedrooms() + ")");
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
                String businessType = promptForInput("Enter new business type (current: " + cp.getBusinessType() + ")");
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
    }

    private void deleteProperty() {
        String id = promptForInput("Enter property ID to delete");
        Property property = propertyManager.getProperty(id);
        if (property != null) {
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
    }

    private void viewProperty() {
        String id = promptForInput("Enter property ID to view");
        Property property = propertyManager.getProperty(id);
        if (property != null) {
            displayPropertyDetails(property);
        } else {
            displayError("Property not found.");
        }
    }

    private void displayPropertyDetails(Property property) {
        String[] headers = {"Field", "Value"};
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Property ID", property.getId()});
        data.add(new String[]{"Address", property.getAddress()});
        data.add(new String[]{"Price", String.format("$%.2f", property.getPrice())});
        data.add(new String[]{"Status", property.getStatus().toString()});
        data.add(new String[]{"Owner", property.getOwner()});
        data.add(new String[]{"Type", property instanceof ResidentialProperty ? "Residential" : "Commercial"});
        
        if (property instanceof ResidentialProperty) {
            ResidentialProperty rp = (ResidentialProperty) property;
            data.add(new String[]{"Bedrooms", String.valueOf(rp.getNumberOfBedrooms())});
            data.add(new String[]{"Has Garden", rp.isHasGarden() ? "Yes" : "No"});
            data.add(new String[]{"Pet Friendly", rp.isPetFriendly() ? "Yes" : "No"});
        } else if (property instanceof CommercialProperty) {
            CommercialProperty cp = (CommercialProperty) property;
            data.add(new String[]{"Business Type", cp.getBusinessType()});
            data.add(new String[]{"Parking Spaces", String.valueOf(cp.getParkingSpaces())});
            data.add(new String[]{"Square Footage", String.format("%.2f", cp.getSquareFootage())});
        }

        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
    }

    private void viewAllProperties() {
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
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
        }
    }

    private void managePayments() {
        boolean managing = true;
        while (managing) {
            String[] options = {
                "1. Add Payment",
                "2. View Payments for Rental Agreement",
                "3. View All Payments",
                "4. Return to Rental Agreement Menu"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateMenuTable("MANAGE PAYMENTS", options) + ANSI_RESET);

            String choice = promptForInput("Enter your choice");
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

    private void addPayment() {
        String[] prompts = {
            "Payment ID",
            "Rental Agreement ID",
            "Payment Amount",
            "Payment Date (YYYY-MM-DD)",
            "Payment Method"
        };
        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateInputPromptTable("ADD PAYMENT", prompts) + ANSI_RESET);

        String id = promptForInput("Payment ID");
        String rentalAgreementId = promptForInput("Rental Agreement ID");
        double amount = Double.parseDouble(promptForInput("Payment Amount"));
        String dateStr = promptForInput("Payment Date");
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
    }

    private void viewPaymentsForRentalAgreement() {
        String rentalAgreementId = promptForInput("Enter Rental Agreement ID");
        List<Payment> payments = rentalManager.getPaymentsForRentalAgreement(rentalAgreementId);
        
        if (payments == null || payments.isEmpty()) {
            displayWarning("No payments found for this rental agreement.");
        } else {
            displayPayments(payments);
        }
    }

    private void viewAllPayments() {
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
        System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
    }

    private void generateReports() {
        boolean generating = true;
        while (generating) {
            String[] options = {
                "1. Generate Tenant Report",
                "2. Generate Host Report",
                "3. Generate Property Report",
                "4. Generate Rental Agreement Report",
                "5. Return to Main Menu"
            };
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateMenuTable("GENERATE REPORTS", options) + ANSI_RESET);

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
        List<Tenant> tenants = tenantManager.getAllTenants();
        if (tenants.isEmpty()) {
            displayWarning("No tenants found.");
        } else {
            String[] headers = {"ID", "Name", "Date of Birth", "Contact Info", "Rental Agreements", "Payments"};
            List<String[]> data = new ArrayList<>();
            for (Tenant tenant : tenants) {
                data.add(new String[]{
                    tenant.getId(),
                    tenant.getFullName(),
                    DateUtil.formatDate(tenant.getDateOfBirth()),
                    tenant.getContactInformation(),
                    String.valueOf(tenant.getRentalAgreements().size()),
                    String.valueOf(tenant.getPaymentTransactions().size())
                });
            }
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
        }
    }

    private void generateHostReport() {
        List<Host> hosts = hostManager.getAllHosts();
        if (hosts.isEmpty()) {
            displayWarning("No hosts found.");
        } else {
            String[] headers = {"ID", "Name", "Date of Birth", "Contact Info", "Managed Properties", "Cooperating Owners", "Rental Agreements"};
            List<String[]> data = new ArrayList<>();
            for (Host host : hosts) {
                data.add(new String[]{
                    host.getId(),
                    host.getFullName(),
                    DateUtil.formatDate(host.getDateOfBirth()),
                    host.getContactInformation(),
                    String.valueOf(host.getManagedProperties().size()),
                    String.valueOf(host.getCooperatingOwners().size()),
                    String.valueOf(host.getRentalAgreements().size())
                });
            }
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
        }
    }

    private void generatePropertyReport() {
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
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
        }
    }

    private void generateRentalAgreementReport() {
        List<RentalAgreement> agreements = rentalManager.getAllRentalAgreements();
        if (agreements.isEmpty()) {
            displayWarning("No rental agreements found.");
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
            System.out.println(ANSI_BLUE + AsciiTableGenerator.generateTable(headers, data) + ANSI_RESET);
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