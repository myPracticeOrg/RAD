package server;

import generic.RoverServerRunnable;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.Rad;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RadServer extends RoverServerRunnable {

	private Rad rad = new Rad();
	String path = "3.json";

	public RadServer(int port) throws IOException {
		super(port);
	}

	@Override
	public void run() {

		try {
			while (true) {

				System.out.println("RAD Server: Waiting for client request");

				// read the JSON file
				readJson();

				// do work
				doWork();

				// creating socket and waiting for client connection
				getRoverServerSocket().openSocket();

				// read from socket to ObjectInputStream object
				ObjectInputStream inputFromAnotherObject = new ObjectInputStream(
						getRoverServerSocket().getSocket().getInputStream());

				// convert ObjectInputStream object to String
				String message = (String) inputFromAnotherObject.readObject();
				System.out
						.println("RAD Server: Message Received from Client - "
								+ message.toUpperCase());

				// create ObjectOutputStream object
				ObjectOutputStream outputToAnotherObject = new ObjectOutputStream(
						getRoverServerSocket().getSocket().getOutputStream());

				if (message.equals("RAD_BOOTUP")) {

					message = "RAD: Booting up -- Please wait";

					rad.bootup();
					rad.science();

				} else if (message.equals("RAD_CHECKOUT")) {

					// sends back data
					// and clears it
					rad.checkout();
					message = "Rad Data:" + rad.getData();

					rad.clearData();

				} else if (message.equals("RAD_SHUTDOWN")) {

					// loads sleep timer and initiates sleep state
					rad.sleep(); // 45 mins then bootup again

					message = "Sleeping for 45 minutes.";

				} else if (message.equals("RAD_IS_ON")) {
					// return state

					message = "RAD is currently: "
							+ (rad.isOn() ? "On" : "Off");
				} else if (message.equals("RAD_OFF")) {
					rad.off();

					message = "Turning RAD off.";
				} else if (message.equals("RAD_GET_POWER")) {

					message = "RAD power consumption is: "
							+ rad.getPowerConsumption();
				}

				writeJson();

				// write object to Socket
				outputToAnotherObject.writeObject("Rad Server response - "
						+ message);

				// close resources
				inputFromAnotherObject.close();
				outputToAnotherObject.close();

				// getRoverServerSocket().closeSocket();
				// terminate the server if client sends exit request
				if (message.equalsIgnoreCase("exit"))
					break;
			}
			System.out.println("Server: Shutting down Socket server 1!!");
			// close the ServerSocket object
			closeAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception error) {
			System.out.println("Server: Error:" + error.getMessage());
		}

	}

	String doWork() throws InterruptedException {

		String message = "";

		if (rad.isScience()) {

			message += "\nRAD: Now in SCIENCE mode. Reading data for 15 mins.";
			message += "\nRAD: Adding measurements from the environment.";

			for (int i = 0; i < 15; i++) {

				double calc = Rad.MIN_RADIATION
						+ (Math.random() * ((Rad.MAX_RADIATION - Rad.MIN_RADIATION) + 1));

				rad.addMeasurement(calc);
			}

			message += "\nRAD: Collection completed.\nGoing to sleep for 45 mins";

			Thread.sleep(15000); // sleep for 15 seconds

			rad.sleep();

		} else if (rad.isSleeping()) {

			message += "\nRAD: Now in SCIENCE mode. Reading data for 15 mins.";

			rad.science();

		}

		return message;
	}

	void writeJson() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// Instantiate the writer since we're writing to a JSON file.
		FileWriter writer = null;
		try {
			writer = new FileWriter(path);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Object is converted to a JSON String
		String jsonString = gson.toJson(rad);

		// Write the file
		try {
			writer.write(jsonString);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Close the Writer
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	void readJson() {
		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(path));
			JSONObject json = (JSONObject) obj;

			rad.setState((String) json.get("state"));

			JSONObject j = (JSONObject) json.get("data");

			rad.setData(j);

		} catch (FileNotFoundException e) {
			System.out.println("No file found. " + e.getMessage());
			// e.printStackTrace();
		} catch (IOException e) {
			System.out.println("I/O exception found.");
			e.printStackTrace();
		} catch (ParseException e) {
			System.out.println("Parse exception found.");
			e.printStackTrace();
		}

	}
}
