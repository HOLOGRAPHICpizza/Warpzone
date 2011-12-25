// Superclass for ship types.

package org.peak15.warpzone.shared.ships;

public interface Ship {
	/**
	 * @return Max speed of ship.
	 */
	public double getMaxSpeed();
	
	/**
	 * @return Acceleration of ship.
	 */
	public double getAccel();
	
	/**
	 * @return ID number of ship.
	 */
	public byte getID();
	
	/**
	 * @return Filename of ship image.
	 */
	public String getImage();
}
