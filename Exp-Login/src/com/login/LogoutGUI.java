package com.login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class LogoutGUI {
    LogoutGUI(String hashSalt){
        initial(hashSalt);
    }
    LogoutGUI(){initialWithNOhash();}
    public void initialWithNOhash(){
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
    public void initial(String hashSalt) {
        JFrame mainGUI = new JFrame();
        mainGUI.setLayout(null);

        JLabel logoutStr = new JLabel("在线中...");
        logoutStr.setBounds(150, 75, 100, 25);
        mainGUI.add(logoutStr);

        JCheckBox keepLoggedInCheckBox = new JCheckBox("保持登录");
        keepLoggedInCheckBox.setBounds(115, 110, 140, 25);  // 设置位置和大小
        mainGUI.add(keepLoggedInCheckBox);

        JButton userNameChanger= new JButton("修改用户名");
        userNameChanger.setBounds(75,150,100,40);
        mainGUI.add(userNameChanger);

        JButton buttonLogout = new JButton("登出");
        buttonLogout.setBounds(175, 150, 100, 40);
        mainGUI.add(buttonLogout);

        mainGUI.setSize(360, 270);
        LoginGUI.centerWindow(mainGUI);
        mainGUI.setVisible(true);
        mainGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        userNameChanger.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            JFrame userChanegerFrame = new JFrame();
            JLabel currentUsername = new JLabel("当前用户名");
            JLabel newUsername = new JLabel("新用户名：");
            JLabel currentPassword = new JLabel("当前密码：");

            JTextField newUsernameField = new JTextField();
            JTextField currentPasswordField = new JTextField();

            JButton confirmButton = new JButton("确认修改");

            userChanegerFrame.setSize(270,180);
            LoginGUI.centerWindow(userChanegerFrame);
            userChanegerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

          }
        });
        buttonLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (keepLoggedInCheckBox.isSelected()) {
                    try (FileWriter writer = new FileWriter("Token.txt")) {
                        writer.write(TokenManager.generateToken(hashSalt));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try (FileWriter writer = new FileWriter("Token.txt")) {
                        writer.write("false");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                mainGUI.setVisible(false);  // 关闭窗口
            }
        });
        buttonLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainGUI, "已登出！");
                mainGUI.setVisible(false);
            }
        });
    }
    public static void main(String[] args){
        LogoutGUI test = new LogoutGUI("DPE6AkgzZ0wlQ/dFMKsdOi/OBoJQ7mtKroZjNVas/tI=");
    }
}
