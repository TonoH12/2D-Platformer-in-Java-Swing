import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GetAssets {
    public static final String PLAYER_ATLAS = "MC_Spritesheet.png";
    public static final String LEVEL_ATLAS = "TileMap.png";
    public static final String MAIN_LEVEL = "Tiled_Layout.png";
    public static final String SPIKE_TRAP = "Spikes.png";
    public static final String CHECKPOINT = "Respawn.png";
    
    public static BufferedImage getSpriteAtlas(String filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("Assets/" + filename));
        } catch (IOException e) {
            
        }
        return img;
    }

    public static int[][][] getLevelData() {
        BufferedImage img = getSpriteAtlas(MAIN_LEVEL);
        int[][][] levelData = new int[img.getHeight()][img.getWidth()][3];
        
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth(); j++) {
                Color color = new Color(img.getRGB(j, i)); 
                int rValue = color.getRed();
                int gValue = color.getGreen();
                int bValue = color.getBlue();
                
                if (rValue >= 48) {
                    rValue = 0;
                }
                levelData[i][j][0] = rValue;
                levelData[i][j][1] = gValue;
                levelData[i][j][2] = bValue;
            }
        }
        return levelData;
    }
}