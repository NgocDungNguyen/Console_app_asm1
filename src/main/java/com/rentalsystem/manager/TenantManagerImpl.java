package com.rentalsystem.manager;

import com.rentalsystem.model.Tenant;
import com.rentalsystem.util.FileHandler;
import java.util.*;

public class TenantManagerImpl implements TenantManager {
    private List<Tenant> tenants;
    private FileHandler fileHandler;

    public TenantManagerImpl(FileHandler fileHandler, List<Tenant> initialTenants) {
        this.fileHandler = fileHandler;
        this.tenants = new ArrayList<>(initialTenants);
    }

    @Override
    public boolean addTenant(Tenant tenant) {
        if (getTenant(tenant.getId()) != null) {
            System.out.println("Tenant with ID " + tenant.getId() + " already exists.");
            return false;
        }
        tenants.add(tenant);
        fileHandler.saveTenants(tenants);
        return true;
    }

    @Override
    public boolean updateTenant(Tenant tenant) {
        Tenant existingTenant = getTenant(tenant.getId());
        if (existingTenant == null) {
            System.out.println("Tenant with ID " + tenant.getId() + " not found.");
            return false;
        }
        int index = tenants.indexOf(existingTenant);
        tenants.set(index, tenant);
        fileHandler.saveTenants(tenants);
        return true;
    }

    @Override
    public boolean deleteTenant(String tenantId) {
        Tenant tenant = getTenant(tenantId);
        if (tenant == null) {
            System.out.println("Tenant with ID " + tenantId + " not found.");
            return false;
        }
        tenants.remove(tenant);
        fileHandler.saveTenants(tenants);
        return true;
    }

    @Override
    public Tenant getTenant(String tenantId) {
        return tenants.stream()
                .filter(tenant -> tenant.getId().equals(tenantId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Tenant> getAllTenants() {
        return new ArrayList<>(tenants);
    }

    @Override
    public void saveToFile() {
        fileHandler.saveTenants(tenants);
    }

    @Override
    public void loadFromFile() {
        tenants = fileHandler.loadTenants();
    }
}