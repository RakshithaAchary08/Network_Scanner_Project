package com.networkscanner;

import java.util.HashMap;
import java.util.Map;

/**
 * MAC Vendor Lookup using OUI (Organizationally Unique Identifier) database.
 * Maps MAC address prefixes (first 3 octets) to vendor names.
 */
public class MacVendorLookup {

    private static final Map<String, String> OUI_DATABASE = new HashMap<>();

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

        // Google
        OUI_DATABASE.put("54:27:1E", "Google");
        OUI_DATABASE.put("68:5B:35", "Google");
        OUI_DATABASE.put("A0:56:F3", "Google");

        // Amazon (Alexa/Echo)
        OUI_DATABASE.put("14:CC:20", "Amazon");
        OUI_DATABASE.put("AC:63:BE", "Amazon");

        // LG
        OUI_DATABASE.put("00:1A:8A", "LG");
        OUI_DATABASE.put("00:23:FB", "LG");
        OUI_DATABASE.put("38:01:97", "LG");
        OUI_DATABASE.put("7C:2F:80", "LG");

        // Sony
        OUI_DATABASE.put("00:04:4B", "Sony");
        OUI_DATABASE.put("00:1F:3A", "Sony");
        OUI_DATABASE.put("08:E9:F3", "Sony");
        OUI_DATABASE.put("50:46:5D", "Sony");

        // Nintendo
        OUI_DATABASE.put("00:17:AB", "Nintendo");
        OUI_DATABASE.put("00:19:1D", "Nintendo");
        OUI_DATABASE.put("A4:EF:32", "Nintendo");

        // Microsoft
        OUI_DATABASE.put("00:50:F2", "Microsoft");
        OUI_DATABASE.put("6C:AD:F8", "Microsoft");
        OUI_DATABASE.put("74:6E:2B", "Microsoft");

        // Honeywell
        OUI_DATABASE.put("00:07:F0", "Honeywell");
        OUI_DATABASE.put("00:14:C6", "Honeywell");

        // 3Com
        OUI_DATABASE.put("00:00:74", "3Com");
        OUI_DATABASE.put("00:20:35", "3Com");

        // Xerox
        OUI_DATABASE.put("00:00:AA", "Xerox");
        OUI_DATABASE.put("00:80:37", "Xerox");
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
