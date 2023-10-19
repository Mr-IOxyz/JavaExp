package com.login;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
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

        JTextField userID = new JTextField();
        userID.setBounds(125, 75, 150, 25);
        mainGUI.add(userID);

        JPasswordField password = new JPasswordField();
        password.setBounds(125, 125, 150, 25);
        mainGUI.add(password);

        JCheckBox rememberUsernameCheckBox = new JCheckBox("记住账号");
        rememberUsernameCheckBox.setBounds(75, 150, 100, 25);
        mainGUI.add(rememberUsernameCheckBox);


        JCheckBox showPasswordCheckBox = new JCheckBox();
        showPasswordCheckBox.setBounds(275,125,20,20);
        mainGUI.add(showPasswordCheckBox);

        JButton buttonLogin = new JButton("登录");
        buttonLogin.setBounds(75, 175, 70, 25);
        mainGUI.add(buttonLogin);

        JButton buttonRegister = new JButton("注册");
        buttonRegister.setBounds(225, 175, 70, 25);
        mainGUI.add(buttonRegister);
        String rememberedUsername = readRememberedUsername();
        if (!rememberedUsername.isEmpty()) {
            userID.setText(rememberedUsername); // 如果已保存用户名，自动填充用户名输入框
        }
        JButton forgetPasswordButton = new JButton("忘记密码");
        forgetPasswordButton.setBounds(130, 225, 100, 25);
        mainGUI.add(forgetPasswordButton);

        mainGUI.add(userID);
        mainGUI.setSize(400, 300);
        centerWindow(mainGUI);
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.setVisible(true);

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


        buttonLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String loginUsername = userID.getText();
                String loginPassword = password.getText();
                boolean usernameExist = false;
                boolean passwordCorrect = false;


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
                            String username = parts[0];
                            String password = parts[1];
                            String answer = parts[2];
                            if (username.equals(loginUsername)) {
                                usernameExist = true;
                                if (password.equals(loginPassword)) {
                                    passwordCorrect = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (usernameExist) {
                        if (passwordCorrect) {
                            JOptionPane.showMessageDialog(mainGUI, "登陆成功！");
                            LogoutGUI logoutGUI = new LogoutGUI();
                        } else {
                            JOptionPane.showMessageDialog(mainGUI, "密码错误！");
                            errortimes++;
                            if(errortimes>=3) {
                                CaptchaGUI yz = new CaptchaGUI();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(mainGUI, "用户名不存在！");
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

    public static void main(String[] args){

        LoginGUI test=new LoginGUI();
//        RegisterGUI test=new RegisterGUI();
//        CaptchaGUI test=new CaptchaGUI();
//        System.out.println(CaptchaGenerator.getCaptchaTEXT());
    }
}
