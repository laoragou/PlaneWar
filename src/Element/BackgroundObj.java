package Element;

import java.awt.*;
import javax.swing.ImageIcon;

public class BackgroundObj extends ElementObj {
    private int scrollSpeed = 2; // Speed of background scrolling
    
    @Override
    public void ShowElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
    }

    @Override
    public void Move() {
        // Move background downward to create scrolling effect
        this.setY(this.getY() + scrollSpeed);
        
        // Reset position when background goes off screen
        if (this.getY() >= 600) {
            this.setY(-600); // Reset to create seamless scrolling
        }
        
        // Debug output removed for performance
    }

    @Override
    public ElementObj CreatElement(String str) {
        String[] arrs = str.split(",");
        String bgType = arrs[0];
        
        // Load background image - try different extensions
        try {
            icon = new ImageIcon("image/airplane/background/" + bgType + ".png");
            if (icon.getIconWidth() <= 0) {
                icon = new ImageIcon("image/airplane/background/" + bgType + ".jpg");
            }
        } catch (Exception e) {
            // Fallback to a default background
            icon = new ImageIcon("image/airplane/background/1.png");
        }
        
        int x = Integer.parseInt(arrs[1]);
        int y = Integer.parseInt(arrs[2]);
        int w = 800; // Full screen width
        int h = 600; // Full screen height
        
        this.setX(x);
        this.setY(y);
        this.setW(w);
        this.setH(h);
        this.setIcon(icon);
        
        // Background properties
        this.setCanPass(true);
        this.setCanDistroy(false);
        this.setCanThrough(true);
        
        return this;
    }
}