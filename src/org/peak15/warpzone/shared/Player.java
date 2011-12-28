package org.peak15.warpzone.shared;

import org.peak15.warpzone.shared.ships.*;

public class Player {
	private String name;
	private byte LID = -1; // Local ID
	private Ship ship; // Ship Type
	
	private Vector pos = new Vector(0, 0); // Current position
	private Vector TVol = new Vector(0, 0); // Target velocity
	private Vector CVol = new Vector(0, 0); // Current velocity
	private int score = 0;
	private int angle = 0;

	public static final int SIZE = Global.TILE_SIZE * 2; // width and height of all players
	
	public Player() {}
	
	public Player(String name, byte LID) {
		this(name, LID, Global.CENTER, new DefaultShip());
	}
	
	public Player(String name, byte LID, Vector pos, Ship ship) {
		this.name = name;
		this.LID = LID;
		this.pos = pos;
		this.ship = ship;
	}
	
	public int getAngle() {
		return angle;
	}
	
	public void turnRight() {
		angle += 2;
		if(angle >= 360) {
			angle = 0;
		}
	}
	
	public void turnLeft() {
		angle -= 2;
		if(angle < 0) {
			angle = 359;
		}
	}
	
	public byte getLID() {
		return LID;
	}

	public Vector getPos() {
		return pos;
	}

	public void setPos(Vector pos) {
		this.pos = pos;
	}

	public Vector getTVol() {
		return TVol;
	}

	public void setTVol(Vector tVol) {
		TVol = tVol;
	}

	public String getName() {
		return name;
	}

	public Ship getShip() {
		return ship;
	}

	public Vector getCVol() {
		return CVol;
	}

	public int getScore() {
		return score;
	}
}
