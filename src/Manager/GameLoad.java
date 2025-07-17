package Manager;

import Element.ElementObj;
import Element.MapObj;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.swing.ImageIcon;

public class GameLoad {
    static ElementManager elementManager = ElementManager.GetManager();
    
    //加载器(工具类)
    //图片集合，使用map存储图片
    public static Map<String, ImageIcon> imgMap = new HashMap<>();

    public static Map<String, ImageIcon> imgMapEnemy = new HashMap<>();

    static {
        // Load airplane game images
        imgMap = new HashMap<>();
        imgMap.put("player", new ImageIcon("image/airplane/play/1.gif"));
        imgMap.put("explode", new ImageIcon("image/airplane/bang1.png"));
        
        imgMapEnemy = new HashMap<>();
        imgMapEnemy.put("enemy", new ImageIcon("image/airplane/enemy/1.png"));
        imgMapEnemy.put("boss", new ImageIcon("image/airplane/boss/1.png"));
    }

    //读取文件的类
    private static Properties pro = new Properties();
    //传入地图id，加载地图
    public static void MapLoad(int mapId){
        String mapName = "Text/" + mapId + ".map";  // 移除了"src/"前缀
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        InputStream maps = classLoader.getResourceAsStream(mapName);
        
        if (maps == null) {
            System.err.println("无法加载地图文件: " + mapName);
            return;
        }
        
        try {
            pro.clear(); 
            pro.load(maps);
            Enumeration<?> names = pro.propertyNames();
            while(names.hasMoreElements()){
                String key = names.nextElement().toString();
                String[] arrs = pro.getProperty(key).split(";");
                for(int i = 0; i < arrs.length; i++){
                    ElementObj ele = new MapObj().CreatElement(key + "," + arrs[i]);
                    elementManager.AddElement(ele, GameElements.MAPS);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (maps != null) {
                    maps.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //加载图片
    public static void LoadImg(){
        String playData = "Text/GameData.pro";
        String enePlayData = "Text/EneGameData.pro";
        ClassLoader classLoader = GameLoad.class.getClassLoader();
        
        // 加载玩家图片
        InputStream playDataStream = classLoader.getResourceAsStream(playData);
        pro.clear();
        try {
            pro.load(playDataStream);
            Set<Object> set = pro.keySet();
            for(Object o:set){
                String url = pro.getProperty(o.toString());
                imgMap.put(o.toString(), new ImageIcon(url));
                String invisibleUrl = url.replace("play1", "play1Invisible");
                imgMap.put(o.toString() + "V", new ImageIcon(invisibleUrl));
            }
            
            // 加载敌人图片
            InputStream enePlayDataStream = classLoader.getResourceAsStream(enePlayData);
            pro.clear();
            pro.load(enePlayDataStream);
            Set<Object> eneSet = pro.keySet();
            for(Object o:eneSet){
                String url = pro.getProperty(o.toString());
                imgMapEnemy.put(o.toString(), new ImageIcon(url));
                String invisibleUrl = url.replace("play2", "play2Invisible");
                imgMapEnemy.put(o.toString() + "V", new ImageIcon(invisibleUrl));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
