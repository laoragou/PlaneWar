package Element;

import java.awt.*;
import javax.swing.ImageIcon;

public class PropObj extends ElementObj {
    private int fallSpeed = 3;
    private String propType;
    
    @Override
    public void ShowElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
    }

    @Override
    public void Move() {
        // Props fall downward
        this.setY(this.getY() + fallSpeed);
        
        // Remove prop if it goes off screen
        if (this.getY() > 600) {
            this.setAlive(false);
        }
    }

    public String getPropType() {
        return propType;
    }

    @Override
    public ElementObj CreatElement(String str) {
        String[] arrs = str.split(",");
        propType = arrs[0];
        
        // Load prop image based on type
        switch(propType) {
            case "POWER": 
                icon = new ImageIcon("image/airplane/prop/1.png"); 
                break;
            case "HEALTH": 
                icon = new ImageIcon("image/airplane/prop/2.png"); 
                break;
            case "SPEED": 
                icon = new ImageIcon("image/airplane/prop/3.png"); 
                break;
            case "SHIELD": 
                icon = new ImageIcon("image/airplane/prop/4.png"); 
                break;
            default: 
                icon = new ImageIcon("image/airplane/prop/1.png");
                break;
        }
        
        int x = Integer.parseInt(arrs[1]);
        int y = Integer.parseInt(arrs[2]);
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();
        
        this.setX(x);
        this.setY(y);
        this.setW(w);
        this.setH(h);
        this.setIcon(icon);
        
        // Prop properties
        this.setCanPass(true);
        this.setCanDistroy(false);
        this.setCanThrough(true);
        
        return this;
    }
}