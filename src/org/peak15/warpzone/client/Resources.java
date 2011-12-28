// Provides resources retrieved from web location
// NOT A STATIC CLASS

//TODO: This needs local caching to disk!!

package org.peak15.warpzone.client;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.peak15.warpzone.shared.*;

import org.peak15.warpzone.shared.Global;

public class Resources implements Runnable {
	private java.util.Map<String, Image> images = new HashMap<String, Image>();
	private Map map = null;
	
	private Set<String> downloading = new HashSet<String>();
	private boolean cacheIsCurrent = false;
	
	public Resources() {
		try {
			// check if local cache exists
			if(Global.CACHE_DIR.exists()) {
				// compare serial on content server to serial in local cache
				File serial = new File(Global.CACHE_DIR, "serial");
				if(serial.exists()) {
					BufferedReader localIn = new BufferedReader(new InputStreamReader(new FileInputStream(serial)));
					String localSerial = localIn.readLine();
					localIn.close();
					
					URL serialUrl = new URL(Global.getContentServer() + "serial");
					BufferedReader remoteIn = new BufferedReader(new InputStreamReader(serialUrl.openStream()));
					String remoteSerial = remoteIn.readLine();
					remoteIn.close();
					
					Global.printDbg("Local content serial: " + localSerial);
					Global.printDbg("Remote content serial: " + remoteSerial);
					
					if(localSerial != null && remoteSerial != null && localSerial.equals(remoteSerial)) {
						// This is the only case where we can say the cache is current.
						cacheIsCurrent = true;
						Global.print("Local cache is up to date, prefering it to content server...");
						return;
					}
				}
			}
			
			// Create local cache dir if it does not exist
			if(!Global.CACHE_DIR.exists()) {
				Global.CACHE_DIR.mkdir();
			}
			
			// Overwrite or create local serial file
			URL serialUrl = new URL(Global.getContentServer() + "serial");
			BufferedReader remoteIn = new BufferedReader(new InputStreamReader(serialUrl.openStream()));
			String remoteSerial = remoteIn.readLine();
			remoteIn.close();
			
			File serial = new File(Global.CACHE_DIR, "serial");
			BufferedWriter serialOut = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(serial)));
			serialOut.write(remoteSerial);
			serialOut.close();
			
		} catch(Exception e) {
			Global.printErr("Failed to start resource handler!");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	// Begin downloading crap
	@Override
	public void run() {
		Global.printDbg("Begining background downloads.");
		getImage("background.png");
		getImage("ships/default.png");
	}
	
	/**
	 * Retrieves an image from (in order of preference) memory, local cache, or content server.
	 * @param filename Image to retrieve.
	 * @return Image retrieved.
	 */
	public Image getImage(String filename) {
		// Loaded in memory?
		if(images.containsKey(filename) && images.get(filename) != null) {
			return images.get(filename);
		}
		
		// Currently downloading?
		else if(downloading.contains(filename)) {
			// Wait for it to finish
			while(downloading.contains(filename)) {
				// Sleep a bit
				try {
					Thread.sleep(10);
				}
				catch(InterruptedException e) {}
			}
			return getImage(filename);
		}
		
		// In local cache?
		else if(cacheIsCurrent && isInCache(filename)) {
			downloading.add(filename);
			
			try {
				Global.printDbg("Grabbing " + filename + " from local cache.");
				Image img = javax.imageio.ImageIO.read(new File(Global.CACHE_DIR, filename));
				images.put(filename, img);
				downloading.remove(filename);
				return img;
			} catch (Exception e) {
				Global.printErr("Failed to grab " + filename + " from local cache.");
				e.printStackTrace();
				downloading.remove(filename);
				return null;
			}
		}
		
		// We must download it.
		else {
			downloading.add(filename);
			
			try {
				Global.printDbg("Dowloading " + filename);
				Image img = javax.imageio.ImageIO.read(new URL(Global.getContentServer() + filename));
				images.put(filename, img);
				downloading.remove(filename);
				
				// Save to local cache
				File cacheFile = new File(Global.CACHE_DIR, filename);
				if(!cacheFile.getParentFile().exists()) {
					// create parent dir if it does not exist
					cacheFile.getParentFile().mkdirs();
				}
				javax.imageio.ImageIO.write((RenderedImage) img, "png", cacheFile);
				
				return img;
			} catch (Exception e) {
				Global.printErr("Failed to grab " + filename + " from content server.");
				e.printStackTrace();
				downloading.remove(filename);
				return null;
			}
		}
	}
	
	private boolean isInCache(String filename) {
		File file = new File(Global.CACHE_DIR, filename);
		return file.exists();
	}
	
	/**
	 * Retrieves the map from (in order of preference) memory, local cache, or content server.
	 * @param filename Image to retrieve.
	 * @return Image retrieved.
	 */
	public Map getMap() {
		// This has been downloaded.
		if(map != null) {
			return map;
		}
		else {
			// Is it currently downloading?
			if(downloading.contains("MAP")) {
				// Wait for it to finish
				while(downloading.contains("MAP")) {
					// Sleep a bit
					try {
						Thread.sleep(10);
					}
					catch(InterruptedException e) {}
				}
				return getMap();
			}
			else {
				downloading.add("MAP");
				
				// download it
				try {
					Global.printDbg("Dowloading map...");
					
					NetMap nmap = new NetMap(Shared.client.getRemoteAddressTCP().getAddress());
					this.map = nmap.getMap();
					
					downloading.remove("MAP");
					return map;
				} catch (Exception e) {
					Global.printErr("Failed to grab map from server.");
					e.printStackTrace();
					downloading.remove("MAP");
					return null;
				}
			}
		}
	}
}
