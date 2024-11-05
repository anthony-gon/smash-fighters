package Screens;

import Engine.*;
import SpriteFont.SpriteFont;

import java.awt.*;

// This is the class for the winner/lose screen
public class LevelLoseScreen extends Screen {
    protected SpriteFont winMessage;
    protected SpriteFont instructions;
    protected KeyLocker keyLocker = new KeyLocker();
    protected PlayLevelScreen playLevelScreen;
    protected boolean isPlayer1Winner; // Track if Player 1 is the winner

    // Constructor that accepts PlayLevelScreen and who the winner is
    public LevelLoseScreen(PlayLevelScreen playLevelScreen, boolean isPlayer1Winner) {
        this.playLevelScreen = playLevelScreen;
        this.isPlayer1Winner = isPlayer1Winner; // Assign the winner flag

        // Debug statement to confirm the received value of isPlayer1Winner
        System.out.println("LevelLoseScreen initialized with isPlayer1Winner: " + isPlayer1Winner);
        
        initialize(); // Initialize the screen with winner details
    }

    @Override
    public void initialize() {
        // Display a different message based on who won
        if (isPlayer1Winner) {
            winMessage = new SpriteFont("Player 1 Wins!", 300, 200, "fibberish", 30, Color.decode("#FFD700"));
        } else {
            winMessage = new SpriteFont("Player 2 Wins!", 300, 200, "fibberish", 30, Color.decode("#FFD700"));
        }
        
        instructions = new SpriteFont("Press Space to play again or Escape to return to menu", 
                                      80, 500, "Arial", 10, Color.white);
        instructions.setOutlineColor(Color.black);
        instructions.setOutlineThickness(2);

        keyLocker.lockKey(Key.SPACE); // Lock keys initially
        keyLocker.lockKey(Key.ESC);
    }

    @Override
    public void update() {
        // Unlock keys when they are released
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // If space is pressed, reset the level
        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)) {
            playLevelScreen.resetLevel(); // Reset the level
        }
        // If escape is pressed, go back to the main menu
        else if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            playLevelScreen.goBackToMenu(); // Go back to the main menu
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Fill the background with black color
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);
        
        // Draw the win message and instructions
        winMessage.draw(graphicsHandler);
        instructions.draw(graphicsHandler);
    }
}
