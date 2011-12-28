// Non-blocking server to send maps to clients.
// Code based on Oracle example at http://docs.oracle.com/javase/1.4.2/docs/guide/nio/example/NBTimeServer.java

package org.peak15.warpzone.server;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.net.*;
import java.util.*;

import org.peak15.warpzone.shared.NetFile;
import org.peak15.warpzone.shared.Global;

import com.esotericsoftware.kryo.Kryo;

public class MapServer implements Runnable {
	private static byte[] name;
	private static byte[] file;
	private static byte[] table;
	
	public MapServer(String name, NetFile file, boolean[][] table) throws Exception {
		MapServer.name = name.getBytes();
		MapServer.file = file.cbytes;
		
		// Serialize table
		Kryo kryo = Shared.server.getKryo();
		int tableLen = (table[0].length * table.length)+ 200;
		ByteBuffer tableBb = ByteBuffer.allocate(tableLen);
		kryo.writeObject(tableBb, table);
		
		// Compress array
		MapServer.table = Global.compressBytes(tableBb.array());
		
		Thread t = new Thread(this);
		t.start();
	}
	
	private static void acceptConnections() throws Exception {
		// Selector for incoming map requests
		Selector acceptSelector = SelectorProvider.provider().openSelector();
		
		// Create non-blocking server socket.
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		
		// Bind to local host and port.
		InetAddress lh = Shared.server.getInetSocket().getAddress();
		InetSocketAddress isa = new InetSocketAddress(lh, Global.MAP_PORT);
		ssc.socket().bind(isa);
		
		// Register accepts on the server socket with the selector. This
		// step tells the selector that the socket wants to be put on the
		// ready list when accept operations occur, so allowing multiplexed
		// non-blocking I/O to take place.
		ssc.register(acceptSelector, SelectionKey.OP_ACCEPT);
		
		// Here's where everything happens. The select method will
		// return when any operations registered above have occurred, the
		// thread has been interrupted, etc.
		while (acceptSelector.select() > 0) {
			// Someone is ready for I/O, get ready keys
			Set<SelectionKey> readyKeys = acceptSelector.selectedKeys();
			Iterator<SelectionKey> i = readyKeys.iterator();
			
			// Walk through ready keys collection and process map requests.
			while(i.hasNext()) {
				SelectionKey sk = i.next();
				i.remove();
				
				// Retrieve socket ready for IO.
				ServerSocketChannel nextReady = (ServerSocketChannel) sk.channel();
				
				// Accept map request
				Socket s = nextReady.accept().socket();
				OutputStream os = s.getOutputStream();
				WritableByteChannel oc = Channels.newChannel(os);
				
				// ------------------------
				// Send the size descriptor
				// ------------------------
				ByteBuffer sizeBuf = ByteBuffer.allocate(12);
				sizeBuf.putInt(name.length);
				sizeBuf.putInt(file.length);
				sizeBuf.putInt(table.length);
				sizeBuf.flip();
				
				// debug print size descriptor
				//System.out.println(name.length);
				//System.out.println(file.length);
				//System.out.println(table.length);
				
				oc.write(sizeBuf);
				
				// -------------
				// Send the name
				// -------------
				os.write(name);
				
				// -------------
				// Send the file
				// -------------
				// split into packets
				List<byte[]> filePackets = new ArrayList<byte[]>();
				filePackets = Global.toPackets(file);
				// and send them
				for(byte[] bt : filePackets) {
					os.write(bt);
				}
				
				// --------------
				// Send the table
				// --------------
				// split into packets
				List<byte[]> tablePackets = new ArrayList<byte[]>();
				tablePackets = Global.toPackets(table);
				// and send them
				for(byte[] bt : tablePackets) {
					os.write(bt);
				}
				
				oc.close();
				os.close();
			}
		}
	}

	@Override
	public void run() {
		try {
			acceptConnections();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
