package Screens;

import Engine.GraphicsHandler;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;
import Engine.Key;
import Engine.Keyboard;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntroScreen extends Screen {
    private SpriteFont errorText;
    private ScreenCoordinator screenCoordinator;
    private Random random = new Random();

    // Glitch properties
    private int glitchTimer = 0;
    private int glitchInterval = 5; // How often the glitch effect updates
    private int glitchDuration = 3; // How long a glitch effect lasts
    private boolean errorSoundPlaying = false; // Track if error sound is currently playing

    // Explosion properties
    private boolean explosionTriggered = false;
    private List<ExplosionParticle> explosionParticles = new ArrayList<>();
    private int explosionTimer = 0;
    private int explosionDuration = 25; // Duration of each explosion round
    private int explosionRound = 0; // Tracks which explosion round is occurring
    private int maxRounds = 4; // Number of explosion rounds
    private int explosionsPerRound = 100; // Number of simultaneous explosions per round

    // Fade properties for transitioning
    private int fadeAlpha = 0; // Opacity for fade effect
    private int fadeSpeed = 5; // Speed of fade effect
    private boolean isFadingOut = false;

    // Sound properties
    private Clip explosionClip;
    private Clip errorSoundClip;
    private Clip backgroundMusicClip;

    public IntroScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Initialize the "ERROR" text in the center of the screen
        errorText = new SpriteFont("ERROR", 350, 300, "fibberish", 50, Color.red);
        errorText.setOutlineColor(Color.black);
        errorText.setOutlineThickness(3);

        // Load the sounds
        loadExplosionSound();
        loadErrorSound();
        loadBackgroundMusic();

        // Play background music
        playBackgroundMusic();
    }

    @Override
    public void update() {
        // Check if the F key is pressed to skip to the menu
        if (Keyboard.isKeyDown(Key.F)) {
            stopErrorSound(); // Stop any ongoing sounds
            stopBackgroundMusic(); // Stop background music
            screenCoordinator.setGameState(GameState.MENU); // Transition to the menu screen
            return; // Return to prevent further execution of this method
        }

        // Glitch effect update
        glitchTimer++;
        if (glitchTimer % glitchInterval == 0) {
            glitchErrorText();
        }

        // Play error sound while "ERROR" text is on the screen
        if (!explosionTriggered && !errorSoundPlaying) {
            playErrorSound();
        }

        // If SPACE is pressed, trigger the explosion sequence
        if (Keyboard.isKeyDown(Key.SPACE) && !explosionTriggered) {
            triggerExplosions();
            stopErrorSound(); // Stop the error sound once explosions begin
        }

        // Update explosion animation if triggered
        if (explosionTriggered) {
            explosionTimer++;
            for (ExplosionParticle particle : explosionParticles) {
                particle.update();
            }

            // If the current explosion duration is over, prepare the next round
            if (explosionTimer > explosionDuration) {
                explosionTimer = 0; // Reset timer
                explosionRound++;

                // Trigger next explosion round if any remaining
                if (explosionRound < maxRounds) {
                    triggerExplosions();
                } else {
                    // Transition to group name screen
                    isFadingOut = true;
                }
            }
        }

        // Fade-out effect to transition to GroupNameScreen
        if (isFadingOut) {
            fadeAlpha = Math.min(fadeAlpha + fadeSpeed, 255);
            if (fadeAlpha >= 255) {
                // Once fade out is complete, switch to the GroupNameScreen
                stopBackgroundMusic();
                screenCoordinator.setGameState(GameState.GROUP_NAME);
                isFadingOut = false;
                fadeAlpha = 0;
            }
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Set background color based on the current round
        Color backgroundColor = getBackgroundColorForRound(explosionRound);
        graphicsHandler.drawFilledRectangle(0, 0, 800, 600, backgroundColor);

        // Draw the "ERROR" text on the screen if explosion hasn't started
        if (!explosionTriggered) {
            errorText.draw(graphicsHandler);
        }

        // Draw the explosion particles if triggered
        if (explosionTriggered) {
            for (ExplosionParticle particle : explosionParticles) {
                particle.draw(graphicsHandler);
            }
        }

        // Draw the fade-out effect
        if (isFadingOut) {
            graphicsHandler.drawFilledRectangle(0, 0, 800, 600, new Color(0, 0, 0, fadeAlpha));
        }
    }

    // Get the background color based on the current round
    private Color getBackgroundColorForRound(int round) {
        switch (round) {
            case 1:
                return Color.red; // First round - red
            case 2:
                return Color.yellow; // Second round - yellow
            case 3:
                return Color.orange; // Third round - orange
            default:
                return Color.blue; // Initial background color is blue
        }
    }

    // Trigger multiple simultaneous explosions across the screen
    private void triggerExplosions() {
        explosionTriggered = true;

        // Clear the previous explosion particles for the new round
        explosionParticles.clear();

        int numParticles = 200; // Consistent large number of particles per explosion
        double sizeMultiplier = 3; // Larger size of particles

        // Play explosion sound
        playExplosionSound();

        // Create multiple explosions happening at random positions on the screen
        for (int i = 0; i < explosionsPerRound; i++) {
            int explosionX = random.nextInt(800); // Random x-coordinate within screen width
            int explosionY = random.nextInt(600); // Random y-coordinate within screen height

            // Create explosion particles radiating from the random position on the screen
            for (int j = 0; j < numParticles; j++) {
                double angle = 2 * Math.PI * random.nextDouble(); // Random angle for particle direction
                double speed = 2 + random.nextDouble() * 5; // Random speed for a big spread
                explosionParticles.add(new ExplosionParticle(explosionX, explosionY, angle, speed, sizeMultiplier));
            }
        }
    }

    // Play the explosion sound
    private void playExplosionSound() {
        if (explosionClip != null) {
            explosionClip.setFramePosition(0); // Rewind the clip to the start
            explosionClip.start();
        }
    }

    // Play the error sound and loop it while the error is on the screen
    private void playErrorSound() {
        if (errorSoundClip != null) {
            errorSoundClip.loop(Clip.LOOP_CONTINUOUSLY); // Loop the error sound continuously
            errorSoundClip.start();
            errorSoundPlaying = true;
        }
    }

    // Stop the error sound
    private void stopErrorSound() {
        if (errorSoundClip != null && errorSoundClip.isRunning()) {
            errorSoundClip.stop();
            errorSoundPlaying = false;
        }
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

    // Load the explosion sound
    private void loadExplosionSound() {
        try {
            File soundFile = new File("Resources/MediumExplosion.wav"); // Update the path
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            explosionClip = AudioSystem.getClip();
            explosionClip.open(audioInputStream);
        } catch (Exception e) {
            System.err.println("Error loading explosion sound: " + e.getMessage());
        }
    }

    // Load the error sound
    private void loadErrorSound() {
        try {
            File soundFile = new File("Resources/Error.wav"); // Update the path
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            errorSoundClip = AudioSystem.getClip();
            errorSoundClip.open(audioInputStream);
        } catch (Exception e) {
            System.err.println("Error loading error sound: " + e.getMessage());
        }
    }

    // Load the background music
    private void loadBackgroundMusic() {
        try {
            File soundFile = new File("Resources/BackgroundMusic.wav"); // Update the path
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            backgroundMusicClip = AudioSystem.getClip();
            backgroundMusicClip.open(audioInputStream);
        } catch (Exception e) {
            System.err.println("Error loading background music: " + e.getMessage());
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
            speed *= 0.92; // Gradually slow down the particle
            size *= 0.95; // Gradually shrink the particle
        }

        // Draw the particle
        public void draw(GraphicsHandler graphicsHandler) {
            graphicsHandler.drawFilledRectangle((int) x, (int) y, (int) size, (int) size, color);
        }
    }

    // Trigger a glitch effect on the "ERROR" text
    private void glitchErrorText() {
        // Randomly change position slightly
        int xOffset = random.nextInt(20) - 10; // Random x offset between -10 and +10
        int yOffset = random.nextInt(20) - 10; // Random y offset between -10 and +10
        errorText.setX(350 + xOffset);
        errorText.setY(300 + yOffset);

        // Randomly change color
        errorText.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))); // Random color

        // Randomly change outline color and thickness
        errorText.setOutlineColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))); // Random outline color
        errorText.setOutlineThickness(random.nextInt(5)); // Random outline thickness between 0 and 4

        // Randomly change font size for a brief flicker effect
        if (glitchTimer % glitchDuration == 0) {
            errorText.setFontSize(50 + random.nextInt(10) - 5); // Slight size change between -5 and +5
        }
    }
}

