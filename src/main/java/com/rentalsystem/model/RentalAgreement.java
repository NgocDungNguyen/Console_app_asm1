 
package com.rentalsystem.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RentalAgreement {
    public enum Status {
        NEW, ACTIVE, COMPLETED
    }

    public enum Period {
        DAILY, WEEKLY, FORTNIGHTLY, MONTHLY
    }

    private String id;
    private Tenant mainTenant;
    private List<Tenant> subTenants;
    private Property property;
    private Period period;
    private Date contractDate;
    private double rentingFee;
    private Status status;

    public RentalAgreement(String id, Tenant mainTenant, Property property, Period period,
                           Date contractDate, double rentingFee, Status status) {
        this.id = id;
        this.mainTenant = mainTenant;
        this.property = property;
        this.period = period;
        this.contractDate = contractDate;
        this.rentingFee = rentingFee;
        this.status = status;
        this.subTenants = new ArrayList<>();
    }

    public void addSubTenant(Tenant subTenant) {
        subTenants.add(subTenant);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Tenant getMainTenant() {
        return mainTenant;
    }

    public void setMainTenant(Tenant mainTenant) {
        this.mainTenant = mainTenant;
    }

    public List<Tenant> getSubTenants() {
        return subTenants;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Date getContractDate() {
        return contractDate;
    }

    public void setContractDate(Date contractDate) {
        this.contractDate = contractDate;
    }

    public double getRentingFee() {
        return rentingFee;
    }

    public void setRentingFee(double rentingFee) {
        this.rentingFee = rentingFee;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RentalAgreement{" +
                "id='" + id + '\'' +
                ", mainTenant=" + mainTenant.getFullName() +
                ", subTenants=" + subTenants.size() +
                ", property=" + property.getId() +
                ", period=" + period +
                ", contractDate=" + contractDate +
                ", rentingFee=" + rentingFee +
                ", status=" + status +
                '}';
    }
}