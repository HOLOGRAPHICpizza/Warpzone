package org.peak15.warpzone.test;

import org.peak15.warpzone.shared.*;

public class Testing {

	public static void main(String[] args) {
		NetworkStuff.setServer();
		
		Map map = null;
		try {
			map = new Map("test");
		} catch (MapException e) {
			e.printStackTrace();
		}
		
		// This should start map server.
		NetMap nmap = new NetMap(map);
		NetworkStuff.print("Non-blocking! ;D");
		NetworkStuff.print(nmap.getName());
	}

}
