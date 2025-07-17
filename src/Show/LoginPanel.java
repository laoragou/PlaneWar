package Show;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class LoginPanel extends JPanel {
    private BufferedImage background;

    public LoginPanel() {
        setLayout(new GridBagLayout());
        
        try {
            // 修改为通过类加载器获取绝对路径
            URL imgUrl = getClass().getClassLoader().getResource("Show/image/login_background.png");
            System.out.println("尝试加载资源路径：" + (imgUrl != null ? imgUrl.toExternalForm() : "null"));
            background = ImageIO.read(imgUrl);
        } catch (Exception e) {
            System.err.println("背景图片加载失败: " + e.getMessage());
            e.printStackTrace();
        }

        // 创建开始按钮
        JButton startBtn = new JButton("开始游戏");
        startBtn.setPreferredSize(new Dimension(150, 50));
        startBtn.setFont(new Font("微软雅黑", Font.BOLD, 20));
        startBtn.setBackground(new Color(70, 130, 180));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // 添加按钮到面板中心
        add(startBtn, new GridBagConstraints());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            // 绘制自适应背景
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600); // 默认面板尺寸
    }
}
