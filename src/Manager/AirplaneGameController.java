package Manager;

import Element.*;
import java.util.List;
import java.util.Random;

public class AirplaneGameController {
    private static ElementManager elementManager = ElementManager.GetManager();
    private static Random random = new Random();
    private static long lastEnemySpawn = 0;
    private static long lastBossSpawn = 0;
    private static long lastPropSpawn = 0;
    private static boolean gameOver = false;

    // Spawn intervals in milliseconds (will be adjusted by difficulty)
    private static long ENEMY_SPAWN_INTERVAL = 200;
    private static long BOSS_SPAWN_INTERVAL = 1000;
    private static long PROP_SPAWN_INTERVAL = 500;

    public static void initializeGame() {
        // Add scrolling background
        createScrollingBackground();

        // Create player airplane
        createPlayer();
    }

    private static void createScrollingBackground() {
        // Create two background objects for seamless scrolling
        ElementObj bg1 = new BackgroundObj().CreatElement("1,0,0");
        ElementObj bg2 = new BackgroundObj().CreatElement("1,0,-600");

        elementManager.AddElement(bg1, GameElements.MAPS);
        elementManager.AddElement(bg2, GameElements.MAPS);
        
        // Background objects created successfully
    }

    private static void createPlayer() {
        String playerData = "x:400,y:500,direction:up,hp:5";
        ElementObj player = new Play().CreatElement(playerData);
        elementManager.AddElement(player, GameElements.PLAY);
    }

    public static void updateGame(long gameTime) {
        // Check if player is still alive
        checkGameOver();
        
        // Only spawn new enemies/bosses/props if game is not over
        if (!gameOver) {
            spawnEnemies(gameTime);
            spawnBosses(gameTime);
            spawnProps(gameTime);
        }
        
        checkCollisions();
    }
    
    public static boolean isGameOver() {
        return gameOver;
    }
    
    public static void resetGame() {
        gameOver = false;
        lastEnemySpawn = 0;
        lastBossSpawn = 0;
        lastPropSpawn = 0;
        LevelManager.reset();
        // Reset difficulty
        ENEMY_SPAWN_INTERVAL = 200;
        BOSS_SPAWN_INTERVAL = 1000;
        PROP_SPAWN_INTERVAL = 500;
    }
    
    public static void adjustDifficulty(int level) {
        // Increase difficulty with each level
        ENEMY_SPAWN_INTERVAL = Math.max(50, 200 - (level * 30));
        BOSS_SPAWN_INTERVAL = Math.max(300, 1000 - (level * 100));
        PROP_SPAWN_INTERVAL = Math.max(200, 500 - (level * 50));
    }
    
    private static void checkGameOver() {
        List<ElementObj> players = elementManager.GetElementsByKey(GameElements.PLAY);
        boolean playerAlive = false;
        
        for (ElementObj player : players) {
            if (player.isAlive() && player.getHp() > 0) {
                playerAlive = true;
                break;
            }
        }
        
        if (!playerAlive && !players.isEmpty()) {
            gameOver = true;
        }
    }

    private static void spawnEnemies(long gameTime) {
        if (gameTime - lastEnemySpawn >= ENEMY_SPAWN_INTERVAL) {
            int x = random.nextInt(750); // Random X position
            String enemyData = "x:" + x + ",y:-50,direction:" + (random.nextBoolean() ? "left" : "right") + ",hp:1";
            ElementObj enemy = new Enemy().CreatElement(enemyData);
            elementManager.AddElement(enemy, GameElements.ENEMY);
            lastEnemySpawn = gameTime;
        }
    }

    private static void spawnBosses(long gameTime) {
        if (gameTime - lastBossSpawn >= BOSS_SPAWN_INTERVAL) {
            int x = random.nextInt(700); // Random X position
            String bossData = "x:" + x + ",y:-80,direction:" + (random.nextBoolean() ? "left" : "right") + ",hp:10";
            ElementObj boss = new Enemy().CreatElement(bossData); // Using Enemy class for now
            elementManager.AddElement(boss, GameElements.BOSS);
            lastBossSpawn = gameTime;
        }
    }

    private static void spawnProps(long gameTime) {
        if (gameTime - lastPropSpawn >= PROP_SPAWN_INTERVAL) {
            if (random.nextDouble() < 0.3) { // 30% chance to spawn a prop
                int x = random.nextInt(750);
                String[] propTypes = {"POWER"};
                String propType = propTypes[random.nextInt(propTypes.length)];
                String propData = propType + "," + x + ",-30";
                ElementObj prop = new PropObj().CreatElement(propData);
                elementManager.AddElement(prop, GameElements.DROP); // Using MAPS for props temporarily
            }
            lastPropSpawn = gameTime;
        }
    }

    private static void checkCollisions() {
        List<ElementObj> bullets = elementManager.GetElementsByKey(GameElements.BULLET);
        List<ElementObj> enemies = elementManager.GetElementsByKey(GameElements.ENEMY);
        List<ElementObj> bosses = elementManager.GetElementsByKey(GameElements.BOSS);
        List<ElementObj> players = elementManager.GetElementsByKey(GameElements.PLAY);

        // Check each bullet against potential targets
        for (int i = bullets.size() - 1; i >= 0; i--) {
            ElementObj bullet = bullets.get(i);
            if (!bullet.isAlive()) continue;

            String bulletFrom = bullet.getFrom();
            
            // Player bullets hitting enemies and bosses
            if ("Element.Play".equals(bulletFrom)) {
                // Check against enemies
                for (int j = enemies.size() - 1; j >= 0; j--) {
                    ElementObj enemy = enemies.get(j);
                    if (enemy.isAlive() && bullet.Collide(enemy)) {
                        enemy.GetHit(bullet.getDamage());
                        bullet.setAlive(false);
                        // Check if enemy died
                        if (enemy.getHp() <= 0) {
                            LevelManager.enemyKilled();
                        }
                        break;
                    }
                }
                
                // Check against bosses
                if (bullet.isAlive()) {
                    for (int j = bosses.size() - 1; j >= 0; j--) {
                        ElementObj boss = bosses.get(j);
                        if (boss.isAlive() && bullet.Collide(boss)) {
                            boss.GetHit(bullet.getDamage());
                            bullet.setAlive(false);
                            // Check if boss died (worth 3 enemy kills)
                            if (boss.getHp() <= 0) {
                                LevelManager.enemyKilled();
                                LevelManager.enemyKilled();
                                LevelManager.enemyKilled();
                            }
                            break;
                        }
                    }
                }
            }
            
            // Enemy bullets hitting player
            else if ("Element.Enemy".equals(bulletFrom)) {
                for (int j = players.size() - 1; j >= 0; j--) {
                    ElementObj player = players.get(j);
                    if (player.isAlive() && bullet.Collide(player)) {
                        player.GetHit(bullet.getDamage());
                        bullet.setAlive(false);
                        // Player hit by enemy bullet
                        break;
                    }
                }
            }
        }
    }
}
