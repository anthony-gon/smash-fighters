package Game;

/*
 * This is used by the ScreenCoordinator class to determine which "state" the game is currently in
 */
public enum GameState {
   INTRO_SCREEN, GROUP_NAME, GAME_NAME, MENU, MAP_SELECT, CHARACTER, COUNTDOWN, LEVEL, CREDITS, PRACTICE_RANGE // Added PRACTICE_RANGE here
, HOW_TO_PLAY, PAUSE_SCREEN,
}