import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import java.awt.*;

public class Player extends Entity{
    public static final double GRAVITY = 2.0;
    public Image sprite;
    public Rectangle hitbox;
    public double speed;
    public double jumpSpeed;
    public boolean isJumping;
    public boolean isFalling;
    public Player(int x1, int y1, String imgName) throws SlickException
    {
        x = x1;
        y = y1;
        img = imgName;
        speed = 5.0;
        jumpSpeed = 20.0;
        isJumping = false; isFalling = false;
        
        sprite = new Image(img);
        w = sprite.getWidth();
        h = sprite.getHeight();
        hitbox = new Rectangle(x, y, w, h);
    }

    private void gravCalc() {
        if (this.isFalling || this.isJumping) {
            this.yv += GRAVITY;
        }
        if (isJumping && this.yv <= 0) {
            this.isJumping = false;
            this.isFalling = true;
        }
        // basic floor
        if (this.y > 200) {
            this.y = 200;
            this.yv = 0.0;
            this.isJumping = false;
            this.isFalling = false;
        }
    }
    public void jump() {
        if (!this.isFalling) {
            this.yv -= this.jumpSpeed;
            this.isJumping = true;
        }
    }
    // also updates hitbox position
    public void movePlayer() {
        this.gravCalc();
        this.hitbox.x += this.xv;
        this.hitbox.y += this.yv;
        this.move();
    }
    /*
      * returns 1 if other player hits from left, 2 if from right, 3 if from
      * top, 4 if from the bottom. Vertical/ horizontal distinction is based
      * on which has the greater net velocity.
     */
    public int checkHit(Player that) {
        if (this.hitbox.intersects(that.hitbox)) {
            if (Math.abs(this.xv) + Math.abs(that.xv) +
                    Math.abs(this.yv) + Math.abs(that.yv) == 0) {
                return 0;
            }
            if ((Math.abs(this.xv) + Math.abs(that.xv)) >
                    (Math.abs(this.yv) + Math.abs(that.yv))) { // if horizontal
                if (this.x > that.x) return 1;
                else                 return 2;
            }
            else {
                if (this.y > that.y) return 3;
                else                 return 4;
            }
        }
        else return 0;
    }

    /*
    uses the checkHit, but also adjusts the position to avoid overlap.
     */
    public void adjustTo(Player that) {
        int compare = this.checkHit(that);
        if (compare > 0) System.out.println("collision");
        if (compare == 0) return;
        else if (compare == 1) { // left
            this.x = that.x + that.w;
            this.xv = 0;
            that.xv = 0;
        }
        else if (compare == 2) { //right
            this.x = that.x - this.w;
            this.xv = 0;
            that.xv = 0;
        }
        else if (compare == 3) { // down
            this.y = that.y + that.h;
            this.yv = 0;
            that.yv = 0;
        }
        else if (compare == 4) { // up
            this.y = that.y - this.h;
            this.yv = 0;
            that.yv = 0;
        }
    }
    
    public void drawThis()
    {
        sprite.draw(x, y);
    }
}