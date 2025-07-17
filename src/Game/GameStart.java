package Game;

import Controller.GameListener;
import Controller.GameThread;
import Show.GameJFrame;
import Show.GameMainPanel;

//唯一入口
public class GameStart {
    public static void main(String[] args) {
        GameJFrame gameFrame = new GameJFrame();
        //实例化面板
        GameMainPanel gameMainPanel = new GameMainPanel();
        //实例化监听
        GameListener gameListener = new GameListener();
        //实例化主进程
        GameThread gameThread = new GameThread();
        //注入
        gameFrame.setJpanel(gameMainPanel);
        gameFrame.setKeyListener(gameListener);
        gameFrame.setThread(gameThread);
        gameFrame.Start();

        Enum en = null;
    }
}
