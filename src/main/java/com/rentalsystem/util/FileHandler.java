package com.rentalsystem.util;

import com.rentalsystem.model.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class FileHandler {
    private static final String TENANTS_FILE = "resources/tenants.txt";
    private static final String HOSTS_FILE = "resources/hosts.txt";
    private static final String PROPERTIES_FILE = "resources/properties.txt";
    private static final String RENTAL_AGREEMENTS_FILE = "resources/rental_agreements.txt";

    public List<Tenant> loadTenants() {
        List<Tenant> tenants = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TENANTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Tenant tenant = new Tenant(parts[0], parts[1], DateUtil.parseDate(parts[2]), parts[3]);
                tenants.add(tenant);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tenants;
    }

    public List<Host> loadHosts() {
        List<Host> hosts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(HOSTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Host host = new Host(parts[0], parts[1], DateUtil.parseDate(parts[2]), parts[3]);
                hosts.add(host);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return hosts;
    }

    public List<Property> loadProperties() {
        List<Property> properties = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PROPERTIES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
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
                properties.add(property);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public List<RentalAgreement> loadRentalAgreements() {
        List<RentalAgreement> agreements = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(RENTAL_AGREEMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Tenant dummyTenant = new Tenant("DUMMY", "Dummy Tenant", new Date(), "dummy@email.com");
                    Property dummyProperty = new Property("DUMMY", "Dummy Address", 0, Property.Status.AVAILABLE, "Dummy Owner");
                    RentalAgreement agreement = new RentalAgreement(
                        parts[0], dummyTenant, dummyProperty,
                        RentalAgreement.Period.valueOf(parts[3]),
                        DateUtil.parseDate(parts[4]),
                        Double.parseDouble(parts[5]),
                        RentalAgreement.Status.valueOf(parts[6])
                    );
                    agreements.add(agreement);
                } else {
                    System.err.println("Invalid rental agreement data: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return agreements;
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
}
            