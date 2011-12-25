package org.peak15.warpzone.client;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.peak15.warpzone.shared.*;
import org.peak15.warpzone.shared.ships.*;

public class Main extends Applet {
	
	private static final long serialVersionUID = -6718818095447609119L;
	
	// Double Buffering
	private Image dbImage;
	private Graphics dbg;
	
	public void init() {
		NetworkStuff.setClient();
		Shared.main = this;
		this.setSize(NetworkStuff.WIDTH, NetworkStuff.HEIGHT);
		
		// Double buffering
		//dbImage = createImage(NetworkStuff.WIDTH, NetworkStuff.HEIGHT);
		//dbg = dbImage.getGraphics();
		
		// Begin background downloading
		Thread d = new Thread(Shared.resources);
		d.start();
		
		// Initialize KryoNet
		NetworkStuff.register(Shared.client);
		Shared.client.addListener(new ClientListener());
		Shared.client.start();
		
		// -----------
		// Join Player
		// -----------
		String name = "";
		String hostname = "";
		
		// Get player info
		if(!Shared.secure) {
			name = JOptionPane.showInputDialog("Name:", "blarg");
			hostname = JOptionPane.showInputDialog("Server IP / hostname:", "localhost");
		}
		else {
			//TODO: Connect to master server and verify and crap
		}
		
		// Connect to server
		try {
			Shared.client.connect(5000, hostname, NetworkStuff.TCP_PORT, NetworkStuff.UDP_PORT);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		// Send Join Request TODO: Ship Selection
		// Server will create and send back player object, map, and game state.
		Shared.client.sendTCP(new JoinRequest(name, new DefaultShip()));
		
		// Start Main Loop
		ClientLoop cl = new ClientLoop();
		// Hook up keyboard polling
		addKeyListener(cl.keyboard);
		Thread t = new Thread(cl);
		t.start();
	}
	
	public void startGraphics() {
		if(dbg != null) {
			NetworkStuff.printErr("Graphics already started!");
			return;
		}
		
		dbImage = createImage(NetworkStuff.WIDTH, NetworkStuff.HEIGHT);
		//dbImage = createImage(Shared.map.getImage().getWidth(null), Shared.map.getImage().getHeight(null));
		dbg = dbImage.getGraphics();
	}
	
	public void paint(Graphics g) {
		if(dbg == null) return; // Blocks until graphics are started.
		
		// 1. Draw everything within radius of the player to the map
		// 2. Draw the portion of the map centered on the player.
		// 3. ???
		// 4. Profit!!!
		
		// Draw background
		dbg.drawImage(Shared.resources.getImage("background.png"), 0, 0, this);
		
		// Draw players
		for(Player ply : Shared.players.values()) {
			Drawing.draw(ply, dbg);
		}
		
		// Draw Status
		dbg.setColor(Shared.getStatusColor());
		dbg.drawString(Shared.getStatus(), 10, 20);
		
		g.drawImage(dbImage, 0, 0, this);
	}
	
	// Override update method to do nothing but paint the screen when repaint() is called
	// Makes the double buffering more sexy
	public void update(Graphics g) {
		paint(g);
	}
	
}