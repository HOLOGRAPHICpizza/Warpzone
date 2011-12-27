// A whole bunch of random crap needed by client and server.

package org.peak15.warpzone.shared;

import java.util.ArrayList;
import org.peak15.warpzone.shared.ships.*;
import com.esotericsoftware.kryonet.*;
import com.esotericsoftware.kryo.*;
import com.esotericsoftware.minlog.Log;
//import com.esotericsoftware.kryo.serialize.SimpleSerializer;
//import java.awt.Color;
//import java.nio.ByteBuffer;

public class NetworkStuff {
	
	// Set true if this is running on client, false if running on the server.
	private static boolean CLIENT;
	private static boolean clientSet = false;
	public static void setClient() {
		if(!clientSet) {
			CLIENT = true;
			clientSet = true;
		}
		else {
			printErr("Invalid call to setClient().");
		}
	}
	public static void setServer() {
		if(!clientSet) {
			CLIENT = false;
			clientSet = true;
		}
		else {
			printErr("Invalid call to setServer().");
		}
	}
	public static boolean isClient() {
		if(!clientSet) {
			printErr("Client/Server not set.");
			System.exit(1);
		}
		return CLIENT;
	}
	public static boolean isServer() {
		if(!clientSet) {
			printErr("Client/Server not set.");
			System.exit(1);
		}
		return !CLIENT;
	}
	
	public static boolean DEBUG = true;
	
	public static final int TCP_PORT = 1337;
	public static final int UDP_PORT = 1337;
	public static final int MAP_PORT = 9001;
	public static final int WIDTH = 854;
	public static final int HEIGHT = 480;
	public static final int TILE_SIZE = 24;
	
	public static final Vector CENTER = new Vector( (double) WIDTH/2.0, (double) HEIGHT/2.0);
	
	/**
	 * Converts a short to a byte table.
	 * @param s Short to convert.
	 * @return Byte table from short.
	 */
	public static byte[] toBytes(short s) {
        return new byte[]{(byte)(s & 0x00FF),(byte)((s & 0xFF00)>>8)};
    }
	
	/**
	 * Converts a byte table to a short.
	 * @param bytes Byte table to convert.
	 * @return Short from byte table.
	 */
	public static short toShort(byte[] bytes) {
		return (short)( ((bytes[1]&0xFF)<<8) | (bytes[0]&0xFF) );
	}
	
	/**
	 * Converts an int to a byte table.
	 * @param value Int to convert.
	 * @return Byte table from int.
	 */
	public static byte[] toBytes(int value) {
	    return new byte[] {
	            (byte)(value >>> 24),
	            (byte)(value >>> 16),
	            (byte)(value >>> 8),
	            (byte)value};
	}
	
	// Universal print functions.
	public static void print(String string) {
		if(CLIENT) {
			org.peak15.warpzone.client.Shared.print(string);
		}
		else {
			org.peak15.warpzone.server.Shared.print(string);
		}
	}
	
	public static void printDbg(String string) {
		if(DEBUG) {
			if(CLIENT) {
				org.peak15.warpzone.client.Shared.printDbg(string);
			}
			else {
				org.peak15.warpzone.server.Shared.printDbg(string);
			}
		}
	}
	
	public static void printErr(String string) {
		if(CLIENT) {
			org.peak15.warpzone.client.Shared.printErr(string);
		}
		else {
			org.peak15.warpzone.server.Shared.printErr(string);
		}
	}
	
	// Kryo Register functions
	public static void register(Server server) {
		Kryo kryo = server.getKryo();
		reg(kryo);
	}
	
	public static void register(Client client) {
		Kryo kryo = client.getKryo();
		reg(kryo);
	}
	
	private static void reg(Kryo kryo) {
		if(DEBUG) {
			Log.set(Log.LEVEL_DEBUG);
		}
		
		kryo.register(JoinRequest.class);
		kryo.register(Player.class);
		kryo.register(Vector.class);
		kryo.register(DefaultShip.class);
		//kryo.register(NetFile.class, new DeflateCompressor(new FieldSerializer(kryo, NetFile.class), 104857600)); // This buffer may need to be increased.
		kryo.register(NetFile.class);
		kryo.register(NetMap.class);
		kryo.register(byte[].class);
		kryo.register(boolean[][].class);
		kryo.register(ArrayList.class);
		/*kryo.register(Map.class);
		kryo.register(java.awt.image.BufferedImage.class);
		kryo.register(java.awt.image.ComponentColorModel.class);
		kryo.register(java.awt.color.ICC_ColorSpace.class);
		kryo.register(float[].class);
		kryo.register(java.awt.color.ICC_ProfileRGB.class);
		kryo.register(short[].class);*/
		
		/*kryo.register(Color.class, new SimpleSerializer<Color>() {
			public void write (ByteBuffer buffer, Color color) {
				buffer.putInt(color.getRGB());
			}
			public Color read (ByteBuffer buffer) {
				return new Color(buffer.getInt());
			}
		});*/
	}
	
	// Returns a player from a player id
	/*public static Player getPlayer(byte id, ArrayList<Player> players) {
		Player temp;
		for(int i=0; i<players.size(); i++) { // find the right player
			temp = (Player)players.get(i);
			
			if(temp.id == id) {
				return temp;
			}
		}
		
		return null;
	}*/
}
