package org.peak15.warpzone.shared;

public class MapException extends Exception {

	private static final long serialVersionUID = -1579772096310635543L;

	public MapException() {}

	public MapException(String arg0) {
		super(arg0);
	}

	public MapException(Throwable arg0) {
		super(arg0);
	}

	public MapException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
