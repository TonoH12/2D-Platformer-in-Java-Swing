import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;

public class Panel extends JPanel {
    private Game game;
    
    public Panel(Game game) { 
        this.game = game;

        setPanelSize();
        addKeyListener(new Inputs(this));
    }

    public void setPanelSize() {
        setPreferredSize(new Dimension(Game.GAME_WIDTH, Game.GAME_HEIGHT));
        System.out.println("Size: " + game.GAME_WIDTH + " " + game.GAME_HEIGHT);
    }
    
    public void update() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.update();
        game.render(g);
    }

    public Game getGame() {
        return game;
    }
}