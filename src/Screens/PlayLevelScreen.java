package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import Level.HealthBar;
import Level.Map;
import Level.Player;
import Level.PlayerListener;
import Maps.Map2;
import Maps.ToadsMap;
import Players.Brawler;
import Players.Brawler2;
import Players.Knight;
import Players.Knight2;
import Players.Mage;
import Players.Mage2;
import SpriteFont.SpriteFont;
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

        // Initialize map based on selected map name
        if (selectedMapName.equals("Inferno")) {
            this.map = new ToadsMap();
        } else if (selectedMapName.equals("Ice Kingdom")) {
            this.map = new Map2();
        }

        // Setup player 1 based on the selected character
        if (selectedCharacterP1 == CharacterScreen.SelectedCharacter.BRAWLER) {
            this.player = new Brawler(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        } else if (selectedCharacterP1 == CharacterScreen.SelectedCharacter.SWORDSMAN) {
            this.player = new Knight(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        } else if (selectedCharacterP1 == CharacterScreen.SelectedCharacter.GUNNER) {
            this.player = new Mage(map.getPlayerStartPosition().x, map.getPlayerStartPosition().y);
        }
        int player2OffsetX = 430;
        // Setup player 2 based on the selected character
        if (selectedCharacterP2 == CharacterScreen.SelectedCharacter.BRAWLER) {
            this.player2 = new Brawler2(map.getPlayerStartPosition().x + player2OffsetX,
                    map.getPlayerStartPosition().y); // Player 2
        } else if (selectedCharacterP2 == CharacterScreen.SelectedCharacter.SWORDSMAN) {
            this.player2 = new Knight2(map.getPlayerStartPosition().x + player2OffsetX, map.getPlayerStartPosition().y); // Player
                                                                                                                         // 2
        } else if (selectedCharacterP2 == CharacterScreen.SelectedCharacter.GUNNER) {
            this.player2 = new Mage2(map.getPlayerStartPosition().x + player2OffsetX, map.getPlayerStartPosition().y); // Player
                                                                                                                       // 2
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
                    checkAttackCollisions();
    
                    // Determine if any player has lost all health and set the winner
                    if (player2.getPlayerHealth() <= 0) {
                        isPlayer1Winner = true;
                        playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
                        levelLoseScreen = new LevelLoseScreen(this, isPlayer1Winner);
                        stopBackgroundMusic();
                    } else if (player.getPlayerHealth() <= 0) {
                        isPlayer1Winner = false;
                        playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
                        levelLoseScreen = new LevelLoseScreen(this, isPlayer1Winner);
                        stopBackgroundMusic();
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
    
    private void checkAttackCollisions() {
        if (player instanceof Knight && player2 instanceof Knight2) {
            Knight knight1 = (Knight) player;
            Knight2 knight2 = (Knight2) player2;
    
            if (!attackProcessed) { // Only check if not processed
                if (knight1.getAttackHitbox().intersects(knight2.getHitbox())) {
                    handleAttackCollision1();
                    attackProcessed = true; // Mark as processed
                }
                if (knight2.getAttackHitbox().intersects(knight1.getHitbox())) {
                    handleAttackCollision2();
                    attackProcessed = true; // Mark as processed
                }
            }
    
            // Reset attackProcessed flag if the hitboxes are no longer intersecting
            if (!knight1.getAttackHitbox().intersects(knight2.getHitbox()) &&
                    !knight2.getAttackHitbox().intersects(knight1.getHitbox())) {
                attackProcessed = false; // Reset if no longer colliding
            }
        }
    
        // Other combinations for Brawler, Mage, etc.
        // Repeat the same structure as above for other player types
        // Example for Brawler vs. Brawler2 and other classes
        // ...
    
    

                    if (player instanceof Brawler && player2 instanceof Brawler2) {
                        Brawler brawler1 = (Brawler) player;
                        Brawler2 brawler2 = (Brawler2) player2;

                        if (!attackProcessed) { // Only check if not processed
                            if (brawler1.getAttackHitbox().intersects(brawler2.getHitbox())) {
                                handleAttackCollision1();
                                attackProcessed = true; // Mark as processed
                            }
                            if (brawler2.getAttackHitbox().intersects(brawler1.getHitbox())) {
                                handleAttackCollision2();
                                attackProcessed = true; // Mark as processed
                            }
                        }

                        // Reset attackProcessed flag if the hitboxes are no longer intersecting
                        if (!brawler1.getAttackHitbox().intersects(brawler2.getHitbox()) &&
                                !brawler2.getAttackHitbox().intersects(brawler1.getHitbox())) {
                            attackProcessed = false; // Reset if no longer colliding
                        }
                    }

                    if (player instanceof Mage && player2 instanceof Mage2) {
                        Mage mage1 = (Mage) player;
                        Mage2 mage2 = (Mage2) player2;

                        if (!attackProcessed) { // Only check if not processed
                            if (mage1.getAttackHitbox().intersects(mage2.getHitbox())) {
                                handleAttackCollision1();
                                attackProcessed = true; // Mark as processed
                            }
                            if (mage1.getAttackHitbox().intersects(mage2.getHitbox())) {
                                handleAttackCollision2();
                                attackProcessed = true; // Mark as processed
                            }
                        }

                        // Reset attackProcessed flag if the hitboxes are no longer intersecting
                        if (!mage1.getAttackHitbox().intersects(mage2.getHitbox()) &&
                                !mage2.getAttackHitbox().intersects(mage1.getHitbox())) {
                            attackProcessed = false; // Reset if no longer colliding
                        }
                    }

                    if (player instanceof Knight && player2 instanceof Mage2) {
                        Knight knight1 = (Knight) player;
                        Mage2 mage2 = (Mage2) player2;

                        if (!attackProcessed) { // Only check if not processed
                            if (knight1.getAttackHitbox().intersects(mage2.getHitbox())) {
                                handleAttackCollision1();
                                attackProcessed = true; // Mark as processed
                            }
                            if (mage2.getAttackHitbox().intersects(knight1.getHitbox())) {
                                handleAttackCollision2();
                                attackProcessed = true; // Mark as processed
                            }
                        }

                        // Reset attackProcessed flag if the hitboxes are no longer intersecting
                        if (!knight1.getAttackHitbox().intersects(mage2.getHitbox()) &&
                                !mage2.getAttackHitbox().intersects(knight1.getHitbox())) {
                            attackProcessed = false; // Reset if no longer colliding
                        }
                    }

                    if (player instanceof Knight && player2 instanceof Brawler2) {
                        Knight knight1 = (Knight) player;
                        Brawler2 brawler2 = (Brawler2) player2;

                        if (!attackProcessed) { // Only check if not processed
                            if (knight1.getAttackHitbox().intersects(brawler2.getHitbox())) {
                                handleAttackCollision1();
                                attackProcessed = true; // Mark as processed
                            }
                            if (brawler2.getAttackHitbox().intersects(knight1.getHitbox())) {
                                handleAttackCollision2();
                                attackProcessed = true; // Mark as processed
                            }
                        }

                        // Reset attackProcessed flag if the hitboxes are no longer intersecting
                        if (!knight1.getAttackHitbox().intersects(brawler2.getHitbox()) &&
                                !brawler2.getAttackHitbox().intersects(knight1.getHitbox())) {
                            attackProcessed = false; // Reset if no longer colliding
                        }
                    }

                    if (player instanceof Mage && player2 instanceof Knight2) {
                        Mage mage1 = (Mage) player;
                        Knight2 knight2 = (Knight2) player2;

                        if (!attackProcessed) { // Only check if not processed
                            if (mage1.getAttackHitbox().intersects(knight2.getHitbox())) {
                                handleAttackCollision1();
                                attackProcessed = true; // Mark as processed
                            }
                            if (knight2.getAttackHitbox().intersects(mage1.getHitbox())) {
                                handleAttackCollision2();
                                attackProcessed = true; // Mark as processed
                            }
                        }

                        // Reset attackProcessed flag if the hitboxes are no longer intersecting
                        if (!mage1.getAttackHitbox().intersects(knight2.getHitbox()) &&
                                !knight2.getAttackHitbox().intersects(mage1.getHitbox())) {
                            attackProcessed = false; // Reset if no longer colliding
                        }
                    }

                    if (player instanceof Mage && player2 instanceof Brawler2) {
                        Mage mage1 = (Mage) player;
                        Brawler2 brawler2 = (Brawler2) player2;

                        if (!attackProcessed) { // Only check if not processed
                            if (mage1.getAttackHitbox().intersects(brawler2.getHitbox())) {
                                handleAttackCollision1();
                                attackProcessed = true; // Mark as processed
                            }
                            if (brawler2.getAttackHitbox().intersects(mage1.getHitbox())) {
                                handleAttackCollision2();
                                attackProcessed = true; // Mark as processed
                            }
                        }

                        // Reset attackProcessed flag if the hitboxes are no longer intersecting
                        if (!mage1.getAttackHitbox().intersects(brawler2.getHitbox()) &&
                                !brawler2.getAttackHitbox().intersects(mage1.getHitbox())) {
                            attackProcessed = false; // Reset if no longer colliding
                        }
                    }

                    if (player instanceof Brawler && player2 instanceof Knight2) {
                        Brawler brawler1 = (Brawler) player;
                        Knight2 knight2 = (Knight2) player2;

                        if (!attackProcessed) { // Only check if not processed
                            if (brawler1.getAttackHitbox().intersects(knight2.getHitbox())) {
                                handleAttackCollision1();
                                attackProcessed = true; // Mark as processed
                            }
                            if (knight2.getAttackHitbox().intersects(brawler1.getHitbox())) {
                                handleAttackCollision2();
                                attackProcessed = true; // Mark as processed
                            }
                        }

                        // Reset attackProcessed flag if the hitboxes are no longer intersecting
                        if (!brawler1.getAttackHitbox().intersects(knight2.getHitbox()) &&
                                !knight2.getAttackHitbox().intersects(brawler1.getHitbox())) {
                            attackProcessed = false; // Reset if no longer colliding
                        }
                    }

                    if (player instanceof Brawler && player2 instanceof Mage2) {
                        Brawler brawler1 = (Brawler) player;
                        Mage2 mage2 = (Mage2) player2;

                        if (!attackProcessed) { // Only check if not processed
                            if (brawler1.getAttackHitbox().intersects(mage2.getHitbox())) {
                                handleAttackCollision1();
                                attackProcessed = true; // Mark as processed
                            }
                            if (mage2.getAttackHitbox().intersects(brawler1.getHitbox())) {
                                handleAttackCollision2();
                                attackProcessed = true; // Mark as processed
                            }
                        }

                        // Reset attackProcessed flag if the hitboxes are no longer intersecting
                        if (!brawler1.getAttackHitbox().intersects(mage2.getHitbox()) &&
                                !mage2.getAttackHitbox().intersects(brawler1.getHitbox())) {
                            attackProcessed = false; // Reset if no longer colliding
                        }
                    }
                
                   
                        switch (playLevelScreenState) {
                            case RUNNING:
                                if (!gameOver) { // Only run this logic if the game is not over
                                    try {
                                        player.update(); // Player 1
                                        player2.update(); // Player 2
                
                                      
                
                                        // Determine if any player has lost all health and set the winner
                                        if (player2.getPlayerHealth() <= 0) {
                                            isPlayer1Winner = true;  // Player 1 wins
                                            gameOver = true;         // Set gameOver to true to prevent re-triggering
                                            System.out.println("Player 1 wins! Setting isPlayer1Winner to " + isPlayer1Winner);
                                            playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
                                            levelLoseScreen = new LevelLoseScreen(this, isPlayer1Winner);  // Pass the correct flag
                                            stopBackgroundMusic();
                                        } else if (player.getPlayerHealth() <= 0) {
                                            isPlayer1Winner = false;  // Player 2 wins
                                            gameOver = true;          // Set gameOver to true to prevent re-triggering
                                            System.out.println("Player 2 wins! Setting isPlayer1Winner to " + isPlayer1Winner);
                                            playLevelScreenState = PlayLevelScreenState.LEVEL_LOSE;
                                            levelLoseScreen = new LevelLoseScreen(this, isPlayer1Winner);  // Pass the correct flag
                                            stopBackgroundMusic();
                                        }
                
                                        map.update(player);
                                        map.update(player2);
                                    } catch (NullPointerException e) {
                                        System.err.println("Error updating player: " + e.getMessage());
                                    }
                                }
                                break;
                
                            case LEVEL_LOSE:
                                levelLoseScreen.update();
                                break;
                
                            case LEVEL_COMPLETED:
                                // Handle level completed logic...
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

                playerOneHB.draw(graphicsHandler, player.getPlayerHealth());
                playerTwoHB.draw(graphicsHandler, player2.getPlayerHealth());

                // Draw Second Health Bar Here

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

    private void handleAttackCollision1() {
        int damage = 10;
        player2.damagePlayer(damage);
    
        // Debug output to confirm damage and health status
        System.out.println("Player 2 damaged! New health: " + player2.getPlayerHealth());
    
        // Check if player2's health is zero or below to trigger a loss
        if (player2.getPlayerHealth() <= 1) {
            onDeath(); // Trigger level lose
        }
    }
    
    private void handleAttackCollision2() {
        int damage = 10;
        player.damagePlayer(damage);
    
        // Debug output to confirm damage and health status
        System.out.println("Player 1 damaged! New health: " + player.getPlayerHealth());
    
        // Check if player's health is zero or below to trigger a loss
        if (player.getPlayerHealth() <= 1) {
            onDeath(); // Trigger level lose
        }
    }
    
}
