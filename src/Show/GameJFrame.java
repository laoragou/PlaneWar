package Show;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

//游戏窗体
public class GameJFrame extends JFrame{
    public static int GAMEX = 800;
    public static int GAMEY = 600;
    private  JPanel jpanel = null;//正在显示的面板
    private KeyListener keyListener = null; //键盘监听器
    private MouseMotionListener mouseMotionListener = null; //鼠标移动监听器
    private MouseListener mouseListener = null; //鼠标点击监听器
    private Thread thread = null; //游戏主线程

    public GameJFrame(){ 
        init();
    }
    public void init(){
        this.setTitle("Java");
        this.setSize(GAMEX, GAMEY);  
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void Start(){
        if(jpanel != null){
            this.add(jpanel);
        }
        if(keyListener != null){
            this.addKeyListener(keyListener);
        }
        if(mouseMotionListener != null){
            this.addMouseMotionListener(mouseMotionListener);
        }
        if(mouseListener != null){
            this.addMouseListener(mouseListener);
        }
        if(thread != null){
            thread.start();// 启动游戏主线程
        }
        setVisible(true); 
        if(this.jpanel instanceof Runnable) {
            // 如果jpanel实现了Runnable接口，则启动线程
            new Thread((Runnable)this.jpanel).start();
        }
    }

    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }
    public void setMouseMotionListener(MouseMotionListener mouseMotionListener) {
        this.mouseMotionListener = mouseMotionListener;
    }
    public void setMouseListener(MouseListener mouseListener) {
        this.mouseListener = mouseListener;
    }
    public void setThread(Thread thread) {
        this.thread = thread;
    }
    public void setJpanel(JPanel jpanel) {
        this.jpanel = jpanel;
    }
    
}
