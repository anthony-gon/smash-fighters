package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PracticeRangeScreen extends Screen {
    private int width;
    private int height;

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

    // Constructor to accept width and height
    public PracticeRangeScreen(int width, int height) {
        this.width = width;
        this.height = height;

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
            dummySprite = ImageIO.read(new File("C:\\Users\\panag\\.vscode\\smash-fighters\\Resources\\dummy.png")); // Set the correct path to your sprite
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error (e.g., set a default image or log the error)
        }
    }

    @Override
    public void initialize() {
        // Initialization code for the practice range, if needed
    }

    @Override
    public void update() {
        // Logic for updating the practice range, if needed
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