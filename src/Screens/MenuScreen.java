package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;
import Players.Knight;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MenuScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMenuItemHovered = 0; // Current menu item being "hovered" over
    protected int menuItemSelected = -1;
    protected SpriteFont playGame;
    protected SpriteFont credits;
    protected SpriteFont practiceRange; // New sprite font for Practice Range
    protected SpriteFont howToPlay; // New sprite font for How To Play
    protected SpriteFont gameTitle; // Title of the game "Smash Fighters"
    protected List<SpriteFont> menuItems; // List to hold all menu items
    protected Map background;
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
    protected int titleSizeChangeDelay = 0; // Delay counter for size change
    protected int titleSizeChangeDelayMax = 5; // Max delay before size change

    // Sound properties for background music
    protected Clip musicClip;

    public MenuScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Initialize the menu items
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

        // Add items to the menuItems list in the correct order
        menuItems = Arrays.asList(playGame, practiceRange, howToPlay, credits);

        // Initialize the game title "Smash Fighters" in the top right corner
        gameTitle = new SpriteFont("SMASH FIGHTERS", 100, 50, "fibberish", titleSize, new Color(255, 69, 0));
        gameTitle.setOutlineColor(Color.black);
        gameTitle.setOutlineThickness(5);

        background = new TitleScreenMap();
        background.setAdjustCamera(false);
        keyPressTimer = 0;
        menuItemSelected = -1;
        keyLocker.lockKey(Key.SPACE);

        // Load and play background music using the direct path
        playBackgroundMusic();
    }

    public void update() {
        // Update background map (to play tile animations)
        background.update(null);
        // Change menu item "hovered" over
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

        // Loop the selection back around
        if (currentMenuItemHovered >= menuItems.size()) {
            currentMenuItemHovered = 0;
        } else if (currentMenuItemHovered < 0) {
            currentMenuItemHovered = menuItems.size() - 1;
        }

        // Update pointer location
        updatePointerLocation();

        // Handle selection
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            menuItemSelected = currentMenuItemHovered;
            stopBackgroundMusic(); // Stop the music before transitioning
            if (menuItemSelected == 0) {
                screenCoordinator.setGameState(GameState.MAP_SELECT);
            } else if (menuItemSelected == 1) { // If Practice Range is selected
                screenCoordinator.setGameState(GameState.PRACTICE_RANGE);
            } else if (menuItemSelected == 2) { // If How to Play is selected
                screenCoordinator.setGameState(GameState.HOW_TO_PLAY);
            } else if (menuItemSelected == 3) { // If Credits is selected
                screenCoordinator.setGameState(GameState.CREDITS);
            }
        }

        // Update the size of the title to oscillate between min and max sizes
        updateTitleSize();
    }

    public void updatePointerLocation() {
        // Loop over menuItems to set their colors and determine pointer location
        for (int i = 0; i < menuItems.size(); i++) {
            if (i == currentMenuItemHovered) {
                menuItems.get(i).setColor(new Color(255, 215, 0)); // Highlighted color
                // Set pointer location based on the current hovered item
                pointerLocationX = (int) (menuItems.get(i).getX() - pointerOffsetX);
                pointerLocationY = (int) (menuItems.get(i).getY() - pointerOffsetY);
            } else {
                menuItems.get(i).setColor(new Color(49, 207, 240)); // Default color
            }
        }
    }

    // Update title size to make it oscillate back and forth
    public void updateTitleSize() {
        // Increment the delay counter
        if (titleSizeChangeDelay >= titleSizeChangeDelayMax) {
            // Change the size of the title
            titleSize += titleSizeChange;

            // Check bounds and reverse direction if needed
            if (titleSize >= maxTitleSize || titleSize <= minTitleSize) {
                titleSizeChange *= -1;
            }

            // Update the title font size
            gameTitle.setFontSize(titleSize);

            // Reset the delay counter
            titleSizeChangeDelay = 0;
        } else {
            // Increment the delay counter to slow down size change
            titleSizeChangeDelay++;
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        // Draw the title
        gameTitle.draw(graphicsHandler);
        // Draw each menu item
        for (SpriteFont menuItem : menuItems) {
            menuItem.draw(graphicsHandler);
        }
        // Draw the pointer
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20,
                new Color(49, 207, 240), Color.black, 2);
    }

    // Method to load and play background music
    private void playBackgroundMusic() {
        try {
            // Use a direct file path for testing
            String filePath = "Resources/MenuMusic.wav"; // Replace with the correct relative or absolute path
            File soundFile = new File(filePath);

            if (!soundFile.exists()) {
                System.err.println("Sound file not found at path: " + soundFile.getAbsolutePath());
                return;
            }

            // Load audio input stream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);

            // Optionally set the volume to a desired level
            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volumeControl.getMaximum()); // Max volume

            // Play music in a loop
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            System.out.println("Music clip started? " + musicClip.isRunning());
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
}
