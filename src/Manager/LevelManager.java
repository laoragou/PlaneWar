package Manager;

import Element.*;
import java.util.Random;

public class LevelManager {
    private static int currentLevel = 1;
    private static int maxLevel = 5;
    private static int enemiesKilled = 0;
    private static int enemiesRequiredPerLevel = 10;
    private static Random random = new Random();
    
    public static int getCurrentLevel() {
        return currentLevel;
    }
    
    public static void enemyKilled() {
        enemiesKilled++;
        System.out.println("Enemies killed: " + enemiesKilled + "/" + enemiesRequiredPerLevel);
        
        if (enemiesKilled >= enemiesRequiredPerLevel && currentLevel < maxLevel) {
            nextLevel();
        }
    }
    
    private static void nextLevel() {
        currentLevel++;
        enemiesKilled = 0;
        System.out.println("LEVEL UP! Now at level " + currentLevel);
        
        // Increase difficulty
        increaseDifficulty();
        
        // Change background for new level
        changeBackground();
    }
    
    private static void increaseDifficulty() {
        // Reduce spawn intervals for higher levels
        AirplaneGameController.adjustDifficulty(currentLevel);
    }
    
    private static void changeBackground() {
        ElementManager elementManager = ElementManager.GetManager();
        
        // Clear existing backgrounds
        elementManager.getGameElements().get(GameElements.MAPS).removeIf(obj -> obj instanceof BackgroundObj);
        
        // Create new background based on current level
        String bgType = String.valueOf(currentLevel);
        ElementObj bg1 = new BackgroundObj().CreatElement(bgType + ",0,0");
        ElementObj bg2 = new BackgroundObj().CreatElement(bgType + ",0,-600");
        
        elementManager.AddElement(bg1, GameElements.MAPS);
        elementManager.AddElement(bg2, GameElements.MAPS);
    }
    
    public static void reset() {
        currentLevel = 1;
        enemiesKilled = 0;
    }
    
    public static boolean isMaxLevel() {
        return currentLevel >= maxLevel;
    }
    
    public static String getLevelInfo() {
        return "Level: " + currentLevel + " | Enemies: " + enemiesKilled + "/" + enemiesRequiredPerLevel;
    }
}