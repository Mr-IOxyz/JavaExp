package com.login;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CaptchaGUI {
    CaptchaGUI(){
        inti();
    }
    public static void inti(){
        JFrame mainGUI=new JFrame("验证窗口");
        mainGUI.setLayout(null);

        JLabel nameStr = new JLabel("请输入验证码：");
        nameStr.setBounds(50, 75, 100, 25);
        mainGUI.add(nameStr);

        JTextField captchacode = new JTextField();
        captchacode.setBounds(150, 75, 150, 25);
        mainGUI.add(captchacode);

        JButton buttonConfirm = new JButton("确认");
        buttonConfirm.setBounds(175, 175, 100, 25);
        mainGUI.add(buttonConfirm);

        String text= CaptchaGenerator.getCaptchaTEXT();

        JLabel imageLabel = new JLabel();
        ImageIcon imageIcon = new ImageIcon("captcha.png"); // 本地文件
        imageLabel.setIcon(imageIcon);
        imageLabel.setBounds(85,100,180,70);
        mainGUI.add(imageLabel);

        mainGUI.setSize(400, 300);
        LoginGUI.centerWindow(mainGUI);
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.setVisible(true);
        buttonConfirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(captchacode.getText().equals(text)){
                    JOptionPane.showMessageDialog(mainGUI, "验证通过！");
                    mainGUI.dispose();
                }
                else {
                    JOptionPane.showMessageDialog(mainGUI, "验证码错误，请重新输入！");
                }
            }
        });
    }
}