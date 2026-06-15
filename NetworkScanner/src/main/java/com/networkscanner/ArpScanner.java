package com.networkscanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ARP Scanner to retrieve MAC addresses and their vendors from the local network.
 * Uses platform-specific ARP commands (arp -a on Windows, arp -n on Linux/Mac).
 */
public class ArpScanner {

    // Cached ARP table — populated once per scan to avoid spawning hundreds of processes
    private static volatile Map<String, String> cachedArpTable = null;

    /** Call this once before scanning to pre-load the ARP cache. */
    public static void refreshArpCache() {
        cachedArpTable = scanArpTable();
    }

    /**
     * Scans the local ARP table and returns a map of IP addresses to MAC addresses.
     *
     * @return Map of IP -> MAC address
     */
    public static Map<String, String> scanArpTable() {
        Map<String, String> arpTable = new HashMap<>();
        String osName = System.getProperty("os.name").toLowerCase();

        try {
            if (osName.contains("win")) {
                arpTable = parseWindowsArp();
            } else if (osName.contains("linux") || osName.contains("mac")) {
                arpTable = parseUnixArp();
            }
        } catch (Exception e) {
            System.err.println("Error scanning ARP table: " + e.getMessage());
        }

        return arpTable;
    }

    /**
     * Parses Windows ARP table output.
     *
     * @return Map of IP -> MAC address
     */
    private static Map<String, String> parseWindowsArp() throws Exception {
        Map<String, String> arpTable = new HashMap<>();
        Process process = Runtime.getRuntime().exec("arp -a");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        // Windows ARP format: "  192.168.1.100          aa-bb-cc-dd-ee-ff     dynamic"
        Pattern pattern = Pattern.compile("([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)\\s+([0-9A-Fa-f:-]+)");

        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String ip = matcher.group(1);
                String mac = matcher.group(2).replace("-", ":");
                arpTable.put(ip, mac);
            }
        }

        reader.close();
        process.waitFor();
        return arpTable;
    }

    /**
     * Parses Unix/Linux/Mac ARP table output.
     *
     * @return Map of IP -> MAC address
     */
    private static Map<String, String> parseUnixArp() throws Exception {
        Map<String, String> arpTable = new HashMap<>();
        Process process = Runtime.getRuntime().exec("arp -n");
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        // Unix ARP format: "192.168.1.100 (192.168.1.100) at aa:bb:cc:dd:ee:ff on eth0"
        Pattern pattern = Pattern.compile("([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)\\s+.*at\\s+([0-9A-Fa-f:]+)");

        while ((line = reader.readLine()) != null) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String ip = matcher.group(1);
                String mac = matcher.group(2);
                arpTable.put(ip, mac);
            }
        }

        reader.close();
        process.waitFor();
        return arpTable;
    }

    /**
     * Gets the MAC address for a specific IP from the cached ARP table.
     * Falls back to a fresh scan if the cached table is empty or missing the IP.
     *
     * @param ip IP address to look up
     * @return MAC address if found, null otherwise
     */
    public static String getMacAddress(String ip) {
        // Use cache if available
        if (cachedArpTable != null) {
            String mac = cachedArpTable.get(ip);
            if (mac != null) return mac;
            // Refresh once more in case host just became reachable
            cachedArpTable = scanArpTable();
            return cachedArpTable.get(ip);
        }
        return scanArpTable().get(ip);
    }

    /**
     * Gets the vendor for a specific IP address by performing ARP lookup and MAC vendor lookup.
     *
     * @param ip IP address to look up
     * @return Vendor name if found, "Unknown" otherwise
     */
    public static String getVendorForIp(String ip) {
        String mac = getMacAddress(ip);
        if (mac != null) {
            return MacVendorLookup.getVendor(mac);
        }
        return "Unknown";
    }
}
