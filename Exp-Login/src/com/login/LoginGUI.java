package com.login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LoginGUI {
    public static int errortimes=0;
    LoginGUI(){
        initial();
    }
    public void initial() {
        JFrame mainGUI = new JFrame("登陆系统");
        mainGUI.setLayout(null);

        JLabel nameStr = new JLabel("用户名:");
        nameStr.setBounds(75, 75, 100, 25);
        mainGUI.add(nameStr);

        JLabel passwordStr = new JLabel("密码:");
        passwordStr.setBounds(75, 125, 100, 25);
        mainGUI.add(passwordStr);

        JButton changeEmailButton = new JButton("更换邮箱");
        changeEmailButton.setBounds(180, 225, 90, 25);
        mainGUI.add(changeEmailButton);

        JTextField userID = new JTextField();
        userID.setBounds(125, 75, 150, 25);
        mainGUI.add(userID);

        JPasswordField password = new JPasswordField();
        password.setBounds(125, 125, 150, 25);
        mainGUI.add(password);

        JCheckBox rememberUsernameCheckBox = new JCheckBox("记住账号");
        rememberUsernameCheckBox.setBounds(75, 150, 100, 25);
        mainGUI.add(rememberUsernameCheckBox);

        JButton oneClickLoginButton = new JButton("一键登录");
        oneClickLoginButton.setBounds(125, 175, 90, 25); // 请根据您的界面布局调整位置和大小
        mainGUI.add(oneClickLoginButton);

        JCheckBox showPasswordCheckBox = new JCheckBox();
        showPasswordCheckBox.setBounds(275,125,20,20);
        mainGUI.add(showPasswordCheckBox);

        JButton buttonLogin = new JButton("登录");
        buttonLogin.setBounds(50, 175, 70, 25);
        mainGUI.add(buttonLogin);

        JButton buttonRegister = new JButton("注册");
        buttonRegister.setBounds(225, 175, 70, 25);
        mainGUI.add(buttonRegister);
        String rememberedUsername = readRememberedUsername();
        if (!rememberedUsername.isEmpty()) {
            userID.setText(rememberedUsername); // 如果已保存用户名，自动填充用户名输入框
        }
        JButton forgetPasswordButton = new JButton("忘记密码");
        forgetPasswordButton.setBounds(90, 225, 90, 25);
        mainGUI.add(forgetPasswordButton);

        mainGUI.add(userID);
        mainGUI.setSize(400, 300);
        centerWindow(mainGUI);
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.setVisible(true);

        changeEmailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EmailChanger emailChanger = new EmailChanger();
                // You can perform any other actions related to changing email here
            }
        });
        oneClickLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 从文件中读取 token
                String token = readToken("Token.txt");  // 假设 token 保存在 "token.txt" 文件中

                if (!token.isEmpty() && TokenManager.isTokenValid(token)) {
                    JOptionPane.showMessageDialog(mainGUI, "一键登录成功！");
                    LogoutGUI test = new LogoutGUI();
                } else {
                    JOptionPane.showMessageDialog(mainGUI, "无效的登录状态，请进行常规登录。");
                }
            }
        });

        forgetPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResetGUI resetGUI = new ResetGUI();
            }
        });
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



// 在登录按钮的 ActionListener 中生成令牌
        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginUsername = userID.getText();
                String loginPassword = password.getText();
                String hashSalt = PasswordUtils.hashPasswordWithUsernameAsSalt(loginPassword, loginUsername);
                boolean correct = false;
                // 根据 rememberUsernameCheckBox 的状态来决定是否记住账号
                if (rememberUsernameCheckBox.isSelected()) {
                    // 保存用户名到文件或其他适当的存储方式
                    try (FileWriter writer = new FileWriter("rememberedUsername.txt")) {
                        writer.write(loginUsername);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                try (BufferedReader reader = new BufferedReader(new FileReader("userDB.txt"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 3) {
                            String username= parts[0];
                            String hashDB = parts[1];
                            String answer = parts[2];
                            if (hashSalt.equals(hashDB)) {
                                correct = true;
                                break; // 登录成功，退出循环
                            }
                        }
                    }

                    if (correct) {
                        JOptionPane.showMessageDialog(mainGUI, "登陆成功！");
                        LogoutGUI loginSucessfully = new LogoutGUI(hashSalt);
                    } else {
                        JOptionPane.showMessageDialog(mainGUI, "登陆失败！");
                        errortimes++;
                        if (errortimes >= 3) {
                            CaptchaGUI yz = new CaptchaGUI();
                        }
                    }
                } catch (IOException c) {
                    c.printStackTrace();
                }
            }
        });


        buttonRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterGUI registerGUI = new RegisterGUI();
//                mainGUI.setVisible(false);

            }
        });
    }

    public static void centerWindow(Window window) {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - window.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - window.getHeight()) / 2);
        window.setLocation(x, y);
    }
    private String readRememberedUsername() {
        try (BufferedReader reader = new BufferedReader(new FileReader("rememberedUsername.txt"))) {
            String line;
            if ((line = reader.readLine()) != null) {
                return line; // 返回已保存的用户名
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ""; // 如果没有已保存的用户名，返回空字符串
    }

    private static String readToken(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = br.readLine()) != null) {
                contentBuilder.append(currentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return contentBuilder.toString();
    }

    public static void main(String[] args){
        LoginGUI mAIN = new LoginGUI();
    }
}
