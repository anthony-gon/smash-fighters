package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CharacterScreen extends Screen {
    protected ScreenCoordinator screenCoordinator;
    protected int currentCharacterItemHovered = 0; // Current character item being hovered over
    protected List<SpriteFont> characterItems; // List to hold character buttons
    protected SpriteFont brawler, swordsman, gunner; // Buttons for characters
    protected int keyPressTimer;
    protected int pointerLocationX, pointerLocationY;
    protected int pointerOffsetX = 30;
    protected int pointerOffsetY = 7;
    protected KeyLocker keyLocker = new KeyLocker();

    // Enum to represent character selections
    public enum SelectedCharacter {
        BRAWLER, SWORDSMAN, GUNNER
    }

    // Field to store the selected character
    protected SelectedCharacter selectedCharacter = SelectedCharacter.BRAWLER; // Default to BRAWLER

    public CharacterScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Initialize the character select buttons
        brawler = new SpriteFont("BRAWLER", 300, 200, "fibberish", 30, new Color(49, 207, 240));
        brawler.setOutlineColor(Color.black);
        brawler.setOutlineThickness(3);

        swordsman = new SpriteFont("SWORDSMAN", 300, 300, "fibberish", 30, new Color(49, 207, 240));
        swordsman.setOutlineColor(Color.black);
        swordsman.setOutlineThickness(3);

        gunner = new SpriteFont("GUNNER", 300, 400, "fibberish", 30, new Color(49, 207, 240));
        gunner.setOutlineColor(Color.black);
        gunner.setOutlineThickness(3);

        // Add items to the characterItems list
        characterItems = Arrays.asList(brawler, swordsman, gunner);

        keyPressTimer = 0;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // Change character item "hovered" over
        if (Keyboard.isKeyDown(Key.DOWN) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentCharacterItemHovered++;
            System.out.println("Hovered down: currentCharacterItemHovered = " + currentCharacterItemHovered);
        } else if (Keyboard.isKeyDown(Key.UP) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentCharacterItemHovered--;
            System.out.println("Hovered up: currentCharacterItemHovered = " + currentCharacterItemHovered);
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
        }

        // Loop the selection back around
        if (currentCharacterItemHovered >= characterItems.size()) {
            currentCharacterItemHovered = 0;
        } else if (currentCharacterItemHovered < 0) {
            currentCharacterItemHovered = characterItems.size() - 1;
        }

        // Update pointer location and item color
        for (int i = 0; i < characterItems.size(); i++) {
            if (i == currentCharacterItemHovered) {
                characterItems.get(i).setColor(new Color(255, 215, 0)); // Highlighted color
                // Fix casting issue by converting float to int
                pointerLocationX = (int) characterItems.get(i).getX() - pointerOffsetX;
                pointerLocationY = (int) characterItems.get(i).getY() - pointerOffsetY;
            } else {
                characterItems.get(i).setColor(new Color(49, 207, 240)); // Default color
            }
        }

        // Handle selection with SPACE key
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            System.out.println("SPACE pressed, currentCharacterItemHovered: " + currentCharacterItemHovered);
            // When a character is selected, transition to the play level screen
            switch (currentCharacterItemHovered) {
                case 0:
                    selectedCharacter = SelectedCharacter.BRAWLER;
                    System.out.println("Brawler selected");
                    break;
                case 1:
                    selectedCharacter = SelectedCharacter.SWORDSMAN;
                    System.out.println("Swordsman selected");
                    break;
                case 2:
                    selectedCharacter = SelectedCharacter.GUNNER;
                    System.out.println("Gunner selected");
                    break;
                default:
                    System.out.println("No character selected");
                    break;
            }
            // Transition to the next game state
            screenCoordinator.setGameState(GameState.LEVEL);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        // Draw character selection buttons
        for (SpriteFont characterItem : characterItems) {
            characterItem.draw(graphicsHandler);
        }
        // Draw the pointer next to the selected character
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
    }

    // Getter to retrieve the selected character
    public SelectedCharacter getSelectedCharacter() {
        return selectedCharacter;
    }
}
