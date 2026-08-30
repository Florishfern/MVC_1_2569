package view;

import controller.ElectionController;
import model.Candidate;
import model.ElectionModel;
import model.Voter;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class VotingView extends JPanel {
    private ElectionModel model;
    private ElectionController controller;

    private JComboBox<Voter> voterComboBox;
    private JComboBox<Candidate> rank1ComboBox;
    private JComboBox<Candidate> rank2ComboBox;
    private JComboBox<Candidate> rank3ComboBox;
    private JLabel statusLabel;
    private JButton voteButton;

    public VotingView(ElectionModel model, ElectionController controller) {
        this.model = model;
        this.controller = controller;
        setLayout(new BorderLayout());
        initComponents();
    }

    private void initComponents() {
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("เลือกตัวตนของคุณ (ผู้มีสิทธิ์):"));
        voterComboBox = new JComboBox<>();
        formPanel.add(voterComboBox);

        formPanel.add(new JLabel("อันดับ 1 (3 คะแนน):"));
        rank1ComboBox = new JComboBox<>();
        formPanel.add(rank1ComboBox);

        formPanel.add(new JLabel("อันดับ 2 (2 คะแนน):"));
        rank2ComboBox = new JComboBox<>();
        formPanel.add(rank2ComboBox);

        formPanel.add(new JLabel("อันดับ 3 (1 คะแนน):"));
        rank3ComboBox = new JComboBox<>();
        formPanel.add(rank3ComboBox);

        formPanel.add(new JLabel("")); // empty
        voteButton = new JButton("ส่งบัตรลงคะแนน");
        voteButton.addActionListener(e -> submitVote());
        formPanel.add(voteButton);

        add(formPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("รอเปิดรับคะแนน...");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(statusLabel, BorderLayout.NORTH);
    }

    public void updateView() {
        // Update Status
        if (model.getStatus().equals(ElectionModel.STATUS_OPEN)) {
            statusLabel.setText("สถานะ: กำลังเปิดรับคะแนน | รับบัตรแล้ว " + model.getBallots().size() + " ใบ");
            voteButton.setEnabled(true);
        } else {
            statusLabel.setText("สถานะ: " + model.getStatus() + " (ปิดรับคะแนนแล้ว)");
            voteButton.setEnabled(false);
        }

        // Keep selections if possible
        Object selectedVoter = voterComboBox.getSelectedItem();
        Object selectedR1 = rank1ComboBox.getSelectedItem();
        Object selectedR2 = rank2ComboBox.getSelectedItem();
        Object selectedR3 = rank3ComboBox.getSelectedItem();

        // Update Combo boxes
        voterComboBox.setModel(new DefaultComboBoxModel<>(new Vector<>(model.getVoters())));
        
        Vector<Candidate> candidateVector = new Vector<>(model.getCandidates());
        rank1ComboBox.setModel(new DefaultComboBoxModel<>(candidateVector));
        rank2ComboBox.setModel(new DefaultComboBoxModel<>(candidateVector));
        rank3ComboBox.setModel(new DefaultComboBoxModel<>(candidateVector));

        if (selectedVoter != null) voterComboBox.setSelectedItem(selectedVoter);
        if (selectedR1 != null) rank1ComboBox.setSelectedItem(selectedR1);
        if (selectedR2 != null) rank2ComboBox.setSelectedItem(selectedR2);
        if (selectedR3 != null) rank3ComboBox.setSelectedItem(selectedR3);
    }

    private void submitVote() {
        Voter voter = (Voter) voterComboBox.getSelectedItem();
        Candidate r1 = (Candidate) rank1ComboBox.getSelectedItem();
        Candidate r2 = (Candidate) rank2ComboBox.getSelectedItem();
        Candidate r3 = (Candidate) rank3ComboBox.getSelectedItem();

        if (voter == null || r1 == null || r2 == null || r3 == null) {
            JOptionPane.showMessageDialog(this, "กรุณาเลือกข้อมูลให้ครบ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<String> ranks = Arrays.asList(r1.getId(), r2.getId(), r3.getId());
        controller.handleVote(voter.getId(), ranks);
    }
}
