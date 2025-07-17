package Element;

import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

/*
 * 所有元素的基类
 */

public abstract  class ElementObj {
    private int x; // x坐标
    private int y; // y坐标
    private int w; // 宽度
    private int h; // 高度
    private int hp;
    private int damage;
    private boolean canPass;
    private boolean canDistroy;
    private boolean canThrough = false;
    private boolean inInvisibleZone = false;
    private boolean isInvisible = false;

    public void setInvisible(boolean isInvisible) {
        this.isInvisible = isInvisible;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public boolean isInInvisibleZone() {
        return inInvisibleZone;
    }

    public void setInInvisibleZone(boolean invisible) {
        this.inInvisibleZone = invisible;
    }

    public boolean isCanThrough() {
        return canThrough;
    }
    public void setCanThrough(boolean canThrough) {
        this.canThrough = canThrough;
    }
    public boolean isCanPass() {
        return canPass;
    }
    public void setCanPass(boolean canPass) {
        this.canPass = canPass;
    }
    public boolean isCanDistroy() {
        return canDistroy;
    }
    public void setCanDistroy(boolean canDistroy) {
        this.canDistroy = canDistroy;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }

    //图片对象
    protected   ImageIcon icon;
    //状态
    private boolean isAlive = true; 
    //来自
    private String from = null;
    private String direction = "up"; // 默认方向

    //构造函数
    public ElementObj() {}
    public ElementObj(int x, int y, int w, int h, ImageIcon icon) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.icon = icon;
    }

    public abstract  void ShowElement(Graphics g);
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getW() {
        return w;
    }
    public void setW(int w) {
        this.w = w;
    }
    public int getH() {
        return h;
    }
    public void setH(int h) {
        this.h = h;
    }
    public ImageIcon getIcon() {
        return icon;
    }
    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }
        public boolean isAlive() {
        return isAlive;
    }
    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    //在父类定义，在需要的子类中重写(与抽象不同，抽象方法必须重写)
    //true=按下，false=松开， key=按键
    public void keyClick(boolean isPressed, int key){}

    //新的设计模式：模版模式
    //在模式中定义对象执行方法的先后顺序，子类选择性重写
    public final void model(Long gameTime) {
        UpdateImg();
        Move();
        Shoot(gameTime);
        LifeTimeCheck(gameTime);
        Death(gameTime);
    }

    //移动，需要的进行实现
    protected  void Move(){}
    //射击，子类可以重写
    protected void Shoot(Long gameTime){}
    //生命周期检查，子类可以重写
    protected void LifeTimeCheck(Long gameTime) {}
    //更新图片，子类可以重写
    protected void UpdateImg(){}
    //死亡方法，死亡也是一个对象
    public void Death(Long gameTime) {}
    //专供给子弹的From返回方法
    public String getFrom() {
        return from;
    }

    //供给重写的工厂
    public ElementObj CreatElement(String str){
        return null; // 返回null，子类需要重写
    }


    //实时返回元素的矩形碰撞对象
    public Rectangle getRectangle(){
        return new Rectangle(x, y, w, h);
    }

    //碰撞方法
    public boolean Collide(ElementObj obj){
        return this.getRectangle().intersects(obj.getRectangle());
    }

    public void GetHit(int damage){}

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    //绘制血条
    public void DrawHp(Graphics g){}

    public void AddHp(int hp){}

}
