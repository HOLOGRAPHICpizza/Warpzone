package org.peak15.warpzone.shared;

public class Vector  {
	public double x, y;
	
	public Vector() {};
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector add(Vector arg) {
		Vector temp = new Vector();
		temp.x = this.x + arg.x;
		temp.y = this.y + arg.y;
		return temp;
	}
	
	public Vector subtract(Vector arg) {
		Vector temp = new Vector();
		temp.x = this.x - arg.x;
		temp.y = this.y - arg.y;
		return temp;
	}
	
	public Vector multiply(int arg) {
		Vector temp = new Vector();
		temp.x = this.x * arg;
		temp.y = this.y * arg;
		return temp;
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}
	
	// Returns distance from origin to point stored.
	public double magnitude() {
		double x = Math.abs(this.x);
		double y = Math.abs(this.y);
		
		// Distance formula.
		double mag = Math.sqrt( Math.pow(x, 2.0) + Math.pow(y, 2.0));
		
		return mag;
	}
}