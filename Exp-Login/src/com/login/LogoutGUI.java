package com.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LogoutGUI {
    LogoutGUI(){
        initial();
    }

    public void initial() {
        JFrame mainGUI = new JFrame();
        mainGUI.setLayout(null);

        JLabel logoutStr = new JLabel("在线中...");
        logoutStr.setBounds(150, 75, 100, 25);
        mainGUI.add(logoutStr);

        JButton buttonLogout = new JButton("登出");
        buttonLogout.setBounds(115, 150, 140, 50);
        mainGUI.add(buttonLogout);

        mainGUI.setSize(360, 270);
        LoginGUI.centerWindow(mainGUI);
        mainGUI.setVisible(true);
        mainGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        buttonLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainGUI, "已登出！");
                mainGUI.setVisible(false);
            }
        });
    }
}
