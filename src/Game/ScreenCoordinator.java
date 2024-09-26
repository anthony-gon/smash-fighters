package Game;

import Engine.DefaultScreen;
import Engine.GraphicsHandler;
import Engine.Screen;
import Screens.CreditsScreen;
import Screens.MenuScreen;
import Screens.PlayLevelScreen;
import Screens.PracticeRangeScreen;
import Screens.HowToPlayScreen;
import Screens.ErrorScreen;

/*
 * Based on the current game state, this class determines which Screen should be shown.
 * There can only be one "currentScreen", although screens can have "nested" screens.
 */
public class ScreenCoordinator extends Screen {
    // Currently shown Screen
    protected Screen currentScreen = new DefaultScreen();

    // Keep track of gameState so ScreenCoordinator knows which Screen to show
    protected GameState gameState;
    protected GameState previousGameState;

    public GameState getGameState() {
        return gameState;
    }

    // Other Screens can set the gameState of this class to force it to change the currentScreen
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void initialize() {
        // Start game off with Error Screen
        gameState = GameState.ERROR_SCREEN;
    }

    @Override
    public void update() {
        // Check for state change
        if (previousGameState != gameState) {
            switch (gameState) {
                case ERROR_SCREEN:
                    currentScreen = new ErrorScreen(this);
                    break;
                case MENU:
                    currentScreen = new MenuScreen(this);
                    break;
                case LEVEL:
                    currentScreen = new PlayLevelScreen(this);
                    break;
                case CREDITS:
                    currentScreen = new CreditsScreen(this);
                    break;
                case PRACTICE_RANGE:
                    int windowWidth = 800; // Replace with your actual width
                    int windowHeight = 600; // Replace with your actual height
                    currentScreen = new PracticeRangeScreen(windowWidth, windowHeight);
                    break;
                case HOW_TO_PLAY:
                    currentScreen = new HowToPlayScreen(this); // Transition to HowToPlayScreen
                    break;
            }
            // Initialize the new screen after changing it
            currentScreen.initialize();
        }
        previousGameState = gameState;

        // Call the update method for the currentScreen
        if (currentScreen != null) {
            currentScreen.update();
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // Call the draw method for the currentScreen
        if (currentScreen != null) {
            currentScreen.draw(graphicsHandler);
        }
    }
}

