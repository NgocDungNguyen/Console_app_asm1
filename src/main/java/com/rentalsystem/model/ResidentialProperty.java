 
package com.rentalsystem.model;

public class ResidentialProperty extends Property {
    private int numberOfBedrooms;
    private boolean hasGarden;
    private boolean isPetFriendly;

    public ResidentialProperty(String id, String address, double price, Status status, String owner,
                               int numberOfBedrooms, boolean hasGarden, boolean isPetFriendly) {
        super(id, address, price, status, owner);
        this.numberOfBedrooms = numberOfBedrooms;
        this.hasGarden = hasGarden;
        this.isPetFriendly = isPetFriendly;
    }

    // Getters and setters
    public int getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public void setNumberOfBedrooms(int numberOfBedrooms) {
        this.numberOfBedrooms = numberOfBedrooms;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    public boolean isPetFriendly() {
        return isPetFriendly;
    }

    public void setPetFriendly(boolean petFriendly) {
        isPetFriendly = petFriendly;
    }

    @Override
    public String toString() {
        return "ResidentialProperty{" +
                "id='" + getId() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", price=" + getPrice() +
                ", status=" + getStatus() +
                ", owner='" + getOwner() + '\'' +
                ", hosts=" + getHosts().size() +
                ", numberOfBedrooms=" + numberOfBedrooms +
                ", hasGarden=" + hasGarden +
                ", isPetFriendly=" + isPetFriendly +
                '}';
    }
}