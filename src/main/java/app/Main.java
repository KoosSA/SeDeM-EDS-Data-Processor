package app;

import app.graphical.MainFrame;

// TODO: Auto-generated Javadoc
/**
 * Entry point of the application.
 *
 * @author Koos
 */
public class Main implements Runnable {

	/**
	 * Starting point of the app.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		Main app = new Main();
		Thread mainThread = new Thread(app);
		mainThread.start();
	}

	/**
	 * Called when the app starts. This opens the main ui.
	 */
	@Override
	public void run() {
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}

}
