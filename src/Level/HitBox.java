package Level;

import Level.Player;
import Level.PlayerState;
import Utils.Direction;
import GameObject.Rectangle;



public class HitBox {
    protected int size;
    protected int damage;
    protected int attackX;
    protected int attackY;
    protected int player;

    public HitBox(int attackX, int attackY, int size, int damage, int player) {
        this.attackX = attackX;
        this.attackY = attackY;
        this.size = size;
        this.damage = damage;

        this.player = player;
    }

    public void castHitBox(int duration) {
        // Rectangle hitbox = new Rectangle(player.position)


    }

}
