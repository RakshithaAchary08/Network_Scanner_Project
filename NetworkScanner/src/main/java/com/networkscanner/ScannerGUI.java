package com.networkscanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScannerGUI extends JFrame {

    private final DefaultTableModel tableModel;
    private final JButton startButton;
    private final JButton exportButton;
    private final JProgressBar progressBar;
    private final JLabel statusLabel;

    public ScannerGUI() {
        super("Network Monitoring and Device Scanner");

        tableModel = new DefaultTableModel(new String[]{"IP", "MAC Address", "Hostname", "Device Type", "Vendor", "Response (ms)", "Open Ports"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        startButton = new JButton("Start Scan");
        exportButton = new JButton("Export CSV");
        exportButton.setEnabled(false);
        statusLabel = new JLabel("Idle");

        startButton.addActionListener(this::onStart);
        exportButton.addActionListener(this::onExport);

        progressBar = new JProgressBar(0, 254);
        progressBar.setStringPainted(true);
        progressBar.setString("0%");

        JPanel top = new JPanel(new BorderLayout(8, 8));
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.add(startButton);
        left.add(exportButton);
        top.add(left, BorderLayout.WEST);
        top.add(statusLabel, BorderLayout.CENTER);

        JPanel progress = new JPanel(new BorderLayout(8, 8));
        progress.add(new JLabel("Progress:"), BorderLayout.WEST);
        progress.add(progressBar, BorderLayout.CENTER);

        // Combine top and progress panels vertically
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(top);
        header.add(progress);

        getContentPane().setLayout(new BorderLayout(8, 8));
        getContentPane().add(header, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    private void onStart(ActionEvent e) {
        startButton.setEnabled(false);
        exportButton.setEnabled(false);
        tableModel.setRowCount(0);
        progressBar.setValue(0);
        progressBar.setString("0%");
        statusLabel.setText("Scanning...");
        final long scanStart = System.currentTimeMillis();

        SwingWorker<ArrayList<DeviceInfo>, Void> worker = new SwingWorker<>() {
            @Override
            protected ArrayList<DeviceInfo> doInBackground() {
                return NetworkScanner.scanNetwork((completed, total) -> {
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(completed);
                        int pct = (int) (100.0 * completed / total);
                        progressBar.setString(pct + "%");
                        statusLabel.setText("Scanning " + completed + "/" + total + " hosts...");
                    });
                });
            }

            @Override
            protected void done() {
                try {
                    ArrayList<DeviceInfo> devices = get();
                    for (DeviceInfo d : devices) {
                        tableModel.addRow(new Object[]{
                                d.getIp(),
                                d.getMacAddress() == null ? "Unknown" : d.getMacAddress(),
                                d.getHostname(),
                                d.getDeviceType(),
                                d.getVendor(),
                                d.getResponseTime(),
                                d.getOpenPorts().toString()
                        });
                    }
                    progressBar.setValue(254);
                    progressBar.setString("100%");
                    double elapsedSeconds = (System.currentTimeMillis() - scanStart) / 1000.0;
                    statusLabel.setText(String.format("Scan Complete - %d devices found in %.1f seconds", devices.size(), elapsedSeconds));
                    exportButton.setEnabled(devices.size() > 0);
                } catch (Exception ex) {
                    statusLabel.setText("Scan failed: " + ex.getMessage());
                } finally {
                    startButton.setEnabled(true);
                }
            }
        };

        worker.execute();
    }

    private void onExport(ActionEvent e) {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No results to export.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        String defaultName = "network_scan_" + LocalDateTime.now().format(fmt) + ".csv";
        chooser.setSelectedFile(new File(defaultName));
        int res = chooser.showSaveDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv")) {
            file = new File(file.getParentFile(), file.getName() + ".csv");
        }

        try (Writer w = new FileWriter(file)) {
            w.write("IP,MAC Address,Hostname,DeviceType,Vendor,ResponseTime,OpenPorts\n");
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                String ip = String.valueOf(tableModel.getValueAt(r, 0));
                String mac = String.valueOf(tableModel.getValueAt(r, 1));
                String host = String.valueOf(tableModel.getValueAt(r, 2));
                String device = String.valueOf(tableModel.getValueAt(r, 3));
                String vendor = String.valueOf(tableModel.getValueAt(r, 4));
                String resp = String.valueOf(tableModel.getValueAt(r, 5));
                String ports = String.valueOf(tableModel.getValueAt(r, 6));
                String portsClean = ports.replace("[", "").replace("]", "").replace(", ", ";");
                w.write(escapeCsv(ip) + "," + escapeCsv(mac) + "," + escapeCsv(host) + "," + escapeCsv(device) + "," + escapeCsv(vendor) + "," + escapeCsv(resp) + "," + escapeCsv(portsClean) + "\n");
            }
            JOptionPane.showMessageDialog(this, "Exported to " + file.getAbsolutePath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
        }
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        String out = s.replace("\"", "\"\"");
        if (out.contains(",") || out.contains("\n") || out.contains("\"")) {
            return "\"" + out + "\"";
        }
        return out;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ScannerGUI gui = new ScannerGUI();
            gui.setVisible(true);
        });
    }
}
