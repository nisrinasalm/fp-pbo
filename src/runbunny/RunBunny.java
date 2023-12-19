package runbunny;

import javax.swing.JFrame;

public class RunBunny extends JFrame{
	private static final long serialVersionUID = 1L;

	public RunBunny() {
		add(new Game());
	}
	
	public static void main(String[] args) {
		RunBunny rb = new RunBunny();
		rb.setVisible(true);
		rb.setTitle("RunBunny");
		rb.setSize(615,670);
		rb.setDefaultCloseOperation(EXIT_ON_CLOSE);
		rb.setLocationRelativeTo(null);	
	}
}