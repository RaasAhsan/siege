package com.jantox.dungmast.network;

import java.net.Socket;

public class Client {

	Socket socket;
	
	public Client(String server, int port) {
		
	}
	
	public int getPort() {
		return socket.getPort();
	}
	
}
