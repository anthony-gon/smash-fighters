package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.Player;

import java.util.HashMap;

public class Brawler2 extends Player {

    public Brawler2(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Brawler2.png"), 24, 24), x, y, "STAND_RIGHT");
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
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 3), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build()
                });

                put("STAND_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(0, 0), 14)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 1), 14)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 2), 14)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(0, 3), 14)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build()
                });

                put("WALK_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(1, 0), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 1), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 2), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 3), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 4), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 5), 7)
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build()
                });

                put("WALK_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(1, 0), 7)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 1), 7)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 2), 7)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 3), 7)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 4), 7)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(1, 5), 7)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build()
                });

                put("JUMP_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(4, 0), 14)
                                .withScale(3)
                                .withBounds(8, 9, 7, 9)
                                .build()
                });

                put("JUMP_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(4, 0), 14)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build()
                });

                put("FALL_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(2, 2))
                                .withScale(3)
                                .withBounds(8, 9, 8, 9)
                                .build()
                });

                put("FALL_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(2, 2))
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 9, 8, 9)
                                .build()
                });

                put("CROUCH_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(5, 0))
                                .withScale(3)
                                .withBounds(8, 12, 8, 6)
                                .build()
                });

                put("CROUCH_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(5, 0))
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .withBounds(8, 12, 8, 6)
                                .build()
                });

                put("DEATH_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(5, 0), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 1), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 2), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 3), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 4), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 5), -1)
                                .withScale(3)
                                .build()
                });

                put("DEATH_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(5, 0), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 1), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 2), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 3), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 4), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(5, 5), -1)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });

                put("ATTACK_RIGHT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(2, 0), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 1), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 2), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 3), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 4), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 5), 8)
                                .withScale(3)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 6), 8)
                                .withScale(3)
                                .build()
                });

                put("ATTACK_LEFT", new Frame[] {
                        new FrameBuilder(spriteSheet.getSprite(2, 0), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 1), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 2), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 3), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 4), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 5), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build(),
                        new FrameBuilder(spriteSheet.getSprite(2, 6), 8)
                                .withScale(3)
                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                .build()
                });
            }
        };
    }
}
