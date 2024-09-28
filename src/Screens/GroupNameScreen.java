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

public class GroupNameScreen extends Screen {
    private SpriteFont groupNameText;
    private ScreenCoordinator screenCoordinator;
    private Random random = new Random();

    // Explosion properties
    private boolean explosionTriggered = false;
    private List<ExplosionParticle> explosionParticles = new ArrayList<>();
    private int explosionTimer = 0;
    private int explosionDuration = 100; // Duration for explosion visibility
    private int fadeAlpha = 0; // Opacity for fade effect
    private int fadeSpeed = 3; // Speed of fade-in transition
    private boolean fadeInStarted = false;
    private final int numExplosions = 10; // Number of simultaneous explosions

    // Sound properties
    private Clip explosionClip;
    private int soundTimer = 0; // Timer to control sound playback
    private final int soundDuration = 180; // Duration in frames (assuming 60 FPS, 120 frames = 2 seconds)

    public GroupNameScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    
    public void initialize() {
        // Initialize the group name text
        groupNameText = new SpriteFont("MJs Of Game Design", 150, 300, "fibberish", 60, Color.white);
        groupNameText.setOutlineColor(Color.black);
        groupNameText.setOutlineThickness(3);

        // Load the explosion sound
        loadExplosionSound();

        // Trigger multiple explosions at the start
        triggerMultipleExplosions();
    }

    @Override
    public void update() {
        // Update explosion animation if triggered
        if (explosionTriggered) {
            explosionTimer++;
            for (ExplosionParticle particle : explosionParticles) {
                particle.update();
            }

            // Manage sound duration timer
            soundTimer++;
            if (soundTimer > soundDuration) {
                stopExplosionSound(); // Stop sound after the desired duration
            }

            // If explosion duration is over, stop explosions and start fade-in
            if (explosionTimer > explosionDuration) {
                explosionTriggered = false;
                fadeInStarted = true;
                explosionTimer = 0; // Reset timer
            }
        }

        // Handle fade-in of the group name text
        if (fadeInStarted) {
            fadeAlpha = Math.min(fadeAlpha + fadeSpeed, 255);

            // Transition to the next screen when fade-in is complete
            if (fadeAlpha >= 255) {
                stopExplosionSound(); // Ensure sound stops before transitioning
                screenCoordinator.setGameState(GameState.GAME_NAME); // Transition to the next screen
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Set background to black
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, Color.black);

        // Draw explosion particles if triggered
        if (explosionTriggered) {
            for (ExplosionParticle particle : explosionParticles) {
                particle.draw(graphicsHandler);
            }
        }

        // Draw the group name text with a fade-in effect
        if (fadeInStarted) {
            groupNameText.setColor(new Color(255, 255, 255, fadeAlpha));
            groupNameText.draw(graphicsHandler);
        }
    }

    // Trigger multiple explosions at random positions across the screen
    private void triggerMultipleExplosions() {
        explosionTriggered = true;
        explosionParticles.clear(); // Clear any previous particles

        int particlesPerExplosion = 100; // Particles per explosion
        double sizeMultiplier = 4; // Larger size for particles

        // Play the explosion sound when explosions are triggered
        if (explosionClip != null) {
            explosionClip.setFramePosition(0); // Rewind the clip to the start
            explosionClip.start(); // Start playing
            System.out.println("Explosion sound started."); // Debugging output
        }

        // Generate explosions at random positions across the screen
        for (int i = 0; i < numExplosions; i++) {
            int explosionX = random.nextInt(800); // Random x-coordinate within screen width
            int explosionY = random.nextInt(600); // Random y-coordinate within screen height

            // Generate particles for this explosion
            for (int j = 0; j < particlesPerExplosion; j++) {
                double angle = 2 * Math.PI * random.nextDouble();
                double speed = 2 + random.nextDouble() * 6;
                explosionParticles.add(new ExplosionParticle(explosionX, explosionY, angle, speed, sizeMultiplier));
            }
        }
        System.out.println("Multiple explosions triggered with " + explosionParticles.size() + " particles."); // Debugging output
    }

    // Load the explosion sound
    private void loadExplosionSound() {
        try {
            // Use direct file path to ensure the sound file is found
            File soundFile = new File("Resources/GroupNameBoom.wav"); // Update the path if necessary
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            explosionClip = AudioSystem.getClip();
            explosionClip.open(audioInputStream);

            // Optionally set volume to maximum for clarity
            FloatControl volumeControl = (FloatControl) explosionClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volumeControl.getMaximum()); // Set to maximum volume

            System.out.println("Explosion sound loaded successfully."); // Debugging output
        } catch (Exception e) {
            System.err.println("Error loading explosion sound: " + e.getMessage());
        }
    }

    // Stop the explosion sound when needed
    private void stopExplosionSound() {
        if (explosionClip != null && explosionClip.isRunning()) {
            explosionClip.stop();
            explosionClip.close();
        }
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
        }

        // Draw the particle
        public void draw(GraphicsHandler graphicsHandler) {
            graphicsHandler.drawFilledRectangle((int) x, (int) y, (int) size, (int) size, color);
        }
    }
}
