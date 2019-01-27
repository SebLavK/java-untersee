package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import commons.Screen;
import master.Master;

/**
*@author Sebas Lavigne
*/

public class SidePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final int LOG_FONT_SIZE = 10; 
	
	private GamePanel dataPanel;
	private JPanel commandPanel;
	
	private JTextField commandLine;
	private JTextArea log;
	
	private Font logFont;
	
	private Master master;
	
	public void initializePanel() {
		this.setLayout(new GridBagLayout());
		dataPanel = new GamePanel();
		commandPanel = new JPanel();
	}
	
	public void initializeComponents() {
		try{
            logFont = Font.createFont(Font.TRUETYPE_FONT,
            		Main.class.getResourceAsStream("/fonts/joystix monospace.ttf"));
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		logFont = logFont.deriveFont(Font.PLAIN, LOG_FONT_SIZE);
		
		GridBagConstraints settings;
		
		settings = new GridBagConstraints();
		settings.fill = GridBagConstraints.BOTH;
		settings.weightx = 1;
		settings.weighty = 1;
		this.add(dataPanel, settings);
		
		commandPanel.setLayout(new GridBagLayout());
		settings = new GridBagConstraints();
		settings.fill = GridBagConstraints.BOTH;
		settings.gridy = 1;
		this.add(commandPanel, settings);
		
		log = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(log);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setFocusable(false);
		scrollPane.setBackground(Color.BLACK);
		scrollPane.setBorder(new EmptyBorder(10, 10, 0, 10));
		log.setForeground(Color.GRAY);
		log.setBackground(Color.BLACK);
		log.setRows(10);
		log.setColumns(60);
		log.setLineWrap(true);
		log.setWrapStyleWord(true);
		log.setFont(logFont);
		log.setFocusable(false);
		log.setEditable(false);
		settings = new GridBagConstraints();
		settings.fill = GridBagConstraints.BOTH;
		settings.weightx = 1;
		settings.weighty = 1;
		commandPanel.add(scrollPane, settings);
		
		commandLine = new JTextField();
		commandLine.setMinimumSize(new Dimension(20, 20));
		commandLine.setColumns(20);
		commandLine.setFont(logFont);
		commandLine.setForeground(Color.WHITE);
		commandLine.setBackground(Color.BLACK);
		commandLine.setBorder(new EmptyBorder(10, 10, 10, 10));
		settings = new GridBagConstraints();
		settings.gridy = 1;
		settings.fill = GridBagConstraints.BOTH;
		settings.weightx = 1;
		settings.weighty = 1;
		commandPanel.add(commandLine, settings);
		
	}
	
	public void initializeListeners() {
		commandLine.addActionListener(e -> sendCommand());
	}
	
	public void sendCommand() {
		String command = commandLine.getText();
		commandLine.setText("");
		master.getXo().sendCommand(command);
	}
	
	public void addToLog(String text) {
		log.setText(log.getText() + "\n" + text);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		dataPanel.repaint();
	}

	/**
	 * @return the dataPanel
	 */
	public GamePanel getDataPanel() {
		return dataPanel;
	}

	/**
	 * @return the currentScreen
	 */
	public Screen getCurrentScreen() {
		return dataPanel.getCurrentScreen();
	}

	/**
	 * @param currentScreen the currentScreen to set
	 */
	public void setCurrentScreen(Screen currentScreen) {
		dataPanel.setCurrentScreen(currentScreen);
	}

	/**
	 * @param master the master to set
	 */
	public void setMaster(Master master) {
		this.master = master;
	}
	
	

}
