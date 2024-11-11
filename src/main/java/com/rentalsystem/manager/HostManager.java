package com.rentalsystem.manager;

import com.rentalsystem.model.Host;
import java.util.List;

public interface HostManager {
    boolean addHost(Host host);
    boolean updateHost(Host host);
    boolean deleteHost(String hostId);
    Host getHost(String hostId);
    List<Host> getAllHosts();
    void saveToFile();
    void loadFromFile();
}