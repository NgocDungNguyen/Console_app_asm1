package com.rentalsystem.manager;

import com.rentalsystem.model.Tenant;
import java.util.List;

public interface TenantManager {
    boolean addTenant(Tenant tenant);
    boolean updateTenant(Tenant tenant);
    boolean deleteTenant(String tenantId);
    Tenant getTenant(String tenantId);
    List<Tenant> getAllTenants();
    void saveToFile();
    void loadFromFile();
}