 
package com.rentalsystem.model;

public class CommercialProperty extends Property {
    private String businessType;
    private int parkingSpaces;
    private double squareFootage;

    public CommercialProperty(String id, String address, double price, Status status, String owner,
                              String businessType, int parkingSpaces, double squareFootage) {
        super(id, address, price, status, owner);
        this.businessType = businessType;
        this.parkingSpaces = parkingSpaces;
        this.squareFootage = squareFootage;
    }

    // Getters and setters
    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(int parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    public double getSquareFootage() {
        return squareFootage;
    }

    public void setSquareFootage(double squareFootage) {
        this.squareFootage = squareFootage;
    }

    @Override
    public String toString() {
        return "CommercialProperty{" +
                "id='" + getId() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", price=" + getPrice() +
                ", status=" + getStatus() +
                ", owner='" + getOwner() + '\'' +
                ", hosts=" + getHosts().size() +
                ", businessType='" + businessType + '\'' +
                ", parkingSpaces=" + parkingSpaces +
                ", squareFootage=" + squareFootage +
                '}';
    }
}