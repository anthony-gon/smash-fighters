package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import Engine.Key;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.Player;
import Level.PlayerState;
import Utils.Direction;

import java.util.HashMap;
import java.awt.Color; // Add this line

public class Knight2 extends Player {
        private Rectangle hitbox;
        private Rectangle attackHitbox;

        public Knight2(float x, float y) {
                super(new SpriteSheet(ImageLoader.load("Knight.png"), 22, 24), x, y, "STAND_RIGHT");
                gravity = .5f;
                terminalVelocityY = 6f;
                jumpHeight = 14.5f;
                jumpDegrade = .5f;
                walkSpeed = 2.3f;
                momentumYIncrease = .5f;
                Key JUMP_KEY = Key.I;
                Key MOVE_LEFT_KEY = Key.J;
                Key MOVE_RIGHT_KEY = Key.L;
                Key CROUCH_KEY = Key.K;
                Key ATTACK_KEY = Key.O;
                setMovementKeys(JUMP_KEY, MOVE_LEFT_KEY, MOVE_RIGHT_KEY, CROUCH_KEY, ATTACK_KEY);
                
                this.hitbox = new Rectangle(x, y, 33, 39);
                hitbox.setColor(Color.RED);
                
                this.attackHitbox = new Rectangle(x, y, 15, 30);
                attackHitbox.setColor(Color.BLUE);
        }

        public void update() {
                super.update();

                int xOffset = 5;
                int yOffset = 15;

                hitbox.setLocation(getX() + xOffset, getY() + yOffset);

                if (getFacingDirection() == Direction.LEFT) {
                        hitbox.setLocation(getX() + 22 + xOffset, getY() + yOffset); // Offset left
        }       else {
                        hitbox.setLocation(getX() + xOffset, getY() + yOffset); // Offset right
        }
        if (getPlayerState() == PlayerState.ATTACKING) {
                if (getFacingDirection() == Direction.LEFT) {
                        hitbox.setLocation(getX() + 15  + xOffset, getY() + 18  + yOffset);
                        attackHitbox.setLocation(getX()+4, getY() +38);
                } else {
                        hitbox.setLocation(getX() + 11  + xOffset, getY() + 18  + yOffset);
                        attackHitbox.setLocation(getX() + 50, getY() + 38);
                }
        }
        }

        public void draw(GraphicsHandler graphicsHandler) {
                super.draw(graphicsHandler);
                //hitbox.draw(graphicsHandler);
                if (getPlayerState() == PlayerState.ATTACKING) {
                        //attackHitbox.draw(graphicsHandler);
                }
                // drawBounds(graphicsHandler, new Color(255, 0, 0, 170));
        }

        public Rectangle getHitbox() {
                return this.hitbox; // Getter for hitbox
            }

        public Rectangle getAttackHitbox() {
                return this.attackHitbox; // Getter for hitbox
            }

        @Override
        public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
                return new HashMap<String, Frame[]>() {
                        {
                                put("STAND_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(6, 0), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 1), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 2), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 3), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("STAND_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(6, 0), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 1), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 2), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(6, 3), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

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

                                put("JUMP_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(7, 0), 14)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 7, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 1), 14)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 2), 14)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 3), 14)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 4), 14)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 5), 14)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("JUMP_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(7, 0), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 1), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 2), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 3), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 4), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(7, 5), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("FALL_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(7, 5))
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("FALL_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(7, 5))
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("CROUCH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 1))
                                                                .withScale(3)
                                                                .withBounds(8, 12, 8, 6)
                                                                .build()
                                });

                                put("CROUCH_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 1))
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 12, 8, 6)
                                                                .build()
                                });

                                put("DEATH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 1), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 2), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 3), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 4), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 5), -1)
                                                                .withScale(3)
                                                                .build()
                                });

                                put("DEATH_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 1), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 2), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 3), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 4), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 5), -1)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build()
                                });

                                put("ATTACK_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                                                                .withScale(3)
                                                                .build()
                                });

                                put("ATTACK_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 0), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 1), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 2), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 3), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(0, 4), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build()
                                });

                        }
                };
        }
}
