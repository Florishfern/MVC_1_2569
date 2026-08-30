package controller;

import model.ElectionModel;
import view.MainFrame;

import javax.swing.*;
import java.util.List;

public class ElectionController {
    private ElectionModel model;
    private MainFrame view;

    public ElectionController(ElectionModel model) {
        this.model = model;
    }

    public void setView(MainFrame view) {
        this.view = view;
    }

    public void start() {
        if (view != null) {
            view.updateViews();
            view.setVisible(true);
        }
    }

    public void handleVote(String voterId, List<String> ranks) {
        try {
            model.submitVote(voterId, ranks);
            JOptionPane.showMessageDialog(view, "ลงคะแนนสำเร็จ", "Success", JOptionPane.INFORMATION_MESSAGE);
            view.updateViews();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleCloseVoting() {
        try {
            model.closeVoting();
            JOptionPane.showMessageDialog(view, "ปิดรับคะแนนเรียบร้อย ระบบตรวจสอบรูปแบบซ้ำเสร็จสิ้น", "Success", JOptionPane.INFORMATION_MESSAGE);
            view.updateViews();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleVerifyGroup(String pattern, boolean isVerified) {
        try {
            model.verifyGroup(pattern, isVerified);
            String action = isVerified ? "รับรอง" : "ไม่นับ";
            JOptionPane.showMessageDialog(view, "กลุ่ม " + pattern + " ถูกตัดสินเป็น: " + action, "Success", JOptionPane.INFORMATION_MESSAGE);
            
            if (model.getStatus().equals(ElectionModel.STATUS_SUMMARIZED)) {
                JOptionPane.showMessageDialog(view, "การตัดสินครบถ้วน ระบบทำการสรุปผลคะแนนแล้ว", "Election Summarized", JOptionPane.INFORMATION_MESSAGE);
            }
            
            view.updateViews();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
