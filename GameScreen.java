import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
//import com.teamdev.jxbrowser.chromium.javafx.BrowserView;

public class GameScreen {
	JPanel gameScreen;  // Panel containing map and game controls.
	
	public GameScreen(GoogleMaps gMap, Browser browser, BrowserView bv) {
		gameScreen = createFullGamePanel(gMap, browser, bv);
	}
	
	protected JPanel createGameControlsPanel() { // Creating Panel Containing Game Controls
		JPanel gameControlsPanel = new JPanel();
		
		gameControlsPanel.setLayout(new BoxLayout(gameControlsPanel, BoxLayout.PAGE_AXIS));
		//gameControlsPanel.setLayout(new GridBagLayout());
		gameControlsPanel.setPreferredSize(new Dimension(400, 500));
		gameControlsPanel.setBackground(new Color(0, 204, 255));
				
		// Get real and randomized answers for choices.
		
		Dimension btnDim = new Dimension(100, 50);
		
		JRadioButton choice1 = new JRadioButton("Choice 1");
		JRadioButton choice2 = new JRadioButton("Choice 2");
		JRadioButton choice3 = new JRadioButton("Choice 3");
		JRadioButton choice4 = new JRadioButton("Choice 4");
		
		choice1.setPreferredSize(btnDim);
		choice2.setPreferredSize(btnDim);
		choice3.setPreferredSize(btnDim);
		choice4.setPreferredSize(btnDim);
		
		JButton nextBtn = new JButton("Next");
		
		nextBtn.setPreferredSize(new Dimension(110, 60));
		
		gameControlsPanel.add(choice1);
		gameControlsPanel.add(choice2);
		gameControlsPanel.add(choice3);
		gameControlsPanel.add(choice4);
		gameControlsPanel.add(nextBtn);
		
		return gameControlsPanel;
	}
	
	public JPanel createFullGamePanel(GoogleMaps gMap, Browser browser, BrowserView bv) { // Creates Panel Containing Map and Game Controls
		JPanel gamePanel = new JPanel(new BorderLayout());
		
		// Google Map and Game Controls Panels
		JPanel mapPanel = gMap.createGoogleMapsPanel(browser, bv);
		JPanel gameControlsPanel = createGameControlsPanel();
				
		gamePanel.add(mapPanel, BorderLayout.CENTER);	
		gamePanel.add(gameControlsPanel, BorderLayout.EAST);
		
		// Google Maps Object Location Data
		//ArrayList<String> location = gMap.getGoogleMapsPlace();
		
		//System.out.println(location);
		
		//System.out.println("Name: " + location.get(0));
		//System.out.println("Address: " + location.get(1));
		
		return gamePanel;
	}
	
	public JPanel getGameScreen() { // Returns Panel Containing Map and Game Controls
		return gameScreen;
	}		
}
