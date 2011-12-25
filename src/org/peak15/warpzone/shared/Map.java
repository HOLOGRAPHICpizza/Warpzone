// Contains map image and collision table.

package org.peak15.warpzone.shared;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Map {
	private String name;
	private File imgFile;
	private Image image;
	private boolean[][] table;
	
	public Map() {}
	
	public Map(String name, Image image, boolean[][] table) {
		this.image = image;
		this.table = table;
	}
	
	public Map(String name) throws MapException {
		this.name = name;
		
		File mapDir = new File("maps");
		File dir = new File(mapDir, name);
		File srcDir = new File(dir, "src");
		imgFile = new File(dir, name + ".png");
		File tblFile = new File(dir, name + ".dat");
		
		if(!imgFile.exists() || !tblFile.exists()) { // Map not generated
			
			if(dir.isDirectory()) { // Map resources exist.
				File keyFile = new File(srcDir, "key.png");
				if(keyFile.exists()) {
					try {
						BufferedImage key = ImageIO.read(keyFile);
						
						// Do actual map generation.
						generate(key, dir);
						
						// Serialize map parts and save to disk.
						save(imgFile, tblFile);
						
					} catch (IOException e) {
						throw new MapException(e);
					}
				}
				else {
					throw new MapException("Keyfile for map " + name + " is missing.");
				}
			}
			else {
				throw new MapException("Resources for map " + name + " are missing.");
			}
		}
		else { // Map Generated, Load Map
			try {
				image = ImageIO.read(imgFile);
				
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(tblFile));
                table = (boolean[][]) in.readObject();
                in.close();
			} catch (IOException e) {
				throw new MapException(e);
			} catch (ClassNotFoundException e) {
				throw new MapException(e);
			}
		}
	}

	private void generate(BufferedImage key, File dir) throws MapException {
		try {
			int width = key.getWidth();
			int height = key.getHeight();
			
			HashMap<Integer, Image> tiles = new HashMap<Integer, Image>();
			
			// Create the canvas
			image = new BufferedImage(NetworkStuff.TILE_SIZE * width, NetworkStuff.TILE_SIZE * height, BufferedImage.TYPE_INT_ARGB);
			Graphics draw = image.getGraphics();
			
			// collision map
			table = new boolean[width][height];
			
			File srcDir = new File(dir, "src");
			
			// Iterate through pixels.
			for(int i=0;i<width;i++) {
				for(int j=0;j<height;j++) {
					int color = key.getRGB(i, j);
					
					// Not a completely transparent pixel
					if(color != 0) {
						if(!tiles.containsKey(color)) {
							// This color has not yet been encountered.
							File tile = new File(srcDir, Integer.toHexString(color).substring(2) + ".png");
							tiles.put(color, ImageIO.read(tile));
						}
						
						// render to canvas
						Image tile = tiles.get(color);
						int x = NetworkStuff.TILE_SIZE * i;
						int y = NetworkStuff.TILE_SIZE * j;
						draw.drawImage(tile, x, y, null);
					}
					
					// generate collision map
					byte[] colByte = NetworkStuff.toBytes(color);
					if(colByte[0] != 0xffffffff) {
						// This pixel is at least partially transparent
						table[i][j] = false;
					}
					else {
						table[i][j] = true;
					}
				}
			}
			
		} catch(IOException e) {
			throw new MapException(e);
		}
	}
	
	private void save(File imgFile, File tblFile) throws MapException {
		try {
			ImageIO.write((RenderedImage) image, "png", imgFile);
			
			ObjectOutputStream tout = new ObjectOutputStream(new FileOutputStream(tblFile));
            tout.writeObject(table);
            tout.close();
		} catch (IOException e) {
			throw new MapException(e);
		}
	}

	public Image getImage() {
		return image;
	}

	public boolean[][] getTable() {
		return table;
	}
	
	public String getName() {
		return name;
	}
	
	public File getImgFile() {
		return imgFile;
	}
}
