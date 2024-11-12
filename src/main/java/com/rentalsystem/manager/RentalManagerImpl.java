package com.rentalsystem.manager;

import com.rentalsystem.model.*;
import com.rentalsystem.util.FileHandler;
import java.util.*;
import java.util.stream.Collectors;

public class RentalManagerImpl implements RentalManager {
    private List<RentalAgreement> rentalAgreements;
    private List<Payment> payments;
    private FileHandler fileHandler;  // Add this line

    public RentalManagerImpl(FileHandler fileHandler) {
        this.fileHandler = fileHandler;  // Add this line
        Map<String, List<?>> loadedData = fileHandler.loadAllData();
        this.rentalAgreements = new ArrayList<>((List<RentalAgreement>) loadedData.get("rentalAgreements"));
        this.payments = new ArrayList<>((List<Payment>) loadedData.get("payments"));
        
        // If payments is still null after loading, initialize it as an empty list
        if (this.payments == null) {
            this.payments = new ArrayList<>();
        }
        
        System.out.println("RentalManagerImpl initialized with " + this.rentalAgreements.size() + " agreements and " + this.payments.size() + " payments.");
    }

    @Override
    public boolean addRentalAgreement(RentalAgreement agreement) {
        if (getRentalAgreement(agreement.getId()) != null) {
            System.out.println("Rental Agreement with ID " + agreement.getId() + " already exists.");
            return false;
        }
        rentalAgreements.add(agreement);
        fileHandler.saveRentalAgreements(rentalAgreements);
        return true;
    }

    @Override
    public boolean updateRentalAgreement(RentalAgreement agreement) {
        RentalAgreement existingAgreement = getRentalAgreement(agreement.getId());
        if (existingAgreement == null) {
            System.out.println("Rental Agreement with ID " + agreement.getId() + " not found.");
            return false;
        }
        int index = rentalAgreements.indexOf(existingAgreement);
        rentalAgreements.set(index, agreement);
        fileHandler.saveRentalAgreements(rentalAgreements);
        return true;
    }

    @Override
    public boolean deleteRentalAgreement(String agreementId) {
        RentalAgreement agreement = getRentalAgreement(agreementId);
        if (agreement == null) {
            System.out.println("Rental Agreement with ID " + agreementId + " not found.");
            return false;
        }
        rentalAgreements.remove(agreement);
        fileHandler.saveRentalAgreements(rentalAgreements);
        return true;
    }

    @Override
    public RentalAgreement getRentalAgreement(String agreementId) {
        return rentalAgreements.stream()
                .filter(agreement -> agreement.getId().equals(agreementId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<RentalAgreement> getAllRentalAgreements() {
        return new ArrayList<>(rentalAgreements);
    }

    @Override
    public List<RentalAgreement> getSortedRentalAgreements(String sortBy) {
        switch (sortBy.toLowerCase()) {
            case "id":
                return rentalAgreements.stream()
                        .sorted(Comparator.comparing(RentalAgreement::getId))
                        .collect(Collectors.toList());
            case "date":
                return rentalAgreements.stream()
                        .sorted(Comparator.comparing(RentalAgreement::getContractDate))
                        .collect(Collectors.toList());
            case "fee":
                return rentalAgreements.stream()
                        .sorted(Comparator.comparing(RentalAgreement::getRentingFee))
                        .collect(Collectors.toList());
            default:
                return getAllRentalAgreements();
        }
    }

    @Override
    public boolean addPayment(Payment payment) {
        RentalAgreement agreement = getRentalAgreement(payment.getRentalAgreementId());
        if (agreement != null) {
            agreement.addPayment(payment);
            agreement.getMainTenant().addPayment(payment);
            payments.add(payment);
            fileHandler.savePayments(payments);
            return true;
        }
        System.out.println("Rental Agreement with ID " + payment.getRentalAgreementId() + " not found.");
        return false;
    }

    @Override
    public List<Payment> getPaymentsForRentalAgreement(String rentalAgreementId) {
        return payments.stream()
                .filter(payment -> payment.getRentalAgreementId().equals(rentalAgreementId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> getAllPayments() {
        return new ArrayList<>(payments);
    }

   @Override
    public void saveToFile() {
        fileHandler.saveRentalAgreements(rentalAgreements);
        fileHandler.savePayments(payments);
    }

    @Override
    public void loadFromFile() {
        Map<String, List<?>> loadedData = fileHandler.loadAllData();
        this.rentalAgreements = (List<RentalAgreement>) loadedData.get("rentalAgreements");
        this.payments = (List<Payment>) loadedData.get("payments");
    }
}
