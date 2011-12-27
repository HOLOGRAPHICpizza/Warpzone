// Non-blocking server to send maps to clients.
// Code based on Oracle example at http://docs.oracle.com/javase/1.4.2/docs/guide/nio/example/NBTimeServer.java

package org.peak15.warpzone.shared;

import java.io.*;
import java.nio.channels.*;
import java.nio.channels.spi.*;
import java.net.*;
import java.util.*;

import org.peak15.warpzone.server.Shared;

public class MapServer {
	private String name;
	private NetFile file;
	private boolean[][] table;
	
	public MapServer(String name, NetFile file, boolean[][] table) throws Exception {
		this.name = name;
		this.file = file;
		this.table = table;
		
		acceptConnections();
	}
	
	private static void acceptConnections() throws Exception {
		// Selector for incoming map requests
		Selector acceptSelector = SelectorProvider.provider().openSelector();
		
		// Create non-blocking server socket.
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		
		// Bind to local host and port.
		InetAddress lh = InetAddress.getLocalHost();
		InetSocketAddress isa = new InetSocketAddress(lh, NetworkStuff.MAP_PORT);
		ssc.socket().bind(isa);
		
		// Register accepts on the server socket with the selector. This
		// step tells the selector that the socket wants to be put on the
		// ready list when accept operations occur, so allowing multiplexed
		// non-blocking I/O to take place.
		SelectionKey acceptKey = ssc.register(acceptSelector, SelectionKey.OP_ACCEPT);
		
		int keysAdded = 0;
		
		NetworkStuff.printDbg(ssc.socket().getLocalSocketAddress().toString());
		
		// Here's where everything happens. The select method will
		// return when any operations registered above have occurred, the
		// thread has been interrupted, etc.
		while ((keysAdded = acceptSelector.select()) > 0) {
			// Someone is ready for I/O, get ready keys
			Set readyKeys = acceptSelector.selectedKeys();
			Iterator i = readyKeys.iterator();
			
			// Walk through ready keys collection and process map requests.
			while(i.hasNext()) {
				SelectionKey sk = (SelectionKey) i.next();
				i.remove();
				
				// Retrieve socket ready for IO.
				ServerSocketChannel nextReady = (ServerSocketChannel) sk.channel();
				
				// Accept map request
				Socket s = nextReady.accept().socket();
				OutputStream os = s.getOutputStream();
				
				os.write("Hello world...?".getBytes());
				
				os.close();
			}
		}
	}
}
