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
import Level.PlayerListener;
import Maps.PracticeRangeMap; // Ensure you have PracticeRangeMap class defined
import Players.Knight;
import SpriteFont.SpriteFont;
import java.awt.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// This class is for the practice range where players can move around
public class PracticeRangeScreen extends Screen implements PlayerListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;
    protected Player player;
    protected KeyLocker keyLocker = new KeyLocker(); // Locker to handle input
    protected PracticeRangeScreenState practiceRangeScreenState;

    // Pause menu variables
    protected SpriteFont resumeOption;
    protected SpriteFont exitToMenuOption;
    protected List<SpriteFont> pauseMenuItems;
    protected int currentPauseMenuItemHovered = 0; // Currently hovered pause option
    protected int pausePointerLocationX, pausePointerLocationY;
    protected int pausePointerOffsetX = 20;
    protected int pausePointerOffsetY = 5;

    public PracticeRangeScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        this.practiceRangeScreenState = PracticeRangeScreenState.RUNNING; // Start in running state
    }

    @Override
    public void initialize() {
        // Initialize the practice range map
        this.map = new PracticeRangeMap(); // Ensure this map exists
        System.out.println("Initializing PracticeRangeScreen with map: " + map.getClass().getSimpleName()); // Debug

        // Setup player
        this.player = new Knight(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        this.player.setMap(map);
        this.player.addListener(this);

        // Initialize pause menu options
        resumeOption = new SpriteFont("RESUME", 300, 200, "fibberish", 30, new Color(49, 207, 240));
        exitToMenuOption = new SpriteFont("EXIT TO MENU", 300, 250, "fibberish", 30, new Color(49, 207, 240));
        pauseMenuItems = Arrays.asList(resumeOption, exitToMenuOption);

        // Lock the ESCAPE key initially to prevent immediate toggling
        keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        // Handle escape key for pausing the game
        if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            if (practiceRangeScreenState == PracticeRangeScreenState.RUNNING) {
                practiceRangeScreenState = PracticeRangeScreenState.PAUSED;
            } else if (practiceRangeScreenState == PracticeRangeScreenState.PAUSED) {
                practiceRangeScreenState = PracticeRangeScreenState.RUNNING;
            }
            keyLocker.lockKey(Key.ESC); // Lock key to avoid repeated toggling
        }

        // Unlock ESCAPE key to detect next press
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // Based on screen state, perform specific actions
        switch (practiceRangeScreenState) {
            case RUNNING:
                // Handle player movement inputs
                player.update(); // Allow the player to move freely in the practice range

                // Update the map
                map.update(player);
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
                practiceRangeScreenState = PracticeRangeScreenState.RUNNING;
            } else if (currentPauseMenuItemHovered == 1) { // Exit to menu selected
                goBackToMenu();
            }
            keyLocker.lockKey(Key.SPACE); // Lock space key to prevent double activation
        }

        // Unlock the space key for new presses
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Draw the map and player
        map.draw(graphicsHandler);
        player.draw(graphicsHandler);

        // Draw pause menu if paused
        if (practiceRangeScreenState == PracticeRangeScreenState.PAUSED) {
            drawPauseMenu(graphicsHandler);
        }
    }

    private void drawPauseMenu(GraphicsHandler graphicsHandler) {
        // Draw a simple rectangle to represent the pause menu background
        graphicsHandler.drawFilledRectangleWithBorder(250, 150, 300, 200, new Color(0, 0, 0, 150), Color.white, 3);

        // Draw pause menu items
        for (SpriteFont menuItem : pauseMenuItems) {
            menuItem.draw(graphicsHandler);
        }

        // Update pointer position based on hovered item
        pausePointerLocationX = (int) pauseMenuItems.get(currentPauseMenuItemHovered).getX() - pausePointerOffsetX;
        pausePointerLocationY = (int) pauseMenuItems.get(currentPauseMenuItemHovered).getY() - pausePointerOffsetY;

        // Draw pointer
        graphicsHandler.drawFilledRectangleWithBorder(pausePointerLocationX, pausePointerLocationY, 20, 20,
                new Color(49, 207, 240), Color.black, 2);
    }

    @Override
    public void onDeath() {
    }

    public void goBackToMenu() {
        screenCoordinator.setGameState(GameState.MENU);
    }

    private enum PracticeRangeScreenState {
        RUNNING, PAUSED
    }
}
