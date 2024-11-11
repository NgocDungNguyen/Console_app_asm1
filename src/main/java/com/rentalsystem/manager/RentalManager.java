package com.rentalsystem.manager;

import com.rentalsystem.model.RentalAgreement;
import com.rentalsystem.model.Payment;
import java.util.List;

public interface RentalManager {
    boolean addRentalAgreement(RentalAgreement agreement);
    boolean updateRentalAgreement(RentalAgreement agreement);
    boolean deleteRentalAgreement(String agreementId);
    RentalAgreement getRentalAgreement(String agreementId);
    List<RentalAgreement> getAllRentalAgreements();
    List<RentalAgreement> getSortedRentalAgreements(String sortBy);
    
    boolean addPayment(Payment payment);
    List<Payment> getPaymentsForRentalAgreement(String rentalAgreementId);
    List<Payment> getAllPayments();
    
    void saveToFile();
    void loadFromFile();
}