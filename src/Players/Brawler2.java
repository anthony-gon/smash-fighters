package Players;

import Builders.FrameBuilder;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import Level.Player;
import Level.PlayerState;
import Utils.Direction;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;
import java.net.URL;
import java.awt.Color;
import java.util.HashMap;

public class Brawler2 extends Player {
    private Rectangle hitbox;
    private Rectangle attackHitbox;
    protected long attackboxTimer = System.currentTimeMillis();

    private Clip attackClip;
    private Clip deathClip;

    private boolean isAttacking = false;
    private boolean isAlive = true;
    private boolean deathSoundPlayed = false;

    public Brawler2(float x, float y) {
        super(new SpriteSheet(ImageLoader.load("Brawler.png"), 24, 24), x, y, "STAND_RIGHT");
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
        loadSound("Resources/Batk2.wav", "attack");
        loadSound("Resources/TestSound.wav", "death");
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

        // Existing code for handling hitbox and attack box positioning
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

        if (getPlayerState() == PlayerState.ATTACKING) {
            if (getFacingDirection() == Direction.LEFT) {
                hitbox.setLocation(getX() + 15 + xOffset, getY() + 18 + yOffset);
                attackHitbox.setLocation(getX() + 4, getY() + 38);
                attackboxTimer = System.currentTimeMillis();
            } else {
                hitbox.setLocation(getX() + 11 + xOffset, getY() + 18 + yOffset);
                attackHitbox.setLocation(getX() + 50, getY() + 38);
                attackboxTimer = System.currentTimeMillis();
            }
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
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 1), 8)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 2), 8)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 3), 8)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 4), 8)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 5), 8)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 6), 8)
                                                                .withScale(3)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });

                                put("ATTACK_LEFT", new Frame[] {
                                                new FrameBuilder(spriteSheet.getSprite(2, 0), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 1), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 2), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 3), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 4), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 5), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build(),
                                                new FrameBuilder(spriteSheet.getSprite(2, 6), 8)
                                                                .withScale(3)
                                                                .withImageEffect(ImageEffect.FLIP_HORIZONTAL)
                                                                .withBounds(8, 9, 8, 9)
                                                                .build()
                                });
                        }
                };
        }
}
