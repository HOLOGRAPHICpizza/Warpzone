// Stores stuff used by all parts of the client.

package org.peak15.warpzone.client;

import java.awt.Color;
import java.util.HashMap;
import com.esotericsoftware.kryonet.Client;
import org.peak15.warpzone.shared.*;

public class Shared {
	//TODO: Implement Secure Mode
	public static boolean secure = false; // Secure mode uses global server identification.
	public static Client client = new Client(); // Kryonet client
	
	// Will hold reference to main applet.
	public static Main main;
	
	public static Resources resources = new Resources();
	
	public static HashMap<Byte, Player> players = new HashMap<Byte, Player>();
	public static Player ply = null; // Local player.
	
	public static Map map = null;
	
	private static Color statusColor = Color.WHITE;
	private static String status = ""; 
	
	public static Color getStatusColor() {
		return statusColor;
	}
	
	public static String getStatus() {
		return status;
	}
	
	// These methods could later be used to print to an in game console instead of stdout, or expanded to support color.
	public static void print(String string) {
		System.out.println(string);
		statusColor = Color.WHITE;
		status = string;
	}
	
	public static void printDbg(String string) {
		System.out.println(string);
		statusColor = Color.CYAN;
		status = string;
	}
	
	public static void printErr(String string) {
		System.err.println(string);
		statusColor = Color.RED;
		status = string;
	}
}
