package com.rentalsystem.manager;

import com.rentalsystem.model.Property;
import com.rentalsystem.util.FileHandler;
import java.util.*;

public class PropertyManagerImpl implements PropertyManager {
    private List<Property> properties;
    private FileHandler fileHandler;

    public PropertyManagerImpl(FileHandler fileHandler, List<Property> initialProperties) {
        this.fileHandler = fileHandler;
        this.properties = new ArrayList<>(initialProperties);
        System.out.println("PropertyManagerImpl initialized with " + this.properties.size() + " properties.");
    }

    @Override
    public boolean addProperty(Property property) {
        if (getProperty(property.getId()) != null) {
            System.out.println("Property with ID " + property.getId() + " already exists.");
            return false;
        }
        properties.add(property);
        fileHandler.saveProperties(properties);
        return true;
    }

    @Override
    public boolean updateProperty(Property property) {
        Property existingProperty = getProperty(property.getId());
        if (existingProperty == null) {
            System.out.println("Property with ID " + property.getId() + " not found.");
            return false;
        }
        int index = properties.indexOf(existingProperty);
        properties.set(index, property);
        fileHandler.saveProperties(properties);
        return true;
    }

    @Override
    public boolean deleteProperty(String propertyId) {
        Property property = getProperty(propertyId);
        if (property == null) {
            System.out.println("Property with ID " + propertyId + " not found.");
            return false;
        }
        properties.remove(property);
        fileHandler.saveProperties(properties);
        return true;
    }

    @Override
    public Property getProperty(String propertyId) {
        return properties.stream()
                .filter(property -> property.getId().equals(propertyId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Property> getAllProperties() {
        return new ArrayList<>(properties);
    }

    @Override
    public void saveToFile() {
        fileHandler.saveProperties(properties);
    }

    @Override
    public void loadFromFile() {
        properties = fileHandler.loadProperties();
    }
}