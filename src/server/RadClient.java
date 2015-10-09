package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import generic.RoverClientRunnable;

public class RadClient extends RoverClientRunnable {
    
    public RadClient(int port, InetAddress host) throws UnknownHostException {
        super(port, host);
    }
    
    @Override
    public void run() {
        
        sendMessage("RAD_BOOTUP");
        
        sendMessage("RAD_IS_ON");
        
        sendMessage("RAD_GET_POWER");
        
        sendMessage("RAD_CHECKOUT"); // get data and clear it. I suppose you would just read from the JSON file
        
        sendMessage("RAD_SHUTDOWN");
        
        sendMessage("RAD_OFF");
        
        sendMessage("RAD_IS_ON");
        
        sendMessage("exit");
        
        try {
            closeAll();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    void sendMessage(String msg) {
        try {
            ObjectOutputStream outputToAnotherObject = null;
            ObjectInputStream inputFromAnotherObject = null;
            Thread.sleep(500);
            
            // Send 5 messages to the Server
            
            // write to socket using ObjectOutputStream
            outputToAnotherObject = new ObjectOutputStream(getRoverSocket()
                                                           .getNewSocket().getOutputStream());
            
            System.out
            .println("=================================================");
            System.out
            .println("RAD Client: Sending request to Socket Server");
            System.out
            .println("=================================================");
            
            outputToAnotherObject.writeObject(msg);
            
            // read the server response message
            inputFromAnotherObject = new ObjectInputStream(getRoverSocket()
                                                           .getSocket().getInputStream());
            String message = (String) inputFromAnotherObject.readObject();
            System.out.println("RAD Client received: "
                               + message.toUpperCase());
            
            // close resources
            inputFromAnotherObject.close();
            outputToAnotherObject.close();
            Thread.sleep(100);
            
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception error) {
            System.out.println("Client: Error:" + error.getMessage());
        }
    }
    
}
