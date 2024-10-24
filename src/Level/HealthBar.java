package Level;

import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Config;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import java.awt.image.BufferedImage;

import java.awt.*;

public class HealthBar {
    protected int maxHealth;
    protected int currentHealth;
    protected int player;
    protected BufferedImage healthBar100;

    protected Color transpColor = new Color(153, 217, 234);

    protected BufferedImage healthBar10;
    protected BufferedImage healthBar20;
    protected BufferedImage healthBar30;
    protected BufferedImage healthBar40;
    protected BufferedImage healthBar50;
    protected BufferedImage healthBar60;
    protected BufferedImage healthBar70;
    protected BufferedImage healthBar80;
    protected BufferedImage healthBar90;
    protected SpriteSheet spriteSheet;
    protected int healthBarLen = 90;
    protected int barTwoX = (Config.GAME_WINDOW_WIDTH / 2) + healthBarLen;

    protected int x;
    protected int y;

    public HealthBar(int player, int maxHealth, int currentHealth) {
        this.player = player;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public void loadHealthBars() {
        healthBar100 = ImageLoader.load("HealthBarTS/HealthBar100.png", transpColor);
        healthBar90 = ImageLoader.load("HealthBarTS/HealthBar90.png", transpColor);
        healthBar80 = ImageLoader.load("HealthBarTS/HealthBar80.png", transpColor);
        healthBar70 = ImageLoader.load("HealthBarTS/HealthBar70.png", transpColor);
        healthBar60 = ImageLoader.load("HealthBarTS/HealthBar60.png", transpColor);
        healthBar50 = ImageLoader.load("HealthBarTS/HealthBar50.png", transpColor);
        healthBar40 = ImageLoader.load("HealthBarTS/HealthBar40.png", transpColor);
        healthBar30 = ImageLoader.load("HealthBarTS/HealthBar30.png", transpColor);
        healthBar20 = ImageLoader.load("HealthBarTS/HealthBar20.png", transpColor);
        healthBar10 = ImageLoader.load("HealthBarTS/HealthBar10.png", transpColor);
    }

    public void draw(GraphicsHandler graphicsHandler, int health) {
        if (this.player == 0) {
            if (health > 0 && health <= 10) {
                graphicsHandler.drawImage(healthBar10, 10, y, 300, 150);
            } else if (health > 10 && health <= 20) {
                graphicsHandler.drawImage(healthBar20, 10, y, 300, 150);
            } else if (health > 20 && health <= 30) {
                graphicsHandler.drawImage(healthBar30, 10, y, 300, 150);
            } else if (health > 30 && health <= 40) {
                graphicsHandler.drawImage(healthBar40, 10, y, 300, 150);
            } else if (health > 40 && health <= 50) {
                graphicsHandler.drawImage(healthBar50, 10, y, 300, 150);
            } else if (health > 50 && health <= 60) {
                graphicsHandler.drawImage(healthBar60, 10, y, 300, 150);
            } else if (health > 60 && health <= 70) {
                graphicsHandler.drawImage(healthBar70, 10, y, 300, 150);
            } else if (health > 70 && health <= 80) {
                graphicsHandler.drawImage(healthBar80, 10, y, 300, 150);
            } else if (health > 80 && health <= 90) {
                graphicsHandler.drawImage(healthBar90, 10, y, 300, 150);
            } else if (health > 90 && health <= 100) {
                graphicsHandler.drawImage(healthBar100, 10, y, 300, 150);
            }
        }
        if (this.player == 1) {
            if (health > 0 && health <= 10) {
                graphicsHandler.drawImage(healthBar10, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            } else if (health > 10 && health <= 20) {
                graphicsHandler.drawImage(healthBar20, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            } else if (health > 20 && health <= 30) {
                graphicsHandler.drawImage(healthBar30, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            } else if (health > 30 && health <= 40) {
                graphicsHandler.drawImage(healthBar40, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            } else if (health > 40 && health <= 50) {
                graphicsHandler.drawImage(healthBar50, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            } else if (health > 50 && health <= 60) {
                graphicsHandler.drawImage(healthBar60, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            } else if (health > 60 && health <= 70) {
                graphicsHandler.drawImage(healthBar70, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            } else if (health > 70 && health <= 80) {
                graphicsHandler.drawImage(healthBar80, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            } else if (health > 80 && health <= 90) {
                graphicsHandler.drawImage(healthBar90, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            } else if (health > 90 && health <= 100) {
                graphicsHandler.drawImage(healthBar100, barTwoX, y, 300, 150, ImageEffect.FLIP_HORIZONTAL);
            }
        }
    }
}