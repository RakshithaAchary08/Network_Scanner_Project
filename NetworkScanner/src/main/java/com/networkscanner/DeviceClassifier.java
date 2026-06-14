package com.networkscanner;

import java.util.List;

public class DeviceClassifier {

    public static String classifyDevice(String ip, String hostname, List<Integer> openPorts, String defaultGateway) {
        // Check if it's the gateway
        if (ip.equals(defaultGateway)) {
            return "Router";
        }

        // Check hostname patterns for Windows
        if (hostname != null && !hostname.equalsIgnoreCase("Unknown")) {
            String hostLower = hostname.toLowerCase();
            if (hostLower.startsWith("desktop") || hostLower.contains("pc") || hostLower.contains("windows")) {
                return "Windows PC";
            }
            if (hostLower.startsWith("laptop") || hostLower.contains("macbook")) {
                return "Laptop";
            }
            if (hostLower.startsWith("iphone") || hostLower.startsWith("ipad") || hostLower.startsWith("android")) {
                return "Mobile Device";
            }
        }

        // Check open ports for service classification
        if (openPorts != null && !openPorts.isEmpty()) {
            // SSH indicates a server/NAS
            if (openPorts.contains(22)) {
                return "Server/NAS";
            }
            // HTTP/HTTPS indicates web server or network device
            if (openPorts.contains(80) || openPorts.contains(443)) {
                return "Network Device/Server";
            }
            // RDP indicates Windows machine
            if (openPorts.contains(3389)) {
                return "Windows PC";
            }
        }

        // Default to unknown
        return "Unknown Device";
    }

    public static String getDefaultGateway(String subnet) {
        // Gateway is typically the first IP (e.g., 192.168.1.1)
        return subnet + "1";
    }
}
