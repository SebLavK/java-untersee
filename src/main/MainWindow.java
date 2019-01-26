package main;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import master.Master;


/**
*@author Sebas Lavigne
*/

public class MainWindow {
	
	public static final int WINDOW_WIDTH = 1300;
	public static final int WINDOW_HEIGHT = 800;
	public static final int COMMAND_HEIGHT = 300;
	
	private JFrame window;
	private Master master;
	private GamePanel gamePanel;
	private SidePanel sidePanel;
	
	public MainWindow() {
		window = new JFrame("Java Unterzee");
		window.setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initializeComponents() {
		window.setLayout(new BorderLayout());
		gamePanel = new GamePanel();
		gamePanel.setPreferredSize(new Dimension(WINDOW_HEIGHT, WINDOW_HEIGHT));
		
		sidePanel = new SidePanel();
		sidePanel.setPreferredSize(new Dimension(WINDOW_WIDTH - WINDOW_HEIGHT, WINDOW_HEIGHT));
		
		master = new Master(gamePanel, sidePanel);
		gamePanel.initializePanel();
		sidePanel.setMaster(master);
		sidePanel.initializePanel();
		sidePanel.initializeComponents();
		
		master.initializeMaster();
		
		window.getContentPane().add(gamePanel, BorderLayout.CENTER);
		window.getContentPane().add(sidePanel, BorderLayout.LINE_END);
	}
	
	public void initializeListeners() {
		sidePanel.initializeListeners();
		window.addMouseWheelListener(master.getScenario().getCamera());
	}
	
	public void initialize() {
		initializeComponents();
		initializeListeners();
		window.setVisible(true);
		master.startGame();
	}

}
