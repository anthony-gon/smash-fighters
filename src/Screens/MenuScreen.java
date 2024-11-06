package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0;
    protected int menuItemSelected = -1;
    protected SpriteFont playGame;
    protected SpriteFont credits;
    protected SpriteFont practiceRange;
    protected SpriteFont howToPlay;
    protected SpriteFont gameTitle;
    protected List<SpriteFont> menuItems;
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected int pointerOffsetX = 30;
    protected int pointerOffsetY = 7;
    protected KeyLocker keyLocker = new KeyLocker();

    // Variables for oscillating the title size
    protected int titleSize = 40;
    protected int titleSizeChange = 1;
    protected int minTitleSize = 35;
    protected int maxTitleSize = 50;
    protected int titleSizeChangeDelay = 0;
    protected int titleSizeChangeDelayMax = 5;

    // Sound properties for background music
    protected Clip musicClip;

    // Lightning bolt effect variables
    private long lastLightningStrikeTime;
    private boolean lightningVisible = false;
    private static final int LIGHTNING_INTERVAL = 1000; // 1 second for frequent strikes
    private static final int LIGHTNING_DURATION = 300; // 0.3 seconds for visible duration
    private Random random = new Random();

    // Variables to store random lightning path
    private int lightningStartX;
    private int lightningEndX;
    private int[] lightningPathX;
    private int[] lightningPathY;

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        playGame = new SpriteFont("PLAY GAME", 300, 150, "fibberish", 30, new Color(49, 207, 240));
        playGame.setOutlineColor(Color.black);
        playGame.setOutlineThickness(3);

        practiceRange = new SpriteFont("PRACTICE RANGE", 300, 250, "fibberish", 30, new Color(49, 207, 240));
        practiceRange.setOutlineColor(Color.black);
        practiceRange.setOutlineThickness(3);

        howToPlay = new SpriteFont("HOW TO PLAY", 300, 350, "fibberish", 30, new Color(49, 207, 240));
        howToPlay.setOutlineColor(Color.black);
        howToPlay.setOutlineThickness(3);

        credits = new SpriteFont("CREDITS", 680, 540, "fibberish", 30, new Color(49, 207, 240));
        credits.setOutlineColor(Color.black);
        credits.setOutlineThickness(3);

        menuItems = Arrays.asList(playGame, practiceRange, howToPlay, credits);

        gameTitle = new SpriteFont("SMASH FIGHTERS", 100, 50, "fibberish", titleSize, new Color(255, 0, 0));
        gameTitle.setOutlineColor(Color.black);
        gameTitle.setOutlineThickness(5);

        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);

        lastLightningStrikeTime = System.currentTimeMillis();
        playBackgroundMusic();
    }

    public void update() {
        if (Keyboard.isKeyDown(Key.DOWN) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered++;
        } else if (Keyboard.isKeyDown(Key.UP) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMenuItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        if (currentMenuItemHovered >= menuItems.size()) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = menuItems.size() - 1;
        }

        updatePointerLocation();

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            stopBackgroundMusic();
            if (menuItemSelected == 0) {
                screenCoordinator.setGameState(GameState.MAP_SELECT);
            } else if (menuItemSelected == 1) {
                screenCoordinator.setGameState(GameState.PRACTICE_RANGE);
            } else if (menuItemSelected == 2) {
                screenCoordinator.setGameState(GameState.HOW_TO_PLAY);
            } else if (menuItemSelected == 3) {
                screenCoordinator.setGameState(GameState.CREDITS);
            }
        }

        updateTitleSize();
        updateLightningEffect();
    }

    public void updatePointerLocation() {
        for (int i = 0; i < menuItems.size(); i++) {
            if (i == currentMenuItemHovered) {
                menuItems.get(i).setColor(new Color(255, 215, 0));
                pointerLocationX = (int) (menuItems.get(i).getX() - pointerOffsetX);
                pointerLocationY = (int) (menuItems.get(i).getY() - pointerOffsetY);
            } else {
                menuItems.get(i).setColor(new Color(49, 207, 240));
            }
        }
    }

    public void updateTitleSize() {
        if (titleSizeChangeDelay >= titleSizeChangeDelayMax) {
            titleSize += titleSizeChange;
            if (titleSize >= maxTitleSize || titleSize <= minTitleSize) {
                titleSizeChange *= -1;
            }
            gameTitle.setFontSize(titleSize);
            titleSizeChangeDelay = 0;
        } else {
            titleSizeChangeDelay++;
        }
    }

    private void updateLightningEffect() {
        long currentTime = System.currentTimeMillis();

        // Check if it's time for a lightning strike
        if (currentTime - lastLightningStrikeTime >= LIGHTNING_INTERVAL) {
            generateRandomLightningPath();
            lightningVisible = true;
            lastLightningStrikeTime = currentTime;
        }

        // Hide lightning after a short duration
        if (lightningVisible && currentTime - lastLightningStrikeTime >= LIGHTNING_DURATION) {
            lightningVisible = false;
        }
    }

    private void generateRandomLightningPath() {
        int screenWidth = ScreenManager.getScreenWidth();
        int screenHeight = ScreenManager.getScreenHeight();

        // Randomize starting and ending X positions within screen bounds
        lightningStartX = random.nextInt(screenWidth / 2) + screenWidth / 4; // Start roughly in the middle
        lightningEndX = random.nextInt(screenWidth / 2) + screenWidth / 4;

        // Generate a random jagged path between the start and end points
        int segments = 5 + random.nextInt(3); // Randomize number of segments
        lightningPathX = new int[segments + 2];
        lightningPathY = new int[segments + 2];

        lightningPathX[0] = lightningStartX;
        lightningPathY[0] = 0; // Start from the top

        for (int i = 1; i <= segments; i++) {
            lightningPathX[i] = lightningPathX[i - 1] + random.nextInt(100) - 50; // Randomize horizontal displacement
            lightningPathY[i] = lightningPathY[i - 1] + screenHeight / (segments + 1); // Spread evenly vertically
        }

        lightningPathX[segments + 1] = lightningEndX;
        lightningPathY[segments + 1] = screenHeight; // End at the bottom
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        
        gameTitle.draw(graphicsHandler);
        for (SpriteFont menuItem : menuItems) {
            menuItem.draw(graphicsHandler);
        }
        
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20,
                new Color(49, 207, 240), Color.black, 2);

        // Draw lightning effect if visible
        if (lightningVisible) {
            drawLightningBolt(graphicsHandler);
        }
    }

    private void drawLightningBolt(GraphicsHandler graphicsHandler) {
        Graphics2D g2d = (Graphics2D) graphicsHandler.getGraphics();

        g2d.setColor(Color.WHITE);
        g2d.setStroke(new BasicStroke(6)); // Thicker stroke for bigger lightning

        // Draw the lightning path
        for (int i = 0; i < lightningPathX.length - 1; i++) {
            g2d.drawLine(lightningPathX[i], lightningPathY[i], lightningPathX[i + 1], lightningPathY[i + 1]);
        }
    }

    private void playBackgroundMusic() {
        try {
            String filePath = "Resources/MenuMusic.wav";
            File soundFile = new File(filePath);

            if (!soundFile.exists()) {
                System.err.println("Sound file not found at path: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);

            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volumeControl.getMaximum());

            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            System.err.println("Error loading or playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void stopBackgroundMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }
}
