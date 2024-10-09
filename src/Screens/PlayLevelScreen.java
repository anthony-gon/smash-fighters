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

import Players.Knight;
import Players.Knight2;
import SpriteFont.SpriteFont;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This class is for when the platformer game is actually being played
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
    protected int currentPauseMenuItemHovered = 0; // Currently hovered pause option
    protected int pausePointerLocationX, pausePointerLocationY;
    protected int pausePointerOffsetX = 20;
    protected int pausePointerOffsetY = 5;
    protected KeyLocker keyLocker = new KeyLocker(); // Locker to handle input

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Get the selected map name from the ScreenCoordinator
        String selectedMapName = screenCoordinator.getSelectedMap();

        this.healthBar = new HealthBar(100, 0, 100, 100);
        healthBar.loadHealthBars();

        System.out.println("Initializing PlayLevelScreen with map: " + selectedMapName); // Debug


        // Initialize map based on selected map name
        if (selectedMapName.equals("ToadsMap")) {
            this.map = new ToadsMap(); // Assuming ToadsMap corresponds to "ToadsMap"
        } else if (selectedMapName.equals("MAP 2")) {
            this.map = new Map2(); // Replace with actual class for "MAP 2"
        }

        // Check if the map is successfully initialized
        if (map != null) {
            System.out.println("Map loaded successfully: " + map.getClass().getSimpleName());
            System.out.println("Player starting position: " + map.getPlayerStartPosition()); // Debug
        } else {
            System.err.println("Failed to load map: " + selectedMapName);
        }

        // Setup player
        this.player = new Knight(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.player2 = new Knight2(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.player.setMap(map);
        this.player2.setMap(map);
        this.player.addListener(this);

        levelClearedScreen = new LevelClearedScreen();
        levelLoseScreen = new LevelLoseScreen(this);

        this.playLevelScreenState = PlayLevelScreenState.RUNNING;

        // Initialize pause menu options
        resumeOption = new SpriteFont("RESUME", 300, 200, "fibberish", 30, new Color(49, 207, 240));
        exitToMenuOption = new SpriteFont("EXIT TO MENU", 300, 250, "fibberish", 30, new Color(49, 207, 240));
        pauseMenuItems = Arrays.asList(resumeOption, exitToMenuOption);

        // Lock the ESCAPE key initially to prevent immediate toggling
        keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        // Handle pause input with the ESCAPE key
        if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            if (playLevelScreenState == PlayLevelScreenState.RUNNING) {
                playLevelScreenState = PlayLevelScreenState.PAUSED;
            } else if (playLevelScreenState == PlayLevelScreenState.PAUSED) {
                playLevelScreenState = PlayLevelScreenState.RUNNING;
            }
            keyLocker.lockKey(Key.ESC); // Lock key to avoid repeated toggling
        }

        // Unlock ESCAPE key to detect next press
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // Based on screen state, perform specific actions
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
                levelLoseScreen.update();
                break;

            case PAUSED:
                updatePauseMenu();
                break;
        }
    }

    private void updatePauseMenu() {
        // Navigate through pause menu items
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

        // Unlock keys to allow smooth navigation
        if (Keyboard.isKeyUp(Key.UP)) {
            keyLocker.unlockKey(Key.UP);
        }
        if (Keyboard.isKeyUp(Key.DOWN)) {
            keyLocker.unlockKey(Key.DOWN);
        }

        // Handle selection
        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)) {
            if (currentPauseMenuItemHovered == 0) { // Resume selected
                playLevelScreenState = PlayLevelScreenState.RUNNING;
            } else if (currentPauseMenuItemHovered == 1) { // Exit to menu selected
                goBackToMenu();
            }
            keyLocker.lockKey(Key.SPACE); // Lock space key to prevent double activation
        }

        // Unlock the space key for new presses
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // Update pointer position based on hovered item
        pausePointerLocationX = (int) pauseMenuItems.get(currentPauseMenuItemHovered).getX() - pausePointerOffsetX;
        pausePointerLocationY = (int) pauseMenuItems.get(currentPauseMenuItemHovered).getY() - pausePointerOffsetY;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Based on screen state, draw appropriate graphics
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(graphicsHandler);
                player.draw(graphicsHandler);

                healthBar.draw(graphicsHandler, player.getPlayerHealth());
                System.out.println("player health: " + player.getPlayerHealth());

                player2.draw(graphicsHandler);

                break;
            case LEVEL_COMPLETED:
                levelClearedScreen.draw(graphicsHandler);
                break;
            case LEVEL_LOSE:
                levelLoseScreen.draw(graphicsHandler);
                break;
            case PAUSED:
                map.draw(graphicsHandler); // Optionally draw map behind the pause menu
                player.draw(graphicsHandler); // Optionally draw player behind the pause menu
                drawPauseMenu(graphicsHandler);
                break;
        }
    }

    private void drawPauseMenu(GraphicsHandler graphicsHandler) {
        // Draw a simple rectangle to represent the pause menu background
        graphicsHandler.drawFilledRectangleWithBorder(250, 150, 300, 200, new Color(0, 0, 0, 150), Color.white, 3);

        // Draw pause menu items
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
        }
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    // This enum represents the different states this screen can be in
    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, LEVEL_LOSE, PAUSED
    }
}
