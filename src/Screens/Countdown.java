package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Engine.ScreenManager;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;
import java.awt.*;

public class Countdown extends Screen {
    private ScreenCoordinator screenCoordinator;
    private int countdownNumber = 3; // Start countdown at 3
    private long lastUpdateTime; // To track the last time the number changed
    private static final int COUNTDOWN_INTERVAL = 1000; // 1 second interval for each countdown number
    private SpriteFont countdownDisplay; // Display for the countdown number

    public Countdown(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Initialize the countdown display font in the center of the screen
        countdownDisplay = new SpriteFont(String.valueOf(countdownNumber), ScreenManager.getScreenWidth() / 2 - 30,
                ScreenManager.getScreenHeight() / 2, "Arial", 72, Color.WHITE);
        countdownDisplay.setOutlineColor(Color.BLACK);
        countdownDisplay.setOutlineThickness(3);

        // Set initial update time
        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();
        
        // Check if it's time to update the countdown number
        if (currentTime - lastUpdateTime >= COUNTDOWN_INTERVAL) {
            countdownNumber--; // Decrease the countdown number
            lastUpdateTime = currentTime; // Reset the update time
            
            // Update the displayed countdown number
            if (countdownNumber > 0) {
                countdownDisplay.setText(String.valueOf(countdownNumber));
            } else {
                // When countdown reaches 0, transition to the PlayLevelScreen
                screenCoordinator.setGameState(GameState.LEVEL);
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Fill the screen with a black background
        graphicsHandler.drawFilledRectangle(0, 0, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);

        // Draw the countdown number in the center of the screen
        countdownDisplay.draw(graphicsHandler);
    }
}
