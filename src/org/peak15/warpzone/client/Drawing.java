package org.peak15.warpzone.client;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import org.peak15.warpzone.shared.*;

public class Drawing {
	
	public static void draw(Object obj, Graphics g) {
		
		// Players
		if(obj instanceof Player) {
			Player ply = (Player) obj;
			
			Image ship = Shared.resources.getImage(ply.getShip().getImage());
			Image newShip = new BufferedImage(Player.SIZE, Player.SIZE, BufferedImage.TYPE_INT_ARGB);
			Graphics2D ship2D = (Graphics2D) newShip.getGraphics();
			//TODO: Graphics Quality
			ship2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
			ship2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			// Rotate ship
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians(ply.getAngle()), Player.SIZE/2, Player.SIZE/2);
			ship2D.drawImage(ship, at, Shared.main);
			
			// Draw ship
			g.drawImage(newShip,
					(int) NetworkStuff.CENTER.x - Player.SIZE/2,
					(int) NetworkStuff.CENTER.y - Player.SIZE/2,
					Shared.main);
			
			g.setColor(Color.WHITE);
			g.drawString(ply.getName(),
					(int) NetworkStuff.CENTER.x - Player.SIZE/2, 
					(int) NetworkStuff.CENTER.y - Player.SIZE/2 - 20); // Draw name 20 px above player.
			g.drawString(Integer.toString(ply.getScore()),
					(int) NetworkStuff.CENTER.x - Player.SIZE/2,
					(int) NetworkStuff.CENTER.y - Player.SIZE/2 - 5); // Draw score 5 px above player.
			
			// Debug Text
			g.setColor(Color.CYAN);
			g.drawString(Integer.toString(ply.getAngle()),
					(int) NetworkStuff.CENTER.x + Player.SIZE/2 + 5,
					(int) NetworkStuff.CENTER.y);
		}
	}
}
