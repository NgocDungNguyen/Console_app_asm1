 
package com.rentalsystem.manager;

import com.rentalsystem.model.RentalAgreement;
import java.util.List;

public interface RentalManager {
    void addRentalAgreement(RentalAgreement agreement);
    void updateRentalAgreement(RentalAgreement agreement);
    void deleteRentalAgreement(String agreementId);
    RentalAgreement getRentalAgreement(String agreementId);
    List<RentalAgreement> getAllRentalAgreements();
    List<RentalAgreement> getSortedRentalAgreements(String sortBy);
    void saveToFile();
    void loadFromFile();
}
            