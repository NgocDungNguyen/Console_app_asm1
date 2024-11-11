 
package com.rentalsystem.manager;

import com.rentalsystem.model.RentalAgreement;
import com.rentalsystem.util.FileHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RentalManagerImpl implements RentalManager {
    private List<RentalAgreement> rentalAgreements;
    private FileHandler fileHandler;

    public RentalManagerImpl() {
        this.rentalAgreements = new ArrayList<>();
        this.fileHandler = new FileHandler();
    }

    @Override
    public void addRentalAgreement(RentalAgreement agreement) {
        rentalAgreements.add(agreement);
    }

    @Override
    public void updateRentalAgreement(RentalAgreement agreement) {
        for (int i = 0; i < rentalAgreements.size(); i++) {
            if (rentalAgreements.get(i).getId().equals(agreement.getId())) {
                rentalAgreements.set(i, agreement);
                return;
            }
        }
    }

    @Override
    public void deleteRentalAgreement(String agreementId) {
        rentalAgreements.removeIf(agreement -> agreement.getId().equals(agreementId));
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
    public void saveToFile() {
        fileHandler.saveRentalAgreements(rentalAgreements);
    }

    @Override
    public void loadFromFile() {
        rentalAgreements = fileHandler.loadRentalAgreements();
    }
}