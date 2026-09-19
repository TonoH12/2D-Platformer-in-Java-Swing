import java.awt.Graphics;

public class Game implements Runnable {
    private Panel gamePanel;
    private Frame gameFrame;
    private Thread gameThread;
    private final int FPS = 60;
    private final int UPS = 60;
    
    private Player player;
    private LevelManager levelManager;

    private float xlevelOffset;
    private int leftBorder = (int)(0.4 * GAME_WIDTH);
    private int rightBorder = (int)(0.6 * GAME_WIDTH);
    private int levelTilesWide = GetAssets.getLevelData()[0].length;
    private int maxLevelOffsetX = (levelTilesWide * TILES_SIZE) - GAME_WIDTH;

    private float ylevelOffset;
    private int topBorder = (int)(0.4 * GAME_HEIGHT);
    private int bottomBorder = (int)(0.6 * GAME_HEIGHT);
    private int levelTilesHigh = GetAssets.getLevelData().length;
    private int maxLevelOffsetY = (levelTilesHigh * TILES_SIZE) - GAME_HEIGHT;

    public static final int TILES_DEFAULT_SIZE = 16;
    public static final double SCALE = 3;
    public static final int TILES_IN_WIDTH = 30;
    public static final int TILES_IN_HEIGHT = 17;
    public static final int TILES_SIZE = (int)(TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;

    public static double CAMERAX = 0;
    public static double CAMERAY = 0;
    
    public Game() {
        initClasses();

        gamePanel = new Panel(this);    
        gameFrame = new Frame(gamePanel);

        gamePanel.setFocusable(true);
        gamePanel.requestFocus();

        startGame();
    }

    public void initClasses() {
        levelManager = new LevelManager(this);
        player = new Player(200, 200, (int)(16 * SCALE), (int)(16 * SCALE));
        player.loadLevelData(levelManager.getLevelData());
        player.loadSpikes(levelManager.getSpikes());
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        player.update(xlevelOffset, ylevelOffset);
        checkCloseToBorderX();
        checkCloseToBorderY();
        levelManager.update();
    }

    public void render(Graphics g) {
        levelManager.draw(g, xlevelOffset, ylevelOffset);
        player.render(g, xlevelOffset, ylevelOffset);
    }

    public void checkCloseToBorderX() {
        float playerX = (int)(player.getHitbox().x);
        float diff = playerX - xlevelOffset;

        if (diff > rightBorder) {
            xlevelOffset += diff - rightBorder;
        }

        if (diff < leftBorder) {
            xlevelOffset += diff - leftBorder;
        }

        if (xlevelOffset > maxLevelOffsetX) {
            xlevelOffset = maxLevelOffsetX;
        } else if(xlevelOffset < 0) {
            xlevelOffset = 0;
        }
        CAMERAX = xlevelOffset;
    }

    public void checkCloseToBorderY() {
        float playerY = (int)(player.getHitbox().y);
        float diff = playerY - ylevelOffset;

        if (diff > bottomBorder) {
            ylevelOffset += diff - bottomBorder;
        }

        if (diff < topBorder) {
            ylevelOffset += diff - topBorder;
        }

        if (ylevelOffset > maxLevelOffsetY) {
            ylevelOffset = maxLevelOffsetY;
        } else if(ylevelOffset < 0) {
            ylevelOffset = 0;
        }
        CAMERAY = ylevelOffset;
    }

    public void run() {
        double frameLength = 1000000000.0/FPS;
        double updateLength = 1000000000.0/UPS;
        int frames = 0;
        long currentFrame = System.nanoTime();
        long check = System.currentTimeMillis();

        long previousTime = System.nanoTime();
        int updates = 0;
        double changeU = 0;
        double changeF = 0;
        
        while (true) {
            long currentTime = System.nanoTime();

            changeU += (currentTime - previousTime) / updateLength;
            changeF += (currentTime - previousTime) / frameLength;
            previousTime = currentTime;
            
            if (changeU >= 1) {
                update();
                updates++;
                changeU--;
            }

            if (changeF >= 1) {
                gamePanel.repaint();
                frames++;
                changeF--;
            }

            // Displays framerate and resets the frame counter to 0
            if (System.currentTimeMillis() - check >= 1000) {
                check = System.currentTimeMillis();
                frames = 0;
                updates = 0;
            }
        }
    }

    public void focusLost() {
        player.resetDirections();
    }

    public Player getPlayer() {
        return player;
    }
}