package main;

import java.awt.GridLayout;

import javax.swing.JFrame;

import master.Master;


/**
*@author Sebas Lavigne
*/

public class MainWindow {
	
	private JFrame window;
	private Master master;
	private GamePanel gamePanel;
	
	public MainWindow() {
		window = new JFrame("Java Unterzee");
		window.setBounds(100, 100, 800, 800);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initializeComponents() {
		window.setLayout(new GridLayout(1, 1));
		gamePanel = new GamePanel();
		master = new Master(gamePanel);
		gamePanel.initializePanel();
		window.add(gamePanel);
	}
	
	public void initializeListeners() {
		
	}
	
	public void initialize() {
		initializeComponents();
		initializeListeners();
		window.setVisible(true);
		master.initializeMaster();
	}

}
