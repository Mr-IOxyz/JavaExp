package com.login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ResetGUI {
    ResetGUI() {
        initialize();
    }

    public void initialize() {
        JFrame resetFrame = new JFrame("密码重置");
        resetFrame.setLayout(null);

        JLabel nameLabel = new JLabel("用户名:");
        nameLabel.setBounds(75, 50, 100, 25);
        resetFrame.add(nameLabel);

        JLabel answerLabel = new JLabel("密保答案:");
        answerLabel.setBounds(75, 100, 100, 25);
        resetFrame.add(answerLabel);

        JLabel newPasswordLabel = new JLabel("新密码:");
        newPasswordLabel.setBounds(75, 150, 100, 25);
        resetFrame.add(newPasswordLabel);

        JTextField userNameField = new JTextField();
        userNameField.setBounds(175, 50, 150, 25);
        resetFrame.add(userNameField);

        JTextField answerField = new JTextField();
        answerField.setBounds(175, 100, 150, 25);
        resetFrame.add(answerField);

        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setBounds(175, 150, 150, 25);
        resetFrame.add(newPasswordField);

        JButton resetButton = new JButton("重置密码");
        resetButton.setBounds(75, 200, 100, 25);
        resetFrame.add(resetButton);

        JButton cancelButton = new JButton("取消");
        cancelButton.setBounds(225, 200, 100, 25);
        resetFrame.add(cancelButton);

        resetFrame.setSize(400, 300);
        LoginGUI.centerWindow(resetFrame);
        resetFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resetFrame.setVisible(true);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userNameField.getText();
                String answer = answerField.getText();
                String newPassword = new String(newPasswordField.getPassword());

                if (resetPassword(username, answer, newPassword)) {
                    JOptionPane.showMessageDialog(resetFrame, "密码重置成功！");
                    resetFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(resetFrame, "用户名或密保答案错误！");
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFrame.dispose(); // 取消按钮，关闭密码重置窗口
            }
        });
    }

    // 重置密码的方法
    private boolean resetPassword(String username, String answer, String newPassword) {
        try (BufferedReader reader = new BufferedReader(new FileReader("userDB.txt"))){
            String fileContent = "";
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String storedUsername = parts[0];
                    String storedPassword = parts[1];
                    String storedAnswer = parts[2];

                    if (storedUsername.equals(username) && storedAnswer.equals(answer)) {
                        // 用户名和密保答案匹配，重置密码
                        storedPassword = newPassword;
                    }
                    fileContent += storedUsername + "," + storedPassword + "," + storedAnswer + "\n";
                }
            }

            // 将更新后的内容写回userDB.txt
            try (FileWriter writer = new FileWriter("userDB.txt")) {
                writer.write(fileContent);
            }

            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
