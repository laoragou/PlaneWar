package Element;

//玩家子弹
/*
 * 重写show方法，选择性重写部分方法，如move
 */

import java.awt.Color;
import java.awt.Graphics;


public class Bullet extends ElementObj {
    
    private int speed = 10;
    private String direction;
    private Long lifeTime = 40L;
    private Long createTime = -1L; //创建时间

    private String from;//创建对象
    
    @Override
    public String getFrom() {
        return from;
    }

    public Bullet(){}

    //工厂模式：构造一个类需要做比较多的工作，直接封装成一个方法，返回自己
    //重写的工厂方法
    @Override
    public ElementObj CreatElement(String str){
        String[] split = str.split(",");
        for(String strl : split){
            String[] split2 = strl.split(":");
            switch(split2[0]){
                case "x": this.setX((Integer.parseInt(split2[1]))); break;
                case "y": this.setY((Integer.parseInt(split2[1]))); break;
                case "direction": this.direction = split2[1]; break;
                case "from": this.from = split2[1]; break;
                case "damage": this.setDamage(Integer.parseInt(split2[1])); break;
            }
        }
        this.setW(8);
        this.setH(12);
        // No position adjustment needed for airplane bullets as they're already positioned correctly
        return this; 
    }
    @Override
    public void ShowElement(Graphics g) {
        if("Element.Play".equals(this.from)){
            g.setColor(Color.YELLOW); // Player bullets are yellow
        }
        else{
            g.setColor(Color.RED); // Enemy bullets are red
        }

        g.fillRect(this.getX(), this.getY(), this.getW(), this.getH());
    }

    @Override
    public void Move(){
        //边界判定
        if(this.getX() < 0 || this.getX() > 900 || this.getY() < 0 || this.getY() > 600) {
            this.setAlive(false); //超出边界则销毁
            return;
        }
        //生命周期检查
        switch(this.direction) {
            case "up": this.setY(this.getY() - this.speed); break;
            case "down": this.setY(this.getY() + this.speed); break;
            case "left": this.setX(this.getX() - this.speed); break;
            case "right": this.setX(this.getX() + this.speed); break;
        }
    }

    @Override
    protected  void LifeTimeCheck(Long gameTime) {
        if(createTime == -1L) {
            createTime = gameTime; //记录创建时间
        }
        if(gameTime - createTime > lifeTime) {
            this.setAlive(false); //超过生命周期则销毁
        }
    }
}
