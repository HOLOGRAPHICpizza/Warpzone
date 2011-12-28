package org.peak15.warpzone.server;

import java.util.HashMap;
import org.peak15.warpzone.shared.*;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class Shared {
	//TODO: Implement Secure Mode
	public static boolean secure = false;
	
	public static HashMap<Byte, Player> players = new HashMap<Byte, Player>();
	public static Server server = new Server();
	public static Map map; // Currently loaded map.
	
	private static byte LIDCount = 0;
	public static byte generateLID() {
		return LIDCount++;
	}
	
	// Dummy functions so that client style message printing works on the server. 
	public static void print(String string) {
		Log.info("warpzone", string);
	}
	
	public static void printDbg(String string) {
		Log.debug("warpzone", string);
	}
	
	public static void printErr(String string) {
		Log.error("warpzone", string);
	}
}
