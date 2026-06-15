# Network Scanner Project

## Overview

A Java-based Network Monitoring and Device Discovery Tool that scans devices connected to a local network, identifies active hosts, performs TCP port scanning, detects MAC vendors, and exports scan results to CSV.

## Features

- Automatic subnet detection
- Active device discovery
- TCP port scanning
- MAC address detection
- Vendor lookup
- Device classification
- Multithreaded scanning
- Swing GUI dashboard
- Progress tracking
- CSV export

## Technologies Used

- Java
- Swing
- TCP/IP Networking
- Socket Programming
- ExecutorService
- Maven

## Project Structure

src/main/java/com/networkscanner

- NetworkScanner.java
- DeviceInfo.java
- PortScanner.java
- ArpScanner.java
- MacVendorLookup.java
- DeviceClassifier.java
- ScannerGUI.java

## How to Run

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass=com.networkscanner.App