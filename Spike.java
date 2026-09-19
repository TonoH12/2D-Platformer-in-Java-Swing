import java.awt.image.BufferedImage;
import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.Color;

public class Spike {
    private Rectangle hitBox;
    private int xPos;
    private int yPos;
    private int width;
    private int height;

    public Spike(int index, int x, int y) {
        if ((index == 0) || (index == 3)) {
            width = Game.TILES_SIZE;
            height = Game.TILES_SIZE / 2 - (int)(2 * Game.SCALE);
        }

        if ((index == 1) || (index == 2)) {
            width = Game.TILES_SIZE / 2 - (int)(2 * Game.SCALE);
            height = Game.TILES_SIZE;
        }

        if (index == 1) {
            xPos = x + (Game.TILES_SIZE / 2) + (int)(2 * Game.SCALE);
        } else {
            xPos = x;
        }

        if (index == 3) {
            yPos = y + (Game.TILES_SIZE / 2) + (int)(2 * Game.SCALE);
        } else {
            yPos = y;
        }
        createHitBox(xPos, yPos, width, height);
    }

    public void createHitBox(int x, int y, int width, int height) {
        hitBox = new Rectangle(x, y, width, height);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawRect(hitBox.x, hitBox.y, hitBox.width, hitBox.height);
    }
}