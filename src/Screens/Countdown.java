package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Engine.ScreenManager;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;
import java.awt.*;
import java.util.Random;

public class Countdown extends Screen {
    private ScreenCoordinator screenCoordinator;
    private int countdownNumber = 3; // Start countdown at 3
    private long lastUpdateTime; // To track the last time the number changed
    private static final int COUNTDOWN_INTERVAL = 1000; // 1 second interval for each countdown number
    private SpriteFont countdownDisplay; // Display for the countdown number
    private boolean showFightText = false; // Flag to show "FIGHT!" text
    private float opacity = 0.0f; // Opacity for fade-in effect
    private Random random = new Random(); // Random generator for shake effect
    private int shakeIntensity = 100; // Intensity of shake
    private int shakeDuration = 1500; // Duration of shake effect in milliseconds
    private long fightTextStartTime;

    public Countdown(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Initialize the countdown display font in the center of the screen
        countdownDisplay = new SpriteFont(String.valueOf(countdownNumber), ScreenManager.getScreenWidth() / 2 - 30,
                ScreenManager.getScreenHeight() / 2, "fibberish", 72, Color.WHITE);
        countdownDisplay.setOutlineColor(Color.BLACK);
        countdownDisplay.setOutlineThickness(3);

        // Set initial update time
        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        long currentTime = System.currentTimeMillis();

        if (!showFightText) {
            // Fade in each number over FADE_DURATION
            if (opacity < 1.0f) {
                opacity += 0.05f; // Adjust the increment for fade-in speed
                if (opacity > 1.0f) opacity = 1.0f; // Cap at full opacity
            }

            // Check if it's time to move to the next number or "FIGHT!"
            if (currentTime - lastUpdateTime >= COUNTDOWN_INTERVAL) {
                countdownNumber--; // Decrease the countdown number
                lastUpdateTime = currentTime; // Reset the update time
                opacity = 0.0f; // Reset opacity for the next number

                if (countdownNumber > 0) {
                    // Update the displayed countdown number
                    countdownDisplay.setText(String.valueOf(countdownNumber));
                } else {
                    // When countdown reaches 0, show "FIGHT!" text
                    countdownDisplay.setText("FIGHT!");
                    showFightText = true;
                    opacity = 0.0f; // Reset opacity for "FIGHT!"
                    fightTextStartTime = currentTime; // Start timing for "FIGHT!"
                }
            }
        } else {
            // Fade in "FIGHT!" text and trigger the screen shake effect
            if (opacity < 1.0f) {
                opacity += 0.1f; // Speed up fade-in for "FIGHT!"
                if (opacity >= 1.0f) {
                    opacity = 1.0f;
                }
            }

            // Check if shake duration has passed; if so, transition to PlayLevelScreen
            if (currentTime - fightTextStartTime >= shakeDuration) {
                screenCoordinator.setGameState(GameState.LEVEL); // Immediate transition to PlayLevelScreen
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Calculate shake offset
        int shakeOffsetX = 5;
        int shakeOffsetY = 5;

        if (showFightText && opacity >= 1.0f) { // Shake only when "FIGHT!" is fully visible
            shakeOffsetX = random.nextInt(shakeIntensity * 2) - shakeIntensity; // Random offset between -shakeIntensity and +shakeIntensity
            shakeOffsetY = random.nextInt(shakeIntensity * 2) - shakeIntensity;
        }

        // Apply shake offset manually to all drawing positions

        // Draw background with shake offset
        graphicsHandler.drawFilledRectangle(0 + shakeOffsetX, 0 + shakeOffsetY, ScreenManager.getScreenWidth(), ScreenManager.getScreenHeight(), Color.black);

        // Set font color with current opacity for fade effect
        Color fontColor = new Color(1.0f, 1.0f, 1.0f, opacity); // White with variable opacity
        countdownDisplay.setColor(fontColor);

        // Temporarily adjust the display position for shake effect
        countdownDisplay.setX(countdownDisplay.getX() + shakeOffsetX);
        countdownDisplay.setY(countdownDisplay.getY() + shakeOffsetY);

        // Draw the countdown number or "FIGHT!" text in the center of the screen
        countdownDisplay.draw(graphicsHandler);

        // Reset position after drawing
        countdownDisplay.setX(countdownDisplay.getX() - shakeOffsetX);
        countdownDisplay.setY(countdownDisplay.getY() - shakeOffsetY);
    }
}
