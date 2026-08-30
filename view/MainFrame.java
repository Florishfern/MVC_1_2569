package view;

import controller.ElectionController;
import model.ElectionModel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private ElectionModel model;
    private ElectionController controller;

    private JTabbedPane tabbedPane;
    private VotingView votingView;
    private OfficerDashboardView officerView;

    public MainFrame(ElectionModel model, ElectionController controller) {
        this.model = model;
        this.controller = controller;

        setTitle("ระบบเลือกตั้งประธานชมรมโปร่งใสจริง ๆ นะ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        tabbedPane = new JTabbedPane();
        
        votingView = new VotingView(model, controller);
        officerView = new OfficerDashboardView(model, controller);

        tabbedPane.addTab("โหมดผู้ลงคะแนน (Voter)", votingView);
        tabbedPane.addTab("โหมดเจ้าหน้าที่ (Officer)", officerView);

        add(tabbedPane, BorderLayout.CENTER);
    }

    public void updateViews() {
        votingView.updateView();
        officerView.updateView();
    }
}
