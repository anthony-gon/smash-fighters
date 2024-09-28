package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import java.awt.Color;
import SpriteFont.SpriteFont;
import Engine.Key;
import Engine.Keyboard;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

// This class represents the "How to Play" screen
public class HowToPlayScreen extends Screen {
    private SpriteFont titleText;
    private SpriteFont exitText;
    private SpriteFont jumpText;
    private SpriteFont crouchText;
    private SpriteFont rightText;
    private SpriteFont leftText;
    private SpriteFont lightAttackText;
    private SpriteFont heavyAttackText;
    private ScreenCoordinator screenCoordinator;

    // Sound properties
    private Clip backgroundMusicClip;

    public HowToPlayScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Initialize the "How to Play" text
        titleText = new SpriteFont("How to Play", 300, 50, "fibberish", 30, Color.black);
        titleText.setOutlineColor(Color.white);
        titleText.setOutlineThickness(3);

        // Initialize the "Press ESC to exit" text
        exitText = new SpriteFont("Press ESC to Exit", 50, 550, "fibberish", 20, Color.black);
        exitText.setOutlineColor(Color.white);
        exitText.setOutlineThickness(2);

        // Initialize the control instructions
        jumpText = new SpriteFont("Jump: W", 100, 150, "fibberish", 25, Color.black);
        crouchText = new SpriteFont("Crouch: S", 100, 200, "fibberish", 25, Color.black);
        rightText = new SpriteFont("Right: D", 100, 250, "fibberish", 25, Color.black);
        leftText = new SpriteFont("Left: A", 100, 300, "fibberish", 25, Color.black);
        lightAttackText = new SpriteFont("Light Attack: E", 100, 350, "fibberish", 25, Color.black);
        heavyAttackText = new SpriteFont("Heavy Attack: Q", 100, 400, "fibberish", 25, Color.black);

        // Set outline color and thickness for each instruction text
        jumpText.setOutlineColor(Color.white);
        jumpText.setOutlineThickness(2);
        
        crouchText.setOutlineColor(Color.white);
        crouchText.setOutlineThickness(2);
        
        rightText.setOutlineColor(Color.white);
        rightText.setOutlineThickness(2);
        
        leftText.setOutlineColor(Color.white);
        leftText.setOutlineThickness(2);
        
        lightAttackText.setOutlineColor(Color.white);
        lightAttackText.setOutlineThickness(2);
        
        heavyAttackText.setOutlineColor(Color.white);
        heavyAttackText.setOutlineThickness(2);

        // Load and play background music
        loadBackgroundMusic();
        playBackgroundMusic();
    }

    @Override
    public void update() {
        // Check if the ESC key is pressed to exit back to the Menu screen
        if (Keyboard.isKeyDown(Key.ESC)) {
            stopBackgroundMusic();
            screenCoordinator.setGameState(GameState.MENU);
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Fill the entire screen with a light blue background
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, new Color(173, 216, 230)); // Light blue color (assuming 800x600 screen size)

        // Draw the title "How to Play"
        titleText.draw(graphicsHandler);

        // Draw the control instructions
        jumpText.draw(graphicsHandler);
        crouchText.draw(graphicsHandler);
        rightText.draw(graphicsHandler);
        leftText.draw(graphicsHandler);
        lightAttackText.draw(graphicsHandler);
        heavyAttackText.draw(graphicsHandler);

        // Draw the "Press ESC to exit" message
        exitText.draw(graphicsHandler);
    }

    // Play the background music
    private void playBackgroundMusic() {
        if (backgroundMusicClip != null) {
            backgroundMusicClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the background music
            backgroundMusicClip.start();
        }
    }

    // Stop the background music
    private void stopBackgroundMusic() {
        if (backgroundMusicClip != null && backgroundMusicClip.isRunning()) {
            backgroundMusicClip.stop();
        }
    }

    // Load the background music
    private void loadBackgroundMusic() {
        try {
            // Use direct file path to ensure the sound file is found
            File soundFile = new File("Resources/HowToPlayMusic.wav"); // Update the path if necessary
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioInputStream);

            // Optionally set volume to maximum for clarity
            FloatControl volumeControl = (FloatControl) backgroundMusicClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volumeControl.getMaximum()); // Set to maximum volume

            System.out.println("Background music loaded successfully."); // Debugging output
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
        }
    }
}
