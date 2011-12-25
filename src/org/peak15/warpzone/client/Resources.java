// Provides resources retrieved from web location
// NOT A STATIC CLASS

//TODO: This needs local caching to disk!!

package org.peak15.warpzone.client;

import java.awt.Image;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.peak15.warpzone.shared.NetworkStuff;

public class Resources implements Runnable {
	private final String CONTENT_SERVER = "http://www.peak15.org.nyud.net/warpzone/content/";
	
	private Map<String, Image> images = new HashMap<String, Image>();
	Set<String> downloading = new HashSet<String>();
	
	// Begin downloading crap
	@Override
	public void run() {
		NetworkStuff.printDbg("Begining background downloads.");
		getImage("background.png");
		getImage("ships/default.png");
	}
	
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
					Image img = javax.imageio.ImageIO.read(new URL(CONTENT_SERVER + filename));
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
}
