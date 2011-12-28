// Used to prepare a file for transport over the network.

package org.peak15.warpzone.shared;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DataFormatException;

public class NetFile {
	public byte[] cbytes; // compressed file
	
	public NetFile(InputStream is, int length) throws IOException {
		// Read in file.
		byte[] bytes = new byte[length];
		is.read(bytes);
	    is.close();
	    
	    // Compress
	    cbytes = Global.compressBytes(bytes);
	}
	
	public void write(File file) throws IOException, DataFormatException {
		OutputStream os = new FileOutputStream(file);
		os.write(this.getBytes());
		os.close();
	}
	
	public byte[] getBytes() throws IOException, DataFormatException {
		return Global.extractBytes(cbytes);
	}
}
