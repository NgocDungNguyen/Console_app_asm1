package com.rentalsystem.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tenant extends Person {
    private List<RentalAgreement> rentalAgreements;
    private List<Payment> paymentTransactions;

    public Tenant(String id, String fullName, Date dateOfBirth, String contactInformation) {
        super(id, fullName, dateOfBirth, contactInformation);
        this.rentalAgreements = new ArrayList<>();
        this.paymentTransactions = new ArrayList<>();
    }

    public void addRentalAgreement(RentalAgreement agreement) {
        rentalAgreements.add(agreement);
    }

    public List<RentalAgreement> getRentalAgreements() {
        return new ArrayList<>(rentalAgreements);
    }

    public void addPayment(Payment payment) {
        paymentTransactions.add(payment);
    }

    public List<Payment> getPaymentTransactions() {
        return new ArrayList<>(paymentTransactions);
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "id='" + getId() + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", dateOfBirth=" + getDateOfBirth() +
                ", contactInformation='" + getContactInformation() + '\'' +
                ", rentalAgreements=" + rentalAgreements.size() +
                ", paymentTransactions=" + paymentTransactions.size() +
                '}';
    }
}
