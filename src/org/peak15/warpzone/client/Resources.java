// Provides resources retrieved from web location
// NOT A STATIC CLASS

//TODO: This needs local caching to disk!!

package org.peak15.warpzone.client;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.peak15.warpzone.shared.*;

import org.peak15.warpzone.shared.NetworkStuff;

public class Resources implements Runnable {
	private java.util.Map<String, Image> images = new HashMap<String, Image>();
	private Map map = null;
	
	private Set<String> downloading = new HashSet<String>();
	
	/*public Resources() {
		// Download from content server by default
		boolean download = true;
		
		// check if local cache exists
		File cacheDir =
		
		// compare serial on content server to serial in local cache
	}*/
	
	// Begin downloading crap
	@Override
	public void run() {
		NetworkStuff.printDbg("Begining background downloads.");
		getImage("background.png");
		getImage("ships/default.png");
	}
	
	/**
	 * Retrieves an image from (in order of preference) memory, local cache, or content server.
	 * @param filename Image to retrieve.
	 * @return Image retrieved.
	 */
	public Image getImage(String filename) {
		// This has been downloaded.
		if(images.containsKey(filename) && images.get(filename) != null) {
			return images.get(filename);
		}
		else {
			// Is it currently downloading?
			if(downloading.contains(filename)) {
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
			else {
				downloading.add(filename);
				
				// download it
				try {
					NetworkStuff.printDbg("Dowloading " + filename);
					Image img = javax.imageio.ImageIO.read(new URL(NetworkStuff.CONTENT_SERVER + filename));
					images.put(filename, img);
					downloading.remove(filename);
					return img;
				} catch (Exception e) {
					NetworkStuff.printErr("Failed to grab " + filename + " from content server.");
					e.printStackTrace();
					downloading.remove(filename);
					return null;
				}
			}
		}
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
					NetworkStuff.printDbg("Dowloading map...");
					
					NetMap nmap = new NetMap(Shared.client.getRemoteAddressTCP().getAddress());
					this.map = nmap.getMap();
					
					downloading.remove("MAP");
					return map;
				} catch (Exception e) {
					NetworkStuff.printErr("Failed to grab map from server.");
					e.printStackTrace();
					downloading.remove("MAP");
					return null;
				}
			}
		}
	}
}
