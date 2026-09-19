import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.ArrayList;

public class Player {
    private HashMap<Integer, BufferedImage[]> animations = new HashMap<Integer, BufferedImage[]>();
    
    private int xPos;
    private int yPos;
    private int width;
    private int height;
    private int lastX;
    private int lastY;

    private int xOffset;
    private int yOffset;
    private int lastFaced = Constants.Directions.RIGHT;
    
    private int aniTick, aniIndex, aniSpeed = 20;

    private int playerAction = Constants.Actions.IDLE_LEFT;
    private boolean isMoving = false;

    private boolean left, up, right, down;
    private double airSpeed = 0;
    private double gravity = 0.05 * Game.SCALE;
    private double fallAfterCollision = 0.5 * Game.SCALE;
    private double jumpSpeed = -2.25 * Game.SCALE;
    private boolean inAir = false;

    private double playerSpeed = 3;
    private int playerDirection = -1;
    private BufferedImage img;

    private boolean crouched = false;

    private ArrayList<Spike> allSpikes;
    private int[][][] levelData;

    private Rectangle hitBox;
    
    public Player(int xPos, int yPos, int width, int height) {
        this.xPos = xPos;
        this.yPos = yPos;
        lastX = xPos;
        lastY = yPos;
        this.width = width;
        this.height = height;
        createAnimations();
        createHitBox(xPos + (int)(2 * Game.SCALE), yPos, (int)(12 * Game.SCALE), height);
    }

    public void createHitBox(int x, int y, int width, int height) {
        hitBox = new Rectangle(x, y, width, height);
        xOffset = x - xPos;
        yOffset = y - yPos;
    }

    public void updateHitBox() {
        hitBox.x = (int)(xPos + xOffset);
        hitBox.y = (int)(yPos + yOffset);
    }

    public Rectangle getHitbox() {
        return hitBox;
    }

    public void setHitbox(int x, int y, int width, int height) {
        hitBox.x = x;
        xOffset = x - xPos;
        hitBox.y = y;
        yOffset = y - yPos;
        hitBox.width = width;
        hitBox.height = height;
    }

    public void update(float xlevelOffset, float ylevelOffset) {
        updatePosition();
        updateHitBox();
        updateAnimation();
        setAnimation();
    }

    public void render(Graphics g, float xlevelOffset, float ylevelOffset) {
        g.drawImage(animations.get(playerAction)[aniIndex], (int)(xPos - xlevelOffset), (int)(yPos - ylevelOffset), width, height, null);
        // g.setColor(Color.PINK);
        // g.drawRect((int)(hitBox.x - Game.CAMERAX), (int)(hitBox.y - Game.CAMERAY), hitBox.width, hitBox.height);
    }

    public void updateAnimation() {
        aniTick++;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= Constants.getSpriteValue(playerAction)) {
                aniIndex = 0;
            }
        }
    }

    public void setAnimation() {
        boolean side = true;
        int startingAnimation = playerAction;

        if (lastFaced == Constants.Directions.LEFT) {
            side = false;
        } else {
            side = true;
        }

        if (isMoving) {
            playerAction = side ? Constants.Actions.RUN_RIGHT : Constants.Actions.RUN_LEFT;
        } else {
            playerAction = side ? Constants.Actions.IDLE_RIGHT : Constants.Actions.IDLE_LEFT;
        }

        if (inAir) {
            playerAction = side ? Constants.Actions.JUMP_RIGHT : Constants.Actions.JUMP_LEFT;
        }

        if (down) {
            playerAction = side ? Constants.Actions.CROUCH_RIGHT : Constants.Actions.CROUCH_LEFT;
        }
        
        // Used Gemini to change aniTick and aniIndex to 0
        if (startingAnimation != playerAction) {
            startingAnimation = playerAction;
            aniTick = 0;
            aniIndex = 0;
        }
    }

    public void updatePosition() {
        isMoving = false;
        double xSpeed = 0;

        if (down) {
            if (!inAir) {
            crouched = true;
            setHitbox(xPos + (int)(2 * Game.SCALE), (yPos + this.height/2), (int)(12 * Game.SCALE), height/2);
            }
        } else {
            crouched = false;
            setHitbox(xPos + (int)(2 * Game.SCALE), yPos, (int)(12 * Game.SCALE), height);
        }

        if (up) {
            jump();   
        }

        if (!left && !right && !inAir) {
            return;
        }

        if (left) {
            lastFaced = Constants.Directions.LEFT;
            xSpeed -= playerSpeed;
        }
        if (right) {
            lastFaced = Constants.Directions.RIGHT;
            xSpeed += playerSpeed;
        }

        if (!inAir) {
            if (!HelpfulMethods.isOnFloor(hitBox, levelData)) {
                inAir = true;
            }
            if ((!left && !right) || (right && left)) {
                return;
            }
        }

        if (inAir) {
            down = false;
            if (HelpfulMethods.canMoveHere(hitBox.x, (int)(Math.floor(hitBox.y + airSpeed)), hitBox.width, hitBox.height, levelData)) {
                this.yPos += (int)(Math.floor(airSpeed));
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                this.yPos = HelpfulMethods.GetEntityYPosUnderRoofOrAboveFloor(hitBox, airSpeed, yPos);
                if (airSpeed > 0) {
                    resetInAir();
                    if (!inAir) {
                        if (!HelpfulMethods.isOnFloor(hitBox, levelData)) {
                            inAir = true;
                        }
                    }
                } else {
                    airSpeed = fallAfterCollision;
                }
                updateXPos(xSpeed);
            }
            if (HelpfulMethods.lastCheckpoint(hitBox, levelData)) {
                lastX = xPos;
                lastY = yPos;
            }
            if(HelpfulMethods.touchedSpike(hitBox, allSpikes)) {
                xPos = lastX;
                yPos = lastY;
            }
        } else {
            updateXPos(xSpeed);
        }
        
        isMoving = true;
    }

    public void updateXPos(double xSpeed) {
        if (HelpfulMethods.canMoveHere((int)(Math.floor(hitBox.x + xSpeed)), hitBox.y, hitBox.width, hitBox.height, levelData)) {
            this.xPos = (int)(Math.floor(this.xPos + xSpeed));
        }
        if (HelpfulMethods.lastCheckpoint(hitBox, levelData)) {
            lastX = xPos;
            lastY = yPos;
        }
        if(HelpfulMethods.touchedSpike(hitBox, allSpikes)) {
            xPos = lastX;
            yPos = lastY;
        }
    }

    public void jump() {
        if (inAir) {
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }
    
    public void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    public void createAnimations() {
        img = GetAssets.getSpriteAtlas(GetAssets.PLAYER_ATLAS);

        for (int i = 0; i < 8; i++) {
            BufferedImage[] images = new BufferedImage[5];
            for (int j = 0; j < 5; j++) {
                images[j] = img.getSubimage(j*16, i*16, 16, 16);
            }
            animations.put(i, images);
        }
    }

    public void loadLevelData(int[][][] levelData) {
        this.levelData = levelData;
        if (!HelpfulMethods.isOnFloor(hitBox, levelData)) {
            inAir = true;
        }
    }

    public void loadSpikes(ArrayList<Spike> allSpikes) {
        this.allSpikes = allSpikes;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public void setDirection(int direction) {
        playerDirection = direction;
        isMoving = true;
    }

    public void resetDirections() {
        left = false;
        up = false;
        right = false;
        down = false;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setJump(boolean jump) {
        this.up = jump;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setCrouch(boolean crouch) {
        this.down = crouch;
    }
}