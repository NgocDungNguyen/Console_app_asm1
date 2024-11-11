 
package com.rentalsystem.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Host extends Person {
    private List<Property> managedProperties;
    private List<String> cooperatingOwners;
    private List<RentalAgreement> rentalAgreements;

    public Host(String id, String fullName, Date dateOfBirth, String contactInformation) {
        super(id, fullName, dateOfBirth, contactInformation);
        this.managedProperties = new ArrayList<>();
        this.cooperatingOwners = new ArrayList<>();
        this.rentalAgreements = new ArrayList<>();
    }

    public void addManagedProperty(Property property) {
        managedProperties.add(property);
    }

    public void addCooperatingOwner(String owner) {
        cooperatingOwners.add(owner);
    }

    public void addRentalAgreement(RentalAgreement agreement) {
        rentalAgreements.add(agreement);
    }

    public List<Property> getManagedProperties() {
        return managedProperties;
    }

    public List<String> getCooperatingOwners() {
        return cooperatingOwners;
    }

    public List<RentalAgreement> getRentalAgreements() {
        return rentalAgreements;
    }

    @Override
    public String toString() {
        return "Host{" +
                "id='" + getId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", dateOfBirth=" + getDateOfBirth() +
                ", contactInformation='" + getContactInformation() + '\'' +
                ", managedProperties=" + managedProperties.size() +
                ", cooperatingOwners=" + cooperatingOwners.size() +
                ", rentalAgreements=" + rentalAgreements.size() +
                '}';
    }
}