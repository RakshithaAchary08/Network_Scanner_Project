package com.networkscanner;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScannerGUI extends JFrame {

    // ── Colors ─────────────────────────────────────────────────────────────
    private static final Color BG_DARK     = new Color(18, 18, 28);
    private static final Color BG_PANEL    = new Color(26, 26, 40);
    private static final Color BG_TABLE    = new Color(22, 22, 35);
    private static final Color BG_ROW_ALT  = new Color(28, 28, 44);
    private static final Color BG_HEADER   = new Color(35, 35, 55);
    private static final Color ACCENT      = new Color(0, 180, 255);
    private static final Color TEXT_WHITE  = new Color(220, 220, 230);
    private static final Color TEXT_DIM    = new Color(140, 140, 160);
    private static final Color BORDER      = new Color(50, 50, 75);

    private static final Color COLOR_ROUTER  = new Color(30, 60, 50);
    private static final Color COLOR_PC      = new Color(25, 45, 70);
    private static final Color COLOR_CAMERA  = new Color(65, 30, 30);
    private static final Color COLOR_MOBILE  = new Color(55, 35, 70);
    private static final Color COLOR_PRIVATE = new Color(40, 40, 50);
    private static final Color COLOR_IOT     = new Color(60, 50, 20);

    // ── Fields ─────────────────────────────────────────────────────────────
    private JButton           startButton;
    private JButton           exportButton;
    private JProgressBar      progressBar;
    private JLabel            statusLabel;
    private JLabel            deviceCountLabel;
    private DefaultTableModel tableModel;
    private JTable            table;

    public ScannerGUI() {
        super("Network Scanner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 640);
        setMinimumSize(new Dimension(900, 500));
        setLocationRelativeTo(null);

        applyDarkUIDefaults();

        getContentPane().setBackground(BG_DARK);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(buildTopBar(),    BorderLayout.NORTH);
        getContentPane().add(buildTable(),     BorderLayout.CENTER);
        getContentPane().add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── Top bar ────────────────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout(16, 0));
        bar.setBackground(BG_PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
            BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        JLabel title = new JLabel("⬡  Network Scanner");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(ACCENT);
        JLabel sub = new JLabel("LAN Device Discovery & Classification");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(TEXT_DIM);
        titlePanel.add(title);
        titlePanel.add(sub);

        // Progress bar — wrap in a labeled panel so "Ready" is always visible
        progressBar = new JProgressBar(0, 254);
        progressBar.setPreferredSize(new Dimension(220, 28));
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");
        progressBar.setValue(0);
        progressBar.setUI(new javax.swing.plaf.basic.BasicProgressBarUI() {
            @Override protected Color getSelectionBackground() { return Color.WHITE; }
            @Override protected Color getSelectionForeground() { return BG_DARK; }
        });
        progressBar.setForeground(ACCENT);
        progressBar.setBackground(new Color(40, 40, 60));
        progressBar.setFont(new Font("Segoe UI", Font.BOLD, 11));
        progressBar.setBorder(BorderFactory.createLineBorder(BORDER));

        // Buttons
        startButton  = createButton("▶  Start Scan", ACCENT,                  Color.WHITE);
        exportButton = createButton("↓  Export CSV", new Color(70, 70, 100),   TEXT_WHITE);
        exportButton.setEnabled(false);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(progressBar);
        right.add(startButton);
        right.add(exportButton);

        bar.add(titlePanel, BorderLayout.WEST);
        bar.add(right,      BorderLayout.EAST);

        startButton.addActionListener(this::onStart);
        exportButton.addActionListener(this::onExport);
        return bar;
    }

    // ── Table ──────────────────────────────────────────────────────────────
    private JPanel buildTable() {
        tableModel = new DefaultTableModel(
            new String[]{"  IP Address","  MAC Address","  Hostname","  Device Type","  Vendor","  ms","  Ports"}, 0
        ) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setBackground(BG_TABLE);
        table.setForeground(TEXT_WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(0, 180, 255, 80));
        table.setSelectionForeground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setRowSorter(new TableRowSorter<>(tableModel));

        int[] widths = {115, 148, 168, 168, 135, 50, 115};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        // Header
        JTableHeader hdr = table.getTableHeader();
        hdr.setBackground(BG_HEADER);
        hdr.setForeground(ACCENT);
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 12));
        hdr.setPreferredSize(new Dimension(0, 36));
        hdr.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) hdr.getDefaultRenderer())
            .setHorizontalAlignment(SwingConstants.LEFT);

        // Row color renderer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                if (sel) {
                    setBackground(new Color(0, 180, 255, 80));
                    setForeground(Color.WHITE);
                } else {
                    int mr = t.convertRowIndexToModel(row);
                    String type = (String) tableModel.getValueAt(mr, 3);
                    setBackground(rowColor(type, row));
                    setForeground(TEXT_WHITE);
                }
                return this;
            }
        });

        // Hover tooltip
        table.addMouseMotionListener(new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    int mr = table.convertRowIndexToModel(row);
                    table.setToolTipText(String.format(
                        "<html><b>IP:</b> %s &nbsp; <b>MAC:</b> %s &nbsp; <b>Vendor:</b> %s</html>",
                        tableModel.getValueAt(mr, 0),
                        tableModel.getValueAt(mr, 1),
                        tableModel.getValueAt(mr, 4)));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(BG_TABLE);
        scroll.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(BG_DARK);
        p.add(scroll);
        return p;
    }

    // ── Status bar ─────────────────────────────────────────────────────────
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout(8, 0));
        bar.setBackground(BG_PANEL);
        bar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
            BorderFactory.createEmptyBorder(6, 16, 6, 16)
        ));

        statusLabel = new JLabel("Ready — press Start Scan to begin");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(TEXT_DIM);

        deviceCountLabel = new JLabel("");
        deviceCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        deviceCountLabel.setForeground(ACCENT);

        // Legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        legend.setOpaque(false);
        legend(legend, COLOR_ROUTER,  "Router");
        legend(legend, COLOR_PC,      "PC");
        legend(legend, COLOR_CAMERA,  "Camera");
        legend(legend, COLOR_MOBILE,  "Mobile");
        legend(legend, COLOR_IOT,     "IoT");
        legend(legend, COLOR_PRIVATE, "Private");

        bar.add(statusLabel,      BorderLayout.WEST);
        bar.add(deviceCountLabel, BorderLayout.CENTER);
        bar.add(legend,           BorderLayout.EAST);
        return bar;
    }

    private void legend(JPanel p, Color c, String label) {
        JLabel dot = new JLabel("●  " + label);
        dot.setForeground(c.brighter().brighter());
        dot.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        p.add(dot);
    }

    // ── Row color ──────────────────────────────────────────────────────────
    private Color rowColor(String type, int row) {
        if (type == null) return row % 2 == 0 ? BG_TABLE : BG_ROW_ALT;
        String t = type.toLowerCase();
        if (t.contains("router"))                               return COLOR_ROUTER;
        if (t.contains("windows pc") || t.contains("laptop"))  return COLOR_PC;
        if (t.contains("camera"))                               return COLOR_CAMERA;
        if (t.contains("private mac"))                          return COLOR_PRIVATE;
        if (t.contains("mobile"))                               return COLOR_MOBILE;
        if (t.contains("iot"))                                  return COLOR_IOT;
        return row % 2 == 0 ? BG_TABLE : BG_ROW_ALT;
    }

    // ── Scan ───────────────────────────────────────────────────────────────
    private void onStart(ActionEvent e) {
        startButton.setEnabled(false);
        exportButton.setEnabled(false);
        tableModel.setRowCount(0);
        progressBar.setValue(0);
        progressBar.setString("0%");
        deviceCountLabel.setText("");
        statusLabel.setText("Scanning network...");
        statusLabel.setForeground(ACCENT);
        final long t0 = System.currentTimeMillis();

        new SwingWorker<ArrayList<DeviceInfo>, Void>() {
            @Override protected ArrayList<DeviceInfo> doInBackground() {
                return NetworkScanner.scanNetwork((done, total) ->
                    SwingUtilities.invokeLater(() -> {
                        progressBar.setValue(done);
                        progressBar.setString((int)(100.0 * done / total) + "%");
                        statusLabel.setText("Scanning " + done + " / " + total + " hosts...");
                    })
                );
            }
            @Override protected void done() {
                try {
                    ArrayList<DeviceInfo> devices = get();
                    for (DeviceInfo d : devices)
                        tableModel.addRow(new Object[]{
                            d.getIp(),
                            d.getMacAddress() == null ? "—" : d.getMacAddress(),
                            d.getHostname(),
                            d.getDeviceType(),
                            d.getVendor(),
                            d.getResponseTime(),
                            d.getOpenPorts().isEmpty() ? "—" : d.getOpenPorts().toString()
                        });
                    progressBar.setValue(254);
                    progressBar.setString("100%");
                    double sec = (System.currentTimeMillis() - t0) / 1000.0;
                    statusLabel.setText(String.format("Scan complete in %.1f seconds", sec));
                    statusLabel.setForeground(new Color(80, 220, 120));
                    deviceCountLabel.setText(devices.size() + " devices found");
                    exportButton.setEnabled(!devices.isEmpty());
                } catch (Exception ex) {
                    statusLabel.setText("Scan failed: " + ex.getMessage());
                    statusLabel.setForeground(new Color(220, 80, 80));
                } finally {
                    startButton.setEnabled(true);
                }
            }
        }.execute();
    }

    // ── Export ─────────────────────────────────────────────────────────────
    private void onExport(ActionEvent e) {
        // Use a light L&F just for the file chooser dialog so buttons are visible
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("CSV files", "csv"));
        chooser.setSelectedFile(new File("network_scan_" +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".csv"));
        int result = chooser.showSaveDialog(this);

        // Restore dark defaults after dialog closes
        applyDarkUIDefaults();

        if (result != JFileChooser.APPROVE_OPTION) return;
        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".csv"))
            file = new File(file.getParentFile(), file.getName() + ".csv");

        try (Writer w = new FileWriter(file)) {
            w.write("IP,MAC Address,Hostname,Device Type,Vendor,Response(ms),Open Ports\n");
            for (int r = 0; r < tableModel.getRowCount(); r++) {
                StringBuilder row = new StringBuilder();
                for (int c = 0; c < tableModel.getColumnCount(); c++) {
                    if (c > 0) row.append(",");
                    row.append(escapeCsv(String.valueOf(tableModel.getValueAt(r, c))));
                }
                w.write(row + "\n");
            }
            JOptionPane.showMessageDialog(this,
                "Exported to:\n" + file.getAbsolutePath(),
                "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Export failed: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helpers ────────────────────────────────────────────────────────────
    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(145, 34));
        Color hover = bg.brighter();
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private void applyDarkUIDefaults() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        UIManager.put("Panel.background",               BG_DARK);
        UIManager.put("Table.background",               BG_TABLE);
        UIManager.put("Table.foreground",               TEXT_WHITE);
        UIManager.put("TableHeader.background",         BG_HEADER);
        UIManager.put("TableHeader.foreground",         ACCENT);
        UIManager.put("Viewport.background",            BG_TABLE);
        UIManager.put("ScrollBar.background",           BG_PANEL);
        UIManager.put("Label.foreground",               TEXT_WHITE);
        UIManager.put("ProgressBar.background",         new Color(40, 40, 60));
        UIManager.put("ProgressBar.foreground",         ACCENT);
        UIManager.put("ProgressBar.selectionForeground",Color.WHITE);
        UIManager.put("ProgressBar.selectionBackground",BG_DARK);
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        String out = s.replace("\"", "\"\"");
        return (out.contains(",") || out.contains("\n") || out.contains("\""))
            ? "\"" + out + "\"" : out;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ScannerGUI().setVisible(true));
    }
}
