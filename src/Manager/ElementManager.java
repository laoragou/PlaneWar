/*
* 单例模式：内存中有且仅有一个实例
* 饿汉模式：
*      启动时自动加载实例
* 饱汉模式：
*      使用时才加载实例
* 
* 编写方式
* 1.静态属性定义一个常量（单例的应用）
* 2.提供一个方法返回实例
* 3.防止其他人自己使用，私有化构造方法
*/

package Manager;

import Element.ElementObj;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//元素管理器（存储元素，提供方法给视图和控制调用）
    /*
        使用Map快速查找对应的list
        使用enum充当Map的key
        list中存储的是基类
    */
public class ElementManager {
      private Map<GameElements, List<ElementObj>> gameElements;

      public Map<GameElements, List<ElementObj>> getGameElements() {
         return gameElements;
      }
      //添加元素（多半由加载器调用）
      public void AddElement(ElementObj obj, GameElements gameelement){
      List<ElementObj> list = gameElements.get(gameelement);
      list.add(obj);//添加到对应的list中
      }
      //取出某一类元素
      public List<ElementObj> GetElementsByKey(GameElements gameelement){
         return gameElements.get(gameelement);
      }

      private static ElementManager EM = null;
      //synchronized 保证方法中只有一个线程
      public static synchronized ElementManager GetManager(){
         if(EM == null){
         EM = new ElementManager();
         }
         return EM;
      }
      //私有化构造方法
      private ElementManager(){
         init();
      }
     //方便重写
      public void init(){
         gameElements = new HashMap<GameElements, List<ElementObj>>();
         //每种元素放到map中
         for(GameElements ele:GameElements.values()){
            gameElements.put(ele, new ArrayList<ElementObj>());
         }
         //道具、子弹、爆炸……
      }
}
