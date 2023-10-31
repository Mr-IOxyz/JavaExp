package com.login;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class EmailChanger {
    EmailChanger(){init();}
    public void init() {
        JFrame resetFrame = new JFrame("密码重置");
        resetFrame.setLayout(null);

        JLabel nameLabel = new JLabel("用户名:");
        nameLabel.setBounds(75, 50, 100, 25);
        resetFrame.add(nameLabel);

        JLabel answerLabel = new JLabel("当前密码：");
        answerLabel.setBounds(75, 100, 100, 25);
        resetFrame.add(answerLabel);

        JLabel newPasswordLabel = new JLabel("新邮箱:");
        newPasswordLabel.setBounds(75, 150, 100, 25);
        resetFrame.add(newPasswordLabel);

        JLabel codeLabel = new JLabel("验证码:");
        codeLabel.setBounds(75, 200, 100, 25);
        resetFrame.add(codeLabel);

        JTextField codeField = new JTextField();
        codeField.setBounds(175, 200, 150, 25);
        resetFrame.add(codeField);

        JTextField userNameField = new JTextField();
        userNameField.setBounds(175, 50, 150, 25);
        resetFrame.add(userNameField);

        JTextField answerField = new JTextField();
        answerField.setBounds(175, 100, 150, 25);
        resetFrame.add(answerField);

        JTextField newPasswordField = new JTextField();
        newPasswordField.setBounds(175, 150, 150, 25);
        resetFrame.add(newPasswordField);

        JButton resetButton = new JButton("确认更改");
        resetButton.setBounds(75, 275, 100, 25);
        resetFrame.add(resetButton);

        JButton cancelButton = new JButton("发送验证码");
        cancelButton.setBounds(225, 275, 100, 25);
        resetFrame.add(cancelButton);

        resetFrame.setSize(400, 400);
        LoginGUI.centerWindow(resetFrame);
        resetFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resetFrame.setVisible(true);
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterGUI.verificationCode.replace(0, 6, EmailUtil.generateVerificationCode());
                try {
                    EmailUtil.sendEmail(newPasswordField.getText(), "你的验证码", "你的验证码是：" + RegisterGUI.verificationCode);
                } catch (MessagingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginUsername = userNameField.getText();
                String loginPassword = answerField.getText();
                String newEmail = newPasswordField.getText();
                String inputCode = codeField.getText();

                String hashSalt = PasswordUtils.hashPasswordWithUsernameAsSalt(loginPassword, loginUsername);

                if (!inputCode.equals(RegisterGUI.verificationCode.toString())) {
                    JOptionPane.showMessageDialog(resetFrame, "验证码错误！");
                    return;
                }

                File dbFile = new File("userDB.txt");
                File tempDB = new File("tempUserDB.txt");

                try (
                        BufferedReader reader = new BufferedReader(new FileReader(dbFile));
                        BufferedWriter writer = new BufferedWriter(new FileWriter(tempDB))
                ) {
                    String currentLine;
                    boolean found = false;
                    while ((currentLine = reader.readLine()) != null) {
                        String[] userInfo = currentLine.split(",");
                        if (userInfo.length == 3) {
                            String username = userInfo[0];
                            String hashEmail = userInfo[1];
                            String email = userInfo[2];

                            if (hashEmail.equals(hashSalt)) {
                                found = true;
                                String updatedUserInfo = hashEmail + "," + newEmail;
                                writer.write(updatedUserInfo + System.lineSeparator());
                            } else {
                                writer.write(currentLine + System.lineSeparator());
                            }
                        }
                    }

                    if (!found) {
                        JOptionPane.showMessageDialog(resetFrame, "未找到用户！");
                    }

                } catch (IOException ex) {
                    throw new RuntimeException("更新用户信息时出错", ex);
                }

                if (!dbFile.delete()) {
                    System.out.println("删除原文件失败");
                    return;
                }

                if (!tempDB.renameTo(dbFile)) {
                    System.out.println("重命名文件失败");
                }

                JOptionPane.showMessageDialog(resetFrame, "邮箱更新成功！");
            }
        });


    }
    public static void main(String[] args){
        EmailChanger test = new EmailChanger();

    }
}
