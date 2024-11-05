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
    
    // Player 1 controls
    private SpriteFont jumpText;
    private SpriteFont crouchText;
    private SpriteFont rightText;
    private SpriteFont leftText;
    private SpriteFont lightAttackText;
    private SpriteFont heavyAttackText;
    private SpriteFont Select;
    private SpriteFont P1;
    
    // Player 2 controls
    private SpriteFont jumpTextP2;
    private SpriteFont crouchTextP2;
    private SpriteFont rightTextP2;
    private SpriteFont leftTextP2;
    private SpriteFont lightAttackTextP2;
    private SpriteFont heavyAttackTextP2;
    private SpriteFont SelectP2;
    private SpriteFont P2;
    
    private ScreenCoordinator screenCoordinator;

    // Sound properties
    private Clip backgroundMusicClip;

    public HowToPlayScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Initialize the "How to Play" title text
        titleText = new SpriteFont("How to Play", 300, 50, "fibberish", 30, Color.black);
        titleText.setOutlineColor(Color.white);
        titleText.setOutlineThickness(3);

        // Initialize the "Press ESC to exit" text
        exitText = new SpriteFont("Press ESC to Exit", 50, 550, "fibberish", 20, Color.black);
        exitText.setOutlineColor(Color.white);
        exitText.setOutlineThickness(2);

        // Initialize Player 1 controls
        P1 = new SpriteFont("Player 1", 100, 100, "fibberish", 25, Color.black);
        jumpText = new SpriteFont("Jump: W", 100, 150, "fibberish", 25, Color.black);
        crouchText = new SpriteFont("Crouch: S", 100, 200, "fibberish", 25, Color.black);
        rightText = new SpriteFont("Right: D", 100, 250, "fibberish", 25, Color.black);
        leftText = new SpriteFont("Left: A", 100, 300, "fibberish", 25, Color.black);
        lightAttackText = new SpriteFont("Light Attack: E", 100, 350, "fibberish", 25, Color.black);
        heavyAttackText = new SpriteFont("Heavy Attack: Q", 100, 400, "fibberish", 25, Color.black);
        Select = new SpriteFont("Select: Space", 100, 450, "fibberish", 25, Color.black);

        // Initialize Player 2 controls
        P2 = new SpriteFont("Player 2", 550, 100, "fibberish", 25, Color.black);
        jumpTextP2 = new SpriteFont("Jump: I", 550, 150, "fibberish", 25, Color.black);
        crouchTextP2 = new SpriteFont("Crouch: K", 550, 200, "fibberish", 25, Color.black);
        rightTextP2 = new SpriteFont("Right: K", 550, 250, "fibberish", 25, Color.black);
        leftTextP2 = new SpriteFont("Left: H", 550, 300, "fibberish", 25, Color.black);
        lightAttackTextP2 = new SpriteFont("Light Attack: U", 550, 350, "fibberish", 25, Color.black);
        heavyAttackTextP2 = new SpriteFont("Heavy Attack: O", 550, 400, "fibberish", 25, Color.black);
        SelectP2 = new SpriteFont("Select: Enter", 550, 450, "fibberish", 25, Color.black);
        // Set outline color and thickness for each Player 1 control text
        SpriteFont[] p1Controls = {jumpText, crouchText, rightText, leftText, lightAttackText, heavyAttackText};
        for (SpriteFont control : p1Controls) {
            control.setOutlineColor(Color.white);
            control.setOutlineThickness(2);
        }

        // Set outline color and thickness for each Player 2 control text
        SpriteFont[] p2Controls = {Select, SelectP2, jumpTextP2, crouchTextP2, rightTextP2, leftTextP2, lightAttackTextP2, heavyAttackTextP2};
        for (SpriteFont control : p2Controls) {
            control.setOutlineColor(Color.white);
            control.setOutlineThickness(2);
        }

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
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, new Color(173, 216, 230)); // Light blue color

        // Draw the title "How to Play"
        titleText.draw(graphicsHandler);

        // Draw Player 1 control instructions
        jumpText.draw(graphicsHandler);
        crouchText.draw(graphicsHandler);
        rightText.draw(graphicsHandler);
        leftText.draw(graphicsHandler);
        lightAttackText.draw(graphicsHandler);
        heavyAttackText.draw(graphicsHandler);
        Select.draw(graphicsHandler);
        P1.draw(graphicsHandler);

        // Draw Player 2 control instructions
        jumpTextP2.draw(graphicsHandler);
        crouchTextP2.draw(graphicsHandler);
        rightTextP2.draw(graphicsHandler);
        leftTextP2.draw(graphicsHandler);
        lightAttackTextP2.draw(graphicsHandler);
        heavyAttackTextP2.draw(graphicsHandler);
        SelectP2.draw(graphicsHandler);
        P2.draw(graphicsHandler);

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
