package org.peak15.warpzone.shared.ships;

public class DefaultShip implements Ship {
	
	public DefaultShip() {}
	
	@Override
	public double getMaxSpeed() {
		return 10;
	}

	@Override
	public double getAccel() {
		return 1;
	}
	
	@Override
	public byte getID() {
		return 0;
	}

	@Override
	public String getImage() {
		return "ships/default.png";
	}
}
