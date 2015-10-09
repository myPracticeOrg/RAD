package main;

import generic.RoverThreadHandler;

import java.io.IOException;

import server.RadClient;
import server.RadServer;

public class RadMain {

	public static void main(String[] args) {

		// Each module has its own port
		int port_one = 9003;

		try {

			// create a thread for module one
			RadServer serverOne = new RadServer(port_one);
			Thread server_1 = RoverThreadHandler.getRoverThreadHandler()
					.getNewThread(serverOne);

			// server begins listening
			server_1.start();

			// client one server sending messages to server
			RadClient clientOne = new RadClient(port_one, null); // notice
																	// port_two
			Thread client_1 = RoverThreadHandler.getRoverThreadHandler()
					.getNewThread(clientOne);

			// start the thread which communicates through sockets
			client_1.start();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
