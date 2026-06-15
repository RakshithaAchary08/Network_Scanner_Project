package com.networkscanner;

import java.util.List;

public class DeviceClassifier {

    /** Returns true if the MAC address is locally administered (randomized by OS for privacy). */
    private static boolean isRandomizedMac(String mac) {
        if (mac == null || mac.length() < 2) return false;
        try {
            int firstOctet = Integer.parseInt(mac.substring(0, 2), 16);
            return (firstOctet & 0x02) != 0; // bit 1 set = locally administered
        } catch (Exception e) {
            return false;
        }
    }

    public static String classifyDevice(String ip, String hostname, List<Integer> openPorts, String defaultGateway, String vendor, String mac) {
        boolean hasPort22  = openPorts != null && openPorts.contains(22);
        boolean hasPort80  = openPorts != null && openPorts.contains(80);
        boolean hasPort443 = openPorts != null && openPorts.contains(443);
        boolean hasPort3389 = openPorts != null && openPorts.contains(3389);

        String hostLower   = (hostname != null) ? hostname.toLowerCase() : "";
        String vendorLower = (vendor   != null) ? vendor.toLowerCase()   : "";

        // 1. Hostname clearly identifies a Windows desktop — check BEFORE gateway
        if (hostLower.startsWith("desktop") || hostLower.contains("windows")) {
            return "Windows PC";
        }

        // 2. Gateway IP → Router
        if (ip.equals(defaultGateway)) {
            return "Router";
        }

        // 3. Vendor-based router detection
        if (vendorLower.contains("tp-link") || vendorLower.contains("d-link") ||
            vendorLower.contains("netgear") || vendorLower.contains("linksys") ||
            vendorLower.contains("zyxel")   || vendorLower.contains("asus") ||
            vendorLower.contains("huawei")  || vendorLower.contains("cisco")) {
            return "Router";
        }

        // 4. Hostname patterns
        if (!hostLower.isEmpty() && !hostLower.equals("unknown")) {
            if (hostLower.startsWith("laptop") || hostLower.contains("macbook")) {
                return "Laptop";
            }
            if (hostLower.startsWith("iphone") || hostLower.startsWith("ipad")) {
                return "Apple Mobile";
            }
            if (hostLower.startsWith("android")) {
                return "Android Device";
            }
        }

        // 5. Vendor-based device classification
        if (vendorLower.contains("hikvision") || vendorLower.contains("dahua")) {
            return "IP Camera";
        }
        if (vendorLower.contains("apple")) {
            return "Apple Device";
        }
        if (vendorLower.contains("samsung") || vendorLower.contains("xiaomi") ||
            vendorLower.contains("lg")       || vendorLower.contains("sony")  ||
            vendorLower.contains("vivo")     || vendorLower.contains("oppo")  ||
            vendorLower.contains("oneplus")  || vendorLower.contains("motorola")) {
            return "Mobile Device";
        }
        if (vendorLower.contains("dell") || vendorLower.contains("hp") ||
            vendorLower.contains("lenovo") || vendorLower.contains("intel") ||
            vendorLower.contains("foxconn") || vendorLower.contains("hon hai")) {
            return "Windows PC";
        }
        if (vendorLower.contains("raspberry")) {
            return "Raspberry Pi";
        }
        if (vendorLower.contains("vmware")) {
            return "Virtual Machine";
        }
        if (vendorLower.contains("amazon")) {
            return "Smart Device";
        }

        // 6. Port-based fallback
        if (hasPort3389) {
            return "Windows PC";
        }
        if (hasPort22 && hasPort80) {
            return "Linux Device";
        }
        if (hasPort22) {
            return "Server/NAS";
        }
        if (hasPort80 && !hasPort443) {
            return "IoT Device";
        }
        if (hasPort443) {
            return "Network Device";
        }

        // Randomized MAC = phone/tablet with privacy MAC enabled
        if (isRandomizedMac(mac)) {
            return "Mobile Device (Private MAC)";
        }

        return "Unknown Device";
    }

    public static String getDefaultGateway(String subnet) {
        // Gateway is typically the first IP (e.g., 192.168.1.1)
        return subnet + "1";
    }
}
