// import needed libraries
import javax.swing.*;

import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.BrowserPreferences;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// class to control how program switches between user interfaces 
// intend to implement into an interface for every class to use 
public class CardLayoutG6 {	
	// constructor of class
	public CardLayoutG6(Browser browser, BrowserView bv) {
		
		
		// set font
        Font font = new Font("sans serif",Font.PLAIN, 30);

        // set JFrame 
		JFrame frame = new JFrame();
		// set Window constraints
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.HORIZONTAL;
		frame.setPreferredSize(new Dimension(1000,1000));

		//frame.setSize(new Dimension(700,700));
	    LoginScreen login = new LoginScreen(1000, 1000, "Map");
	    WelcomeScreenClass mainW = new WelcomeScreenClass();
	    GoogleMaps mapW = new GoogleMaps(500, 500, -33.870775, 151.199025);
	    
		JPanel panelCont = new JPanel();
		JPanel panelFirst = mainW.setWelcomeScreen();
		JPanel panelSecond = login.setPlayers();
		//JPanel panelThird = mapW.createGoogleMapsFrame();
		JPanel panelThird = new GameScreen(mapW, browser, bv).getGameScreen();
		
		//JButton buttonTwo = new JButton("Switch to third panel");
		CardLayout face = new CardLayout();
		
		// button to start the game and transition into game
		JButton btnStartGame = new JButton("Start game");
		Image Start = new ImageIcon(this.getClass().getResource("/img/Start.png")).getImage();
		btnStartGame.setIcon(new ImageIcon(Start));
		btnStartGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnStartGame.setForeground(Color.BLUE);
		btnStartGame.setFont(new Font("Times New Roman", Font.ITALIC, 22));
		
        cs.gridx = 1;
        cs.gridy = 2;		

		panelCont.setLayout(face);
		
		// button to transfer after login screen
        JButton btnSubmit = new JButton("Submit");
        btnSubmit.setFont(font);
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //add button interaction here
            }
        });
        cs.gridx = 2;
        cs.gridy = 1;
        cs.gridwidth = 1;
        panelSecond.add(btnSubmit, cs);
        
		
		panelFirst.add(btnStartGame, cs);
		//panelSecond.add(buttonTwo);
		//panelThird.add(buttonTwo);
		panelFirst.setBackground(Color.BLUE);
		
		panelCont.add(panelFirst, "1");
		panelCont.add(panelSecond, "2");
		panelCont.add(panelThird,"3");
		face.show(panelCont, "1");
		
		btnStartGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				face.show(panelCont, "2");
			}
			
		});
		
		btnSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				face.show(panelCont, "3");
			}
			
		});
		
		frame.add(panelCont);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
		
		
		
	} // end of constructor


	public static void main(String[] args) {	
		//Using JXBrowser in order to display the maps.
		BrowserPreferences.setChromiumSwitches("--disable-web-security", "--allow-file-access-from-files");
		final Browser browser = new Browser(new BrowserContext(new BrowserContextParams("C:\\")));	
		final BrowserView bv = new BrowserView(browser);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new CardLayoutG6(browser, bv);
			}		
		});			
	}
}
