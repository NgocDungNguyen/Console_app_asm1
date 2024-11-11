package com.rentalsystem.model;

import java.util.Date;

public class Payment {
    private String id;
    private double amount;
    private Date paymentDate;
    private String paymentMethod;
    private String rentalAgreementId;

    public Payment(String id, double amount, Date paymentDate, String paymentMethod, String rentalAgreementId) {
        this.id = id;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.rentalAgreementId = rentalAgreementId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getRentalAgreementId() {
        return rentalAgreementId;
    }

    public void setRentalAgreementId(String rentalAgreementId) {
        this.rentalAgreementId = rentalAgreementId;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", rentalAgreementId='" + rentalAgreementId + '\'' +
                '}';
    }
}
