package main;

import commons.Configuration;
import commons.ImageResource;
import master.Master;

import javax.swing.*;
import java.awt.*;


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
		window = new JFrame("Java Untersee");
		window.setBounds(100, 100, WINDOW_WIDTH, WINDOW_HEIGHT);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void initializeComponents() {
		ImageResource.instantiateImages();
		window.setLayout(new BorderLayout());
		gamePanel = new GamePanel();
		gamePanel.setPreferredSize(new Dimension(WINDOW_HEIGHT, WINDOW_HEIGHT));
		
		sidePanel = new SidePanel();
		sidePanel.setPreferredSize(new Dimension(WINDOW_WIDTH - WINDOW_HEIGHT, WINDOW_HEIGHT));
		
		master = new Master(this, gamePanel, sidePanel);
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
		window.addMouseListener(master.getScenario().getCamera());
	}
	
	public void initialize() {
		initializeComponents();
		initializeListeners();
		window.setVisible(true);
		Configuration.loadProperties();
		master.startGame();
	}

}
