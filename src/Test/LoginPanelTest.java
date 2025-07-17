package Test;

import Show.LoginPanel;
import java.net.URL;
import javax.swing.*;

public class LoginPanelTest {
    public static void main(String[] args) {
        // 在测试类中添加路径验证
        URL testUrl = LoginPanelTest.class.getClassLoader().getResource("Show/image/login_background.png");
        System.out.println("测试资源路径：" + (testUrl != null ? testUrl.toExternalForm() : "未找到"));
        
        JFrame frame = new JFrame("登录界面测试");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // 创建登录面板并添加到窗口
        LoginPanel loginPanel = new LoginPanel();
        frame.add(loginPanel);
        
        frame.pack(); // 自动适配面板尺寸
        frame.setLocationRelativeTo(null); // 窗口居中
        frame.setResizable(false); // 禁止调整窗口大小
        frame.setVisible(true);
    }
}
