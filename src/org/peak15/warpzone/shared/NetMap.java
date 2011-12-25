// Used to send map over the network.

package org.peak15.warpzone.shared;

import java.awt.Image;
import java.io.IOException;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;

public class NetMap {
	
	private String name;
	private NetFile file;
	private boolean[][] table;
	
	public NetMap() {};
	
	public NetMap(Map map) {
		name = map.getName();
		table = map.getTable();
		try {
			file = new NetFile(map.getImgFile());
		} catch (IOException e) {
			e.printStackTrace();
			NetworkStuff.printErr("Failed to send map.");
		}
	}
	
	public Map getMap() {
		Image image;
		try {
			image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
			return new Map(name, image, table);
		} catch (IOException e) {
			e.printStackTrace();
			NetworkStuff.printErr("Failed to read map.");
			return null;
		}
	}

	public String getName() {
		return name;
	}
}
