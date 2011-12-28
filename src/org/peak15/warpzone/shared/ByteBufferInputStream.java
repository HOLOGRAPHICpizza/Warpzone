// An input stream backed by a byte buffer
// Based on example at: http://www.java2s.com/Code/Java/File-Input-Output/CreatinganinputoroutputstreamonaByteBuffer.htm

package org.peak15.warpzone.shared;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferInputStream extends InputStream {
	private ByteBuffer buf;
	
	ByteBufferInputStream(ByteBuffer buf) {
		this.buf = buf;
	}
	
	@Override
	public synchronized int read() throws IOException {
		if (!buf.hasRemaining()) {
			return -1;
		}
		return buf.get();
	}
	
	@Override
	public synchronized int read(byte[] bytes, int off, int len) throws IOException {
		len = Math.min(len, buf.remaining());
		buf.get(bytes, off, len);
		return len;
	}
}
