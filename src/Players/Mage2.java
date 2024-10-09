package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player2;

import java.util.HashMap;

public class Mage2 extends Player2 {

    public Mage2(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("FireMage.png"), 22, 24), x, y, "STAND_RIGHT");
        gravity = .5f;
        terminalVelocityY = 6f;
        jumpHeight = 14.5f;
        jumpDegrade = .5f;
        walkSpeed = 2.3f;
        momentumYIncrease = .5f;
    }

    public void update() {
        super.update();
    }

    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
        return new HashMap<String, Frame[]>() {
            {
                put("STAND_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(10, 0), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(10, 1), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(10, 2), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(10, 3), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build()
                });

                put("STAND_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(10, 0), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(10, 1), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(10, 2), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(10, 3), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build()
                });

                // Additional animations: WALK_RIGHT, WALK_LEFT, JUMP_RIGHT, etc.
                put("WALK_RIGHT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(9, 0), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 1), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 2), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 3), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 4), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 5), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 6), 7)
                        .withScale(3)
                        .withBounds(8, 9, 8, 9)
                        .build()
                });

                put("WALK_LEFT", new Frame[] {
                    new FrameBuilder(spriteSheet.getSprite(9, 0), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 1), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 2), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 3), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 4), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 5), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build(),
                    new FrameBuilder(spriteSheet.getSprite(9, 6), 7)
                        .withScale(3)
                        .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                        .withBounds(8, 9, 8, 9)
                        .build()
                });

                // Add jump, fall, crouch, attack, death animations similar to the Mage class.
            }
        };
    }
}
