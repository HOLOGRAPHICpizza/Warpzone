package org.peak15.warpzone.server;

import java.io.IOException;
import org.peak15.warpzone.shared.*;

import com.esotericsoftware.minlog.Log;

public class Main {
	
	public static void main(String[] args) {
		Global.setServer();
		
		if(Global.DEBUG) {
			Log.set(Log.LEVEL_DEBUG);
		}
		
		Global.print("Warpzone server starting...");
		
		// Generate and / or Load Map
		// TODO: Choose Map
		try {
			Shared.map = new Map("test");
		} catch (MapException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		// Initialize KryoNet
		Global.register(Shared.server);
		Shared.server.addListener(new ServerListener());
		Shared.server.start();
		
		// Bind to Port
		try {
			Shared.server.bind(Global.TCP_PORT, Global.UDP_PORT);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		Global.print("Listening on TCP port " + Global.TCP_PORT + " and UDP port " + Global.UDP_PORT + ".");
		
		// Start map server.
		new NetMap(Shared.map);
		Global.print("Map server started on TCP port " + Global.MAP_PORT + ".");
	}

}
