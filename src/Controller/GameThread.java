package Controller;

import Element.ElementObj;
import Element.Enemy;
import Element.Play;
import Manager.AirplaneGameController;
import Manager.ElementManager;
import Manager.GameElements;
import Manager.GameLoad;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

public class GameThread extends Thread{
    private ElementManager elementManager;
    public GameThread() {
        this.elementManager = ElementManager.GetManager();
    }

    //主线程
    @Override
    public void run() {
        while(true){
            //开始前（登录）
            gameLoad();
            //开始时（判断）
            gameRun();
            //结束后（资源回收）
            gameOver();
            //休眠
            try {
                sleep(30);
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //游戏加载
    private void gameLoad() {
        GameLoad.LoadImg();
        // Reset game controller before initializing
        AirplaneGameController.resetGame();
        // Initialize airplane game instead of tank game
        AirplaneGameController.initializeGame();
    }

    //游戏运行
    //移动，碰撞，死亡，instantiate
    private void gameRun() {
        //游戏时间
        Long gameTime = 0L;
        //true可以用于扩展游戏跳出
        while(true){
            Map<GameElements, List<ElementObj>> gameElements = elementManager.getGameElements();
            List<ElementObj> play = elementManager.GetElementsByKey(GameElements.PLAY);
            List<ElementObj> enemy = elementManager.GetElementsByKey(GameElements.ENEMY);
            List<ElementObj> boss = elementManager.GetElementsByKey(GameElements.BOSS);
            List<ElementObj> bullet = elementManager.GetElementsByKey(GameElements.BULLET);
            List<ElementObj> map = elementManager.GetElementsByKey(GameElements.MAPS);
            List<ElementObj> drop = elementManager.GetElementsByKey(GameElements.DROP);
            
            // Update airplane game logic
            AirplaneGameController.updateGame(gameTime);

            DropCollider(play, drop);
            
            BaseModel(gameElements,gameTime);
            // Collision detection is now handled in AirplaneGameController.updateGame()
            
            // Check if game is over
            if (AirplaneGameController.isGameOver()) {
                // Wait for restart instead of breaking immediately
                while (AirplaneGameController.isGameOver()) {
                    try {
                        sleep(100); // Check every 100ms for restart
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // Game has been restarted, continue the loop
                gameTime = 0L; // Reset game time
            }
            
            gameTime++;
            try {
                sleep(30);
            } 
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //游戏结束
    private void gameOver() {}

    public void BaseModel(Map<GameElements, List<ElementObj>> gameElements, Long gameTime){
        for(GameElements key:GameElements.values()){
            List<ElementObj> list = gameElements.get(key);
            for(int i = 0; i < list.size(); i++) {
                ElementObj obj = list.get(i);
                //死亡判定，移出列表
                if(!obj.isAlive()) {
                    list.remove(i--);
                    continue; // 跳过当前循环，继续下一个元素
                }
                //需要一个死亡方法，进行动画/装备掉落
                obj.model(gameTime);
            }
        }       
    }

    public void BulletCollide(List<ElementObj> targets, List<ElementObj> bullets){
        for(int i = 0; i < targets.size(); i++){
            ElementObj target = targets.get(i);
            if (!target.isAlive()) continue;
            
            for(int j = 0; j < bullets.size(); j++){
                ElementObj bullet = bullets.get(j);
                if (!bullet.isAlive()) continue;
                
                if(target.Collide(bullet)){
                    // Check if bullet is from different source than target
                    String bulletFrom = bullet.getFrom();
                    boolean shouldHit = false;
                    
                    if ("Element.Play".equals(bulletFrom) && (target instanceof Element.Enemy)) {
                        shouldHit = true; // Player bullet hits enemy
                    } else if ("Element.Enemy".equals(bulletFrom) && (target instanceof Element.Play)) {
                        shouldHit = true; // Enemy bullet hits player
                    }
                    
                    if (shouldHit) {
                        target.GetHit(bullet.getDamage());
                        bullet.setAlive(false);
                        break;
                    }
                }
            } 
        }
    }

    public void DropCollider(List<ElementObj> play, List<ElementObj> drop){
        for(int i = 0; i < play.size(); i++){
            ElementObj player = play.get(i);
            for(int j = 0; j < drop.size(); j++){
                ElementObj theDrop = drop.get(j);
                if(player.Collide(theDrop)){
                    player.AddHp(1);
                    drop.remove(j--);
                }
            }
        }
    }

    public void MapCollider(List<ElementObj> A, List<ElementObj> B){
        for(int i = 0; i < A.size(); i++){
            ElementObj tank = A.get(i);
            boolean inGrass = false; 
            for(int j = 0; j < B.size(); j++){
                ElementObj wall = B.get(j);
                if(tank.Collide(wall)){
                    if(wall.isInInvisibleZone()) {
                        inGrass = true;
                    }
                    if(!wall.isCanPass()){
                        switch(tank.getDirection()){
                            case "up":
                                tank.setY(wall.getY() + wall.getH()); 
                                break;
                            case "down":
                                tank.setY(wall.getY() - tank.getH());
                                break;
                            case "left":
                                tank.setX(wall.getX() + wall.getW());
                                break;
                            case "right":
                                tank.setX(wall.getX() - tank.getW());
                                break;
                        }
                        break;
                    }
                }
            }
            tank.setInvisible(inGrass);
        }
    }

    


    //加载玩家1
    public void LoadPlay(){
        String playStr = "500,500,up,5";
        ElementObj play = new Play().CreatElement(playStr);
        elementManager.AddElement(play, GameElements.PLAY);
    }
    //加载玩家2
    public void LoadEnemy(){
        String enemyStr = "500,100,down,5";
        ElementObj enemy = new Enemy().CreatElement(enemyStr);
        elementManager.AddElement(enemy, GameElements.ENEMY);
    }

    // 飞机间碰撞检测
    public void TankCollider(List<ElementObj> tanks) {
        for(int i = 0; i < tanks.size(); i++) {
            ElementObj tankA = tanks.get(i);
            for(int j = i + 1; j < tanks.size(); j++) {
                ElementObj tankB = tanks.get(j);
                if(tankA.Collide(tankB)) {
                    resolveTankCollision(tankA, tankB);
                }
            }
        }
    }

    private void resolveTankCollision(ElementObj tankA, ElementObj tankB) {
        // 计算重叠区域
        Rectangle intersection = tankA.getRectangle().intersection(tankB.getRectangle());
        
        // 根据重叠区域尺寸判断主要碰撞方向
        if(intersection.width < intersection.height) { // 水平方向碰撞
            adjustHorizontalCollision(tankA, tankB);
        } else { // 垂直方向碰撞
            adjustVerticalCollision(tankA, tankB);
        }
    }

    private void adjustHorizontalCollision(ElementObj tankA, ElementObj tankB) {
        // 判断相对位置
        if(tankA.getX() < tankB.getX()) { // A在左，B在右
            handleMovement(tankA, "right");
            handleMovement(tankB, "left");
        } else { // A在右，B在左
            handleMovement(tankA, "left");
            handleMovement(tankB, "right");
        }
    }

    private void adjustVerticalCollision(ElementObj tankA, ElementObj tankB) {
        // 判断相对位置
        if(tankA.getY() < tankB.getY()) { // A在上，B在下
            handleMovement(tankA, "down");
            handleMovement(tankB, "up");
        } else { // A在下，B在上
            handleMovement(tankA, "up");
            handleMovement(tankB, "down");
        }
    }

    private void handleMovement(ElementObj tank, String restrictDir) {
        // 只处理当前移动方向与限制方向一致的情况
        if(tank.getDirection().equals(restrictDir)) {
            switch(restrictDir) {
                case "up":
                    tank.setY(tank.getY() + 5); // 向下回退
                    break;
                case "down":
                    tank.setY(tank.getY() - 5); // 向上回退
                    break;
                case "left":
                    tank.setX(tank.getX() + 5); // 向右回退
                    break;
                case "right":
                    tank.setX(tank.getX() - 5); // 向左回退
                    break;
            }
        }
    }
}