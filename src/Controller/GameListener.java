package Controller;

//用于监听用户操作

import Element.ElementObj;
import Manager.ElementManager;
import Manager.GameElements;
import Manager.AirplaneGameController;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


public class GameListener implements KeyListener{
    private ElementManager elementManager = ElementManager.GetManager();

    //通过集合记录所有按下的键，重复触发则结束
    private Set<Integer> keySet = new HashSet<Integer>();//唯一不可重复，且效率更高

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        // Handle restart key (R) when game is over
        if (key == KeyEvent.VK_R && AirplaneGameController.isGameOver()) {
            restartGame();
            return;
        }
        
        // Regular game input handling
        if(keySet.contains(key)) {
            // 如果已经按下了这个键，则不再处理
            return;
        }
        keySet.add(key);
        
        // 拿到玩家集合
        List<ElementObj> play = elementManager.GetElementsByKey(GameElements.PLAY);
        // 处理敌人按键
        List<ElementObj> enemies = elementManager.GetElementsByKey(GameElements.ENEMY);
        
        for(ElementObj obj : play) {
            obj.keyClick(true, e.getKeyCode());
        }
        // 处理敌人按键
        for(ElementObj enemy : enemies) {
            enemy.keyClick(true, e.getKeyCode());
        }
    }
    
    private void restartGame() {
        // Clear all game elements
        elementManager.getGameElements().get(GameElements.PLAY).clear();
        elementManager.getGameElements().get(GameElements.ENEMY).clear();
        elementManager.getGameElements().get(GameElements.BOSS).clear();
        elementManager.getGameElements().get(GameElements.BULLET).clear();
        elementManager.getGameElements().get(GameElements.MAPS).clear();
        
        // Reset game controller
        AirplaneGameController.resetGame();
        
        // Reinitialize game
        AirplaneGameController.initializeGame();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(!keySet.contains(e.getKeyCode())){
            return;
        }
        keySet.remove(e.getKeyCode());
        for(ElementObj obj : elementManager.GetElementsByKey(GameElements.PLAY)) {
            obj.keyClick(false, e.getKeyCode());
        }
        // 处理敌人按键释放
        for(ElementObj enemy : elementManager.GetElementsByKey(GameElements.ENEMY)) {
            enemy.keyClick(false, e.getKeyCode());
        }
    }
}
