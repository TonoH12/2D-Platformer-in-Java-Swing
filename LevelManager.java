import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;

public class LevelManager {
    private Game game;
    private BufferedImage[] platformMap = new BufferedImage[48];
    private BufferedImage[] spikeMap = new BufferedImage[4];
    private ArrayList<Spike> allSpikes = new ArrayList<Spike>();
    
    private BufferedImage respawn = GetAssets.getSpriteAtlas(GetAssets.CHECKPOINT).getSubimage(0, 0, 16, 16);
    
    private int[][][] map = GetAssets.getLevelData();
    
    public LevelManager(Game game) {
        this.game = game;
        importTileMap();
    }

    public void importTileMap() {
        BufferedImage rImg = GetAssets.getSpriteAtlas(GetAssets.LEVEL_ATLAS);
        BufferedImage gImg = GetAssets.getSpriteAtlas(GetAssets.SPIKE_TRAP);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                platformMap[(i * 12 + j)] = rImg.getSubimage(j * 16, i * 16, 16, 16);
            }
        }

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                spikeMap[(i * 2 + j)] = gImg.getSubimage(j * 16, i * 16, 16, 16);
            }
        }
    }

    public void draw(Graphics g, float xlevelOffset, float ylevelOffset) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                int rIndex = map[i][j][0];
                int gIndex = map[i][j][1];
                int bIndex = map[i][j][2];
                
                g.drawImage(platformMap[rIndex], (int)(Game.TILES_SIZE * j - xlevelOffset), (int)(Game.TILES_SIZE * i - ylevelOffset), Game.TILES_SIZE, Game.TILES_SIZE, null);
                if (gIndex < 5) {
                    g.drawImage(spikeMap[gIndex], (int)(Game.TILES_SIZE * j - xlevelOffset), (int)(Game.TILES_SIZE * i - ylevelOffset), Game.TILES_SIZE, Game.TILES_SIZE, null);
                    Spike aSpike = new Spike(gIndex, (int)(Game.TILES_SIZE * j - xlevelOffset), (int)(Game.TILES_SIZE * i - ylevelOffset));
                    allSpikes.add(aSpike);
                    // aSpike.draw(g);
                }

                if (bIndex == 0) {
                    g.drawImage(respawn, (int)(Game.TILES_SIZE * j - xlevelOffset), (int)(Game.TILES_SIZE * i - ylevelOffset), Game.TILES_SIZE, Game.TILES_SIZE, null);
                    // g.setColor(Color.GREEN);
                    // g.drawRect((int)(Game.TILES_SIZE * j - xlevelOffset), (int)(Game.TILES_SIZE * i - ylevelOffset), Game.TILES_SIZE, Game.TILES_SIZE);
                }
            }
        }
    }

    public void update() {
        
    }

    public int[][][] getLevelData() {
        return map;
    }

    public ArrayList<Spike> getSpikes() {
        return allSpikes;
    }
}