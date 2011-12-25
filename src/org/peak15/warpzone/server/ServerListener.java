package org.peak15.warpzone.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.peak15.warpzone.shared.*;

public class ServerListener extends Listener {
	
	public void received(Connection connection, Object object) {
		
		// Echo strings
		if(object instanceof String) {
			String str = (String) object;
			connection.sendTCP(str);
			NetworkStuff.printDbg("Echoed: " + str);
		}
		
		// Player Joining
		else if(object instanceof JoinRequest) {
			JoinRequest req = (JoinRequest) object;
			
			// Make sure security matches.
			if(Shared.secure != req.secure) {
				NetworkStuff.printErr("Secuity mismatch on join request from " + connection.getRemoteAddressTCP().toString());
				return;
			}
			
			// Create Player
			String name = "";
			
			if(!Shared.secure) {
				// Insecure
				name = req.name;
			}
			else {
				// Secure
			}
			
			Player ply = new Player(name, Shared.generateLID());
			
			// Send player to all clients.
			Shared.server.sendToAllTCP(ply);
			
			// Send state to connecting client.
			for(Player player:Shared.players.values()) {
				connection.sendTCP(player);
			}
			
			// Add player to list
			Shared.players.put(ply.getLID(), ply);
			
			NetworkStuff.print(name + " has joined the game.");
			
			// Send map to client.
			NetMap nmap = new NetMap(Shared.map);
			connection.sendTCP(nmap);
		}
	}
}
