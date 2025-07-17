package Element;

import Manager.ElementManager;
import Manager.GameElements;
import Manager.GameLoad;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.ImageIcon;

/*
 * 添加属性
 * 1.单属性  配合方向枚举类型属性使用，一次只能移动一个方向
 * 2.双属性  上下和左右，配合bool
 * 3.多属性  上下左右和前后，配合bool
 * 同时按上下，后按会重置先按
 */

public class Play extends ElementObj {
    private boolean up = false;    // 上
    private boolean down = false;  // 下
    private boolean left = false;  // 左
    private boolean right = false; // 右
    private Long lastShootTime = 0L; //上次射击时间
    private Long shootCD = 20L; //射击冷却时间 (reduced for better responsiveness)
    private Long deathTime = -1L;
    private int maxHp;

    //记录玩家方向，默认up
    private String direction;
    public String getDirection() {
        return direction;
    }
    public void setDirection(String direction) {
        this.direction = direction;
    }

    //攻击状态
    private boolean attackState = false;
    
    public Play() {}
    public Play(int x, int y, int width, int height, ImageIcon icon) {
        super(x, y, width, height, icon);
    }

    @Override
    public void ShowElement(Graphics g) {
        g.drawImage(this.getIcon().getImage(), this.getX(), this.getY(), this.getW(), this.getH(), null);
    }
    

    //重写按键方法
    public void keyClick(boolean isPressed, int key) {
        if(isPressed){
            switch(key) {
                case 65: // 左 
                    up = false;down = false;
                    right = false;left = true;direction = "left";
                    break;
                case 87: // 上
                    left = false;right = false;
                    down = false;up = true;direction = "up";
                    break;
                case 68: // 右
                    up = false;down = false;
                    left = false;right = true;direction = "right";
                    break;
                case 83: // 下
                    left = false;right = false;
                    up = false;down = true;direction = "down";
                    break;
                case 32://攻击
                    attackState = true;
                    break;
            }
        } 
        else{
            switch(key){
                case 65: // 左
                    left = false;
                    break;
                case 87: // 上
                    up = false;
                    break;
                case 68: // 右
                    right = false;
                    break;
                case 83: // 下
                    down = false;
                    break;
                case 32://攻击
                    attackState = false;
                    break;
            }
        }
    }

    @Override
    public void Move() {
        // Airplane movement - restrict to bottom half of screen for player
        if (up && this.getY() > 300) {
            this.setY(this.getY() - 8);
        }
        if (down && this.getY() <  600- this.getH()) { 
            this.setY(this.getY() + 8);
        }
        if (left && this.getX() > 0) {
            this.setX(this.getX() - 8);
        }
        if (right && this.getX() < 800 - this.getW()) { 
            this.setX(this.getX() + 8);
        }
    }

    @Override
    protected void UpdateImg() {
        // For airplane game, use default airplane sprite
        String key = "player";
        setIcon(GameLoad.imgMap.get(key));
    }

    @Override
    protected void Shoot(Long gameTime) {
        if(!this.attackState) {
            return; 
        }
        if(gameTime - lastShootTime < shootCD) {
            return; 
        }
        // Create airplane bullet going upward
        String bulletData = "x:" + (this.getX() + this.getW()/2 - 5) + 
                           ",y:" + this.getY() + 
                           ",direction:up" +
                           ",from:" + Play.class.getName() +
                           ",damage:1";
        ElementObj ele = new Bullet().CreatElement(bulletData);
        //装入集合
        ElementManager.GetManager().AddElement(ele, GameElements.BULLET);
        lastShootTime = gameTime; //更新上次射击时间
        // Player bullet fired
    }

    @Override
    public String toString(){
        return "x:" + this.getX() + 
               ",y:" + this.getY() + 
               ",direction:" + direction
               +",from:" + Play.class.getName()
               +",damage:" + 1
               ;
    }

    @Override
    public void GetHit(int damage){
        this.setHp(this.getHp() - damage);
    }

    @Override
    public ElementObj CreatElement(String str){
        String[] split = str.split(",");
        for(String part : split){
            String[] keyValue = part.split(":");
            switch(keyValue[0]){
                case "x": this.setX(Integer.parseInt(keyValue[1])); break;
                case "y": this.setY(Integer.parseInt(keyValue[1])); break;
                case "direction": this.setDirection(keyValue[1]); break;
                case "hp": this.setHp(Integer.parseInt(keyValue[1])); break;
            }
        }
        ImageIcon icon = GameLoad.imgMap.get("player");
        this.setH(40);
        this.setW(40);
        this.setIcon(icon);
        maxHp = getHp();
        return this;
    }

    @Override
    public void Death(Long gameTime){
        if(this.getHp() <= 0){
            if(deathTime == -1L){
                deathTime = gameTime;
            }
            ImageIcon explodeIcon = GameLoad.imgMap.get("explode");
            this.setIcon(explodeIcon);
            if(gameTime - deathTime > 20){
                this.setAlive(false);
            }
        }
    }

    @Override
    public void DrawHp(Graphics g){
        int barWidth = 20;
        int barHeight = 3;
        int x = (int) this.getX() - barWidth/2 + this.getW()/2;
        int y = (int) this.getY() - 15; // 血条在坦克上方
        g.setColor(Color.RED);
        g.fillRect(x, y, barWidth, barHeight);
        
        float hpPercent = (float) getHp() / maxHp;
        g.setColor(Color.GREEN);
        g.fillRect(x, y, (int)(barWidth * hpPercent), barHeight);
    }

    @Override
    public void AddHp(int hp){
        this.setHp(this.getHp() + hp);
        if(this.getHp() > maxHp){
            this.setHp(maxHp);
        }
    }
}