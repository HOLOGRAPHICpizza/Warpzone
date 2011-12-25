package org.peak15.warpzone.server;

import java.io.IOException;
import org.peak15.warpzone.shared.*;

public class Main {
	
	public static void main(String[] args) {
		NetworkStuff.setServer();
		
		NetworkStuff.print("Warpzone server starting...");
		
		// Generate and / or Load Map
		// TODO: Choose Map
		try {
			Shared.map = new Map("test");
		} catch (MapException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		// Initialize KryoNet
		NetworkStuff.register(Shared.server);
		Shared.server.addListener(new ServerListener());
		Shared.server.start();
		
		// Bind to Port
		try {
			Shared.server.bind(NetworkStuff.TCP_PORT, NetworkStuff.UDP_PORT);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		NetworkStuff.print("Listening on TCP port " + NetworkStuff.TCP_PORT + " and UDP port " + NetworkStuff.UDP_PORT + ".");
	}

}
