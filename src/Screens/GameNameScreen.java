package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameNameScreen extends Screen {
    private SpriteFont gameNameText;
    private ScreenCoordinator screenCoordinator;
    private Random random = new Random();

    // Explosion properties
    private boolean explosionTriggered = false;
    private List<ExplosionParticle> explosionParticles = new ArrayList<>();
    private int explosionTimer = 0;
    private int explosionDuration = 100; // Duration for explosion visibility
    private int fadeAlpha = 0; // Opacity for fade-in effect
    private int fadeSpeed = 3; // Speed of fade-in transition
    private boolean fadeInStarted = false;

    // Screen dimensions and text positioning
    private final int SCREEN_WIDTH = 800;
    private final int SCREEN_HEIGHT = 600;

    // Center position
    private int centerX;
    private int centerY;

    // Screen shake effect properties
    private boolean isShaking = false;
    private int shakeIntensity = 20; // Intensity of shake
    private int shakeDuration = 200; // Duration of shake effect in frames
    private int shakeTimer = 0; // Timer for shake duration
    private int shakeStartDelay = 60; // Delay before shake starts (1 second at 60 FPS)

    // Glow effect properties
    private double glowScale = 1.0;
    private double glowSpeed = 0.1; // Speed of glowing effect (pulsation)
    private boolean glowIncreasing = true;

    // Fade-out effect properties for transitioning to menu
    private boolean fadeToMenuStarted = false;
    private int fadeToMenuAlpha = 0;
    private int fadeToMenuSpeed = 3; // Speed of fade-out to menu

    // Sound properties
    private Clip musicClip;

    public GameNameScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Calculate the position to center the game name text
        String gameName = "SMASH FIGHTERS";
        int textWidth = 650; // Approximate width of the text
        int textHeight = 70; // Approximate height of the text

        centerX = (SCREEN_WIDTH - textWidth) / 2;
        centerY = (SCREEN_HEIGHT - textHeight) / 2;

        // Initialize the game name text centered on the screen and set to red
        gameNameText = new SpriteFont(gameName, centerX, centerY, "fibberish", 100, Color.red);
        gameNameText.setOutlineColor(Color.black);
        gameNameText.setOutlineThickness(3);

        // Trigger explosions across the screen, but delay shaking
        triggerExplosionsAcrossScreen();

        // Play background music
        playBackgroundMusic();
    }

    @Override
    public void update() {
        // Update explosion animation if triggered
        if (explosionTriggered) {
            explosionTimer++;
            for (ExplosionParticle particle : explosionParticles) {
                particle.update();
            }

            // Start shaking after delay
            if (explosionTimer > shakeStartDelay && shakeTimer < shakeDuration) {
                isShaking = true;
                shakeTimer++;
            } else {
                isShaking = false;
            }

            // If explosion duration is over, start fade-in for game name text
            if (explosionTimer > explosionDuration + shakeStartDelay) {
                explosionTriggered = false;
                fadeInStarted = true; // Start fade-in
                shakeTimer = 0; // Reset shake timer
            }
        }

        // Handle fade-in of the game name text
        if (fadeInStarted) {
            fadeAlpha = Math.min(fadeAlpha + fadeSpeed, 255);

            // Handle glow effect (pulsing)
            if (glowIncreasing) {
                glowScale += glowSpeed;
                if (glowScale >= 1.3) glowIncreasing = false; // Reverse direction of glow
            } else {
                glowScale -= glowSpeed;
                if (glowScale <= 1.0) glowIncreasing = true; // Reverse direction of glow
            }

            // Once fade-in is complete, start fade-out to menu
            if (fadeAlpha >= 255) {
                fadeToMenuStarted = true;
            }
        }

        // Handle fade-out to menu
        if (fadeToMenuStarted) {
            fadeToMenuAlpha = Math.min(fadeToMenuAlpha + fadeToMenuSpeed, 255);

            // Transition to the menu screen when fade-out is complete
            if (fadeToMenuAlpha >= 255) {
                stopBackgroundMusic();
                screenCoordinator.setGameState(GameState.MENU);
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Calculate shake offset
        int shakeOffsetX = 0;
        int shakeOffsetY = 0;

        if (isShaking) {
            shakeOffsetX = random.nextInt(shakeIntensity * 2) - shakeIntensity;
            shakeOffsetY = random.nextInt(shakeIntensity * 2) - shakeIntensity;
        }

        // Set background to black with shake offset
        graphicsHandler.drawFilledRectangle(0 + shakeOffsetX, 0 + shakeOffsetY, SCREEN_WIDTH, SCREEN_HEIGHT, Color.black);

        // Draw explosion particles if triggered with shake offset
        if (explosionTriggered) {
            for (ExplosionParticle particle : explosionParticles) {
                particle.draw(graphicsHandler, shakeOffsetX, shakeOffsetY);
            }
        }

        // Draw the game name text with a fade-in effect and glow effect
        if (fadeInStarted) {
            gameNameText.setColor(new Color(255, 0, 0, fadeAlpha)); // Keep red color with fading effect

            // Apply scaling using the new scale method
            graphicsHandler.scale(glowScale, glowScale, centerX + shakeOffsetX, centerY + shakeOffsetY);

            // Draw the game name with shake offset
            gameNameText.setX(centerX + shakeOffsetX);
            gameNameText.setY(centerY + shakeOffsetY);
            gameNameText.draw(graphicsHandler);

            // Reset the scaling transformation
            graphicsHandler.scale(1.0 / glowScale, 1.0 / glowScale, centerX + shakeOffsetX, centerY + shakeOffsetY);
        }

        // Draw fade-to-menu effect
        if (fadeToMenuStarted) {
            graphicsHandler.drawFilledRectangle(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, new Color(0, 0, 0, fadeToMenuAlpha));
        }
    }

    // Trigger explosions at random positions all over the screen
    private void triggerExplosionsAcrossScreen() {
        explosionTriggered = true;
        explosionParticles.clear(); // Clear any previous particles

        int numParticles = 1000; // Increase number of particles for visibility
        double sizeMultiplier = 4; // Larger size for particles

        // Generate explosion particles at random positions across the screen
        for (int i = 0; i < numParticles; i++) {
            int explosionX = random.nextInt(SCREEN_WIDTH); // Random x position
            int explosionY = random.nextInt(SCREEN_HEIGHT); // Random y position

            // Create particles radiating from each random position
            for (int j = 0; j < 10; j++) { // Each explosion has 10 particles
                double angle = 2 * Math.PI * random.nextDouble();
                double speed = 2 + random.nextDouble() * 6;
                explosionParticles.add(new ExplosionParticle(explosionX, explosionY, angle, speed, sizeMultiplier));
            }
        }
        System.out.println("Explosions triggered with " + explosionParticles.size() + " particles."); // Debugging output
    }

    // Inner class to represent an explosion particle
    private class ExplosionParticle {
        private double x, y; // Position of the particle
        private double angle; // Direction in radians
        private double speed; // Speed of particle
        private Color color; // Color of particle
        private double size; // Size of particle

        public ExplosionParticle(int startX, int startY, double angle, double speed, double sizeMultiplier) {
            this.x = startX;
            this.y = startY;
            this.angle = angle;
            this.speed = speed;
            this.color = new Color(255, random.nextInt(256), 0); // Yellowish to red particles
            this.size = (8 + random.nextDouble() * 10) * sizeMultiplier; // Bigger random size
        }

        // Update particle position
        public void update() {
            x += speed * Math.cos(angle);
            y += speed * Math.sin(angle);
            speed *= 0.95; // Gradually slow down the particle
            size *= 0.95; // Gradually shrink the particle

            // Gradually shift the particle color to black for a fading effect
            int r = Math.max(color.getRed() - 5, 0);
            int g = Math.max(color.getGreen() - 5, 0);
            int b = Math.max(color.getBlue() - 5, 0);
            color = new Color(r, g, b);
        }

        // Draw the particle with optional offset for screen shake
        public void draw(GraphicsHandler graphicsHandler, int offsetX, int offsetY) {
            graphicsHandler.drawFilledRectangle((int) (x + offsetX), (int) (y + offsetY), (int) size, (int) size, color);
        }
    }

    // Method to load and play background music
    private void playBackgroundMusic() {
        try {
            // Use a direct file path to the .wav file
            String filePath = "Resources/explosion.wav"; // Replace with the correct path
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