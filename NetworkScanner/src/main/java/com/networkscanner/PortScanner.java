package com.networkscanner;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class PortScanner {

    public static List<Integer> checkOpenPorts(String ip, int[] ports, int timeoutMs) {
        List<Integer> open = new ArrayList<>();
        for (int port : ports) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(ip, port), timeoutMs);
                open.add(port);
            } catch (Exception e) {
                // port closed or filtered
            }
        }
        return open;
    }
}
