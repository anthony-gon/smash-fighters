package Level;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Config;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import java.awt.image.BufferedImage;

import java.awt.*;

public class LivesDisplay {
    protected int maxLives;
    protected int currentLives;
    protected int player;
    protected BufferedImage threeHearts;
    protected BufferedImage twoHearts;
    protected BufferedImage oneHeart;

    protected Color transpColor = new Color(255, 255, 255);

    protected SpriteSheet spriteSheet;
    protected int livesDisplayLen = 90;
    protected int barTwoX = (Config.GAME_WINDOW_WIDTH / 2) + livesDisplayLen;

    protected int x;
    protected int y;

    public LivesDisplay(int player, int maxLives, int currentLives) {
        this.player = player;
        this.maxLives = maxLives;
        this.currentLives = currentLives;
    }

    public void loadLives() {
        threeHearts = ImageLoader.load("LivesSprites/ThreeHearts.png", transpColor);
        twoHearts = ImageLoader.load("LivesSprites/TwoHearts.png", transpColor);
        oneHeart = ImageLoader.load("LivesSprites/OneHeart.png", transpColor);
    }

    public void draw(GraphicsHandler graphicsHandler, int lives) {
        if (this.player == 0) {
            if (lives > 0 && lives <= 1) {
                graphicsHandler.drawImage(oneHeart, 110, y + 75, 99, 30);
            } else if (lives > 1 && lives <= 2) {
                graphicsHandler.drawImage(twoHearts, 110, y + 75, 99, 30);
            } else if (lives > 2 && lives <= 3) {
                graphicsHandler.drawImage(threeHearts, 110, y + 75, 99, 30);
            }
        }
        if (this.player == 1) {
            if (lives > 0 && lives <= 1) {
                graphicsHandler.drawImage(oneHeart, barTwoX + 100, y + 75, 99, 30, ImageEffect.FLIP_HORIZONTAL);
            } else if (lives > 1 && lives <= 2) {
                graphicsHandler.drawImage(twoHearts, barTwoX + 100, y + 75, 99, 30, ImageEffect.FLIP_HORIZONTAL);
            } else if (lives > 2 && lives <= 3) {
                graphicsHandler.drawImage(threeHearts, barTwoX + 100, y + 75, 99, 30, ImageEffect.FLIP_HORIZONTAL);
            } 
        }
    }
}