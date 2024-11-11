 
package com.rentalsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Property {
    public enum Status {
        AVAILABLE, RENTED, UNDER_MAINTENANCE
    }

    private String id;
    private String address;
    private double price;
    private Status status;
    private String owner;
    private List<Host> hosts;

    public Property(String id, String address, double price, Status status, String owner) {
        this.id = id;
        this.address = address;
        this.price = price;
        this.status = status;
        this.owner = owner;
        this.hosts = new ArrayList<>();
    }

    public void addHost(Host host) {
        hosts.add(host);
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id='" + id + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", status=" + status +
                ", owner='" + owner + '\'' +
                ", hosts=" + hosts.size() +
                '}';
    }
}