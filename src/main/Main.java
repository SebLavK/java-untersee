package main;

import java.awt.EventQueue;

/**
 * @author Sebas Lavigne
 */

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					MainWindow mainWindow = new MainWindow();
					mainWindow.initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
