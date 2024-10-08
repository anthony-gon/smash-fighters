package Level;

import java.io.File;
import javax.imageio.ImageIO;
import Engine.GraphicsHandler;
import GameObject.SpriteSheet;
import java.awt.image.BufferedImage;

public class HealthBar {
    protected int maxHealth;
    protected int currentHealth;
    protected BufferedImage healthBar100;

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
    protected int x;
    protected int y;

    public HealthBar(int x, int y, int maxHealth, int currentHealth) {
        this.x = Engine.Config.GAME_WINDOW_WIDTH / 5;
        this.y = 0;
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public void loadHealthBars() {
        try {
            healthBar100 = ImageIO.read(new File("resources/HealthBarTS/HealthBar100.png"));
            healthBar90 = ImageIO.read(new File("resources/HealthBarTS/HealthBar90.png"));
            healthBar80 = ImageIO.read(new File("resources/HealthBarTS/HealthBar80.png"));
            healthBar70 = ImageIO.read(new File("resources/HealthBarTS/HealthBar70.png"));
            healthBar60 = ImageIO.read(new File("resources/HealthBarTS/HealthBar60.png"));
            healthBar50 = ImageIO.read(new File("resources/HealthBarTS/HealthBar50.png"));
            healthBar40 = ImageIO.read(new File("resources/HealthBarTS/HealthBar40.png"));
            healthBar30 = ImageIO.read(new File("resources/HealthBarTS/HealthBar30.png"));
            healthBar20 = ImageIO.read(new File("resources/HealthBarTS/HealthBar20.png"));
            healthBar10 = ImageIO.read(new File("resources/HealthBarTS/HealthBar10.png"));

        } catch (Exception e) {
            System.err.println("Failed to load Health Bar: " + e.getMessage());
            healthBar100 = null;
        }
    }

    public void draw(GraphicsHandler graphicsHandler, int health) {
        switch (health) {
            case 10:
                graphicsHandler.drawImage(healthBar10, 10, y, 300, 150);
                break;
            case 20:
                graphicsHandler.drawImage(healthBar20, 10, y, 300, 150);
                break;
            case 30:
                graphicsHandler.drawImage(healthBar30, 10, y, 300, 150);
                break;
            case 40:
                graphicsHandler.drawImage(healthBar40, 10, y, 300, 150);
                break;
            case 50:
                graphicsHandler.drawImage(healthBar50, 10, y, 300, 150);
                break;
            case 60:
                graphicsHandler.drawImage(healthBar60, 10, y, 300, 150);
                break;
            case 70:
                graphicsHandler.drawImage(healthBar70, 10, y, 300, 150);
                break;
            case 80:
                graphicsHandler.drawImage(healthBar80, 10, y, 300, 150);
                break;
            case 90:
                graphicsHandler.drawImage(healthBar90, 10, y, 300, 150);
                break;
            case 100:
                graphicsHandler.drawImage(healthBar100, 10, y, 300, 150);
                break;
        }
    }
}