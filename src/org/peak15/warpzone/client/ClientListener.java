package org.peak15.warpzone.client;

import org.peak15.warpzone.shared.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener {

	public ClientListener() {}
	
	public void received(Connection connection, Object object) {
		
		// Echo Strings
		if(object instanceof String) {
			String str = (String) object;
			NetworkStuff.print("Received: " + str);
		}
		
		// Player has joined the game.
		else if(object instanceof Player) {
			Player player = (Player) object;
			
			if(Shared.players.isEmpty()) { // First player, local player.
				Shared.ply = player;
			}
			
			Shared.players.put(player.getLID(), player);
			
			NetworkStuff.print(player.getName() + " has joined the game.");
		}
	}
}
