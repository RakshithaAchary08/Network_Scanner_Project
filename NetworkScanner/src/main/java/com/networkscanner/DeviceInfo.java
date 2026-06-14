package com.networkscanner;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfo {

    private String ip;
    private String hostname;
    private long responseTime;
    private List<Integer> openPorts = new ArrayList<>();
    private String deviceType = "Unknown Device";
    private String macAddress = null;
    private String vendor = "Unknown";

    public DeviceInfo(String ip, String hostname, long responseTime) {
        this.ip = ip;
        this.hostname = hostname;
        this.responseTime = responseTime;
    }

    public String getIp() {
        return ip;
    }

    public String getHostname() {
        return hostname;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public List<Integer> getOpenPorts() {
        return openPorts;
    }

    public void setOpenPorts(List<Integer> openPorts) {
        this.openPorts = openPorts;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
}
