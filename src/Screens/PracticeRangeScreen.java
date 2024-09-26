package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Engine.Key;
import Engine.Keyboard;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PracticeRangeScreen extends Screen {
    private ScreenCoordinator screenCoordinator; // Reference to the ScreenCoordinator
    private int width; // Will be set internally
    private int height; // Will be set internally

    // Platform properties
    private int platformX;
    private int platformY;
    private int platformWidth;
    private int platformHeight;

    // Dummy properties
    private int dummyX;
    private int dummyY;
    private int dummyWidth;
    private int dummyHeight;
    private BufferedImage dummySprite; // Placeholder for the dummy sprite

    // Constructor to accept only ScreenCoordinator
    public PracticeRangeScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator; // Store the reference

        // Set dimensions internally (you can also change these values)
        this.width = 800;  // Example width
        this.height = 600; // Example height

        // Initialize platform properties
        this.platformX = (int) (width * 0.05);
        this.platformY = (int) (height * (2.0 / 3.0));
        this.platformWidth = (int) (width * 0.90);
        this.platformHeight = 20;

        // Initialize dummy properties
        this.dummyWidth = 30; // Adjust based on your sprite's dimensions
        this.dummyHeight = 60; // Adjust based on your sprite's dimensions
        this.dummyX = platformX + platformWidth - dummyWidth - 10;
        this.dummyY = platformY - dummyHeight; // Align the dummy above the platform

        // Load the sprite
        loadDummySprite();
    }

    private void loadDummySprite() {
        try {
            // Load the resource from the "resources" directory
            dummySprite = ImageIO.read(new File("resources/dummy.png"));
        } catch (Exception e) {
            System.err.println("Failed to load dummy sprite: " + e.getMessage()); // Log the error
            dummySprite = null; // If there's an error, dummySprite remains null
        }
    }

    @Override
    public void initialize() {
        // Initialization code for the practice range, if needed
    }

    @Override
    public void update() {
        // Logic for updating the practice range, if needed

        // Check if the escape key is pressed to exit the screen
        if (Keyboard.isKeyDown(Key.ESC)) {
            screenCoordinator.setGameState(GameState.MENU); // Transition to the main menu
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Fill the screen with a white color
        graphicsHandler.drawFilledRectangle(0, 0, width, height, Color.WHITE);

        // Draw the platform
        graphicsHandler.drawFilledRectangle(platformX, platformY, platformWidth, platformHeight, Color.GRAY);

        // Draw the placeholder for the training dummy (sprite)
        if (dummySprite != null) {
            graphicsHandler.drawImage(dummySprite, dummyX, dummyY); // Use a method to draw the image
        }
    }
}
