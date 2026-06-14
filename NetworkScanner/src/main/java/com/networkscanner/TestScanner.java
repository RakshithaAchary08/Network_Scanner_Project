package com.networkscanner;

import java.util.ArrayList;

public class TestScanner {

    public static void main(String[] args) {
        System.out.println("Program Started");
        System.out.println("Starting Scan...");

        ArrayList<DeviceInfo> devices = NetworkScanner.scanNetwork();

        System.out.println("\nDevices Found: " + devices.size());

        for (DeviceInfo d : devices) {
            System.out.println(
                    d.getIp()
                            + " | "
                            + d.getHostname()
                            + " | "
                            + d.getDeviceType()
                            + " | "
                            + d.getResponseTime()
                            + " ms"
                            + " | Open ports: "
                            + d.getOpenPorts()
            );
        }
    }
}
