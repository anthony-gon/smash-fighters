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

import javax.sound.sampled.Clip;

public class MapSelectScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentMapItemHovered = 0; // Current map item being hovered over
    protected List<SpriteFont> mapItems; // List to hold map buttons
    protected SpriteFont map1, map2, map3; // Buttons for maps
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected int pointerOffsetX = 30;
    protected int pointerOffsetY = 7;
    protected KeyLocker keyLocker = new KeyLocker();
    public String selectedMapName;

    // Spinning wheel attributes
    protected boolean wheelSpinning = true; // Track if the wheel is spinning
    protected int wheelSpinTime = 150; // Spin for 300 frames
    protected Random random;
    protected int selectedPlayer = 0; // 1 for Player 1, 2 for Player 2
    protected boolean wheelFinished = false;
    protected int postSpinDelay = 120; // Wait for 50 frames after stopping

    // Explosion effect attributes
    protected boolean explosionStarted = false; // Track if the explosion has started
    protected int explosionTime = 70; // Explosion duration in frames (1 second)
    protected int rippleRadius = 0; // Radius of the ripple explosion
    protected boolean removeWheel = false; // Whether to remove the wheel after explosion

    // Wheel visual attributes
    protected float angle = 0; // Current rotation angle of the wheel
    protected float spinSpeed; // The speed of the wheel spinning
    protected float friction = 0.03f; // Friction factor for slowing down the wheel

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

        map2 = new SpriteFont("Tundra", 300, 300, "fibberish", 30, new Color(49, 207, 240));
        map2.setOutlineColor(Color.black);
        map2.setOutlineThickness(3);

        map3 = new SpriteFont("Toads", 300, 400, "fibberish", 30, new Color(49, 207, 240));
        map3.setOutlineColor(Color.black);
        map3.setOutlineThickness(3);

        // Add items to the mapItems list
        mapItems = Arrays.asList(map1, map2, map3);

        keyPressTimer = 0;
        keyLocker.lockKey(Key.SPACE); // Lock the key initially

        // Initialize the wheel text and random generator
        wheelResultText = new SpriteFont("SPINNING THE WHEEL...", 250, 150, "fibberish", 30, Color.WHITE);
        wheelResultText.setOutlineColor(Color.black);
        wheelResultText.setOutlineThickness(3);

        // Set the random spin time and initial speed
        spinSpeed = random.nextFloat() * 10 + 10; // Random initial speed between 10 and 20

    }

    @Override
    public void update() {
        if (wheelSpinning) {
            // The wheel is spinning; decrement spin time
            wheelSpinTime--;

            // Update angle based on current spin speed
            angle += spinSpeed;

            // Gradually decrease speed with friction
            spinSpeed = Math.max(spinSpeed - friction, 0.05f); // Minimum speed for smoother stop

            if (wheelSpinTime <= 0 || spinSpeed <= 0) {
                // Normalize angle to [0, 360) range
                float finalAngle = angle % 360;
                if (finalAngle < 0) {
                    finalAngle += 360;
                }

                // Determine which player's segment the wheel lands on
                if (finalAngle >= 0 && finalAngle < 180) {
                    selectedPlayer = 1; // Player 1 (Blue)
                    wheelResultText.setText("PLAYER 1 SELECTS THE MAP!");
                } else if (finalAngle >= 180 && finalAngle < 360) {
                    selectedPlayer = 2; // Player 2 (Red)
                    wheelResultText.setText("PLAYER 2 SELECTS THE MAP!");
                }

                // Stop the wheel and start the post-spin delay
                wheelSpinning = false;
                wheelFinished = true;
            }
        } else if (wheelFinished) {
            // Post-spin delay before removing the wheel
            postSpinDelay--;
            if (postSpinDelay <= 0) {
                explosionStarted = true; // Start the explosion effect
                wheelFinished = false;
            }
        } else if (explosionStarted) {
            // Handle the ripple explosion effect
            explosionTime--;
            rippleRadius += 15; // Expand the ripple effect faster

            if (explosionTime <= 0) {
                explosionStarted = false;
                removeWheel = true; // Mark wheel as removed after explosion
            }
        } else if (removeWheel) {
            // Proceed to map selection for the chosen player
            handleMapSelection();
        }
    }

    private void handleMapSelection() {
        // Player 1 selects the map
        if (selectedPlayer == 1) {
            if (Keyboard.isKeyDown(Key.S) && keyPressTimer == 0) {
                keyPressTimer = 14;
                currentMapItemHovered++;
            } else if (Keyboard.isKeyDown(Key.W) && keyPressTimer == 0) {
                keyPressTimer = 14;
                currentMapItemHovered--;
            }
        }
        // Player 2 selects the map
        else if (selectedPlayer == 2) {
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

        // Highlight the hovered map item
        for (int i = 0; i < mapItems.size(); i++) {
            if (i == currentMapItemHovered) {
                mapItems.get(i).setColor(new Color(255, 215, 0)); // Highlight color
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
            selectedMapName = mapItems.get(currentMapItemHovered).getText(); // Get the map name text
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
            // Draw the ripple explosion effect
            drawRippleExplosion(graphicsHandler);
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
            Color pointerColor = (selectedPlayer == 1) ? new Color(49, 207, 240) : new Color(240, 49, 49); // Blue for
                                                                                                           // Player 1,
                                                                                                           // Red for
                                                                                                           // Player 2
            graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, pointerColor,
                    Color.black, 2);
        }
    }

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

        // Draw a pointer above the wheel
        g2d.setColor(Color.WHITE);
        int pointerHeight = 20;
        g2d.fillPolygon(
                new int[] { centerX - 10, centerX + 10, centerX },
                new int[] { centerY - wheelRadius - pointerHeight, centerY - wheelRadius - pointerHeight,
                        centerY - wheelRadius },
                3);
    }

    ;

    private void drawRippleExplosion(GraphicsHandler graphicsHandler) {
        Graphics2D g2d = (Graphics2D) graphicsHandler.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Center of the ripple explosion
        int centerX = 400;
        int centerY = 300;

        // Draw concentric circles for the ripple effect
        for (int i = 0; i < 5; i++) {
            int currentRadius = rippleRadius - (i * 20);
            if (currentRadius > 0) {
                float alpha = Math.max(0, 1.0f - (i * 0.2f)); // Fade outer ripples
                g2d.setColor(new Color(255, 100, 0, (int) (alpha * 255))); // Fading orange
                g2d.drawOval(centerX - currentRadius, centerY - currentRadius, currentRadius * 2, currentRadius * 2);
            }
        }
    }
}
