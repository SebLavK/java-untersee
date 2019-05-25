package controller.main;

import commons.gameObject.Verbose;
import controller.master.Master;
import view.ImageResource;
import view.Screen;
import view.screens.DataScreen;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

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
	
	private BufferedImage crtShadow;
	
	private Font logFont;
	
	private Master master;
	
	public void initializePanel() {
		this.setLayout(new GridBagLayout());
		dataPanel = new GamePanel();
		commandPanel = new JPanel();
		this.setBackground(DataScreen.BG_COLOR);
	}
	
	public void initializeComponents() {
		try{
            logFont = Font.createFont(Font.TRUETYPE_FONT,
            		Main.class.getResourceAsStream("/resources/fonts/joystix monospace.ttf"));
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		logFont = logFont.deriveFont(Font.PLAIN, LOG_FONT_SIZE);
		crtShadow = ImageResource.getCrtShadow();
		
		GridBagConstraints settings;
		
		settings = new GridBagConstraints();
		settings.fill = GridBagConstraints.BOTH;
		settings.weightx = 1;
		settings.weighty = 1;
		this.add(dataPanel, settings);
		
		commandPanel.setLayout(new GridBagLayout());
		commandPanel.setOpaque(false);
		settings = new GridBagConstraints();
		settings.fill = GridBagConstraints.BOTH;
		settings.gridy = 1;
		this.add(commandPanel, settings);
		
		log = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(log);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setFocusable(false);
		scrollPane.setBackground(Color.BLACK);
		scrollPane.setBorder(new EmptyBorder(10, 10, 0, 10));
		log.setForeground(Color.GRAY);
		log.setOpaque(false);
//		log.setBackground(Color.BLACK);
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
		commandLine.setOpaque(false);
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
	
	public void addToLog(Verbose verbose) {
		StringBuilder sb = new StringBuilder();
		sb.append(Configuration.getGameLogValue(verbose.getHeader()));
		if (verbose.getMessageParams() != null) {
			sb.append(MessageFormat.format(
					Configuration.getGameLogValue(verbose.getMessage()),
					(Object[]) verbose.getMessageParams())
			);
		} else {
			sb.append(Configuration.getGameLogValue(verbose.getMessage()));
		}
		if (verbose.getAcknowledgement() != null) {
			sb.append(Configuration.getGameLogValue(verbose.getAcknowledgement()));
		}
		log.setText(log.getText() + "\n" + sb.toString());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		double scaleX = (double)dataPanel.getWidth() / (double)crtShadow.getWidth();
		double scaleY = (double)dataPanel.getHeight() / (double)crtShadow.getHeight();
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform at4 = new AffineTransform();
		at4.translate(0, this.getHeight() - commandPanel.getHeight() - DataScreen.NAV_Y);
//		at4.translate(0, 750);
		at4.scale(scaleX,
				scaleY * (DataScreen.TARGET_Y - DataScreen.WEAPONS_Y) / dataPanel.getHeight());
		g2d.drawImage(crtShadow, at4, null);
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
