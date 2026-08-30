package view;

import controller.ElectionController;
import model.Ballot;
import model.Candidate;
import model.ElectionModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class OfficerDashboardView extends JPanel {
    private ElectionModel model;
    private ElectionController controller;

    private JLabel statusLabel;
    private JButton closeVotingButton;
    private JPanel pendingPanel;
    private JTable resultsTable;
    private DefaultTableModel resultsTableModel;
    private JLabel summaryLabel;

    public OfficerDashboardView(ElectionModel model, ElectionController controller) {
        this.model = model;
        this.controller = controller;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel = new JLabel("สถานะ: -");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        closeVotingButton = new JButton("ปิดรับคะแนน");
        closeVotingButton.addActionListener(e -> controller.handleCloseVoting());
        topPanel.add(statusLabel);
        topPanel.add(closeVotingButton);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel with SplitPane for pending groups and results
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        
        // Pending Groups panel
        pendingPanel = new JPanel();
        pendingPanel.setLayout(new BoxLayout(pendingPanel, BoxLayout.Y_AXIS));
        JScrollPane pendingScroll = new JScrollPane(pendingPanel);
        pendingScroll.setBorder(BorderFactory.createTitledBorder("กลุ่มบัตรที่รอตรวจสอบ (พบรูปแบบซ้ำ 3 ใบขึ้นไป)"));
        splitPane.setTopComponent(pendingScroll);

        // Results panel
        JPanel resultsPanel = new JPanel(new BorderLayout());
        resultsPanel.setBorder(BorderFactory.createTitledBorder("ผลคะแนน"));
        
        String[] columns = {"รหัส", "ชื่อผู้สมัคร", "คะแนนรวม"};
        resultsTableModel = new DefaultTableModel(columns, 0);
        resultsTable = new JTable(resultsTableModel);
        resultsPanel.add(new JScrollPane(resultsTable), BorderLayout.CENTER);
        
        summaryLabel = new JLabel("รอการสรุปผล...");
        summaryLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        resultsPanel.add(summaryLabel, BorderLayout.SOUTH);
        
        splitPane.setBottomComponent(resultsPanel);
        splitPane.setDividerLocation(150);

        add(splitPane, BorderLayout.CENTER);
    }

    public void updateView() {
        statusLabel.setText("สถานะ: " + model.getStatus());
        closeVotingButton.setEnabled(model.getStatus().equals(ElectionModel.STATUS_OPEN));

        // Update Pending Groups
        pendingPanel.removeAll();
        if (model.getStatus().equals(ElectionModel.STATUS_CLOSED)) {
            Map<String, List<Ballot>> pendingGroups = model.getPendingGroups();
            if (pendingGroups.isEmpty()) {
                pendingPanel.add(new JLabel("ไม่มีกลุ่มบัตรรอตรวจสอบ หรือตรวจสอบครบหมดแล้ว"));
            } else {
                for (Map.Entry<String, List<Ballot>> entry : pendingGroups.entrySet()) {
                    JPanel groupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    groupPanel.add(new JLabel("รูปแบบ: " + entry.getKey() + " (" + entry.getValue().size() + " ใบ)"));
                    
                    JButton btnVerify = new JButton("รับรอง (นับ)");
                    btnVerify.addActionListener(e -> controller.handleVerifyGroup(entry.getKey(), true));
                    
                    JButton btnReject = new JButton("ไม่นับ");
                    btnReject.addActionListener(e -> controller.handleVerifyGroup(entry.getKey(), false));
                    
                    groupPanel.add(btnVerify);
                    groupPanel.add(btnReject);
                    pendingPanel.add(groupPanel);
                }
            }
        } else if (model.getStatus().equals(ElectionModel.STATUS_OPEN)) {
            pendingPanel.add(new JLabel("ยังไม่ปิดรับคะแนน..."));
        } else {
             pendingPanel.add(new JLabel("ตรวจสอบเสร็จสิ้นทั้งหมดแล้ว"));
        }
        pendingPanel.revalidate();
        pendingPanel.repaint();

        // Update Results Table
        resultsTableModel.setRowCount(0);
        for (Candidate c : model.getCandidates()) {
            resultsTableModel.addRow(new Object[]{c.getId(), c.getName(), c.getScore()});
        }

        // Update Summary Label
        if (model.getStatus().equals(ElectionModel.STATUS_SUMMARIZED)) {
            long verifiedCount = model.getBallots().stream().filter(b -> b.getStatus().equals(Ballot.STATUS_VERIFIED)).count();
            long notCountedCount = model.getBallots().stream().filter(b -> b.getStatus().equals(Ballot.STATUS_NOT_COUNTED)).count();
            summaryLabel.setText("สรุปผลแล้ว | รับรองและนับ: " + verifiedCount + " ใบ | ไม่นับ: " + notCountedCount + " ใบ");
        } else if (model.getStatus().equals(ElectionModel.STATUS_CLOSED)) {
            long currentVerified = model.getBallots().stream().filter(b -> b.getStatus().equals(Ballot.STATUS_VERIFIED)).count();
            summaryLabel.setText("ผลคะแนนชั่วคราว | บัตรที่นับแล้ว: " + currentVerified + " ใบ");
        } else {
            summaryLabel.setText("รอปิดรับคะแนน...");
        }
    }
}
