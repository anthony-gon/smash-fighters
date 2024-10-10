package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Level.Player;
import Level.Player2;
import Level.PlayerListener;

import Level.HealthBar;
import Maps.TestMap;
import Maps.ToadsMap;
import Maps.Map2; // Ensure you have Map2 class defined

import Players.Brawler;
import Players.Brawler2;
import Players.Knight;
import Players.Knight2;
import Players.Knight2;
import Players.Mage;
import Players.Mage2;
import SpriteFont.SpriteFont;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class PlayLevelScreen extends Screen implements PlayerListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;

    protected HealthBar healthBar;
    protected Player2 player2;
    protected PlayLevelScreenState playLevelScreenState;
    protected int screenTimer;
    protected LevelClearedScreen levelClearedScreen;
    protected LevelLoseScreen levelLoseScreen;
    protected boolean levelCompletedStateChangeStart;

    // Pause menu variables
    protected SpriteFont resumeOption;
    protected SpriteFont exitToMenuOption;
    protected List<SpriteFont> pauseMenuItems;
    protected int currentPauseMenuItemHovered = 0;
    protected int pausePointerLocationX, pausePointerLocationY;
    protected int pausePointerOffsetX = 20;
    protected int pausePointerOffsetY = 5;
    protected KeyLocker keyLocker = new KeyLocker();
    protected int previousHealth = -1;

    // Music-related properties
    protected Clip musicClip;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Get the selected map name from the ScreenCoordinator
        String selectedMapName = screenCoordinator.getSelectedMap();

        this.healthBar = new HealthBar(100, 0, 100, 100);
        healthBar.loadHealthBars();

        CharacterScreen.SelectedCharacter selectedCharacter = screenCoordinator.getCharacterScreen().getSelectedCharacter();


        // Initialize map based on selected map name
        if (selectedMapName.equals("ToadsMap")) {
            this.map = new ToadsMap();
        } else if (selectedMapName.equals("MAP 2")) {
            this.map = new Map2();
        }

        // Setup player and player2 based on the selected character
        if (selectedCharacter == CharacterScreen.SelectedCharacter.SWORDSMAN) {
            this.player = new Knight(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
            this.player2 = new Knight2(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        } else if (selectedCharacter == CharacterScreen.SelectedCharacter.BRAWLER) {
            this.player = new Brawler(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
            this.player2 = new Brawler2(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        } else if (selectedCharacter == CharacterScreen.SelectedCharacter.GUNNER) {
            this.player = new Mage(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
            this.player2 = new Mage2(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        }
        this.player2 = new Knight2(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.player.setMap(map);
        this.player2.setMap(map);
        this.player.addListener(this);
        this.player2.addListener(this);

        levelClearedScreen = new LevelClearedScreen();
        levelLoseScreen = new LevelLoseScreen(this);

        this.playLevelScreenState = PlayLevelScreenState.RUNNING;

        resumeOption = new SpriteFont("RESUME", 300, 200, "fibberish", 30, new Color(49, 207, 240));
        exitToMenuOption = new SpriteFont("EXIT TO MENU", 300, 250, "fibberish", 30, new Color(49, 207, 240));
        pauseMenuItems = Arrays.asList(resumeOption, exitToMenuOption);
        keyLocker.lockKey(Key.ESC);

        // Play the background music when the PlayLevelScreen starts
        playBackgroundMusic();
    }

    @Override
    public void update() {
        // Handle pause input with the ESCAPE key
        if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            if (playLevelScreenState == PlayLevelScreenState.RUNNING) {
                playLevelScreenState = PlayLevelScreenState.PAUSED;
                stopBackgroundMusic(); // Stop music when the game is paused
            } else if (playLevelScreenState == PlayLevelScreenState.PAUSED) {
                playLevelScreenState = PlayLevelScreenState.RUNNING;
                playBackgroundMusic(); // Resume music when unpaused
            }
            keyLocker.lockKey(Key.ESC);
        }

        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        switch (playLevelScreenState) {
            case RUNNING:
                player.update();
                map.update(player);
                player2.update();
                map.update2(player2);
                break;

            case LEVEL_COMPLETED:
                if (levelCompletedStateChangeStart) {
                    screenTimer = 130;
                    levelCompletedStateChangeStart = false;
                } else {
                    levelClearedScreen.update();
                    screenTimer--;
                    if (screenTimer == 0) {
                        goBackToMenu();
                    }
                }
                break;

            case LEVEL_LOSE:
                stopBackgroundMusic(); // Stop music when the player dies
                levelLoseScreen.update();
                break;

            case PAUSED:
                updatePauseMenu();
                break;
        }
    }

    private void updatePauseMenu() {
        if (Keyboard.isKeyDown(Key.DOWN) && !keyLocker.isKeyLocked(Key.DOWN)) {
            currentPauseMenuItemHovered++;
            if (currentPauseMenuItemHovered >= pauseMenuItems.size()) {
                currentPauseMenuItemHovered = 0;
            }
            keyLocker.lockKey(Key.DOWN);
        } else if (Keyboard.isKeyDown(Key.UP) && !keyLocker.isKeyLocked(Key.UP)) {
            currentPauseMenuItemHovered--;
            if (currentPauseMenuItemHovered < 0) {
                currentPauseMenuItemHovered = pauseMenuItems.size() - 1;
            }
            keyLocker.lockKey(Key.UP);
        }

        if (Keyboard.isKeyUp(Key.UP)) {
            keyLocker.unlockKey(Key.UP);
        }
        if (Keyboard.isKeyUp(Key.DOWN)) {
            keyLocker.unlockKey(Key.DOWN);
        }

        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)) {
            if (currentPauseMenuItemHovered == 0) {
                playLevelScreenState = PlayLevelScreenState.RUNNING;
                playBackgroundMusic(); // Resume music when unpaused
            } else if (currentPauseMenuItemHovered == 1) {
                goBackToMenu();
            }
            keyLocker.lockKey(Key.SPACE);
        }

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        pausePointerLocationX = (int) pauseMenuItems.get(currentPauseMenuItemHovered).getX() - pausePointerOffsetX;
        pausePointerLocationY = (int) pauseMenuItems.get(currentPauseMenuItemHovered).getY() - pausePointerOffsetY;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(graphicsHandler);
                player.draw(graphicsHandler);
    
                // Draw health bar
                healthBar.draw(graphicsHandler, player.getPlayerHealth());
    
                // Only log health when it changes
                if (player.getPlayerHealth() != previousHealth) {
                    System.out.println("Player health: " + player.getPlayerHealth());
                    previousHealth = player.getPlayerHealth();
                }
    
                player2.draw(graphicsHandler);
                break;
            case LEVEL_COMPLETED:
                levelClearedScreen.draw(graphicsHandler);
                break;
            case LEVEL_LOSE:
                levelLoseScreen.draw(graphicsHandler);
                break;
            case PAUSED:
                map.draw(graphicsHandler);
                player.draw(graphicsHandler);
                drawPauseMenu(graphicsHandler);
                break;
        }
    }
    
    private void drawPauseMenu(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangleWithBorder(250, 150, 300, 200, new Color(0, 0, 0, 150), Color.white, 3);

        for (SpriteFont menuItem : pauseMenuItems) {
            menuItem.draw(graphicsHandler);
        }

        // Draw pointer
        graphicsHandler.drawFilledRectangleWithBorder(pausePointerLocationX, pausePointerLocationY, 20, 20,
                new Color(49, 207, 240), Color.black, 2);
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    @Override
    public void onLevelCompleted() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_COMPLETED) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
            levelCompletedStateChangeStart = true;
        }
    }

    @Override
    public void onDeath() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_LOSE) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
            stopBackgroundMusic(); // Stop music when the player dies
        }
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        stopBackgroundMusic(); // Stop the music when going back to the menu
        screenCoordinator.setGameState(GameState.MENU);
    }

    // Method to load and play background music
    private void playBackgroundMusic() {
        try {
            String filePath = "Resources/Fighting.wav"; // Path to your background music
            File soundFile = new File(filePath);

            if (!soundFile.exists()) {
                System.err.println("Sound file not found at path: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);

            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volumeControl.getMaximum()); // Set to max volume
            musicClip.loop(Clip.LOOP_CONTINUOUSLY); // Play music in a loop
            musicClip.start();
        } catch (Exception e) {
            System.err.println("Error loading or playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Method to stop the background music
    private void stopBackgroundMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, LEVEL_LOSE, PAUSED
    }
}
