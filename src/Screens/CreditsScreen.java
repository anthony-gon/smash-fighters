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
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

// This class is for the credits screen
public class CreditsScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected Map background;
    protected KeyLocker keyLocker = new KeyLocker();
    protected List<SpriteFont> creditsTexts = new ArrayList<>();
    protected SpriteFont returnInstructionsLabel;

    // Scrolling properties
    private int scrollSpeed = 1; // Pixels per frame for scrolling
    private int scrollPositionY = 600; // Initial Y position for the first line of credits

    // Music properties
    private Clip musicClip;

    public CreditsScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // setup graphics on screen (background map, spritefont text)
        background = new TitleScreenMap();
        background.setAdjustCamera(false);

        // Create credits text lines
        creditsTexts.add(new SpriteFont("Credits", 300, scrollPositionY, "fibberish", 30, Color.white));
        creditsTexts.add(new SpriteFont("Created by The MJ's of Game Design", 150, scrollPositionY + 60, "fibberish", 20, Color.white));
        creditsTexts.add(new SpriteFont("John Doe - Lead Developer", 200, scrollPositionY + 120, "fibberish", 20, Color.white));
        creditsTexts.add(new SpriteFont("Jane Smith - Game Artist", 200, scrollPositionY + 180, "fibberish", 20, Color.white));
        creditsTexts.add(new SpriteFont("Alice Brown - Sound Design", 200, scrollPositionY + 240, "fibberish", 20, Color.white));
        creditsTexts.add(new SpriteFont("Bob White - Level Designer", 200, scrollPositionY + 300, "fibberish", 20, Color.white));
        // Add more credits as needed...

        returnInstructionsLabel = new SpriteFont("Press Space to return to the menu", 20, 532, "fibberish", 30, Color.white);

        keyLocker.lockKey(Key.SPACE);

        // Play background music
        playBackgroundMusic();
    }

    public void update() {
        background.update(null);

        // Scroll credits upward
        scrollPositionY -= scrollSpeed;

        // Update Y positions for each credit line
        for (SpriteFont text : creditsTexts) {
            text.setY(text.getY() - scrollSpeed);
        }

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        // if space is pressed, go back to main menu
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
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
}
