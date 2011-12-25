// Sent when player joins the game.

package org.peak15.warpzone.shared;

import org.peak15.warpzone.shared.ships.*;

public class JoinRequest {
	public boolean secure;
	
	public String name = ""; // Used if the server is in insecure mode.
	public long GID = 0; // Global ID - Used in secure mode.
	
	public byte ship = 0;
	
	public JoinRequest() {}
	
	public JoinRequest(String name, Ship ship) {
		this.secure = false;
		this.name = name;
		this.ship = ship.getID();
	}
	
	public JoinRequest(long GID, Ship ship) {
		this.secure = true;
		this.GID = GID;
		this.ship = ship.getID();
	}
}
