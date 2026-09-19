import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

public class Frame extends JFrame{
	private JFrame jframe;

	public Frame(Panel gamePanel) {
		jframe = new JFrame();

        gamePanel.setPanelSize();
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(gamePanel);
		jframe.setLocationRelativeTo(null);
        jframe.setResizable(true);
        jframe.pack();
		jframe.setVisible(true);
		jframe.addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
                gamePanel.getGame().focusLost();
            }

            public void windowLostFocus(WindowEvent e) {
               
            }
		});
	}
}