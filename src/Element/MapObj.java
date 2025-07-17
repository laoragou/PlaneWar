package Element;

import java.awt.*;
import javax.swing.ImageIcon;


public class MapObj extends ElementObj{


    @Override
    public void ShowElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
    }

    @Override
    public ElementObj CreatElement(String str){
        String[] arrs = str.split(",");
        switch(arrs[0]){
            case "GRASS": icon = new ImageIcon("image/wall/grass.png"); setCanPass(true); setCanDistroy(false); setCanThrough(true); setInInvisibleZone(true); break;
            case "BRICK": icon = new ImageIcon("image/wall/brick.png"); setCanPass(false); setCanDistroy(true); setHp(1); break;
            case "RIVER": icon = new ImageIcon("image/wall/river.png"); setCanPass(false); setCanDistroy(false); setCanThrough(true); break;
            case "IRON": icon = new ImageIcon("image/wall/iron.png"); setCanPass(false); setCanDistroy(true); setHp(3); break;
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
        return this;
    }

    @Override
    public void Death(Long gameTime){
        if(isCanDistroy()){
            if(getHp() <= 0){
                setAlive(false);
            }
        }
    }

    @Override
    public void GetHit(int damage){
        if(isCanDistroy()){
            setHp(getHp() - damage);
        }
    }
}
