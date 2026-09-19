import java.awt.Rectangle;
import java.util.ArrayList;

public class HelpfulMethods {

    public static boolean canMoveHere(int x, int y, int width, int height, int[][][] levelData) {
        boolean corners = false;
        boolean sides = false;
        
        if (!isSolid(x, y, levelData)) {
            if (!isSolid(x + width, y + height - 1, levelData)) {
                if (!isSolid(x + width, y, levelData)) {
                    if (!isSolid(x, y + height - 1, levelData)) {
                        corners = true;
                    }
                }
            }
        }

        // Used Gemini to adress hitbox sides
        if (!isSolid(x + (width / 2), y, levelData)) {
            if (!isSolid(x + (width / 2), y + height - 1, levelData)) {
                if (!isSolid(x, y + (height / 2), levelData)) {
                    if (!isSolid(x + width - 1, y + (height / 2), levelData)) {
                        sides = true;
                    }
                }
            }
        }

        if (sides && corners) {
            return true;
        }

        return false;
    }

    public static boolean isSolid(int x, int y, int[][][] levelData) {
        int maxWidth = levelData[0].length * Game.TILES_SIZE;
        // Gemini reminded me to add maxHeight
        int maxHeight = levelData.length * Game.TILES_SIZE;

        if ((x < 0) || (x >= maxWidth)) {
            return true;
        }
        
        // Used Gemini to change from GAME_WIDTH to GAME_HEIGHT
        if ((y < 0) || (y >= maxHeight)) {
            return true;
        }
        
        int xIndex = x / Game.TILES_SIZE;
        int yIndex = y / Game.TILES_SIZE;
        
        // Used Gemini to change from [xIndex][yIndex] to [yIndex][xIndex]
        int value = levelData[yIndex][xIndex][0];

        if ((value >= 300) || (value < 0) || (value != 11)) {
            return true;
        }

        return false;
    }

    public static boolean lastCheckpoint(Rectangle hitBox, int[][][] levelData) {
        int x1Index = hitBox.x / Game.TILES_SIZE;
        int y1Index = hitBox.y / Game.TILES_SIZE;
        int x2Index = (hitBox.x + hitBox.width) / Game.TILES_SIZE;
        int y2Index = (hitBox.y + hitBox.height) / Game.TILES_SIZE;
        
        int value1 = levelData[y1Index][x1Index][2];
        int value2 = levelData[y1Index][x2Index][2];
        int value3 = levelData[y2Index][x1Index][2];
        int value4 = levelData[y2Index][x2Index][2];
        
        if (value1 == 0) {
            return true;
        }
        if (value2 == 0) {
            return true;
        }
        if (value3 == 0) {
            return true;
        }
        if (value4 == 0) {
            return true;
        }
        
        return false;
    }

    public static boolean touchedSpike(Rectangle hitBox, ArrayList<Spike> allSpikes) {
        int x1 = hitBox.x;
        int y1 = hitBox.y;
        int x2 = hitBox.x + hitBox.width;
        int y2 = hitBox.y + hitBox.height;
        boolean touched = false;

        for (int i = 0; i < allSpikes.size(); i++) {
            if (allSpikes.get(i).getHitBox().contains(x1, y1)) {
                touched = true;
            }
            if (allSpikes.get(i).getHitBox().contains(x2, y1)) {
                touched = true;
            }
            if (allSpikes.get(i).getHitBox().contains(x1, y2)) {
                touched = true;
            }
            if (allSpikes.get(i).getHitBox().contains(x2, y2)) {
                touched = true;
            }
        }

        return touched;
    }

    // Used gemini in order to close gap between sprite and floor
    public static int GetEntityYPosUnderRoofOrAboveFloor(Rectangle hitBox, double speed, int yPos) {
        if (speed > 0) {
            int currentTile = (int)((hitBox.y + speed) / Game.TILES_SIZE);
            int yOffset = (int)(Game.TILES_SIZE - hitBox.height);
            return (currentTile * Game.TILES_SIZE) + yOffset;
        } else {
            int currentTile = (int)(hitBox.y / Game.TILES_SIZE);
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean isOnFloor(Rectangle hitBox, int[][][] levelData) {
        if(!isSolid(hitBox.x, hitBox.y + hitBox.height + 1, levelData)) {
           if(!isSolid(hitBox.x + hitBox.width, hitBox.y + hitBox.height + 1, levelData)) {
               return false;
           } 
        }
        return true;
    }
}