package org.peak15.warpzone.client;

import java.awt.event.KeyEvent;

public class ClientLoop implements Runnable {
	
	public KeyboardInput keyboard = new KeyboardInput(); // Keyboard polling
	
	public ClientLoop() {}

	@Override
	public void run() {
		while(true) {
			// Poll keyboard
			processInput();
			
			// Repaint the screen
			Shared.main.repaint();
			
			// Sleep a bit
			try {
				Thread.sleep(10);
			}
			catch(InterruptedException e) {}
		}
	}

	private void processInput() {
		keyboard.poll();
		
		// left
		if( keyboard.keyDown( KeyEvent.VK_A ) ) {
			Shared.ply.turnLeft();
		}
		
		// right
		if( keyboard.keyDown( KeyEvent.VK_D ) ) {
			Shared.ply.turnRight();
		}
	}
}
