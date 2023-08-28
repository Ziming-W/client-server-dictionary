// Author: Ziming Wang (1180051) https://github.com/Ziming-W
// purpose: my own casual test, no businesss logic, random content
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import Communication.ClientRequest;
import Communication.CommandCode;
public class Test {
    public static void main(String[] args){
        try {
            // Create a client socket and connect to the server
            Socket socket = new Socket("127.0.0.1", 8080);

            // Create output stream to send data to server
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());

            // Create a client request object
            ClientRequest request = new ClientRequest(CommandCode.CREATE, "", null);

            // Send the request to the server
            outStream.writeObject(null);
            outStream.flush();

            // Close the socket
            socket.close();

            System.out.println("Client request sent successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
