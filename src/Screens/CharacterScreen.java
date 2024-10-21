package Screens;

import Engine.GraphicsHandler;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Engine.Screen;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class CharacterScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentCharacterItemHovered = 0; // Tracks which character is being hovered
    protected List<SpriteFont> characterItems; // List to hold character buttons
    protected SpriteFont brawler, swordsman, gunner; // Buttons for characters
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected int pointerOffsetX = 30;
    protected int pointerOffsetY = 7;
    protected KeyLocker keyLocker = new KeyLocker();

    // Text to show which player is currently selecting
    protected SpriteFont currentPlayerText;

    // Enum to represent character selections
    public enum SelectedCharacter {
        BRAWLER, SWORDSMAN, GUNNER
    }

    // Fields to store the selected character for Player 1 and Player 2
    protected SelectedCharacter selectedCharacterP1 = null;
    protected SelectedCharacter selectedCharacterP2 = null;

    // Tracks which player is currently selecting their character
    private int currentPlayer = 1;

    public CharacterScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Initialize the character select buttons
        brawler = new SpriteFont("BRAWLER", 100, 200, "fibberish", 30, new Color(49, 207, 240));
        brawler.setOutlineColor(Color.black);
        brawler.setOutlineThickness(3);

        swordsman = new SpriteFont("SWORDSMAN", 100, 300, "fibberish", 30, new Color(49, 207, 240));
        swordsman.setOutlineColor(Color.black);
        swordsman.setOutlineThickness(3);

        gunner = new SpriteFont("GUNNER", 100, 400, "fibberish", 30, new Color(49, 207, 240));
        gunner.setOutlineColor(Color.black);
        gunner.setOutlineThickness(3);

        // Add items to the characterItems list
        characterItems = Arrays.asList(brawler, swordsman, gunner);

        // Initialize text that shows which player is selecting
        currentPlayerText = new SpriteFont("PLAYER 1: SELECT YOUR CHARACTER ", 150, 120, "fibberish", 30, new Color(255, 255, 255));
        currentPlayerText.setOutlineColor(Color.black);
        currentPlayerText.setOutlineThickness(3);

        //More Text


        keyPressTimer = 0;
        keyLocker.lockKey(Key.SPACE); // Lock both keys initially
        keyLocker.lockKey(Key.ENTER);
    }

    public void update() {
        if (currentPlayer == 1) {
            currentPlayerText.setText("PLAYER 1: SELECT YOUR CHARACTER");
            handlePlayer1Selection(); // Handle Player 1's selection
        } else if (currentPlayer == 2) {
            currentPlayerText.setText("PLAYER 2: SELECT YOUR CHARACTER");
            handlePlayer2Selection(); // Handle Player 2's selection
        }
    }

    // Player 1 selection logic using WASD
    private void handlePlayer1Selection() {
        // Navigate character selection for Player 1
        if (Keyboard.isKeyDown(Key.S) && keyPressTimer == 0) { // 'S' for down
            keyPressTimer = 14;
            currentCharacterItemHovered++;
        } else if (Keyboard.isKeyDown(Key.W) && keyPressTimer == 0) { // 'W' for up
            keyPressTimer = 14;
            currentCharacterItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // Loop the selection back around for Player 1
        if (currentCharacterItemHovered >= characterItems.size()) {
            currentCharacterItemHovered = 0;
        } else if (currentCharacterItemHovered < 0) {
            currentCharacterItemHovered = characterItems.size() - 1;
        }

        // Update pointer location and item color for Player 1
        for (int i = 0; i < characterItems.size(); i++) {
            if (i == currentCharacterItemHovered) {
                characterItems.get(i).setColor(new Color(255, 215, 0)); // Highlighted color for P1
                pointerLocationX = (int) characterItems.get(i).getX() - pointerOffsetX;
                pointerLocationY = (int) characterItems.get(i).getY() - pointerOffsetY;
            } else {
                characterItems.get(i).setColor(new Color(49, 207, 240)); // Default color
            }
        }

        // Handle selection with SPACE key for Player 1
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE); // Unlock SPACE once it's released
        }

        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            // When a character is selected, assign it to Player 1
            switch (currentCharacterItemHovered) {
                case 0:
                    selectedCharacterP1 = SelectedCharacter.BRAWLER;
                    break;
                case 1:
                    selectedCharacterP1 = SelectedCharacter.SWORDSMAN;
                    break;
                case 2:
                    selectedCharacterP1 = SelectedCharacter.GUNNER;
                    break;
            }

            // Move to Player 2's character selection
            currentPlayer = 2;
            currentCharacterItemHovered = 0; // Reset hover position for Player 2
            System.out.println("Player 1 selected: " + selectedCharacterP1);
        }
    }

    // Player 2 selection logic using IJKL
    private void handlePlayer2Selection() {
        // Navigate character selection for Player 2
        if (Keyboard.isKeyDown(Key.K) && keyPressTimer == 0) { // 'K' for down
            keyPressTimer = 14;
            currentCharacterItemHovered++;
        } else if (Keyboard.isKeyDown(Key.I) && keyPressTimer == 0) { // 'I' for up
            keyPressTimer = 14;
            currentCharacterItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // Loop the selection back around for Player 2
        if (currentCharacterItemHovered >= characterItems.size()) {
            currentCharacterItemHovered = 0;
        } else if (currentCharacterItemHovered < 0) {
            currentCharacterItemHovered = characterItems.size() - 1;
        }

        // Update pointer location and item color for Player 2
        for (int i = 0; i < characterItems.size(); i++) {
            if (i == currentCharacterItemHovered) {
                characterItems.get(i).setColor(new Color(255, 0, 0)); // Highlighted color for P2
                pointerLocationX = (int) characterItems.get(i).getX() - pointerOffsetX;
                pointerLocationY = (int) characterItems.get(i).getY() - pointerOffsetY;
            } else {
                characterItems.get(i).setColor(new Color(49, 207, 240)); // Default color
            }
        }

        // Handle selection with ENTER key for Player 2
        if (Keyboard.isKeyUp(Key.ENTER)) {
            keyLocker.unlockKey(Key.ENTER); // Unlock ENTER once it's released
        }

        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.ENTER)) {
            // When a character is selected, assign it to Player 2
            switch (currentCharacterItemHovered) {
                case 0:
                    selectedCharacterP2 = SelectedCharacter.BRAWLER;
                    break;
                case 1:
                    selectedCharacterP2 = SelectedCharacter.SWORDSMAN;
                    break;
                case 2:
                    selectedCharacterP2 = SelectedCharacter.GUNNER;
                    break;
            }

            // Move to the game after Player 2 has selected their character
            screenCoordinator.setGameState(GameState.LEVEL);
            System.out.println("Player 2 selected: " + selectedCharacterP2);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // Draw the current player selection text
        currentPlayerText.draw(graphicsHandler);

        // Draw character selection buttons
        for (SpriteFont characterItem : characterItems) {
            characterItem.draw(graphicsHandler);
        }
        
        // Draw the pointer next to the selected character
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
    }

    // Methods to return selected characters for Player 1 and Player 2
    public SelectedCharacter getSelectedCharacterP1() {
        return selectedCharacterP1;
    }

    public SelectedCharacter getSelectedCharacterP2() {
        return selectedCharacterP2;
    }
}
