import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Inputs implements KeyListener {
	private Panel gamePanel;

	public Inputs(Panel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public void keyTyped(KeyEvent e) {
		
	}

    // Used Gemini to fix movement inputs, forgot to add break statements
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
            // Jump key
			case 38, 87:
				gamePanel.getGame().getPlayer().setJump(false);
				break;
            // Left key
			case 37, 65:
				gamePanel.getGame().getPlayer().setLeft(false);
				break;
            // Right key
			case 39, 68:
				gamePanel.getGame().getPlayer().setRight(false);
				break;
            // Crouch key
			case 40, 83:
				gamePanel.getGame().getPlayer().setCrouch(false);
				break;
		}
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
            // Jump key
			case 38, 87:
				gamePanel.getGame().getPlayer().setJump(true);
				break;
            // Left key
			case 37, 65:
				gamePanel.getGame().getPlayer().setLeft(true);
				break;
            // Right key
			case 39, 68:
				gamePanel.getGame().getPlayer().setRight(true);
				break;
            // Crouch key
			case 40, 83:
				gamePanel.getGame().getPlayer().setCrouch(true);
				break;
		}
	}
}