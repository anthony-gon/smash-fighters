package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.Map;
import Maps.TitleScreenMap;
import SpriteFont.SpriteFont;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// This class is for the credits screen
public class CreditsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected List<SpriteFont> creditsTexts = new ArrayList<>();
    protected List<CreditImage> creditsImages = new ArrayList<>();
    protected SpriteFont returnInstructionsLabel;

    // Scrolling properties
    private int scrollSpeed = 1; // Pixels per frame for scrolling
    private int frameCounter = 0; // Counter to control scroll timing
    private int scrollDelay = 4; // Delay frames before updating scroll position
    private int scrollPositionY = 600; // Initial Y position for the first line of credits

    // Music properties
    private Clip musicClip;

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);

        // Create credits text lines
        creditsTexts.add(new SpriteFont("Credits", 250, scrollPositionY, "fibberish", 30, Color.white));
        creditsTexts.add(new SpriteFont("Created by The MJ's of Game Design", 150, scrollPositionY + 60, "fibberish", 20, Color.white));
        creditsTexts.add(new SpriteFont("Sam Joor - Lead Goob", 200, scrollPositionY + 300, "fibberish", 20, Color.white));
        creditsTexts.add(new SpriteFont("Ben Delton - big = better", 200, scrollPositionY + 600, "fibberish", 20, Color.white));
        creditsTexts.add(new SpriteFont("Anthony Gonzalez - Hopefully I spelled that right am rly tired ", 200, scrollPositionY + 900, "fibberish", 20, Color.white));
        creditsTexts.add(new SpriteFont("Hayden I dont know your last name sorry ", 200, scrollPositionY + 1200, "fibberish", 20, Color.white));
        creditsTexts.add(new SpriteFont("Mario I dont know last name mb ", 200, scrollPositionY + 1500, "fibberish", 20, Color.white));
        // Add more credits as needed...

        returnInstructionsLabel = new SpriteFont("<-Esc", 20, 532, "fibberish", 30, Color.white);

        keyLocker.lockKey(Key.ESC);

        // Load credit images from the "Resources" folder
        // Add more images as needed...

        // Play background music
        playBackgroundMusic();
    }

    private void addCreditImage(String relativePath, int x, int y) {
        try {
            // Load the image from the "Resources" folder using the relative path
            BufferedImage image = ImageIO.read(new File(relativePath));
            creditsImages.add(new CreditImage(image, x, y));
        } catch (IOException e) {
            System.err.println("Error loading image " + relativePath + ": " + e.getMessage());
        }
    }

    public void update() {
        background.update(null);

        // Only scroll credits upward every 'scrollDelay' frames
        frameCounter++;
        if (frameCounter >= scrollDelay) {
            frameCounter = 0; // Reset frame counter

            // Scroll credits upward by `scrollSpeed` pixels
            scrollPositionY -= scrollSpeed;

            // Update Y positions for each credit line and image
            for (SpriteFont text : creditsTexts) {
                text.setY(text.getY() - scrollSpeed);
            }

            for (CreditImage creditImage : creditsImages) {
                creditImage.y -= scrollSpeed;
            }
        }

        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        // If escape is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.ESC) && Keyboard.isKeyDown(Key.ESC)) {
            stopBackgroundMusic(); // Stop music when returning to the menu
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        background.draw(graphicsHandler);
        // Draw each line of credits
        for (SpriteFont text : creditsTexts) {
            text.draw(graphicsHandler);
        }
        // Draw each image
        for (CreditImage creditImage : creditsImages) {
            graphicsHandler.drawImage(creditImage.image, creditImage.x, creditImage.y);
        }
        returnInstructionsLabel.draw(graphicsHandler);
    }

    // Method to load and play background music
    private void playBackgroundMusic() {
        try {
            // Use direct file path to the .wav file
            File soundFile = new File("Resources/Credits.wav"); // Update this path to your actual file path
            if (!soundFile.exists()) {
                System.err.println("Music file not found at path: " + soundFile.getAbsolutePath());
                return;
            }

            // Load audio input stream
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);

            // Optionally set volume to a desired level
            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volumeControl.getMaximum()); // Set to maximum volume

            // Play music in a loop
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            musicClip.start();
            System.out.println("Credits music started."); // Debugging output
        } catch (Exception e) {
            System.err.println("Error loading or playing background music: " + e.getMessage());
        }
    }

    // Method to stop the background music
    private void stopBackgroundMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
            musicClip.close();
        }
    }

    // Inner class to hold image data
    private class CreditImage {
        BufferedImage image;
        int x;
        int y;

        public CreditImage(BufferedImage image, int x, int y) {
            this.image = image;
            this.x = x;
            this.y = y;
        }
    }
}
