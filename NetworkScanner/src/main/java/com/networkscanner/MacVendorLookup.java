package com.networkscanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MAC Vendor Lookup using OUI (Organizationally Unique Identifier) database.
 * Maps MAC address prefixes (first 3 octets) to vendor names.
 */
public class MacVendorLookup {

    private static final Map<String, String> OUI_DATABASE = new HashMap<>();

    // Runtime cache for OUIs not in the static database — avoids repeat API calls
    private static final Map<String, String> API_CACHE = new ConcurrentHashMap<>();

    static {
        // Common vendor OUI prefixes (first 3 octets / 6 hex characters)
        // Format: MAC prefix (uppercase) -> Vendor name

        // Apple
        OUI_DATABASE.put("00:1A:E3", "Apple");
        OUI_DATABASE.put("00:1D:4F", "Apple");
        OUI_DATABASE.put("00:1E:52", "Apple");
        OUI_DATABASE.put("00:1F:5B", "Apple");
        OUI_DATABASE.put("00:22:41", "Apple");
        OUI_DATABASE.put("00:23:32", "Apple");
        OUI_DATABASE.put("00:24:36", "Apple");
        OUI_DATABASE.put("00:25:00", "Apple");
        OUI_DATABASE.put("00:25:86", "Apple");
        OUI_DATABASE.put("00:26:08", "Apple");
        OUI_DATABASE.put("00:26:4A", "Apple");
        OUI_DATABASE.put("00:26:B0", "Apple");
        OUI_DATABASE.put("00:3E:53", "Apple");
        OUI_DATABASE.put("00:50:F2", "Microsoft");
        OUI_DATABASE.put("08:00:07", "Apple");
        OUI_DATABASE.put("08:74:02", "Apple");
        OUI_DATABASE.put("0A:00:27", "Apple");
        OUI_DATABASE.put("28:CF:E9", "Apple");
        OUI_DATABASE.put("38:C8:5C", "Apple");
        OUI_DATABASE.put("3C:15:C2", "Apple");
        OUI_DATABASE.put("40:6C:8F", "Apple");
        OUI_DATABASE.put("44:2A:60", "Apple");
        OUI_DATABASE.put("48:D7:05", "Apple");
        OUI_DATABASE.put("4C:51:40", "Apple");
        OUI_DATABASE.put("54:72:4D", "Apple");
        OUI_DATABASE.put("5C:F3:70", "Apple");
        OUI_DATABASE.put("68:A8:6D", "Apple");
        OUI_DATABASE.put("6C:40:08", "Apple");
        OUI_DATABASE.put("70:CD:60", "Apple");
        OUI_DATABASE.put("74:E5:0B", "Apple");
        OUI_DATABASE.put("78:31:F1", "Apple");
        OUI_DATABASE.put("7C:D1:C3", "Apple");
        OUI_DATABASE.put("80:E6:50", "Apple");
        OUI_DATABASE.put("84:38:35", "Apple");
        OUI_DATABASE.put("88:63:DF", "Apple");
        OUI_DATABASE.put("8C:FA:BA", "Apple");
        OUI_DATABASE.put("90:27:E4", "Apple");
        OUI_DATABASE.put("94:77:F6", "Apple");
        OUI_DATABASE.put("98:01:A7", "Apple");
        OUI_DATABASE.put("9C:29:52", "Apple");
        OUI_DATABASE.put("A0:99:9B", "Apple");
        OUI_DATABASE.put("A4:B1:95", "Apple");
        OUI_DATABASE.put("A8:5E:60", "Apple");
        OUI_DATABASE.put("AC:BC:32", "Apple");
        OUI_DATABASE.put("B0:34:95", "Apple");
        OUI_DATABASE.put("B4:FB:E4", "Apple");
        OUI_DATABASE.put("B8:27:EB", "Apple");
        OUI_DATABASE.put("BC:85:56", "Apple");
        OUI_DATABASE.put("C0:A0:BB", "Apple");
        OUI_DATABASE.put("C4:2C:03", "Apple");
        OUI_DATABASE.put("C8:27:19", "Apple");
        OUI_DATABASE.put("CA:0B:34", "Apple");
        OUI_DATABASE.put("CC:07:AB", "Apple");
        OUI_DATABASE.put("D0:23:DB", "Apple");
        OUI_DATABASE.put("D0:7E:35", "Apple");
        OUI_DATABASE.put("D4:6E:0E", "Apple");
        OUI_DATABASE.put("D8:A2:5E", "Apple");
        OUI_DATABASE.put("DC:2B:61", "Apple");
        OUI_DATABASE.put("E0:AC:69", "Apple");
        OUI_DATABASE.put("E4:8B:F6", "Apple");
        OUI_DATABASE.put("E8:8D:28", "Apple");
        OUI_DATABASE.put("EC:35:86", "Apple");
        OUI_DATABASE.put("F0:18:98", "Apple");
        OUI_DATABASE.put("F4:5C:89", "Apple");
        OUI_DATABASE.put("F8:FF:C2", "Apple");
        OUI_DATABASE.put("FC:F5:C4", "Apple");

        // Samsung
        OUI_DATABASE.put("00:1A:8A", "Samsung");
        OUI_DATABASE.put("00:1B:78", "Samsung");
        OUI_DATABASE.put("00:1D:E0", "Samsung");
        OUI_DATABASE.put("00:1E:73", "Samsung");
        OUI_DATABASE.put("00:1F:64", "Samsung");
        OUI_DATABASE.put("00:21:4C", "Samsung");
        OUI_DATABASE.put("00:21:D0", "Samsung");
        OUI_DATABASE.put("00:22:3B", "Samsung");
        OUI_DATABASE.put("00:22:F4", "Samsung");
        OUI_DATABASE.put("00:23:39", "Samsung");
        OUI_DATABASE.put("00:24:17", "Samsung");
        OUI_DATABASE.put("00:25:C6", "Samsung");
        OUI_DATABASE.put("00:26:37", "Samsung");
        OUI_DATABASE.put("00:26:5A", "Samsung");
        OUI_DATABASE.put("00:26:D3", "Samsung");
        OUI_DATABASE.put("0C:9B:83", "Samsung");
        OUI_DATABASE.put("20:3D:B2", "Samsung");
        OUI_DATABASE.put("28:1F:52", "Samsung");
        OUI_DATABASE.put("28:92:4A", "Samsung");
        OUI_DATABASE.put("2C:62:15", "Samsung");
        OUI_DATABASE.put("34:08:04", "Samsung");
        OUI_DATABASE.put("38:A4:ED", "Samsung");
        OUI_DATABASE.put("3C:36:D1", "Samsung");
        OUI_DATABASE.put("40:16:3E", "Samsung");
        OUI_DATABASE.put("44:F4:F1", "Samsung");
        OUI_DATABASE.put("4C:4E:35", "Samsung");
        OUI_DATABASE.put("50:DC:E7", "Samsung");
        OUI_DATABASE.put("54:27:1E", "Samsung");
        OUI_DATABASE.put("58:77:57", "Samsung");
        OUI_DATABASE.put("5C:1D:D9", "Samsung");
        OUI_DATABASE.put("60:F8:1D", "Samsung");
        OUI_DATABASE.put("64:76:BA", "Samsung");
        OUI_DATABASE.put("68:F3:59", "Samsung");
        OUI_DATABASE.put("6C:5A:B0", "Samsung");
        OUI_DATABASE.put("70:DE:F8", "Samsung");
        OUI_DATABASE.put("74:50:3B", "Samsung");
        OUI_DATABASE.put("78:BD:BC", "Samsung");
        OUI_DATABASE.put("7C:1C:05", "Samsung");
        OUI_DATABASE.put("80:C1:6E", "Samsung");
        OUI_DATABASE.put("84:7A:88", "Samsung");
        OUI_DATABASE.put("88:32:9B", "Samsung");
        OUI_DATABASE.put("8C:CD:E8", "Samsung");
        OUI_DATABASE.put("90:B6:35", "Samsung");
        OUI_DATABASE.put("94:65:2D", "Samsung");
        OUI_DATABASE.put("98:40:8F", "Samsung");
        OUI_DATABASE.put("9C:AA:1B", "Samsung");
        OUI_DATABASE.put("A0:15:4D", "Samsung");
        OUI_DATABASE.put("A4:10:26", "Samsung");
        OUI_DATABASE.put("A8:D3:F7", "Samsung");
        OUI_DATABASE.put("AC:5A:14", "Samsung");
        OUI_DATABASE.put("B0:48:7A", "Samsung");
        OUI_DATABASE.put("B4:1C:03", "Samsung");
        OUI_DATABASE.put("B8:37:AB", "Samsung");
        OUI_DATABASE.put("BC:20:A4", "Samsung");
        OUI_DATABASE.put("C0:E4:34", "Samsung");
        OUI_DATABASE.put("C4:43:8F", "Samsung");
        OUI_DATABASE.put("C8:F7:33", "Samsung");
        OUI_DATABASE.put("CC:25:A2", "Samsung");
        OUI_DATABASE.put("D0:13:FD", "Samsung");
        OUI_DATABASE.put("D4:94:CB", "Samsung");
        OUI_DATABASE.put("D8:5D:4C", "Samsung");
        OUI_DATABASE.put("DC:B3:6F", "Samsung");
        OUI_DATABASE.put("E0:D9:E3", "Samsung");
        OUI_DATABASE.put("E4:BA:6A", "Samsung");
        OUI_DATABASE.put("E8:4E:06", "Samsung");
        OUI_DATABASE.put("EC:18:4F", "Samsung");
        OUI_DATABASE.put("F0:64:28", "Samsung");
        OUI_DATABASE.put("F4:4E:05", "Samsung");

        // Cisco
        OUI_DATABASE.put("00:00:01", "Cisco");
        OUI_DATABASE.put("00:00:0C", "Cisco");
        OUI_DATABASE.put("00:01:42", "Cisco");
        OUI_DATABASE.put("00:01:43", "Cisco");
        OUI_DATABASE.put("00:01:63", "Cisco");
        OUI_DATABASE.put("00:01:64", "Cisco");
        OUI_DATABASE.put("00:01:96", "Cisco");
        OUI_DATABASE.put("00:01:97", "Cisco");
        OUI_DATABASE.put("00:01:C7", "Cisco");
        OUI_DATABASE.put("00:01:C9", "Cisco");
        OUI_DATABASE.put("00:02:17", "Cisco");
        OUI_DATABASE.put("00:02:4A", "Cisco");
        OUI_DATABASE.put("00:02:7E", "Cisco");
        OUI_DATABASE.put("00:02:B9", "Cisco");
        OUI_DATABASE.put("00:02:CA", "Cisco");
        OUI_DATABASE.put("00:02:FD", "Cisco");
        OUI_DATABASE.put("00:03:31", "Cisco");
        OUI_DATABASE.put("00:03:6B", "Cisco");
        OUI_DATABASE.put("00:03:93", "Cisco");
        OUI_DATABASE.put("00:03:FE", "Cisco");
        OUI_DATABASE.put("00:04:27", "Cisco");
        OUI_DATABASE.put("00:04:4F", "Cisco");
        OUI_DATABASE.put("00:04:DD", "Cisco");
        OUI_DATABASE.put("00:05:33", "Cisco");
        OUI_DATABASE.put("00:05:73", "Cisco");
        OUI_DATABASE.put("00:05:D5", "Cisco");
        OUI_DATABASE.put("00:06:2A", "Cisco");
        OUI_DATABASE.put("00:06:72", "Cisco");
        OUI_DATABASE.put("00:06:C1", "Cisco");
        OUI_DATABASE.put("00:07:0F", "Cisco");
        OUI_DATABASE.put("00:07:4F", "Cisco");
        OUI_DATABASE.put("00:07:EB", "Cisco");
        OUI_DATABASE.put("00:08:1E", "Cisco");
        OUI_DATABASE.put("00:08:2F", "Cisco");
        OUI_DATABASE.put("00:08:A3", "Cisco");
        OUI_DATABASE.put("00:08:C2", "Cisco");
        OUI_DATABASE.put("00:0A:41", "Cisco");
        OUI_DATABASE.put("00:0A:95", "Cisco");
        OUI_DATABASE.put("00:0A:B8", "Cisco");
        OUI_DATABASE.put("00:0B:46", "Cisco");
        OUI_DATABASE.put("00:0B:85", "Cisco");
        OUI_DATABASE.put("00:0B:BE", "Cisco");
        OUI_DATABASE.put("00:0B:D1", "Cisco");
        OUI_DATABASE.put("00:0D:29", "Cisco");
        OUI_DATABASE.put("00:0D:BC", "Cisco");
        OUI_DATABASE.put("00:0E:D9", "Cisco");
        OUI_DATABASE.put("00:0F:34", "Cisco");
        OUI_DATABASE.put("00:0F:8E", "Cisco");
        OUI_DATABASE.put("00:10:07", "Cisco");
        OUI_DATABASE.put("00:10:11", "Cisco");
        OUI_DATABASE.put("00:10:B6", "Cisco");
        OUI_DATABASE.put("00:11:21", "Cisco");
        OUI_DATABASE.put("00:11:5F", "Cisco");
        OUI_DATABASE.put("00:12:00", "Cisco");
        OUI_DATABASE.put("00:12:43", "Cisco");
        OUI_DATABASE.put("00:12:7F", "Cisco");
        OUI_DATABASE.put("00:13:10", "Cisco");
        OUI_DATABASE.put("00:13:19", "Cisco");
        OUI_DATABASE.put("00:13:5F", "Cisco");
        OUI_DATABASE.put("00:14:38", "Cisco");
        OUI_DATABASE.put("00:14:69", "Cisco");
        OUI_DATABASE.put("00:14:A6", "Cisco");
        OUI_DATABASE.put("00:15:2B", "Cisco");
        OUI_DATABASE.put("00:15:63", "Cisco");
        OUI_DATABASE.put("00:15:B9", "Cisco");
        OUI_DATABASE.put("00:16:46", "Cisco");
        OUI_DATABASE.put("00:16:9A", "Cisco");
        OUI_DATABASE.put("00:16:C7", "Cisco");
        OUI_DATABASE.put("00:17:14", "Cisco");
        OUI_DATABASE.put("00:17:3F", "Cisco");
        OUI_DATABASE.put("00:17:5D", "Cisco");
        OUI_DATABASE.put("00:17:95", "Cisco");
        OUI_DATABASE.put("00:18:0A", "Cisco");
        OUI_DATABASE.put("00:18:73", "Cisco");
        OUI_DATABASE.put("00:19:0F", "Cisco");
        OUI_DATABASE.put("00:19:2F", "Cisco");
        OUI_DATABASE.put("00:19:E8", "Cisco");
        OUI_DATABASE.put("00:1A:2F", "Cisco");
        OUI_DATABASE.put("00:1A:70", "Cisco");
        OUI_DATABASE.put("00:1B:0C", "Cisco");
        OUI_DATABASE.put("00:1B:21", "Cisco");
        OUI_DATABASE.put("00:1B:6C", "Cisco");
        OUI_DATABASE.put("00:1C:0F", "Cisco");
        OUI_DATABASE.put("00:1C:58", "Cisco");
        OUI_DATABASE.put("00:1D:45", "Cisco");
        OUI_DATABASE.put("00:1D:7E", "Cisco");
        OUI_DATABASE.put("00:1E:13", "Cisco");
        OUI_DATABASE.put("00:1E:2A", "Cisco");
        OUI_DATABASE.put("00:1E:7B", "Cisco");
        OUI_DATABASE.put("00:1F:6C", "Cisco");
        OUI_DATABASE.put("00:20:D0", "Cisco");
        OUI_DATABASE.put("00:21:1B", "Cisco");
        OUI_DATABASE.put("00:21:2F", "Cisco");
        OUI_DATABASE.put("00:21:A0", "Cisco");
        OUI_DATABASE.put("00:22:0B", "Cisco");
        OUI_DATABASE.put("00:22:55", "Cisco");
        OUI_DATABASE.put("00:22:6B", "Cisco");
        OUI_DATABASE.put("00:23:04", "Cisco");
        OUI_DATABASE.put("00:23:BE", "Cisco");
        OUI_DATABASE.put("00:24:13", "Cisco");
        OUI_DATABASE.put("00:24:97", "Cisco");
        OUI_DATABASE.put("00:24:AB", "Cisco");
        OUI_DATABASE.put("00:24:FD", "Cisco");
        OUI_DATABASE.put("00:25:45", "Cisco");
        OUI_DATABASE.put("00:25:B3", "Cisco");
        OUI_DATABASE.put("00:25:D0", "Cisco");
        OUI_DATABASE.put("00:26:0B", "Cisco");
        OUI_DATABASE.put("00:26:73", "Cisco");
        OUI_DATABASE.put("00:26:99", "Cisco");
        OUI_DATABASE.put("00:27:13", "Cisco");
        OUI_DATABASE.put("00:27:7B", "Cisco");
        OUI_DATABASE.put("00:27:A0", "Cisco");
        OUI_DATABASE.put("00:30:71", "Cisco");
        OUI_DATABASE.put("00:30:F2", "Cisco");
        OUI_DATABASE.put("00:40:D0", "Cisco");
        OUI_DATABASE.put("00:40:D6", "Cisco");
        OUI_DATABASE.put("00:48:54", "Cisco");
        OUI_DATABASE.put("00:50:3E", "Cisco");
        OUI_DATABASE.put("00:60:B0", "Cisco");
        OUI_DATABASE.put("00:E0:F7", "Cisco");
        OUI_DATABASE.put("00:E0:F8", "Cisco");
        OUI_DATABASE.put("00:E0:F9", "Cisco");
        OUI_DATABASE.put("00:E0:FA", "Cisco");
        OUI_DATABASE.put("00:E0:FB", "Cisco");
        OUI_DATABASE.put("00:E0:FC", "Cisco");
        OUI_DATABASE.put("00:E0:FD", "Cisco");
        OUI_DATABASE.put("00:E0:FE", "Cisco");
        OUI_DATABASE.put("00:E0:FF", "Cisco");
        OUI_DATABASE.put("02:4F:02", "Cisco");
        OUI_DATABASE.put("48:0F:CF", "Cisco");
        OUI_DATABASE.put("A8:9D:21", "Cisco");
        OUI_DATABASE.put("BC:16:65", "Cisco");

        // Dell
        OUI_DATABASE.put("00:02:A5", "Dell");
        OUI_DATABASE.put("00:04:AC", "Dell");
        OUI_DATABASE.put("00:09:5B", "Dell");
        OUI_DATABASE.put("00:0A:59", "Dell");
        OUI_DATABASE.put("00:0C:29", "Dell");
        OUI_DATABASE.put("00:0F:1F", "Dell");
        OUI_DATABASE.put("00:11:43", "Dell");
        OUI_DATABASE.put("00:12:3F", "Dell");
        OUI_DATABASE.put("00:13:72", "Dell");
        OUI_DATABASE.put("00:14:22", "Dell");
        OUI_DATABASE.put("00:15:17", "Dell");
        OUI_DATABASE.put("00:16:D6", "Dell");
        OUI_DATABASE.put("00:17:31", "Dell");
        OUI_DATABASE.put("00:18:8B", "Dell");
        OUI_DATABASE.put("00:19:66", "Dell");
        OUI_DATABASE.put("00:1A:A0", "Dell");
        OUI_DATABASE.put("00:1B:21", "Dell");
        OUI_DATABASE.put("00:1C:C0", "Dell");
        OUI_DATABASE.put("00:1D:92", "Dell");
        OUI_DATABASE.put("00:1E:4F", "Dell");
        OUI_DATABASE.put("00:1F:29", "Dell");
        OUI_DATABASE.put("00:21:70", "Dell");
        OUI_DATABASE.put("00:22:19", "Dell");
        OUI_DATABASE.put("00:23:18", "Dell");
        OUI_DATABASE.put("00:24:E8", "Dell");
        OUI_DATABASE.put("00:25:B5", "Dell");
        OUI_DATABASE.put("00:26:2D", "Dell");
        OUI_DATABASE.put("00:27:0D", "Dell");
        OUI_DATABASE.put("00:1E:68", "Dell");
        OUI_DATABASE.put("14:18:77", "Dell");
        OUI_DATABASE.put("24:BE:05", "Dell");
        OUI_DATABASE.put("28:F1:0E", "Dell");
        OUI_DATABASE.put("2C:41:38", "Dell");
        OUI_DATABASE.put("3C:37:86", "Dell");
        OUI_DATABASE.put("44:A8:42", "Dell");
        OUI_DATABASE.put("54:E1:AD", "Dell");
        OUI_DATABASE.put("58:8D:09", "Dell");
        OUI_DATABASE.put("78:4B:F8", "Dell");
        OUI_DATABASE.put("7C:10:C9", "Dell");
        OUI_DATABASE.put("84:2B:2B", "Dell");
        OUI_DATABASE.put("90:B1:1C", "Dell");
        OUI_DATABASE.put("A4:1F:72", "Dell");
        OUI_DATABASE.put("BC:83:8F", "Dell");
        OUI_DATABASE.put("D4:81:D7", "Dell");

        // HP
        OUI_DATABASE.put("00:01:E6", "HP");
        OUI_DATABASE.put("00:02:B3", "HP");
        OUI_DATABASE.put("00:04:EA", "HP");
        OUI_DATABASE.put("00:05:02", "HP");
        OUI_DATABASE.put("00:06:98", "HP");
        OUI_DATABASE.put("00:07:12", "HP");
        OUI_DATABASE.put("00:08:09", "HP");
        OUI_DATABASE.put("00:09:6B", "HP");
        OUI_DATABASE.put("00:0A:8C", "HP");
        OUI_DATABASE.put("00:0B:FC", "HP");
        OUI_DATABASE.put("00:0C:85", "HP");
        OUI_DATABASE.put("00:0D:9B", "HP");
        OUI_DATABASE.put("00:0E:0C", "HP");
        OUI_DATABASE.put("00:0F:20", "HP");
        OUI_DATABASE.put("00:10:9A", "HP");
        OUI_DATABASE.put("00:11:85", "HP");
        OUI_DATABASE.put("00:12:79", "HP");
        OUI_DATABASE.put("00:13:21", "HP");
        OUI_DATABASE.put("00:14:38", "HP");
        OUI_DATABASE.put("00:15:60", "HP");
        OUI_DATABASE.put("00:16:35", "HP");
        OUI_DATABASE.put("00:17:08", "HP");
        OUI_DATABASE.put("00:18:FE", "HP");
        OUI_DATABASE.put("00:19:BB", "HP");
        OUI_DATABASE.put("00:1A:4B", "HP");
        OUI_DATABASE.put("00:1B:55", "HP");
        OUI_DATABASE.put("00:1C:C4", "HP");
        OUI_DATABASE.put("00:1D:33", "HP");
        OUI_DATABASE.put("00:1E:0B", "HP");
        OUI_DATABASE.put("00:1F:2E", "HP");
        OUI_DATABASE.put("00:21:86", "HP");
        OUI_DATABASE.put("00:22:AA", "HP");
        OUI_DATABASE.put("00:23:7D", "HP");
        OUI_DATABASE.put("00:24:81", "HP");
        OUI_DATABASE.put("00:25:86", "HP");
        OUI_DATABASE.put("00:26:55", "HP");
        OUI_DATABASE.put("00:27:12", "HP");
        OUI_DATABASE.put("00:E0:81", "HP");
        OUI_DATABASE.put("10:60:4B", "HP");
        OUI_DATABASE.put("14:CC:20", "HP");
        OUI_DATABASE.put("18:A9:05", "HP");
        OUI_DATABASE.put("1C:6F:65", "HP");
        OUI_DATABASE.put("20:FD:B8", "HP");
        OUI_DATABASE.put("24:4B:81", "HP");
        OUI_DATABASE.put("28:C6:8E", "HP");
        OUI_DATABASE.put("2C:26:17", "HP");
        OUI_DATABASE.put("34:64:A9", "HP");
        OUI_DATABASE.put("38:60:77", "HP");
        OUI_DATABASE.put("3C:2A:F4", "HP");
        OUI_DATABASE.put("40:4A:03", "HP");
        OUI_DATABASE.put("44:1E:08", "HP");
        OUI_DATABASE.put("48:4C:A9", "HP");
        OUI_DATABASE.put("4C:72:B9", "HP");
        OUI_DATABASE.put("50:E5:49", "HP");
        OUI_DATABASE.put("54:48:E6", "HP");
        OUI_DATABASE.put("58:47:00", "HP");
        OUI_DATABASE.put("5C:F9:38", "HP");
        OUI_DATABASE.put("60:EB:69", "HP");
        OUI_DATABASE.put("64:31:50", "HP");
        OUI_DATABASE.put("68:EB:AE", "HP");
        OUI_DATABASE.put("6C:40:08", "HP");
        OUI_DATABASE.put("70:1D:28", "HP");
        OUI_DATABASE.put("74:56:56", "HP");
        OUI_DATABASE.put("78:92:9D", "HP");
        OUI_DATABASE.put("7C:7A:91", "HP");
        OUI_DATABASE.put("80:30:DC", "HP");
        OUI_DATABASE.put("84:2E:14", "HP");
        OUI_DATABASE.put("88:51:FB", "HP");
        OUI_DATABASE.put("8C:70:5A", "HP");
        OUI_DATABASE.put("90:4C:E5", "HP");
        OUI_DATABASE.put("94:B8:6D", "HP");
        OUI_DATABASE.put("98:F2:B3", "HP");
        OUI_DATABASE.put("9C:5A:8E", "HP");
        OUI_DATABASE.put("A0:D3:C1", "HP");
        OUI_DATABASE.put("A4:BA:DB", "HP");
        OUI_DATABASE.put("A8:5F:78", "HP");
        OUI_DATABASE.put("AC:86:74", "HP");
        OUI_DATABASE.put("B0:25:86", "HP");
        OUI_DATABASE.put("B4:A7:F9", "HP");
        OUI_DATABASE.put("B8:AC:6F", "HP");
        OUI_DATABASE.put("BC:5F:F4", "HP");
        OUI_DATABASE.put("C0:CB:38", "HP");
        OUI_DATABASE.put("C4:B9:CD", "HP");
        OUI_DATABASE.put("C8:5B:76", "HP");
        OUI_DATABASE.put("CC:48:3B", "HP");
        OUI_DATABASE.put("D0:13:FD", "HP");
        OUI_DATABASE.put("D4:66:82", "HP");
        OUI_DATABASE.put("D8:D3:85", "HP");
        OUI_DATABASE.put("DC:2B:61", "HP");
        OUI_DATABASE.put("E0:06:E6", "HP");
        OUI_DATABASE.put("E4:43:4B", "HP");
        OUI_DATABASE.put("E8:B1:32", "HP");
        OUI_DATABASE.put("EC:55:F9", "HP");
        OUI_DATABASE.put("F0:92:1C", "HP");
        OUI_DATABASE.put("F4:6D:04", "HP");

        // Intel
        OUI_DATABASE.put("00:1F:3C", "Intel");
        OUI_DATABASE.put("00:25:86", "Intel");
        OUI_DATABASE.put("00:30:48", "Intel");
        OUI_DATABASE.put("54:04:A6", "Intel");
        OUI_DATABASE.put("5C:51:4F", "Intel");

        // Lenovo
        OUI_DATABASE.put("00:1A:2B", "Lenovo");
        OUI_DATABASE.put("00:21:3C", "Lenovo");
        OUI_DATABASE.put("00:23:54", "Lenovo");
        OUI_DATABASE.put("00:24:E8", "Lenovo");
        OUI_DATABASE.put("00:26:82", "Lenovo");
        OUI_DATABASE.put("18:60:24", "Lenovo");
        OUI_DATABASE.put("28:F0:2C", "Lenovo");
        OUI_DATABASE.put("34:DE:1A", "Lenovo");
        OUI_DATABASE.put("3C:52:82", "Lenovo");
        OUI_DATABASE.put("E0:4F:43", "Lenovo");

        // ASUS
        OUI_DATABASE.put("00:0E:8E", "ASUS");
        OUI_DATABASE.put("00:18:F3", "ASUS");
        OUI_DATABASE.put("00:1F:3A", "ASUS");
        OUI_DATABASE.put("00:24:8C", "ASUS");
        OUI_DATABASE.put("08:10:76", "ASUS");
        OUI_DATABASE.put("14:CC:20", "ASUS");
        OUI_DATABASE.put("18:FB:7B", "ASUS");
        OUI_DATABASE.put("1C:BF:F3", "ASUS");
        OUI_DATABASE.put("20:25:86", "ASUS");
        OUI_DATABASE.put("34:60:F9", "ASUS");
        OUI_DATABASE.put("44:85:00", "ASUS");
        OUI_DATABASE.put("4C:60:DE", "ASUS");
        OUI_DATABASE.put("54:A0:50", "ASUS");
        OUI_DATABASE.put("5C:63:BF", "ASUS");
        OUI_DATABASE.put("70:4F:57", "ASUS");
        OUI_DATABASE.put("88:14:39", "ASUS");
        OUI_DATABASE.put("A0:B3:CC", "ASUS");
        OUI_DATABASE.put("AC:9E:17", "ASUS");
        OUI_DATABASE.put("D8:5D:E2", "ASUS");

        // Netgear
        OUI_DATABASE.put("00:0B:85", "Netgear");
        OUI_DATABASE.put("00:12:3F", "Netgear");
        OUI_DATABASE.put("00:15:3D", "Netgear");
        OUI_DATABASE.put("00:18:E7", "Netgear");
        OUI_DATABASE.put("00:1B:2F", "Netgear");
        OUI_DATABASE.put("00:22:B0", "Netgear");
        OUI_DATABASE.put("1C:7F:64", "Netgear");
        OUI_DATABASE.put("88:F7:C7", "Netgear");
        OUI_DATABASE.put("A0:21:95", "Netgear");

        // TP-Link
        OUI_DATABASE.put("F4:EC:38", "TP-Link");
        OUI_DATABASE.put("48:3D:36", "TP-Link");
        OUI_DATABASE.put("64:09:80", "TP-Link");
        OUI_DATABASE.put("A0:E9:DE", "TP-Link");
        OUI_DATABASE.put("E0:55:3D", "TP-Link");
        OUI_DATABASE.put("1C:BD:B9", "TP-Link");
        OUI_DATABASE.put("18:D6:C7", "TP-Link");
        OUI_DATABASE.put("50:C7:BF", "TP-Link");
        OUI_DATABASE.put("60:32:B1", "TP-Link");
        OUI_DATABASE.put("6C:5A:B0", "TP-Link");
        OUI_DATABASE.put("8C:21:0A", "TP-Link");
        OUI_DATABASE.put("B0:48:7A", "TP-Link");
        OUI_DATABASE.put("C0:25:E9", "TP-Link");
        OUI_DATABASE.put("D8:0D:17", "TP-Link");
        OUI_DATABASE.put("EC:17:2F", "TP-Link");
        OUI_DATABASE.put("20:23:51", "TP-Link");
        OUI_DATABASE.put("10:BE:F5", "TP-Link");
        OUI_DATABASE.put("30:DE:4B", "TP-Link");
        OUI_DATABASE.put("40:3F:8C", "TP-Link");
        OUI_DATABASE.put("54:A7:03", "TP-Link");
        OUI_DATABASE.put("70:4F:57", "TP-Link");
        OUI_DATABASE.put("AC:84:C6", "TP-Link");
        OUI_DATABASE.put("F8:1A:67", "TP-Link");

        // D-Link
        OUI_DATABASE.put("00:05:5D", "D-Link");
        OUI_DATABASE.put("00:0D:88", "D-Link");
        OUI_DATABASE.put("00:11:95", "D-Link");
        OUI_DATABASE.put("00:13:46", "D-Link");
        OUI_DATABASE.put("00:15:E9", "D-Link");
        OUI_DATABASE.put("00:17:9A", "D-Link");
        OUI_DATABASE.put("00:19:5B", "D-Link");
        OUI_DATABASE.put("00:1B:11", "D-Link");
        OUI_DATABASE.put("00:1C:F0", "D-Link");
        OUI_DATABASE.put("00:1E:58", "D-Link");
        OUI_DATABASE.put("00:21:91", "D-Link");
        OUI_DATABASE.put("00:22:B0", "D-Link");
        OUI_DATABASE.put("00:24:01", "D-Link");
        OUI_DATABASE.put("00:26:5A", "D-Link");
        OUI_DATABASE.put("14:D6:4D", "D-Link");
        OUI_DATABASE.put("1C:7E:E5", "D-Link");
        OUI_DATABASE.put("28:10:7B", "D-Link");
        OUI_DATABASE.put("34:08:04", "D-Link");
        OUI_DATABASE.put("84:C9:B2", "D-Link");
        OUI_DATABASE.put("90:94:E4", "D-Link");
        OUI_DATABASE.put("B8:A3:86", "D-Link");
        OUI_DATABASE.put("C8:BE:19", "D-Link");
        OUI_DATABASE.put("CC:B2:55", "D-Link");
        OUI_DATABASE.put("F0:7D:68", "D-Link");
        OUI_DATABASE.put("FC:75:16", "D-Link");

        // Huawei
        OUI_DATABASE.put("00:18:82", "Huawei");
        OUI_DATABASE.put("00:1E:10", "Huawei");
        OUI_DATABASE.put("00:25:9E", "Huawei");
        OUI_DATABASE.put("04:02:1F", "Huawei");
        OUI_DATABASE.put("04:BD:70", "Huawei");
        OUI_DATABASE.put("0C:37:DC", "Huawei");
        OUI_DATABASE.put("10:47:80", "Huawei");
        OUI_DATABASE.put("10:51:72", "Huawei");
        OUI_DATABASE.put("14:EB:B6", "Huawei");
        OUI_DATABASE.put("18:C5:8A", "Huawei");
        OUI_DATABASE.put("1C:8E:5C", "Huawei");
        OUI_DATABASE.put("20:08:ED", "Huawei");
        OUI_DATABASE.put("20:F3:A3", "Huawei");
        OUI_DATABASE.put("24:09:95", "Huawei");
        OUI_DATABASE.put("24:DB:AC", "Huawei");
        OUI_DATABASE.put("28:31:52", "Huawei");
        OUI_DATABASE.put("2C:55:D3", "Huawei");
        OUI_DATABASE.put("30:D1:7E", "Huawei");
        OUI_DATABASE.put("34:6B:D3", "Huawei");
        OUI_DATABASE.put("38:37:8B", "Huawei");
        OUI_DATABASE.put("3C:F8:08", "Huawei");
        OUI_DATABASE.put("40:CB:A8", "Huawei");
        OUI_DATABASE.put("44:55:B1", "Huawei");
        OUI_DATABASE.put("48:00:31", "Huawei");
        OUI_DATABASE.put("4C:1F:CC", "Huawei");
        OUI_DATABASE.put("50:01:BB", "Huawei");
        OUI_DATABASE.put("54:51:1B", "Huawei");
        OUI_DATABASE.put("5C:7D:5E", "Huawei");
        OUI_DATABASE.put("60:DE:44", "Huawei");
        OUI_DATABASE.put("64:3E:8C", "Huawei");
        OUI_DATABASE.put("68:A0:F6", "Huawei");
        OUI_DATABASE.put("6C:8D:C1", "Huawei");
        OUI_DATABASE.put("70:72:3C", "Huawei");
        OUI_DATABASE.put("74:A0:21", "Huawei");
        OUI_DATABASE.put("78:1D:BA", "Huawei");
        OUI_DATABASE.put("7C:A2:31", "Huawei");
        OUI_DATABASE.put("80:FB:06", "Huawei");
        OUI_DATABASE.put("84:A8:E4", "Huawei");
        OUI_DATABASE.put("88:E3:AB", "Huawei");
        OUI_DATABASE.put("8C:0D:76", "Huawei");
        OUI_DATABASE.put("90:67:1C", "Huawei");
        OUI_DATABASE.put("94:77:2B", "Huawei");
        OUI_DATABASE.put("98:E7:F4", "Huawei");
        OUI_DATABASE.put("9C:28:EF", "Huawei");
        OUI_DATABASE.put("A0:08:6F", "Huawei");
        OUI_DATABASE.put("A4:99:47", "Huawei");
        OUI_DATABASE.put("A8:CA:A9", "Huawei");
        OUI_DATABASE.put("AC:E2:15", "Huawei");
        OUI_DATABASE.put("B4:15:13", "Huawei");
        OUI_DATABASE.put("B8:08:D7", "Huawei");
        OUI_DATABASE.put("BC:76:70", "Huawei");
        OUI_DATABASE.put("C0:70:09", "Huawei");
        OUI_DATABASE.put("C4:07:2F", "Huawei");
        OUI_DATABASE.put("C8:51:95", "Huawei");
        OUI_DATABASE.put("CC:A2:23", "Huawei");
        OUI_DATABASE.put("D0:3E:5C", "Huawei");
        OUI_DATABASE.put("D4:6E:5C", "Huawei");
        OUI_DATABASE.put("D8:C7:71", "Huawei");
        OUI_DATABASE.put("DC:D2:FC", "Huawei");
        OUI_DATABASE.put("E0:19:1D", "Huawei");
        OUI_DATABASE.put("E4:68:A3", "Huawei");
        OUI_DATABASE.put("E8:CD:2D", "Huawei");
        OUI_DATABASE.put("EC:CB:30", "Huawei");
        OUI_DATABASE.put("F0:5C:19", "Huawei");
        OUI_DATABASE.put("F4:4C:7F", "Huawei");
        OUI_DATABASE.put("F8:3D:FF", "Huawei");
        OUI_DATABASE.put("FC:48:EF", "Huawei");

        // Xiaomi / MiRouter
        OUI_DATABASE.put("00:9E:C8", "Xiaomi");
        OUI_DATABASE.put("04:CF:8C", "Xiaomi");
        OUI_DATABASE.put("0C:1D:AF", "Xiaomi");
        OUI_DATABASE.put("10:2A:B3", "Xiaomi");
        OUI_DATABASE.put("14:F6:5A", "Xiaomi");
        OUI_DATABASE.put("18:59:36", "Xiaomi");
        OUI_DATABASE.put("20:82:C0", "Xiaomi");
        OUI_DATABASE.put("28:6C:07", "Xiaomi");
        OUI_DATABASE.put("34:CE:00", "Xiaomi");
        OUI_DATABASE.put("38:A4:ED", "Xiaomi");
        OUI_DATABASE.put("3C:BD:3E", "Xiaomi");
        OUI_DATABASE.put("4C:49:E3", "Xiaomi");
        OUI_DATABASE.put("50:64:2B", "Xiaomi");
        OUI_DATABASE.put("58:44:98", "Xiaomi");
        OUI_DATABASE.put("64:09:80", "Xiaomi");
        OUI_DATABASE.put("64:CC:2E", "Xiaomi");
        OUI_DATABASE.put("68:DF:DD", "Xiaomi");
        OUI_DATABASE.put("74:51:BA", "Xiaomi");
        OUI_DATABASE.put("78:11:DC", "Xiaomi");
        OUI_DATABASE.put("7C:1D:D9", "Xiaomi");
        OUI_DATABASE.put("8C:BE:BE", "Xiaomi");
        OUI_DATABASE.put("9C:99:A0", "Xiaomi");
        OUI_DATABASE.put("A0:86:C6", "Xiaomi");
        OUI_DATABASE.put("AC:F7:F3", "Xiaomi");
        OUI_DATABASE.put("B0:E2:35", "Xiaomi");
        OUI_DATABASE.put("C4:0B:CB", "Xiaomi");
        OUI_DATABASE.put("D4:97:0B", "Xiaomi");
        OUI_DATABASE.put("F8:A4:5F", "Xiaomi");
        OUI_DATABASE.put("FC:64:BA", "Xiaomi");
        OUI_DATABASE.put("F4:30:8B", "Xiaomi");

        // Hikvision (IP cameras, NVR, security devices)
        OUI_DATABASE.put("80:7C:62", "Hikvision");
        OUI_DATABASE.put("24:32:AE", "Hikvision");
        OUI_DATABASE.put("08:A1:89", "Hikvision");
        OUI_DATABASE.put("DC:B7:2E", "Xiaomi");
        OUI_DATABASE.put("00:40:48", "Hikvision");
        OUI_DATABASE.put("18:68:CB", "Hikvision");
        OUI_DATABASE.put("28:57:BE", "Hikvision");
        OUI_DATABASE.put("2C:24:37", "Hikvision");
        OUI_DATABASE.put("3C:E8:24", "Hikvision");
        OUI_DATABASE.put("44:19:B6", "Hikvision");
        OUI_DATABASE.put("48:EA:63", "Hikvision");
        OUI_DATABASE.put("4C:11:BF", "Hikvision");
        OUI_DATABASE.put("54:C4:15", "Hikvision");
        OUI_DATABASE.put("70:5D:CC", "Hikvision");
        OUI_DATABASE.put("BC:AD:28", "Hikvision");
        OUI_DATABASE.put("C0:56:E3", "Hikvision");
        OUI_DATABASE.put("D0:AE:EC", "Hikvision");
        OUI_DATABASE.put("E8:D2:D1", "Hikvision");

        // Dahua (IP cameras, NVR)
        OUI_DATABASE.put("00:12:26", "Dahua");
        OUI_DATABASE.put("10:A4:5B", "Dahua");
        OUI_DATABASE.put("14:7D:C5", "Dahua");
        OUI_DATABASE.put("34:01:F9", "Dahua");
        OUI_DATABASE.put("90:02:A9", "Dahua");
        OUI_DATABASE.put("BC:32:B2", "Dahua");

        // Vivo (Android phones)
        OUI_DATABASE.put("20:3B:69", "Vivo");
        OUI_DATABASE.put("00:16:6C", "Vivo");
        OUI_DATABASE.put("08:1F:71", "Vivo");
        OUI_DATABASE.put("0C:1D:CF", "Vivo");
        OUI_DATABASE.put("14:A3:64", "Vivo");
        OUI_DATABASE.put("18:45:94", "Vivo");
        OUI_DATABASE.put("1C:BE:EC", "Vivo");
        OUI_DATABASE.put("20:19:06", "Vivo");
        OUI_DATABASE.put("2C:5B:B8", "Vivo");
        OUI_DATABASE.put("38:69:96", "Vivo");
        OUI_DATABASE.put("40:CB:C0", "Vivo");
        OUI_DATABASE.put("48:7A:DA", "Vivo");
        OUI_DATABASE.put("4C:74:03", "Vivo");
        OUI_DATABASE.put("58:2A:F7", "Vivo");
        OUI_DATABASE.put("5C:E8:EB", "Vivo");
        OUI_DATABASE.put("68:63:5B", "Vivo");
        OUI_DATABASE.put("6C:5A:B0", "Vivo");
        OUI_DATABASE.put("78:08:71", "Vivo");
        OUI_DATABASE.put("7C:8B:B5", "Vivo");
        OUI_DATABASE.put("84:A9:C4", "Vivo");
        OUI_DATABASE.put("8C:79:F0", "Vivo");
        OUI_DATABASE.put("90:96:11", "Vivo");
        OUI_DATABASE.put("94:65:9C", "Vivo");
        OUI_DATABASE.put("9C:B2:06", "Vivo");
        OUI_DATABASE.put("A0:AF:12", "Vivo");
        OUI_DATABASE.put("A4:77:33", "Vivo");
        OUI_DATABASE.put("AC:67:84", "Vivo");
        OUI_DATABASE.put("B4:5D:50", "Vivo");
        OUI_DATABASE.put("BC:8B:B2", "Vivo");
        OUI_DATABASE.put("C4:AC:59", "Vivo");
        OUI_DATABASE.put("C8:14:79", "Vivo");
        OUI_DATABASE.put("D4:61:DA", "Vivo");
        OUI_DATABASE.put("D8:55:A3", "Vivo");
        OUI_DATABASE.put("E0:2B:E9", "Vivo");
        OUI_DATABASE.put("E4:A7:C5", "Vivo");
        OUI_DATABASE.put("E8:9F:80", "Vivo");
        OUI_DATABASE.put("EC:DF:3A", "Vivo");
        OUI_DATABASE.put("F0:5C:77", "Vivo");
        OUI_DATABASE.put("F4:63:1F", "Vivo");
        OUI_DATABASE.put("F8:A9:D0", "Vivo");
        OUI_DATABASE.put("FC:48:EF", "Vivo");

        // OPPO / OnePlus (Android phones)
        OUI_DATABASE.put("00:1A:11", "OPPO");
        OUI_DATABASE.put("04:4F:4C", "OPPO");
        OUI_DATABASE.put("08:F4:AB", "OPPO");
        OUI_DATABASE.put("10:3B:59", "OPPO");
        OUI_DATABASE.put("14:E6:E4", "OPPO");
        OUI_DATABASE.put("18:26:49", "OPPO");
        OUI_DATABASE.put("1C:77:F6", "OPPO");
        OUI_DATABASE.put("20:0D:B0", "OPPO");
        OUI_DATABASE.put("2C:8A:72", "OPPO");
        OUI_DATABASE.put("34:D0:B8", "OPPO");
        OUI_DATABASE.put("38:B5:4D", "OPPO");
        OUI_DATABASE.put("3C:CB:7C", "OPPO");
        OUI_DATABASE.put("40:B0:FA", "OPPO");
        OUI_DATABASE.put("44:C3:46", "OPPO");
        OUI_DATABASE.put("48:45:20", "OPPO");
        OUI_DATABASE.put("4C:BC:A5", "OPPO");
        OUI_DATABASE.put("54:8C:A0", "OPPO");
        OUI_DATABASE.put("58:B4:2D", "OPPO");
        OUI_DATABASE.put("5C:E5:0C", "OPPO");
        OUI_DATABASE.put("60:A4:4C", "OPPO");
        OUI_DATABASE.put("64:BC:58", "OPPO");
        OUI_DATABASE.put("68:3E:34", "OPPO");
        OUI_DATABASE.put("6C:B1:58", "OPPO");
        OUI_DATABASE.put("70:66:55", "OPPO");
        OUI_DATABASE.put("74:74:46", "OPPO");
        OUI_DATABASE.put("78:58:60", "OPPO");
        OUI_DATABASE.put("7C:A9:6B", "OPPO");
        OUI_DATABASE.put("80:CB:46", "OPPO");
        OUI_DATABASE.put("84:D8:1B", "OPPO");
        OUI_DATABASE.put("88:53:2E", "OPPO");
        OUI_DATABASE.put("8C:E1:17", "OPPO");
        OUI_DATABASE.put("90:CC:DF", "OPPO");
        OUI_DATABASE.put("94:4A:0C", "OPPO");
        OUI_DATABASE.put("98:0C:A5", "OPPO");
        OUI_DATABASE.put("9C:E3:3F", "OPPO");
        OUI_DATABASE.put("A0:BC:FD", "OPPO");
        OUI_DATABASE.put("A4:AF:8E", "OPPO");
        OUI_DATABASE.put("A8:9C:ED", "OPPO");
        OUI_DATABASE.put("AC:37:43", "OPPO");
        OUI_DATABASE.put("B0:E5:ED", "OPPO");
        OUI_DATABASE.put("B4:B6:86", "OPPO");
        OUI_DATABASE.put("B8:BC:1B", "OPPO");
        OUI_DATABASE.put("BC:A5:11", "OPPO");
        OUI_DATABASE.put("C0:EE:FB", "OPPO");
        OUI_DATABASE.put("C4:32:B4", "OPPO");
        OUI_DATABASE.put("C8:33:4B", "OPPO");
        OUI_DATABASE.put("CC:90:E9", "OPPO");
        OUI_DATABASE.put("D0:14:11", "OPPO");
        OUI_DATABASE.put("D4:3A:2C", "OPPO");
        OUI_DATABASE.put("D8:31:CF", "OPPO");
        OUI_DATABASE.put("DC:6D:CD", "OPPO");
        OUI_DATABASE.put("E0:D4:E8", "OPPO");
        OUI_DATABASE.put("E4:E0:C5", "OPPO");
        OUI_DATABASE.put("E8:BB:A8", "OPPO");
        OUI_DATABASE.put("EC:6C:B5", "OPPO");
        OUI_DATABASE.put("F0:43:47", "OPPO");
        OUI_DATABASE.put("F4:29:23", "OPPO");
        OUI_DATABASE.put("F8:0C:F3", "OPPO");
        OUI_DATABASE.put("FC:87:43", "OPPO");

        // Realtek (common in budget routers/NICs)
        OUI_DATABASE.put("00:E0:4C", "Realtek");
        OUI_DATABASE.put("00:E0:4D", "Realtek");
        OUI_DATABASE.put("52:54:00", "Realtek");

        // Broadcom (embedded in many routers)
        OUI_DATABASE.put("00:10:18", "Broadcom");
        OUI_DATABASE.put("00:90:4C", "Broadcom");

        // Hon Hai / Foxconn (common in laptops and PCs)
        OUI_DATABASE.put("D8:0F:99", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:26:18", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:22:68", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:1F:E2", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:1E:65", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:1D:D9", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:1C:7B", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:1B:98", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:19:7D", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:17:F2", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:16:CE", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:15:AF", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:14:A5", "Hon Hai/Foxconn");
        OUI_DATABASE.put("00:13:CE", "Hon Hai/Foxconn");
        OUI_DATABASE.put("18:CF:5E", "Hon Hai/Foxconn");
        OUI_DATABASE.put("20:89:84", "Hon Hai/Foxconn");
        OUI_DATABASE.put("28:E3:47", "Hon Hai/Foxconn");
        OUI_DATABASE.put("30:10:B3", "Hon Hai/Foxconn");
        OUI_DATABASE.put("38:2D:E8", "Hon Hai/Foxconn");
        OUI_DATABASE.put("40:A8:F0", "Hon Hai/Foxconn");
        OUI_DATABASE.put("48:4B:AA", "Hon Hai/Foxconn");
        OUI_DATABASE.put("5C:AD:CF", "Hon Hai/Foxconn");
        OUI_DATABASE.put("60:67:20", "Hon Hai/Foxconn");
        OUI_DATABASE.put("6C:71:D9", "Hon Hai/Foxconn");
        OUI_DATABASE.put("74:E5:43", "Hon Hai/Foxconn");
        OUI_DATABASE.put("80:56:F2", "Hon Hai/Foxconn");
        OUI_DATABASE.put("8C:8D:28", "Hon Hai/Foxconn");
        OUI_DATABASE.put("94:0C:6D", "Hon Hai/Foxconn");
        OUI_DATABASE.put("A0:AF:BD", "Hon Hai/Foxconn");
        OUI_DATABASE.put("AC:BC:32", "Hon Hai/Foxconn");
        OUI_DATABASE.put("BC:92:6B", "Hon Hai/Foxconn");
        OUI_DATABASE.put("C4:17:FE", "Hon Hai/Foxconn");
        OUI_DATABASE.put("D4:BE:D9", "Hon Hai/Foxconn");
        OUI_DATABASE.put("E8:9A:8F", "Hon Hai/Foxconn");
        OUI_DATABASE.put("F0:DE:F1", "Hon Hai/Foxconn");

        // Linksys / Belkin
        OUI_DATABASE.put("00:04:5A", "Linksys");
        OUI_DATABASE.put("00:06:25", "Linksys");
        OUI_DATABASE.put("00:0C:41", "Linksys");
        OUI_DATABASE.put("00:12:17", "Linksys");
        OUI_DATABASE.put("00:14:BF", "Linksys");
        OUI_DATABASE.put("00:16:B6", "Linksys");
        OUI_DATABASE.put("00:18:39", "Linksys");
        OUI_DATABASE.put("00:1A:70", "Linksys");
        OUI_DATABASE.put("00:1C:10", "Linksys");
        OUI_DATABASE.put("00:1D:7E", "Linksys");
        OUI_DATABASE.put("00:1E:E5", "Linksys");
        OUI_DATABASE.put("00:20:A6", "Linksys");
        OUI_DATABASE.put("00:22:6B", "Linksys");
        OUI_DATABASE.put("00:25:9C", "Linksys");
        OUI_DATABASE.put("20:AA:4B", "Linksys");
        OUI_DATABASE.put("48:F8:B3", "Linksys");
        OUI_DATABASE.put("C0:C1:C0", "Linksys");

        // ZYXEL
        OUI_DATABASE.put("00:13:49", "ZyXEL");
        OUI_DATABASE.put("00:19:CB", "ZyXEL");
        OUI_DATABASE.put("00:A0:C5", "ZyXEL");
        OUI_DATABASE.put("1C:74:0D", "ZyXEL");
        OUI_DATABASE.put("28:28:5D", "ZyXEL");
        OUI_DATABASE.put("30:53:C1", "ZyXEL");
        OUI_DATABASE.put("48:91:20", "ZyXEL");
        OUI_DATABASE.put("50:67:F0", "ZyXEL");
        OUI_DATABASE.put("58:8B:F3", "ZyXEL");
        OUI_DATABASE.put("80:1F:02", "ZyXEL");
        OUI_DATABASE.put("90:A2:DA", "ZyXEL");
        OUI_DATABASE.put("A0:18:28", "ZyXEL");
        OUI_DATABASE.put("B8:EC:A3", "ZyXEL");
        OUI_DATABASE.put("D4:BF:7F", "ZyXEL");
        OUI_DATABASE.put("E8:37:7A", "ZyXEL");
        OUI_DATABASE.put("F4:3E:61", "ZyXEL");

        // ASUS routers (additional)
        OUI_DATABASE.put("00:0C:6E", "ASUS");
        OUI_DATABASE.put("04:D4:C4", "ASUS");
        OUI_DATABASE.put("08:60:6E", "ASUS");
        OUI_DATABASE.put("10:C3:7B", "ASUS");
        OUI_DATABASE.put("2C:4D:54", "ASUS");
        OUI_DATABASE.put("2C:FD:A1", "ASUS");
        OUI_DATABASE.put("30:85:A9", "ASUS");
        OUI_DATABASE.put("38:2C:4A", "ASUS");
        OUI_DATABASE.put("40:16:7E", "ASUS");
        OUI_DATABASE.put("4C:ED:FB", "ASUS");
        OUI_DATABASE.put("60:45:CB", "ASUS");
        OUI_DATABASE.put("6C:72:20", "ASUS");
        OUI_DATABASE.put("74:D0:2B", "ASUS");
        OUI_DATABASE.put("90:E6:BA", "ASUS");
        OUI_DATABASE.put("AC:22:0B", "ASUS");
        OUI_DATABASE.put("BC:EE:7B", "ASUS");
        OUI_DATABASE.put("C8:60:00", "ASUS");
        OUI_DATABASE.put("E0:CB:4E", "ASUS");
        OUI_DATABASE.put("F8:32:E4", "ASUS");

        // Google
        OUI_DATABASE.put("54:27:1E", "Google");
        OUI_DATABASE.put("68:5B:35", "Google");
        OUI_DATABASE.put("A0:56:F3", "Google");
        OUI_DATABASE.put("F4:F5:D8", "Google");

        // Amazon (Alexa/Echo/FireTV)
        OUI_DATABASE.put("14:CC:20", "Amazon");
        OUI_DATABASE.put("AC:63:BE", "Amazon");
        OUI_DATABASE.put("34:D2:70", "Amazon");
        OUI_DATABASE.put("40:B4:CD", "Amazon");
        OUI_DATABASE.put("44:65:0D", "Amazon");
        OUI_DATABASE.put("68:37:E9", "Amazon");
        OUI_DATABASE.put("74:75:48", "Amazon");
        OUI_DATABASE.put("84:D6:D0", "Amazon");
        OUI_DATABASE.put("A0:02:DC", "Amazon");
        OUI_DATABASE.put("F0:27:2D", "Amazon");
        OUI_DATABASE.put("FC:A1:83", "Amazon");

        // LG
        OUI_DATABASE.put("00:1A:8A", "LG");
        OUI_DATABASE.put("00:23:FB", "LG");
        OUI_DATABASE.put("38:01:97", "LG");
        OUI_DATABASE.put("7C:2F:80", "LG");
        OUI_DATABASE.put("A8:23:FE", "LG");
        OUI_DATABASE.put("C4:36:6C", "LG");
        OUI_DATABASE.put("CC:2D:83", "LG");
        OUI_DATABASE.put("E8:5B:5B", "LG");

        // Sony
        OUI_DATABASE.put("00:04:4B", "Sony");
        OUI_DATABASE.put("00:1F:3A", "Sony");
        OUI_DATABASE.put("08:E9:F3", "Sony");
        OUI_DATABASE.put("50:46:5D", "Sony");
        OUI_DATABASE.put("AC:9B:0A", "Sony");
        OUI_DATABASE.put("F0:BF:97", "Sony");

        // Nintendo
        OUI_DATABASE.put("00:17:AB", "Nintendo");
        OUI_DATABASE.put("00:19:1D", "Nintendo");
        OUI_DATABASE.put("A4:EF:32", "Nintendo");
        OUI_DATABASE.put("E0:F6:B5", "Nintendo");

        // Microsoft
        OUI_DATABASE.put("00:50:F2", "Microsoft");
        OUI_DATABASE.put("6C:AD:F8", "Microsoft");
        OUI_DATABASE.put("74:6E:2B", "Microsoft");
        OUI_DATABASE.put("28:18:78", "Microsoft");
        OUI_DATABASE.put("48:50:73", "Microsoft");
        OUI_DATABASE.put("7C:ED:8D", "Microsoft");
        OUI_DATABASE.put("C8:3F:26", "Microsoft");

        // Honeywell
        OUI_DATABASE.put("00:07:F0", "Honeywell");
        OUI_DATABASE.put("00:14:C6", "Honeywell");

        // 3Com
        OUI_DATABASE.put("00:00:74", "3Com");
        OUI_DATABASE.put("00:20:35", "3Com");

        // Xerox
        OUI_DATABASE.put("00:00:AA", "Xerox");
        OUI_DATABASE.put("00:80:37", "Xerox");

        // Raspberry Pi Foundation
        OUI_DATABASE.put("B8:27:EB", "Raspberry Pi");
        OUI_DATABASE.put("DC:A6:32", "Raspberry Pi");
        OUI_DATABASE.put("E4:5F:01", "Raspberry Pi");

        // VMware (virtual machines)
        OUI_DATABASE.put("00:0C:29", "VMware");
        OUI_DATABASE.put("00:50:56", "VMware");
        OUI_DATABASE.put("00:05:69", "VMware");
    }

    /**
     * Looks up the vendor name for a given MAC address.
     *
     * @param macAddress MAC address in format "XX:XX:XX:XX:XX:XX" or "XX-XX-XX-XX-XX-XX"
     * @return Vendor name if found, "Unknown" otherwise
     */
    public static String getVendor(String macAddress) {
        if (macAddress == null || macAddress.isEmpty()) {
            return "Unknown";
        }

        // Normalize MAC address to XX:XX:XX format
        String normalized = macAddress.toUpperCase()
                .replace("-", ":")
                .replace(".", "");

        // Extract first 8 characters (OUI - first 3 octets)
        String oui = null;
        if (normalized.contains(":")) {
            String[] parts = normalized.split(":");
            if (parts.length >= 3) {
                oui = parts[0] + ":" + parts[1] + ":" + parts[2];
            }
        } else if (normalized.length() >= 6) {
            // Handle MAC without separators
            oui = normalized.substring(0, 2) + ":" + normalized.substring(2, 4) + ":" + normalized.substring(4, 6);
        }

        if (oui != null && OUI_DATABASE.containsKey(oui)) {
            return OUI_DATABASE.get(oui);
        }

        // Fallback: query live API for unknown OUIs (cached to avoid repeat calls)
        if (oui != null) {
            if (API_CACHE.containsKey(oui)) {
                return API_CACHE.get(oui);
            }
            String apiVendor = lookupViaApi(macAddress);
            API_CACHE.put(oui, apiVendor);
            if (!apiVendor.equals("Unknown")) {
                return apiVendor;
            }
        }

        return "Unknown";
    }

    private static String lookupViaApi(String mac) {
        try {
            URL url = new URL("https://api.maclookup.app/v2/macs/" + mac);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            if (conn.getResponseCode() != 200) return "Unknown";
            StringBuilder sb = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) sb.append(line);
            }
            String body = sb.toString();
            // Parse "company" field from JSON without external library
            int idx = body.indexOf("\"company\":");
            if (idx == -1) return "Unknown";
            int start = body.indexOf('"', idx + 10) + 1;
            int end = body.indexOf('"', start);
            if (start > 0 && end > start) {
                String company = body.substring(start, end).trim();
                return company.isEmpty() ? "Unknown" : company;
            }
        } catch (Exception ignored) {}
        return "Unknown";
    }

    /**
     * Gets the OUI (first 3 octets) from a MAC address.
     *
     * @param macAddress MAC address in any format
     * @return OUI in XX:XX:XX format, or null if invalid
     */
    public static String getOUI(String macAddress) {
        if (macAddress == null || macAddress.isEmpty()) {
            return null;
        }

        String normalized = macAddress.toUpperCase()
                .replace("-", ":")
                .replace(".", "");

        if (normalized.contains(":")) {
            String[] parts = normalized.split(":");
            if (parts.length >= 3) {
                return parts[0] + ":" + parts[1] + ":" + parts[2];
            }
        } else if (normalized.length() >= 6) {
            return normalized.substring(0, 2) + ":" + normalized.substring(2, 4) + ":" + normalized.substring(4, 6);
        }

        return null;
    }
}
