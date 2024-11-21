package Players;

import Builders.FrameBuilder;
import Enemies.Fireball;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.Player;
import Level.PlayerState;
import Utils.Direction;
import Utils.Point;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.net.URL;
import java.awt.Color;
import java.util.HashMap;

public class Mage2 extends Player {
    private Rectangle hitbox;
    private Rectangle attackHitbox;
    protected float attackboxTimer = System.currentTimeMillis();
    protected int shootTimer;

    private boolean isAttacking = false;
    private boolean isAlive = true;
    private boolean deathSoundPlayed = false;
    private Clip attackClip;
    private Clip deathClip;
        public Mage2(float x, float y) {
                super(new SpriteSheet(ImageLoader.load("Gunner.png"), 23, 23), x, y, "STAND_RIGHT");
                gravity = .5f;
                terminalVelocityY = 6f;
                jumpHeight = 14.5f;
                jumpDegrade = .5f;
                walkSpeed = 2.3f;
                momentumYIncrease = .5f;

    



        this.hitbox = new Rectangle(x, y, 33, 39);
        hitbox.setColor(Color.RED);

        this.attackHitbox = new Rectangle(x, y, 15, 30);
        attackHitbox.setColor(Color.BLUE);

        // Load sounds for attack and death
        loadSound("Resources/Pew.wav", "attack"); // Replace with the actual path
        loadSound("Resources/Death.wav", "death");   // Replace with the actual path
    }

    // Method to load sound based on the type
    private void loadSound(String path, String type) {
        try {
            URL soundURL = getClass().getClassLoader().getResource(path);
            Clip clip = null;
            if (soundURL != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
                clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(volumeControl.getMaximum());
            } else {
                File soundFile = new File(path);
                if (soundFile.exists()) {
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                    volumeControl.setValue(volumeControl.getMaximum());
                }
            }

            if (type.equals("attack")) attackClip = clip;
            if (type.equals("death")) deathClip = clip;
        } catch (Exception e) {
            System.err.println("Error loading " + type + " sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            new Thread(() -> {
                synchronized (clip) {
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                    clip.setFramePosition(0);
                    clip.start();
                }
            }).start();
        }
    }

    @Override
    public void update() {
        super.update();

        // Check if the player is in the death animation and play death sound once
        if ((getCurrentAnimationName().equals("DEATH_LEFT") || getCurrentAnimationName().equals("DEATH_RIGHT")) && isAlive && !deathSoundPlayed) {
            playSound(deathClip); // Play death sound
            deathSoundPlayed = true; // Prevent replaying the death sound during the same death animation
            isAlive = false; // Character is now considered "dead"
        }

        // Reset the death sound flag when the character is respawned or reinitialized
        if (!getCurrentAnimationName().equals("DEATH_LEFT") && !getCurrentAnimationName().equals("DEATH_RIGHT")) {
            deathSoundPlayed = false; // Allow death sound to play again on the next death
            isAlive = true; // Character is "alive" again
        }

        // Logic for playing attack sound
        if (getPlayerState() == PlayerState.ATTACKING && !isAttacking) {
            playSound(attackClip);
            isAttacking = true;
        }
        if (getPlayerState() != PlayerState.ATTACKING) {
            isAttacking = false;
        }

        int xOffset = 5;
        int yOffset = 15;

        hitbox.setLocation(getX() + xOffset, getY() + yOffset);

        if (getFacingDirection() == Direction.LEFT) {
            hitbox.setLocation(getX() + 22 + xOffset, getY() + yOffset); // Offset left
        } else {
            hitbox.setLocation(getX() + xOffset, getY() + yOffset); // Offset right
        }

        if (System.currentTimeMillis() - attackboxTimer > 300) {
            attackHitbox.setLocation(1000, 1000);
        }

        if (getPlayerState() == PlayerState.ATTACKING && shootTimer == 0) {
            int fireballX;
            float movementSpeed;
            if (facingDirection == Direction.RIGHT) {
                fireballX = Math.round(getX()) + getWidth();
                fireballX = fireballX - 20;
                movementSpeed = 1.5f;
            } else {
                fireballX = Math.round(getX() - 21);
                fireballX = fireballX + 20;
                movementSpeed = -3.5f;
            }

            int fireballY = Math.round(getY()) + 4;
            Fireball fireball = new Fireball(new Point(fireballX, fireballY + 15), movementSpeed, 70);
            map.addEnemy(fireball);
            shootTimer = 50;
        }

        if (shootTimer > 0) {
            shootTimer--;
        }
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        super.draw(graphicsHandler);
    }

    public Rectangle getHitbox() {
        return this.hitbox;
    }

    public Rectangle getAttackHitbox() {
        return this.attackHitbox;
    }

        @Override
        public HashMap<String, Frame[]> loadAnimations(SpriteSheet spriteSheet) {
                return new HashMap<String, Frame[]>() {
                        {
                                put("STAND_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(2, 0), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 1), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 2), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 3), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 4), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("STAND_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(2, 0), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 1), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 2), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 3), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 4), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("WALK_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(4, 0), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 1), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 2), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 3), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 4), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 5), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("WALK_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(4, 0), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 1), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 2), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 3), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 4), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(4, 5), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("JUMP_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 14)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 7, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 1), 14)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("JUMP_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 0), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(3, 1), 14)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("FALL_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 1))
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("FALL_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(3, 1))
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("CROUCH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 2))
                                                                .withScale(3)
                                                                .withBounds(8, 12, 8, 6)
                                                                .build()
                                });

                                put("CROUCH_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(0, 2))
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 12, 8, 6)
                                                                .build()
                                });

                                put("DEATH_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 1), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 2), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 3), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 4), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 5), 8)
                                                                .withScale(3)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 6), -1)
                                                                .withScale(3)
                                                                .build()
                                });

                                put("DEATH_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(1, 0), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 1), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 2), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 3), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 4), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 5), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(1, 6), -1)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .build()
                                });

                                put("ATTACK_RIGHT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(2, 0), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 1), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 2), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 3), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 4), 7)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()

                                });

                                put("ATTACK_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(2, 0), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 1), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 2), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 3), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 4), 7)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });
                        }
                };
        }
}
