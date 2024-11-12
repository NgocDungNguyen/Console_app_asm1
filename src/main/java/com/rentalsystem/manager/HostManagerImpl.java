package com.rentalsystem.manager;

import com.rentalsystem.model.Host;
import com.rentalsystem.util.FileHandler;
import java.util.*;

public class HostManagerImpl implements HostManager {
    private List<Host> hosts;
    private FileHandler fileHandler;

    public HostManagerImpl(FileHandler fileHandler, List<Host> initialHosts) {
        this.fileHandler = fileHandler;
        this.hosts = new ArrayList<>(initialHosts);
        System.out.println("HostManagerImpl initialized with " + this.hosts.size() + " hosts.");
    }

    @Override
    public boolean addHost(Host host) {
        if (getHost(host.getId()) != null) {
            System.out.println("Host with ID " + host.getId() + " already exists.");
            return false;
        }
        hosts.add(host);
        fileHandler.saveHosts(hosts);
        return true;
    }

    @Override
    public boolean updateHost(Host host) {
        Host existingHost = getHost(host.getId());
        if (existingHost == null) {
            System.out.println("Host with ID " + host.getId() + " not found.");
            return false;
        }
        int index = hosts.indexOf(existingHost);
        hosts.set(index, host);
        fileHandler.saveHosts(hosts);
        return true;
    }

    @Override
    public boolean deleteHost(String hostId) {
        Host host = getHost(hostId);
        if (host == null) {
            System.out.println("Host with ID " + hostId + " not found.");
            return false;
        }
        hosts.remove(host);
        fileHandler.saveHosts(hosts);
        return true;
    }

    @Override
    public Host getHost(String hostId) {
        return hosts.stream()
                .filter(host -> host.getId().equals(hostId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Host> getAllHosts() {
        return new ArrayList<>(hosts);
    }

    @Override
    public void saveToFile() {
        fileHandler.saveHosts(hosts);
    }

    @Override
    public void loadFromFile() {
        hosts = fileHandler.loadHosts();
    }
}