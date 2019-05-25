package controller.main;

import view.MainWindow;

import java.awt.*;

/**
 * @author Sebas Lavigne
 */

public class Main {

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				MainWindow mainWindow = new MainWindow();
				mainWindow.initialize();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
