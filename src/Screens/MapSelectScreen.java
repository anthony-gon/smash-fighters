package Screens;

import Engine.*;
import Game.GameState;
import Game.ScreenCoordinator;
import SpriteFont.SpriteFont;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

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

    public MapSelectScreen(ScreenCoordinator screenCoordinator) {
        this.screenCoordinator = screenCoordinator;
    }

    @Override
    public void initialize() {
        // Initialize the map select buttons
        map1 = new SpriteFont("ToadsMap", 300, 200, "fibberish", 30, new Color(49, 207, 240));
        map1.setOutlineColor(Color.black);
        map1.setOutlineThickness(3);

        map2 = new SpriteFont("MAP 2", 300, 300, "fibberish", 30, new Color(49, 207, 240));
        map2.setOutlineColor(Color.black);
        map2.setOutlineThickness(3);

        // Add items to the mapItems list
        mapItems = Arrays.asList(map1, map2);

        keyPressTimer = 0;
        keyLocker.lockKey(Key.SPACE);
    }

    public void update() {
        // Change map item "hovered" over
        if (Keyboard.isKeyDown(Key.DOWN) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMapItemHovered++;
        } else if (Keyboard.isKeyDown(Key.UP) && keyPressTimer == 0) {
            keyPressTimer = 14;
            currentMapItemHovered--;
        } else {
            if (keyPressTimer > 0) {
                keyPressTimer--;
            }
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

        // Handle selection
        if (Keyboard.isKeyUp(Key.SPACE)) {
            keyLocker.unlockKey(Key.SPACE);
        }
        if (!keyLocker.isKeyLocked(Key.SPACE) && Keyboard.isKeyDown(Key.SPACE)) {
            // When a map is selected, set the selected map in ScreenCoordinator
            String selectedMapName = mapItems.get(currentMapItemHovered).getText(); // Get the map name text
            screenCoordinator.setSelectedMap(selectedMapName);

            // Transition to the character select screen
            screenCoordinator.setGameState(GameState.CHARACTER);
        }
    }

    public void draw(GraphicsHandler graphicsHandler) {
        for (SpriteFont mapItem : mapItems) {
            mapItem.draw(graphicsHandler);
        }
        // Draw the pointer
        graphicsHandler.drawFilledRectangleWithBorder(pointerLocationX, pointerLocationY, 20, 20, new Color(49, 207, 240), Color.black, 2);
    }
}
