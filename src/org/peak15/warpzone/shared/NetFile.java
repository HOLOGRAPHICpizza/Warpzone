// Used to send a file over the network.
// Loads up the file and stores it in a byte[]
// Best sent over the network compressed

package org.peak15.warpzone.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NetFile {
	
	// Lets the server know what to do with the file.
	public enum FileType {
		OTHER
	}
	
	private FileType type;
	private byte[] bytes; // the file

	public NetFile() {}
	
	public NetFile(File file) throws IOException {
		this(file, FileType.OTHER);
	}

	public NetFile(File file, FileType type) throws IOException {
		this.type = type;
		
		// Read in file.
		InputStream is = new FileInputStream(file);
		bytes = new byte[(int) file.length()];
		is.read(bytes);
	    is.close();
	}
	
	public void write(File file) throws IOException {
		OutputStream os = new FileOutputStream(file);
		os.write(bytes);
		os.close();
	}
	
	public FileType getType() {
		return type;
	}
	
	public byte[] getBytes() {
		return bytes;
	}
}
