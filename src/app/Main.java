package app;

import app.utils.MainFrame;

public class Main implements Runnable {

	public static void main(String[] args) {
		Main app = new Main();
		Thread mainThread = new Thread(app);
		mainThread.start();
	}

	@Override
	public void run() {
		MainFrame mainFrame = new MainFrame();
		mainFrame.setVisible(true);
	}

}
