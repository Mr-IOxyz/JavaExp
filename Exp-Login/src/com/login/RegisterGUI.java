package com.login;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class RegisterGUI {
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

        JLabel question = new JLabel("你最喜欢的一个字母是？(密保问题)");
        question.setBounds(75, 175, 200, 25);
        mainGUI.add(question);

        JTextField answer = new JTextField();
        answer.setBounds(125, 200, 150, 25);
        mainGUI.add(answer);

        JTextField userID = new JTextField();
        userID.setBounds(125, 75, 150, 25);
        mainGUI.add(userID);

        JPasswordField password = new JPasswordField();
        password.setBounds(125, 125, 150, 25);
        mainGUI.add(password);

        JCheckBox showPasswordCheckBox = new JCheckBox();
        showPasswordCheckBox.setBounds(300,125,20,20);
        mainGUI.add(showPasswordCheckBox);

        JButton buttonConfirm = new JButton("确认注册");
        buttonConfirm.setBounds(155, 275, 100, 25);
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

        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean registersuccess = false;
                String username = userID.getText();
                String userpwd = new String(password.getPassword());
                String answertext= answer.getText();
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
                if ((username.length() > 4) && (username.length() < 40) && isAlphanumeric && sum >= 2 && (userpwd.length() >= 4) && (userpwd.length() <= 40)) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
                        writer.append(username + ',' + userpwd +','+ answertext +'\n');
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
