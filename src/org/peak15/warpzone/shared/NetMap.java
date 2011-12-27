// Used to send map over the network.

// Server-side: Call with map.
// Spawns a thread to run a non-blocking server to give the map to any client that wants it.

// Client-side: Call with IP address to get map from.
// Connects to server and requests map. (blocking)

package org.peak15.warpzone.shared;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;

public class NetMap {
	
	private String name;
	private NetFile file;
	private boolean[][] table;
	
	public NetMap(Map map) {
		if(NetworkStuff.isClient()) {
			NetworkStuff.printErr("NetMap constructed from map on client!!");
			System.exit(1);
		}
		
		name = map.getName();
		table = map.getTable();
		try {
			file = new NetFile(map.getImgFile());
			
			// start non-blocking map server
			new MapServer(name, file, table);
		} catch (Exception e) {
			NetworkStuff.printErr("Could not construct NetMap.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	//TODO: IP address constructor.
	
	public Map getMap() {
		Image image;
		try {
			image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
			return new Map(name, image, table);
		} catch (Exception e) {
			e.printStackTrace();
			NetworkStuff.printErr("Failed to read map.");
			return null;
		}
	}

	public String getName() {
		return name;
	}
}
