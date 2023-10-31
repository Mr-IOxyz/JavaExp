package com.login;

import javax.mail.MessagingException;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class RegisterGUI {
    public static StringBuffer verificationCode = new StringBuffer(EmailUtil.generateVerificationCode());

    private static final String filePath = "userDB.txt";

    RegisterGUI() {
        initial();
    }

    public static void initial() {
        JFrame mainGUI = new JFrame();
        mainGUI.setLayout(null);

        JLabel nameStr = new JLabel("新用户名:");
        nameStr.setBounds(70, 75, 100, 25);
        mainGUI.add(nameStr);

        JLabel passwordStr = new JLabel("密码:");
        passwordStr.setBounds(75, 125, 100, 25);
        mainGUI.add(passwordStr);

        JLabel useremail = new JLabel("邮箱:");
        useremail.setBounds(75, 175, 200, 25);
        mainGUI.add(useremail);

        JLabel code = new JLabel("验证码:");
        code.setBounds(75, 225, 200, 25);
        mainGUI.add(code);

        JTextField emailField = new JTextField();
        emailField.setBounds(125, 175, 150, 25);
        mainGUI.add(emailField);

        JTextField codeField = new JTextField();
        codeField.setBounds(125, 225, 150, 25);
        mainGUI.add(codeField);

        JTextField userID = new JTextField();
        userID.setBounds(125, 75, 150, 25);
        mainGUI.add(userID);

        JPasswordField password = new JPasswordField();
        password.setBounds(125, 125, 150, 25);
        mainGUI.add(password);

        JButton sendVerificationCodeButton = new JButton("发送验证码");
        sendVerificationCodeButton.setBounds(150, 275, 100, 25);
        mainGUI.add(sendVerificationCodeButton);


        JCheckBox showPasswordCheckBox = new JCheckBox();
        showPasswordCheckBox.setBounds(300,125,20,20);
        mainGUI.add(showPasswordCheckBox);

        JButton buttonConfirm = new JButton("确认注册");
        buttonConfirm.setBounds(150, 325, 100, 25);
        mainGUI.add(buttonConfirm);

        mainGUI.setSize(400, 400);
        LoginGUI.centerWindow(mainGUI);
        mainGUI.setVisible(true);
        mainGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        showPasswordCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckBox.isSelected()) {
                    password.setEchoChar((char) 0); // 显示密码
                } else {
                    password.setEchoChar('*'); // 隐藏密码
                }
            }
        });
        sendVerificationCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                verificationCode.replace(0,6,EmailUtil.generateVerificationCode());
                try {
                    EmailUtil.sendEmail(emailField.getText(),"你的验证码","你的验证码是："+verificationCode);
                } catch (MessagingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean registersuccess = false;
                String username = userID.getText();
                String userpwd = new String(password.getPassword());
                String userEmail = emailField.getText();
                String userCode = codeField.getText();
                boolean isAlphanumeric = username.matches("^[a-zA-Z0-9]+$");
                boolean isPwdAlpha = false;
                boolean isSpecialChar = false;
                boolean isNumber = false;
                int sum = 0;

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(filePath));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length > 0 && parts[0].equals(username)) {
                            JOptionPane.showMessageDialog(mainGUI, "用户名重复！");
                            reader.close();
                            return;
                        }
                    }
                    reader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                for (int i = 0; i < userpwd.length(); i++) {
                    char tempChar = userpwd.charAt(i);
                    if (((tempChar >= 'A' && tempChar <= 'Z') || (tempChar >= 'a' && tempChar <= 'z')) && !isPwdAlpha) {
                        isPwdAlpha = true;
                        sum++;
                    }
                    if (tempChar >= '1' && tempChar <= '9' && !isNumber) {
                        isNumber = true;
                        sum++;
                    }
                    if (tempChar >= '!' && tempChar <= '/' && !isSpecialChar) {
                        isSpecialChar = true;
                        sum++;
                    }
                }
                if ((username.length() > 4) && (username.length() < 40) && isAlphanumeric && sum >= 2 && (userpwd.length() >= 4) && (userpwd.length() <= 40) && (userCode.equals(verificationCode.toString()))) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
                        writer.append(username+','+PasswordUtils.hashPasswordWithUsernameAsSalt(userpwd,username) +','+ userEmail +'\n');
                        writer.close();
                    } catch (IOException a) {
                        a.printStackTrace();
                    }
                    JOptionPane.showMessageDialog(mainGUI, "已成功注册！");
                    mainGUI.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(mainGUI, "注册不合法！");
                }
            }
        });
    }
}
