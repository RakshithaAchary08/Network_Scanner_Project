package com.networkscanner;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NetworkScanner {

    public static ArrayList<DeviceInfo> scanNetwork() {
        return scanNetwork(null);
    }

    /** Returns the local machine's MAC address using NetworkInterface (bypasses ARP). */
    private static String getLocalMacAddress() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            NetworkInterface ni = NetworkInterface.getByInetAddress(localHost);
            if (ni == null) return null;
            byte[] mac = ni.getHardwareAddress();
            if (mac == null) return null;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X", mac[i]));
                if (i < mac.length - 1) sb.append(":");
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /** Returns the local machine's IP address. */
    private static String getLocalIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            return null;
        }
    }

    public static ArrayList<DeviceInfo> scanNetwork(ProgressListener listener) {
        String subnet = detectLocalSubnet();
        if (subnet == null) {
            subnet = "192.168.1.";
            System.out.println("Could not detect local subnet, using default " + subnet);
        } else {
            System.out.println("Detected local subnet: " + subnet + "0/24");
        }

        String defaultGateway = detectDefaultGateway(subnet);
        System.out.println("Using default gateway: " + defaultGateway);

        // Resolve local machine identity once so worker threads can use it
        final String localIp  = getLocalIpAddress();
        final String localMac = getLocalMacAddress();

        ArrayList<DeviceInfo> devices = new ArrayList<>();
        List<DeviceInfo> syncDevices = Collections.synchronizedList(devices);
        int timeoutMs = 300;

        // Pre-load ARP cache before scanning (single process, not one per host)
        ArpScanner.refreshArpCache();

        System.out.println("Scanning " + subnet + "1-254 with " + timeoutMs + "ms timeout per host (parallel)...");

        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        AtomicInteger tasks = new AtomicInteger(0);
        AtomicInteger completed = new AtomicInteger(0);
        int totalHosts = 254;

        for (int i = 1; i <= 254; i++) {
            final String ip = subnet + i;
            tasks.incrementAndGet();
            executor.submit(() -> {
                try {
                    long start = System.currentTimeMillis();
                    InetAddress address = InetAddress.getByName(ip);
                    boolean reachable = address.isReachable(timeoutMs);
                    long responseTime = System.currentTimeMillis() - start;

                    // If ICMP is blocked, fall back to a quick TCP port probe
                    if (!reachable) {
                        try {
                            int[] probePorts = new int[]{80, 443, 22, 3389};
                            List<Integer> probeOpen = PortScanner.checkOpenPorts(ip, probePorts, 200);
                            if (probeOpen != null && !probeOpen.isEmpty()) {
                                reachable = true;
                                // approximate response time when discovered via TCP probes
                                responseTime = 200;
                            }
                        } catch (Exception ex) {
                            // ignore probe errors
                        }
                    }

                    if (reachable) {
                        String hostname = HostnameResolver.resolve(ip);
                        DeviceInfo d = new DeviceInfo(ip, hostname, responseTime);

                        // For the local machine, use NetworkInterface directly (not ARP)
                        if (ip.equals(localIp) && localMac != null) {
                            d.setMacAddress(localMac);
                            d.setVendor(MacVendorLookup.getVendor(localMac));
                        } else {
                            try {
                                String mac = ArpScanner.getMacAddress(ip);
                                d.setMacAddress(mac);
                                if (mac != null) {
                                    d.setVendor(MacVendorLookup.getVendor(mac));
                                }
                            } catch (Exception ex) {
                                d.setVendor("Unknown");
                            }
                        }

                        // check common ports (SSH, HTTP, HTTPS, RDP)
                        int[] portsToCheck = new int[]{22, 80, 443, 3389};
                        try {
                            List<Integer> open = PortScanner.checkOpenPorts(ip, portsToCheck, 300);
                            d.setOpenPorts(open);
                        } catch (Exception ex) {
                            // ignore port scanning failures
                        }

                        // classify device type — pass vendor and mac for richer classification
                        String deviceType = DeviceClassifier.classifyDevice(ip, hostname, d.getOpenPorts(), defaultGateway, d.getVendor(), d.getMacAddress());
                        d.setDeviceType(deviceType);

                        System.out.println("Found: " + ip + " | " + hostname + " | " + deviceType + " | mac: " + d.getMacAddress() + " | vendor: " + d.getVendor() + " | open ports: " + d.getOpenPorts());
                        syncDevices.add(d);
                    }
                } catch (Exception e) {
                    // ignore unreachable hosts and continue scanning
                } finally {
                    tasks.decrementAndGet();
                    int done = completed.incrementAndGet();
                    if (listener != null) {
                        listener.onProgress(done, totalHosts);
                    }
                }
            });
        }

        executor.shutdown();
        try {
            // wait until all tasks complete or timeout
            if (!executor.awaitTermination(10, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        // Refresh ARP cache after all hosts have been pinged, then fill in any missing MACs
        ArpScanner.refreshArpCache();
        for (DeviceInfo d : devices) {
            if (d.getMacAddress() == null) {
                if (d.getIp().equals(localIp) && localMac != null) {
                    d.setMacAddress(localMac);
                    d.setVendor(MacVendorLookup.getVendor(localMac));
                } else {
                    String mac = ArpScanner.getMacAddress(d.getIp());
                    d.setMacAddress(mac);
                    if (mac != null) {
                        d.setVendor(MacVendorLookup.getVendor(mac));
                    }
                }
                // Re-classify now that vendor may be known
                String deviceType = DeviceClassifier.classifyDevice(
                    d.getIp(), d.getHostname(), d.getOpenPorts(), defaultGateway, d.getVendor(), d.getMacAddress());
                d.setDeviceType(deviceType);
            }
        }

        System.out.println("Scan complete: " + devices.size() + " devices found.");
        return devices;
    }

    private static String detectDefaultGateway(String subnet) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                String gateway = parseWindowsGateway();
                if (gateway != null) {
                    return gateway;
                }
            } else if (os.contains("linux") || os.contains("mac")) {
                String gateway = parseUnixGateway();
                if (gateway != null) {
                    return gateway;
                }
            }
        } catch (Exception e) {
            // ignore and fallback
        }
        return subnet + "1";
    }

    private static String parseWindowsGateway() throws Exception {
        Process process = Runtime.getRuntime().exec("route PRINT -4");
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
            String line;
            boolean inIPv4Table = false;
            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("IPv4 Route Table")) {
                    inIPv4Table = true;
                }
                if (!inIPv4Table) {
                    continue;
                }
                if (line.trim().startsWith("Network Destination")) {
                    continue;
                }
                String[] parts = line.trim().split("\\s+");
                if (parts.length >= 5 && "0.0.0.0".equals(parts[0])) {
                    return parts[3];
                }
            }
        }
        return null;
    }

    private static String parseUnixGateway() throws Exception {
        // Try `ip route` first, then fallback to `netstat -rn`
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", "ip route 2>/dev/null"});
            try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("default via ")) {
                        String[] parts = line.split("\\s+");
                        if (parts.length >= 3) {
                            return parts[2];
                        }
                    }
                }
            }
        } catch (Exception ignored) {
            // ignore and fallback
        }

        Process process = Runtime.getRuntime().exec("netstat -rn");
        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trim = line.trim();
                if (trim.startsWith("default ") || trim.startsWith("0.0.0.0 ")) {
                    String[] parts = trim.split("\\s+");
                    if (parts.length >= 2) {
                        return parts[1];
                    }
                }
            }
        }
        return null;
    }

    private static String detectLocalSubnet() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (!networkInterface.isUp() || networkInterface.isLoopback() || networkInterface.isVirtual()) {
                    continue;
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress address = interfaceAddress.getAddress();
                    if (address instanceof Inet4Address && !address.isLoopbackAddress()) {
                        String hostAddress = address.getHostAddress();
                        if (isPrivateIpv4(hostAddress)) {
                            int lastDot = hostAddress.lastIndexOf('.');
                            return hostAddress.substring(0, lastDot + 1);
                        }
                    }
                }
            }
        } catch (Exception e) {
            // ignore and return null
        }
        return null;
    }

    private static boolean isPrivateIpv4(String ip) {
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        try {
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            if (a == 10) {
                return true;
            }
            if (a == 172 && b >= 16 && b <= 31) {
                return true;
            }
            return a == 192 && b == 168;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
}
