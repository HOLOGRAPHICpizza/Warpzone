package org.peak15.warpzone.test;

import org.peak15.warpzone.client.Shared;
import org.peak15.warpzone.shared.*;
import java.net.InetAddress;

public class Testing {

	public static void main(String[] args) {
		Global.setClient();
		Global.register(Shared.client);
		
		try {
			new NetMap(InetAddress.getByName("localhost"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
