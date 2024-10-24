package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MapSelectScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMapItemHovered = 0; // Current map item being hovered over
    protected List<SpriteFont> mapItems; // List to hold map buttons
    protected SpriteFont map1, map2; // Buttons for maps
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected int pointerOffsetX = 30;
    protected int pointerOffsetY = 7;
    protected KeyLocker keyLocker = new KeyLocker();

    // Spinning wheel attributes
    protected boolean wheelSpinning = true; // Track if the wheel is spinning
    protected int wheelSpinTime; // The time for the wheel spin
    protected Random random;
    protected int selectedPlayer = 0; // 1 for Player 1, 2 for Player 2
    protected boolean wheelFinished = false;

    // Explosion effect attributes
    protected boolean explosionStarted = false; // Track if the explosion has started
    protected int explosionTime = 30; // Explosion duration in frames (0.5 seconds)
    protected int pixelExplosionRadius = 0; // Radius of the pixel explosion
    protected boolean removeWheel = false; // Whether to remove the wheel after explosion

    // Wheel visual attributes
    protected float angle = 0; // Current rotation angle of the wheel
    protected float spinSpeed; // The speed of the wheel spinning
    protected float friction = 0.1f; // Friction factor for slowing down the wheel

    // SpriteFont for the result of the wheel spin
    protected SpriteFont wheelResultText;

    public MapSelectScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
        this.random = new Random(System.currentTimeMillis()); // Seed the random object with the current time
    }

    @Override
    public void initialize() {
        // Initialize the map select buttons
        map1 = new SpriteFont("Inferno", 300, 200, "fibberish", 30, new Color(49, 207, 240));
        map1.setOutlineColor(Color.black);
        map1.setOutlineThickness(3);

        map2 = new SpriteFont("Ice Kingdom", 300, 300, "fibberish", 30, new Color(49, 207, 240));
        map2.setOutlineColor(Color.black);
        map2.setOutlineThickness(3);

        // Add items to the mapItems list
        mapItems = Arrays.asList(map1, map2);

        keyPressTimer = 0;
        keyLocker.lockKey(Key.SPACE); // Lock the key initially

        // Initialize the wheel text and random generator
        wheelResultText = new SpriteFont("SPINNING THE WHEEL...", 250, 150, "fibberish", 30, Color.WHITE);
        wheelResultText.setOutlineColor(Color.black);
        wheelResultText.setOutlineThickness(3);

        // set the random spin time and initial speed
        wheelSpinTime = 240; //wheel spin time
        spinSpeed = random.nextFloat() * 5 + 5;  // Random initial speed between 5 and 10
    }

    @Override
    public void update() {
        if (wheelSpinning) {
            // The wheel is spinning; decrement spin time
            wheelSpinTime--;

            // Spin the wheel by increasing the angle
            angle += spinSpeed;

            // Gradually decrease the speed to simulate friction
            spinSpeed = Math.max(spinSpeed - friction, 0);

            if (wheelSpinTime <= 0 || spinSpeed <= 0) {
                // Determine which half the wheel landed on based on the final angle
                float finalAngle = angle % 360; // Get the final angle between 0 and 360 degrees
                if (finalAngle >= 0 && finalAngle <= 180) {
                    // Player 1 (Blue) selects the map
                    selectedPlayer = 1;
                    wheelResultText.setText("PLAYER 1 SELECTS THE MAP!");
                } else {
                    // Player 2 (Red) selects the map
                    selectedPlayer = 2;
                    wheelResultText.setText("PLAYER 2 SELECTS THE MAP!");
                }

                wheelSpinning = false;
                wheelFinished = true;

                // Start the explosion effect
                explosionStarted = true;
            }
        } else if (explosionStarted) {
            // Handle the pixelated explosion effect
            explosionTime--;
            pixelExplosionRadius += 5; // Smaller particle radius

            if (explosionTime <= 0) {
                // Explosion is over, remove the wheel
                removeWheel = true;
                explosionStarted = false; // Stop the explosion effect
            }
        } else if (removeWheel) {
            // Proceed to map selection for the chosen player
            handleMapSelection();
        }
    }

    // Handle map selection by the player chosen by the wheel
    private void handleMapSelection() {
        if (selectedPlayer == 1) {
            // Player 1 selects the map
            if (Keyboard.isKeyDown(Key.S) && keyPressTimer == 0) {
                keyPressTimer = 14;
                currentMapItemHovered++;
            } else if (Keyboard.isKeyDown(Key.W) && keyPressTimer == 0) {
                keyPressTimer = 14;
                currentMapItemHovered--;
            }
        } else if (selectedPlayer == 2) {
            // Player 2 selects the map
            if (Keyboard.isKeyDown(Key.K) && keyPressTimer == 0) {
                keyPressTimer = 14;
                currentMapItemHovered++;
            } else if (Keyboard.isKeyDown(Key.I) && keyPressTimer == 0) {
                keyPressTimer = 14;
                currentMapItemHovered--;
            }
        }

        if (keyPressTimer > 0) {
            keyPressTimer--;
        }

        // Loop the selection back around
        if (currentMapItemHovered >= mapItems.size()) {
            currentMapItemHovered = 0;
        } else if (currentMapItemHovered < 0) {
            currentMapItemHovered = mapItems.size() - 1;
        }

        // Update pointer location
        for (int i = 0; i < mapItems.size(); i++) {
            if (i == currentMapItemHovered) {
                mapItems.get(i).setColor(new Color(255, 215, 0)); // Highlighted color
                pointerLocationX = (int) mapItems.get(i).getX() - pointerOffsetX;
                pointerLocationY = (int) mapItems.get(i).getY() - pointerOffsetY;
            } else {
                mapItems.get(i).setColor(new Color(49, 207, 240)); // Default color
            }
        }

        // Handle map selection with SPACE/ENTER
        if (selectedPlayer == 1) {
            handleSelection(Key.SPACE); // Player 1 uses SPACE
        } else {
            handleSelection(Key.ENTER); // Player 2 uses ENTER
        }
    }

    private void handleSelection(Key selectKey) {
        if (Keyboard.isKeyUp(selectKey)) {
            keyLocker.unlockKey(selectKey);
        }
        if (!keyLocker.isKeyLocked(selectKey) && Keyboard.isKeyDown(selectKey)) {
            // When a map is selected, set the selected map in ScreenCoordinator
            String selectedMapName = mapItems.get(currentMapItemHovered).getText(); // Get the map name text
            System.out.println("Selected Map: " + selectedMapName); // Debug
            screenCoordinator.setSelectedMap(selectedMapName);

            // Transition to the character select screen
            screenCoordinator.setGameState(GameState.CHARACTER);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        if (wheelSpinning) {
            // Draw the spinning wheel
            drawWheel(graphicsHandler);
        }

        if (explosionStarted) {
            // Draw the small red, orange, yellow pixelated explosion effect
            drawSmallExplosion(graphicsHandler);
        }

        if (wheelFinished && !explosionStarted) {
            wheelResultText.draw(graphicsHandler); // Draw the result after the wheel finishes
        }

        // Draw map options after the explosion is over
        if (removeWheel) {
            for (SpriteFont mapItem : mapItems) {
                mapItem.draw(graphicsHandler);
            }

            // Draw the pointer
            graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
        }
    }

    // Method to draw the spinning wheel
    private void drawWheel(GraphicsHandler graphicsHandler) {
        // Set the center of the wheel
        int centerX = 400;
        int centerY = 300;
        int wheelRadius = 150;

        // Define the segments for Player 1 and Player 2
        Color player1Color = new Color(49, 207, 240); // Blue for Player 1
        Color player2Color = new Color(240, 49, 49); // Red for Player 2

        Graphics2D g2d = (Graphics2D) graphicsHandler.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Save the current transform (so we can reset later)
        AffineTransform old = g2d.getTransform();

        // Rotate around the center of the wheel
        g2d.rotate(Math.toRadians(angle), centerX, centerY);

        // Draw Player 1's half of the wheel
        g2d.setColor(player1Color);
        g2d.fillArc(centerX - wheelRadius, centerY - wheelRadius, wheelRadius * 2, wheelRadius * 2, 0, 180);

        // Draw Player 2's half of the wheel
        g2d.setColor(player2Color);
        g2d.fillArc(centerX - wheelRadius, centerY - wheelRadius, wheelRadius * 2, wheelRadius * 2, 180, 180);

        // Reset the transform to the original state
        g2d.setTransform(old);

        // Optional: Add a pointer to indicate where the wheel will stop
        g2d.setColor(Color.WHITE);
        g2d.fillPolygon(new int[]{centerX - 10, centerX + 10, centerX}, new int[]{centerY - wheelRadius - 20, centerY - wheelRadius - 20, centerY - wheelRadius}, 3);
    }

    // Method to draw the small red, orange, yellow pixelated explosion effect
    private void drawSmallExplosion(GraphicsHandler graphicsHandler) {
        Graphics2D g2d = (Graphics2D) graphicsHandler.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Center of explosion
        int centerX = 400;
        int centerY = 300;

        // Draw random small rectangles in red, orange, yellow colors
        for (int i = 0; i < 50; i++) {
            int randomX = centerX + random.nextInt(pixelExplosionRadius) - pixelExplosionRadius / 2;
            int randomY = centerY + random.nextInt(pixelExplosionRadius) - pixelExplosionRadius / 2;
            int rectSize = random.nextInt(8) + 3; // Smaller particle sizes

            // Random red, orange, or yellow color
            Color particleColor = new Color(
                    random.nextInt(56) + 200, // Red/orange tones
                    random.nextInt(150) + 100, // Orange/yellow tones
                    random.nextInt(56));       // Less blue for fiery effect

            g2d.setColor(particleColor);
            g2d.fillRect(randomX, randomY, rectSize, rectSize); // Draw the small rectangle
        }
    }
}

