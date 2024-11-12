package com.rentalsystem.manager;

import com.rentalsystem.model.Property;
import java.util.List;

public interface PropertyManager {
    boolean addProperty(Property property);
    boolean updateProperty(Property property);
    boolean deleteProperty(String propertyId);
    Property getProperty(String propertyId);
    List<Property> getAllProperties();
    void saveToFile();
    void loadFromFile();
}