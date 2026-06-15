package com.networkscanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;

public class HostnameResolver {

    /**
     * Resolves hostname for an IP using:
     * 1. NetBIOS (nbtstat -A) — works for Windows PCs on LAN
     * 2. DNS reverse lookup (getCanonicalHostName) — works if router has DNS
     * Returns "Unknown" if neither resolves.
     */
    public static String resolve(String ip) {
        // 1. Try NetBIOS (Windows machines respond to this)
        String nbName = resolveNetBios(ip);
        if (nbName != null) return nbName;

        // 2. Try DNS reverse lookup
        try {
            InetAddress addr = InetAddress.getByName(ip);
            String dns = addr.getCanonicalHostName();
            if (dns != null && !dns.equals(ip)) return dns;
        } catch (Exception ignored) {}

        return "Unknown";
    }

    private static String resolveNetBios(String ip) {
        try {
            Process p = Runtime.getRuntime().exec("nbtstat -A " + ip);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Line format: "    DESKTOP-4KR8LCF    <20>  UNIQUE      Registered"
                    // <20> = File Server service = the actual machine name
                    if (line.contains("<20>") && !line.contains("__")) {
                        String name = line.trim().split("\\s+")[0].trim();
                        // Strip any trailing NetBIOS suffix like <20>
                        name = name.replaceAll("<[^>]+>", "").trim();
                        if (!name.isEmpty()) return name;
                    }
                }
            }
            p.waitFor();
        } catch (Exception ignored) {}
        return null;
    }
}
