// Used to send map over the network.

// Server-side: Call with map.
// Spawns a thread to run a non-blocking server to give the map to any client that wants it.

// Client-side: Call with IP address to get map from.
// Connects to server and requests map. (blocking)

package org.peak15.warpzone.shared;

import java.awt.Image;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.peak15.warpzone.client.Shared;
import org.peak15.warpzone.server.MapServer;

import com.esotericsoftware.kryo.Kryo;

public class NetMap {
	
	private String name;
	private NetFile file;
	private boolean[][] table;
	
	/**
	 * Starts a map server to host the specified map.
	 * @param map Map to host.
	 */
	public NetMap(Map map) {
		if(Global.isClient()) {
			Global.printErr("NetMap constructed from map on client!!");
			System.exit(1);
		}
		
		name = map.getName();
		table = map.getTable();
		try {
			File imgFile = map.getImgFile();
			file = new NetFile(new FileInputStream(imgFile), (int) imgFile.length());
			
			// start non-blocking map server
			new MapServer(name, file, table);
		} catch (Exception e) {
			Global.printErr("Could not construct NetMap.");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * Retrieves a map from the map server at the specified address.
	 * Based on Oracle example at http://docs.oracle.com/javase/1.4.2/docs/guide/nio/example/TimeQuery.java
	 * @param addr Address of the map server.
	 */
	public NetMap(InetAddress addr) throws Exception {
		InetSocketAddress isa = new InetSocketAddress(addr, Global.MAP_PORT);
		SocketChannel sc = null;
		try {
			// Connect
			sc = SocketChannel.open();
			sc.connect(isa);
			
			// -----------------------
			// Get the size descriptor
			// -----------------------
			ByteBuffer sizeBuf = ByteBuffer.allocate(12);
			sc.read(sizeBuf);
			sizeBuf.flip();
			
			int nameLen = sizeBuf.getInt();
			int fileLen = sizeBuf.getInt();
			int tableLen = sizeBuf.getInt();
			
			// debug print size descriptor
			//System.out.println(nameLen);
			//System.out.println(fileLen);
			//System.out.println(tableLen);
			
			// ------------
			// Get the name
			// ------------
			ByteBuffer nameBuf = ByteBuffer.allocate(nameLen);
			sc.read(nameBuf);
			nameBuf.flip();
			this.name = new String(nameBuf.array());
			
			// ------------
			// Get the file
			// ------------
			List<ByteBuffer> filePackets = new ArrayList<ByteBuffer>();
			for(int i=0; i < (fileLen / Global.PACKET_SIZE); i++) {
				ByteBuffer bb = ByteBuffer.allocateDirect(Global.PACKET_SIZE);
				sc.read(bb);
				bb.flip();
				filePackets.add(bb);
			}
			// get the last packet
			ByteBuffer lFPBb = ByteBuffer.allocateDirect(fileLen % Global.PACKET_SIZE);
			sc.read(lFPBb);
			lFPBb.flip();
			filePackets.add(lFPBb);
			
			// combine into one byte buffer
			ByteBuffer fileBuffer = Global.combinePackets(filePackets);
			
			// and make our NetFile out of it
			this.file = new NetFile(new ByteBufferInputStream(fileBuffer), fileLen);
			
			// -------------
			// Get the table
			// -------------
			List<ByteBuffer> tablePackets = new ArrayList<ByteBuffer>();
			for(int i=0; i < (tableLen / Global.PACKET_SIZE); i++) {
				ByteBuffer bb = ByteBuffer.allocateDirect(Global.PACKET_SIZE);
				sc.read(bb);
				bb.flip();
				tablePackets.add(bb);
			}
			// get the last packet
			ByteBuffer lTPBb = ByteBuffer.allocateDirect(tableLen % Global.PACKET_SIZE);
			sc.read(lTPBb);
			lTPBb.flip();
			tablePackets.add(lTPBb);
			
			// combine into one byte array
			byte[] tableArray = Global.combinePacketsToArray(tablePackets);
			
			// decompress array
			byte[] tableArrayDC = Global.extractBytes(tableArray);
			
			// convert to byte buffer
			ByteBuffer tableBuffer = ByteBuffer.allocateDirect(tableArrayDC.length);
			tableBuffer.put(tableArrayDC);
			tableBuffer.flip();
			
			// deserialize table
			Kryo kryo = Shared.client.getKryo();
			this.table = kryo.readObject(tableBuffer, boolean[][].class);
			
		} finally {
			// Make sure we close everything
			if(sc != null)
				sc.close();
		}
	}
	
	public Map getMap() {
		Image image;
		try {
			image = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
			return new Map(name, image, table);
		} catch (Exception e) {
			e.printStackTrace();
			Global.printErr("Failed to read map.");
			return null;
		}
	}

	public String getName() {
		return name;
	}
}
