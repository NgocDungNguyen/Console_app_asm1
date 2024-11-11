package com.rentalsystem.util;

import com.rentalsystem.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FileHandler {
    private static final String TENANTS_FILE = "resources/tenants.txt";
    private static final String HOSTS_FILE = "resources/hosts.txt";
    private static final String PROPERTIES_FILE = "resources/properties.txt";
    private static final String RENTAL_AGREEMENTS_FILE = "resources/rental_agreements.txt";
    private static final String PAYMENTS_FILE = "resources/payments.txt";

    private Map<String, Tenant> tenantMap = new HashMap<>();
    private Map<String, Host> hostMap = new HashMap<>();
    private Map<String, Property> propertyMap = new HashMap<>();
    private List<RentalAgreement> rentalAgreements = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    public Map<String, List<?>> loadAllData() {
       System.out.println("Starting to load all data...");
       Map<String, List<?>> loadedData = new HashMap<>();
       
       List<Tenant> tenants = loadTenants();
       loadedData.put("tenants", tenants);
       
       List<Host> hosts = loadHosts();
       loadedData.put("hosts", hosts);
       
       List<Property> properties = loadProperties();
       loadedData.put("properties", properties);
       
       List<RentalAgreement> agreements = loadRentalAgreements();
       loadedData.put("rentalAgreements", agreements);
       
       List<Payment> payments = loadPayments();
       loadedData.put("payments", payments);
       
       // Print loaded data summary
       System.out.println("Loaded data summary:");
       System.out.println("Tenants: " + tenants.size());
       System.out.println("Hosts: " + hosts.size());
       System.out.println("Properties: " + properties.size());
       System.out.println("Rental Agreements: " + agreements.size());
       System.out.println("Payments: " + payments.size());
       
       return loadedData;
   }

    public List<Tenant> loadTenants() {
       tenantMap.clear();
       System.out.println("Loading tenants...");
       try (BufferedReader reader = new BufferedReader(new FileReader(TENANTS_FILE))) {
           String line;
           while ((line = reader.readLine()) != null) {
               System.out.println("Processing tenant line: " + line);
               String[] parts = line.split(",");
               if (parts.length == 5) {  // Changed from 4 to 5
                   Tenant tenant = new Tenant(parts[0], parts[1], DateUtil.parseDate(parts[2]), parts[3] + "," + parts[4]);
                   tenantMap.put(tenant.getId(), tenant);
                   System.out.println("Added tenant: " + tenant.getId());
               } else {
                   System.out.println("Invalid tenant data format: " + line);
               }
           }
       } catch (IOException e) {
           System.err.println("Error reading tenants file: " + e.getMessage());
           e.printStackTrace();
       }
       System.out.println("Loaded " + tenantMap.size() + " tenants.");
       return new ArrayList<>(tenantMap.values());
   }

    public List<Host> loadHosts() {
        hostMap.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(HOSTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Host host = new Host(parts[0], parts[1], DateUtil.parseDate(parts[2]), parts[3]);
                    hostMap.put(host.getId(), host);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading hosts file: " + e.getMessage());
        }
        return new ArrayList<>(hostMap.values());
    }

    public List<Property> loadProperties() {
        propertyMap.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROPERTIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    Property property;
                    if (parts[0].startsWith("R")) {
                        property = new ResidentialProperty(parts[0], parts[1], Double.parseDouble(parts[2]),
                                Property.Status.valueOf(parts[3]), parts[4], Integer.parseInt(parts[5]),
                                Boolean.parseBoolean(parts[6]), Boolean.parseBoolean(parts[7]));
                    } else {
                        property = new CommercialProperty(parts[0], parts[1], Double.parseDouble(parts[2]),
                                Property.Status.valueOf(parts[3]), parts[4], parts[5],
                                Integer.parseInt(parts[6]), Double.parseDouble(parts[7]));
                    }
                    propertyMap.put(property.getId(), property);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading properties file: " + e.getMessage());
        }
        return new ArrayList<>(propertyMap.values());
    }

    public List<RentalAgreement> loadRentalAgreements() {
        rentalAgreements.clear();
        System.out.println("Loading rental agreements...");
        System.out.println("Tenants loaded: " + tenantMap.size());
        System.out.println("Properties loaded: " + propertyMap.size());

        try (BufferedReader reader = new BufferedReader(new FileReader(RENTAL_AGREEMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Processing rental agreement line: " + line);
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    Tenant tenant = tenantMap.get(parts[1]);
                    Property property = propertyMap.get(parts[2]);
                    System.out.println("Tenant found: " + (tenant != null) + ", Property found: " + (property != null));
                   
                    if (tenant != null && property != null) {
                        RentalAgreement agreement = new RentalAgreement(
                            parts[0],
                            tenant,
                            property,
                            RentalAgreement.Period.valueOf(parts[3]),
                            DateUtil.parseDate(parts[4]),
                            Double.parseDouble(parts[5]),
                            RentalAgreement.Status.valueOf(parts[6])
                        );
                        rentalAgreements.add(agreement);
                        System.out.println("Added rental agreement: " + agreement.getId());
                    } else {
                        System.out.println("Failed to create rental agreement due to missing tenant or property");
                    }
                } else {
                    System.out.println("Invalid rental agreement format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading rental agreements file: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Loaded " + rentalAgreements.size() + " rental agreements.");

        // Associate payments with rental agreements
        for (Payment payment : payments) {
            RentalAgreement agreement = rentalAgreements.stream()
                .filter(a -> a.getId().equals(payment.getRentalAgreementId()))
                .findFirst()
                .orElse(null);
            if (agreement != null) {
                agreement.addPayment(payment);
                System.out.println("Associated payment " + payment.getId() + " with agreement " + agreement.getId());
            } else {
                System.out.println("Could not find rental agreement for payment: " + payment.getId());
            }
        }

        return new ArrayList<>(rentalAgreements);
    }

    public List<Payment> loadPayments() {
        payments.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    Payment payment = new Payment(
                        parts[0],
                        Double.parseDouble(parts[1]),
                        DateUtil.parseDate(parts[2]),
                        parts[3],
                        parts[4]
                    );
                    payments.add(payment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading payments file: " + e.getMessage());
        }
        return payments;
    }

    public void savePayments(List<Payment> payments) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENTS_FILE))) {
            for (Payment payment : payments) {
                writer.write(String.format("%s,%.2f,%s,%s,%s\n",
                    payment.getId(),
                    payment.getAmount(),
                    DateUtil.formatDate(payment.getPaymentDate()),
                    payment.getPaymentMethod(),
                    payment.getRentalAgreementId()
                ));
            }
        } catch (IOException e) {
            System.err.println("Error writing payments file: " + e.getMessage());
        }
    }

    public void saveTenants(List<Tenant> tenants) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TENANTS_FILE))) {
            for (Tenant tenant : tenants) {
                writer.write(String.format("%s,%s,%s,%s\n",
                        tenant.getId(),
                        tenant.getFullName(),
                        DateUtil.formatDate(tenant.getDateOfBirth()),
                        tenant.getContactInformation()));
            }
        } catch (IOException e) {
            System.err.println("Error writing tenants file: " + e.getMessage());
        }
    }

    public void saveHosts(List<Host> hosts) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HOSTS_FILE))) {
            for (Host host : hosts) {
                writer.write(String.format("%s,%s,%s,%s\n",
                        host.getId(),
                        host.getFullName(),
                        DateUtil.formatDate(host.getDateOfBirth()),
                        host.getContactInformation()));
            }
        } catch (IOException e) {
            System.err.println("Error writing hosts file: " + e.getMessage());
        }
    }

    public void saveProperties(List<Property> properties) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PROPERTIES_FILE))) {
            for (Property property : properties) {
                if (property instanceof ResidentialProperty) {
                    ResidentialProperty rp = (ResidentialProperty) property;
                    writer.write(String.format("%s,%s,%.2f,%s,%s,%d,%b,%b\n",
                            rp.getId(), rp.getAddress(), rp.getPrice(), rp.getStatus(), rp.getOwner(),
                            rp.getNumberOfBedrooms(), rp.isHasGarden(), rp.isPetFriendly()));
                } else if (property instanceof CommercialProperty) {
                    CommercialProperty cp = (CommercialProperty) property;
                    writer.write(String.format("%s,%s,%.2f,%s,%s,%s,%d,%.2f\n",
                            cp.getId(), cp.getAddress(), cp.getPrice(), cp.getStatus(), cp.getOwner(),
                            cp.getBusinessType(), cp.getParkingSpaces(), cp.getSquareFootage()));
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing properties file: " + e.getMessage());
        }
    }

    public void saveRentalAgreements(List<RentalAgreement> agreements) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RENTAL_AGREEMENTS_FILE))) {
            for (RentalAgreement agreement : agreements) {
                writer.write(String.format("%s,%s,%s,%s,%s,%.2f,%s\n",
                        agreement.getId(),
                        agreement.getMainTenant().getId(),
                        agreement.getProperty().getId(),
                        agreement.getPeriod(),
                        DateUtil.formatDate(agreement.getContractDate()),
                        agreement.getRentingFee(),
                        agreement.getStatus()));
            }
        } catch (IOException e) {
            System.err.println("Error writing rental agreements file: " + e.getMessage());
        }
    }

    public void saveAllData() {
        saveTenants(new ArrayList<>(tenantMap.values()));
        saveHosts(new ArrayList<>(hostMap.values()));
        saveProperties(new ArrayList<>(propertyMap.values()));
        saveRentalAgreements(rentalAgreements);
        savePayments(payments);
    }
}