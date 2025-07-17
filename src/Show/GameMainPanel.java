package Show;

import Element.ElementObj;
import Manager.AirplaneGameController;
import Manager.ElementManager;
import Manager.GameElements;
import Manager.LevelManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;

// 游戏主面板（界面显示以及刷新（多线程））
public class GameMainPanel extends JPanel implements Runnable {
    //联动管理器
    private ElementManager elementManager;

    //初始化
    public GameMainPanel() {
        init();
    }
    public void init() {
        //初始化联动管理器
        elementManager = ElementManager.GetManager();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        //map的key是无序的，需要提取出所有的key之后，使用key进行遍历
        Map<GameElements, List<ElementObj>> gameElements = elementManager.getGameElements();
        for(GameElements key:GameElements.values()){
            List<ElementObj> list = gameElements.get(key);
            for(int i = 0; i < list.size(); i++) {
                ElementObj obj = list.get(i);
                //调用元素自己的显示方法
                obj.ShowElement(g);
            }
        }
        
        // Draw game over screen if game is over
        if (AirplaneGameController.isGameOver()) {
            drawGameOverScreen(g);
        }
        
        // Draw level information
        drawLevelInfo(g);
    }
    
    private void drawGameOverScreen(Graphics g) {
        // Semi-transparent overlay
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
        
        // Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String gameOverText = "GAME OVER";
        int textWidth = g.getFontMetrics().stringWidth(gameOverText);
        g.drawString(gameOverText, (getWidth() - textWidth) / 2, getHeight() / 2 - 30);
        
        // Restart instruction
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String restartText = "Press R to Restart";
        int restartWidth = g.getFontMetrics().stringWidth(restartText);
        g.drawString(restartText, (getWidth() - restartWidth) / 2, getHeight() / 2 + 30);
    }
    
    private void drawLevelInfo(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        String levelInfo = LevelManager.getLevelInfo();
        
         List<ElementObj> players = ElementManager.GetManager().GetElementsByKey(GameElements.PLAY);
        String hpInfo = "HP: 0";
        if (!players.isEmpty()) {
            ElementObj player = players.get(0); 
            hpInfo = "HP: " + player.getHp();  
        }

        g.drawString(levelInfo, 10, 25);
        g.drawString(hpInfo, 10, 45);
        
        // If max level reached, show victory message
        if (LevelManager.isMaxLevel()) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            String victoryText = "ALL LEVELS COMPLETED!";
            int textWidth = g.getFontMetrics().stringWidth(victoryText);
            g.drawString(victoryText, (getWidth() - textWidth) / 2, 50);
        }
    }
    //以下代码暂且写在此处
    //---------------------******-------------------

    @Override
    //实现多线程刷新
    public void run() {
        while (true) {
            //刷新界面
            this.repaint();
            try {
                Thread.sleep(30); // 每100毫秒刷新一次
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    
        ElementManager elementManager = ElementManager.GetManager();
        for(List<ElementObj> list:elementManager.getGameElements().values()){
            for(ElementObj obj:list){
                obj.DrawHp(g);
            }
        }
    }
}   