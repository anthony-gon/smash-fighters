package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import GameObject.Rectangle;
import Level.HealthBar;
import Level.LivesDisplay;
import Level.Map;
import Level.Player;
import Level.PlayerListener;
import Level.PlayerState;
import Maps.Map2;
import Maps.Toad;
import Maps.ToadsMap;
import Players.Brawler;
import Players.Brawler2;
import Players.Knight;
import Players.Knight2;
import Players.Mage;
import Players.Mage2;
import SpriteFont.SpriteFont;
import Utils.Direction;
import java.awt.Color;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class PlayLevelScreen extends Screen implements PlayerListener {
    protected ScreenCoordinator screenCoordinator;
    protected Map map;

    protected Player player; // Player 1
    protected Player player2; // Player 2

    protected boolean attackProcessed = false;

    protected HealthBar playerOneHB;
    protected HealthBar playerTwoHB;

    protected LivesDisplay playerOneL;
    protected LivesDisplay playerTwoL;

    protected String[] characters = { "Brawler", "Knight", "Gunner" };
    protected String playerOneChar; // Stores what type of player player one is for hitbox purposes
    protected String playerTwoChar; // Stores what type of player player two is for hitbox purposes

    protected int playerOneHurtTimer = 0;
    protected int playerTwoHurtTimer = 0;

    protected int playerOneHurtDelay = 0;
    protected float playerTwoHurtDelay = 0;

    protected boolean kbPlayerTwoLeft = false;
    protected boolean kbPlayerTwoRight = false;

    protected boolean kbPlayerOneLeft = false;
    protected boolean kbPlayerOneRight = false;

    protected KeyLocker keyLocker2 = new KeyLocker();
    protected float playerOneTimerTwo = System.currentTimeMillis();
    protected float playerTwoTimerTwo = System.currentTimeMillis();

    protected int playerOneHitTimer = 0;
    protected int playerTwoHitTimer = 0;

    protected boolean playerOneAttackStarted = false;
    protected boolean playerTwoAttackStarted = false;

    protected PlayLevelScreenState playLevelScreenState;
    protected int screenTimer;
    protected LevelClearedScreen levelClearedScreen;
    protected LevelLoseScreen levelLoseScreen;
    protected boolean levelCompletedStateChangeStart;
    protected boolean isPlayer1Winner; // Field to track if Player 1 is the winner
    protected boolean gameOver = false;

    // Pause menu variables
    protected SpriteFont resumeOption;
    protected SpriteFont exitToMenuOption;
    protected List<SpriteFont> pauseMenuItems;
    protected int currentPauseMenuItemHovered = 0;
    protected int pausePointerLocationX, pausePointerLocationY;
    protected int pausePointerOffsetX = 20;
    protected int pausePointerOffsetY = 5;
    protected KeyLocker keyLocker = new KeyLocker();
    protected int previousHealth = -1;

    // Music-related properties
    protected Clip musicClip;

    public PlayLevelScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Get the selected map name from the ScreenCoordinator
        String selectedMapName = screenCoordinator.getSelectedMap();

        CharacterScreen.SelectedCharacter selectedCharacterP1 = screenCoordinator.getCharacterScreen()
                .getSelectedCharacterP1();
        CharacterScreen.SelectedCharacter selectedCharacterP2 = screenCoordinator.getCharacterScreen()
                .getSelectedCharacterP2();

        this.playerOneHB = new HealthBar(0, 100, 100);
        this.playerTwoHB = new HealthBar(1, 100, 100);
        playerOneHB.loadHealthBars();
        playerTwoHB.loadHealthBars();

        this.playerOneL = new LivesDisplay(0, 3, 3);
        this.playerTwoL = new LivesDisplay(1, 3, 3);
        playerOneL.loadLives();
        playerTwoL.loadLives();

        // Initialize map based on selected map name
        if (selectedMapName.equals("Inferno")) {
            this.map = new ToadsMap();
        } else if (selectedMapName.equals("Tundra")) {
            this.map = new Map2();
        } else if (selectedMapName.equals("Toads")) {
            this.map = new Toad();
        } 

        // Setup player 1 based on the selected character
        if (selectedCharacterP1 == CharacterScreen.SelectedCharacter.BRAWLER) {
            this.player = new Brawler(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
            playerOneChar = characters[0];
        } else if (selectedCharacterP1 == CharacterScreen.SelectedCharacter.SWORDSMAN) {
            this.player = new Knight(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
            playerOneChar = characters[1];
        } else if (selectedCharacterP1 == CharacterScreen.SelectedCharacter.GUNNER) {
            this.player = new Mage(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
            playerOneChar = characters[2];
        }
        int player2OffsetX = 430;
        // Setup player 2 based on the selected character
        if (selectedCharacterP2 == CharacterScreen.SelectedCharacter.BRAWLER) {
            this.player2 = new Brawler2(map.getPlayerStartPosition().x + player2OffsetX,
                    map.getPlayerStartPosition().y); // Player 2
            playerTwoChar = characters[0];
        } else if (selectedCharacterP2 == CharacterScreen.SelectedCharacter.SWORDSMAN) {
            this.player2 = new Knight2(map.getPlayerStartPosition().x + player2OffsetX, map.getPlayerStartPosition().y); // Player
                                                                                                                         // 2
            playerTwoChar = characters[1];

        } else if (selectedCharacterP2 == CharacterScreen.SelectedCharacter.GUNNER) {
            this.player2 = new Mage2(map.getPlayerStartPosition().x + player2OffsetX, map.getPlayerStartPosition().y); // Player
                                                                                                                       // 2
            playerTwoChar = characters[2];

        }

        // **Assign movement keys for Player 2 (JIKL)**
        player2.setMovementKeys(Key.I, Key.J, Key.L, Key.K, Key.O); // JIKL for movement, U for attack

        // Attach players to the map and add listeners
        this.player.setMap(map);
        this.player2.setMap(map);
        this.player.addListener(this);
        this.player2.addListener(this);

        levelClearedScreen = new LevelClearedScreen();
        levelLoseScreen = new LevelLoseScreen(this, attackProcessed);

        this.playLevelScreenState = PlayLevelScreenState.RUNNING;

        resumeOption = new SpriteFont("RESUME", 300, 200, "fibberish", 30, new Color(49, 207, 240));
        exitToMenuOption = new SpriteFont("EXIT TO MENU", 300, 250, "fibberish", 30, new Color(49, 207, 240));
        pauseMenuItems = Arrays.asList(resumeOption, exitToMenuOption);
        keyLocker.lockKey(Key.ESC);

        // Play the background music when the PlayLevelScreen starts
        playBackgroundMusic();
    }

    // Used to calculate the offset and position of each players hit box
    // Returns Rectangle of where the hitbox should be
    public Rectangle playerOneHitboxPos() {
        if (playerOneChar == characters[0]) {
            return new Rectangle((int) player.getX() + 20, (int) player.getY() + 20, 35, 35);
        } else if (playerOneChar == characters[1]) {
            return new Rectangle((int) player.getX() + 20, (int) player.getY() + 20, 35, 35);
        }
        return new Rectangle((int) player.getX() + 20, (int) player.getY() + 20, 35, 35);
    }

    public Rectangle playerTwoHitboxPos() {
        if (playerTwoChar == characters[0]) {
            return new Rectangle((int) player2.getX() + 20, (int) player2.getY() + 20, 35, 35);
        } else if (playerTwoChar == characters[1]) {
            return new Rectangle((int) player2.getX() + 20, (int) player2.getY() + 20, 35, 35);
        }
        return new Rectangle((int) player2.getX() + 20, (int) player2.getY() + 20, 35, 35);
    }

    public Rectangle playerOneHurtBox() {
        if (playerOneChar == characters[0]) {
            if (player.getFacingDirection() == Direction.LEFT) {
                if (player.getPlayerState() == PlayerState.ATTACKING) {
                    return new Rectangle((int) player.getX() - 5, (int) player.getY() + 25, 15, 15);
                }
            } else if (player.getFacingDirection() == Direction.RIGHT) {
                if (player.getPlayerState() == PlayerState.ATTACKING) {
                    return new Rectangle((int) player.getX() + 60, (int) player.getY() + 25, 15, 15);
                }
            }
        }
        return new Rectangle();
    }

    public Rectangle playerTwoHurtBox() {
        if (playerTwoChar == characters[0]) {
            if (player2.getFacingDirection() == Direction.LEFT) {
                if (player2.getPlayerState() == PlayerState.ATTACKING) {
                    return new Rectangle((int) player2.getX() - 5, (int) player2.getY() + 25, 15, 15);
                }
            } else if (player2.getFacingDirection() == Direction.RIGHT) {
                if (player2.getPlayerState() == PlayerState.ATTACKING) {
                    return new Rectangle((int) player2.getX() + 60, (int) player2.getY() + 25, 15, 15);
                }
            }
        }
        return new Rectangle();

    }

    public void playerOneHitDetection() {
        playerOneHurtTimer += 1;
        if (playerOneHurtTimer > 15) {
            if (playerOneHurtBox().intersects(playerTwoHitboxPos())) {
                player2.damagePlayer(10);
                playerOneHurtTimer = 0;
                if (player.getX() > player2.getX()) {
                    kbPlayerTwoLeft = true;
                } else {
                    kbPlayerTwoRight = true;
                }
            }
        }
    }

    public void playerTwoHitDetection() {
        playerTwoHurtTimer += 1;
        if (playerTwoHurtTimer > 15) {
            if (playerTwoHurtBox().intersects(playerOneHitboxPos())) {
                playerTwoHurtTimer = 0;
                player.damagePlayer(10);
                if (player.getX() < player2.getX()) {
                    kbPlayerOneLeft = true;
                } else {
                    kbPlayerOneRight = true;
                }
            }
        }
    }

    @Override
    public void update() {
        // Handle pause input with the ESCAPE key
        if (Keyboard.isKeyDown(Key.ESC) && !keyLocker.isKeyLocked(Key.ESC)) {
            if (playLevelScreenState == PlayLevelScreenState.RUNNING) {
                playLevelScreenState = PlayLevelScreenState.PAUSED;
                stopBackgroundMusic(); // Stop music when the game is paused
            } else if (playLevelScreenState == PlayLevelScreenState.PAUSED) {
                playLevelScreenState = PlayLevelScreenState.RUNNING;
                playBackgroundMusic(); // Resume music when unpaused
            }
            keyLocker.lockKey(Key.ESC);
        }

        if (Keyboard.isKeyUp(Key.ESC)) {
            keyLocker.unlockKey(Key.ESC);
        }

        switch (playLevelScreenState) {
            case RUNNING:
                try {
                    player.update(); // Player 1
                    player2.update(); // Player 2

                    // Check each player combination for attack collisions

                    // Determine if any player has lost all health and set the winner
                    if (player2.getPlayerHealth() <= 0) {
                        kbPlayerTwoLeft = false;
                        kbPlayerTwoRight = false;
                        player2.removeLives(1);
                        player2.setLocation(map.getPlayerStartPosition().x + 410, map.getPlayerStartPosition().y);

                        if (player2.getPlayerLives() > 0) {
                            player2.resetHealth(100);
                        } else {
                            isPlayer1Winner = true;
                            playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
                            levelLoseScreen = new LevelLoseScreen(this, isPlayer1Winner);
                            stopBackgroundMusic();
                        }
                    } else if (player.getPlayerHealth() <= 0) {
                        kbPlayerOneLeft = false;
                        kbPlayerOneRight = false;
                        player.removeLives(1);
                        player.setLocation(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);

                        if (player.getPlayerLives() > 0) {
                            player.resetHealth(100);
                        } else {
                            isPlayer1Winner = false;
                            playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
                            levelLoseScreen = new LevelLoseScreen(this, isPlayer1Winner);
                            stopBackgroundMusic();
                        }
                    }

                    map.update(player);
                    map.update(player2);
                } catch (NullPointerException e) {
                    System.err.println("Error updating player: " + e.getMessage());
                }
                break;

            case LEVEL_COMPLETED:
                if (levelCompletedStateChangeStart) {
                    screenTimer = 130;
                    levelCompletedStateChangeStart = false;
                } else {
                    levelClearedScreen.update();
                    screenTimer--;
                    if (screenTimer == 0) {
                        goBackToMenu();
                    }
                }
                break;

            case LEVEL_LOSE:
                stopBackgroundMusic(); // Stop music when the player dies
                levelLoseScreen.update();
                break;

            case PAUSED:
                updatePauseMenu();
                break;
        }
    }

    private void updatePauseMenu() {
        if (Keyboard.isKeyDown(Key.DOWN) && !keyLocker.isKeyLocked(Key.DOWN)) {
            currentPauseMenuItemHovered++;
            if (currentPauseMenuItemHovered >= pauseMenuItems.size()) {
                currentPauseMenuItemHovered = 0;
            }
            keyLocker.lockKey(Key.DOWN);
        } else if (Keyboard.isKeyDown(Key.UP) && !keyLocker.isKeyLocked(Key.UP)) {
            currentPauseMenuItemHovered--;
            if (currentPauseMenuItemHovered < 0) {
                currentPauseMenuItemHovered = pauseMenuItems.size() - 1;
            }
            keyLocker.lockKey(Key.UP);
        }

        if (Keyboard.isKeyUp(Key.UP)) {
            keyLocker.unlockKey(Key.UP);
        }
        if (Keyboard.isKeyUp(Key.DOWN)) {
            keyLocker.unlockKey(Key.DOWN);
        }

        if (Keyboard.isKeyDown(Key.SPACE) && !keyLocker.isKeyLocked(Key.SPACE)) {
            if (currentPauseMenuItemHovered == 0) {
                playLevelScreenState = PlayLevelScreenState.RUNNING;
                playBackgroundMusic(); // Resume music when unpaused
            } else if (currentPauseMenuItemHovered == 1) {
                goBackToMenu();
            }
            keyLocker.lockKey(Key.SPACE);
        }

        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }

        pausePointerLocationX = (int) pauseMenuItems.get(currentPauseMenuItemHovered).getX() - pausePointerOffsetX;
        pausePointerLocationY = (int) pauseMenuItems.get(currentPauseMenuItemHovered).getY() - pausePointerOffsetY;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        switch (playLevelScreenState) {
            case RUNNING:
                map.draw(graphicsHandler);

                player.draw(graphicsHandler); // Draw Player 1
                player2.draw(graphicsHandler); // Draw Player 2

                // playerOneHitboxPos().draw(graphicsHandler);
                // playerTwoHitboxPos().draw(graphicsHandler);

                playerOneHitboxPos();
                playerTwoHitboxPos();

                playerOneHurtBox();
                playerTwoHurtBox();

                playerOneHitDetection();
                playerTwoHitDetection();

                if (kbPlayerTwoLeft == true) {
                    System.out.println("should kb");
                    if (player2.applyKnockbackLeft() == false) {
                        kbPlayerTwoLeft = false;
                    }
                } else if (kbPlayerTwoRight == true) {
                    System.out.println("should kb");
                    if (player2.applyKnockbackRight() == false) {
                        kbPlayerTwoRight = false;
                    }

                }
                if (kbPlayerOneLeft == true) {
                    System.out.println("should kb");
                    if (player.applyKnockbackLeft() == false) {
                        kbPlayerOneLeft = false;
                    }
                } else if (kbPlayerOneRight == true) {
                    System.out.println("should kb");
                    if (player.applyKnockbackRight() == false) {
                        kbPlayerOneRight = false;
                    }

                }

                playerOneHB.draw(graphicsHandler, player.getPlayerHealth());
                playerTwoHB.draw(graphicsHandler, player2.getPlayerHealth());

                playerOneL.draw(graphicsHandler, player.getPlayerLives());
                playerTwoL.draw(graphicsHandler, player2.getPlayerLives());

                // Draw Second Health Bar Herea

                // Draw health bar

                // Only log health when it changes
                if (player.getPlayerHealth() != previousHealth) {
                    System.out.println("Player health: " + player.getPlayerHealth());
                    previousHealth = player.getPlayerHealth();
                }

                break;

            case LEVEL_COMPLETED:
                levelClearedScreen.draw(graphicsHandler);
                break;

            case LEVEL_LOSE:
                levelLoseScreen.draw(graphicsHandler);
                break;

            case PAUSED:
                map.draw(graphicsHandler);
                player.draw(graphicsHandler);
                drawPauseMenu(graphicsHandler);
                break;
        }
    }

    private void drawPauseMenu(GraphicsHandler graphicsHandler) {
        graphicsHandler.drawFilledRectangleWithBorder(250, 150, 300, 200, new Color(0, 0, 0, 150), Color.white, 3);

        for (SpriteFont menuItem : pauseMenuItems) {
            menuItem.draw(graphicsHandler);
        }

        // Draw pointer
        graphicsHandler.drawFilledRectangleWithBorder(pausePointerLocationX, pausePointerLocationY, 20, 20,
                new Color(49, 207, 240), Color.black, 2);
    }

    public PlayLevelScreenState getPlayLevelScreenState() {
        return playLevelScreenState;
    }

    @Override
    public void onLevelCompleted() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_COMPLETED) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_COMPLETED;
            levelCompletedStateChangeStart = true;
        }
    }

    @Override
    public void onDeath() {
        if (playLevelScreenState != PlayLevelScreenState.LEVEL_LOSE) {
            playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
            stopBackgroundMusic(); // Stop music when the player dies
        }
    }

    public void resetLevel() {
        initialize();
    }

    public void goBackToMenu() {
        stopBackgroundMusic(); // Stop the music when going back to the menu
        screenCoordinator.setGameState(GameState.MENU);
    }

    // Method to load and play background music
    private void playBackgroundMusic() {
        try {
            String filePath = "Resources/Fighting.wav"; // Path to your background music
            File soundFile = new File(filePath);

            if (!soundFile.exists()) {
                System.err.println("Sound file not found at path: " + soundFile.getAbsolutePath());
                return;
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioInputStream);

            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volumeControl.getMaximum()); // Set to max volume
            musicClip.loop(Clip.LOOP_CONTINUOUSLY); // Play music in a loop
            musicClip.start();
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

    private enum PlayLevelScreenState {
        RUNNING, LEVEL_COMPLETED, LEVEL_LOSE, PAUSED
    }

}