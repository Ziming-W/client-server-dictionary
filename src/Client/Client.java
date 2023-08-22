package Client;

import Commnication.ClientRequest;
import Commnication.CommandCode;
import Commnication.ServerResponse;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Client {
    private static void validateArguments(String[] args){
        if(args.length != 2){
            System.err.println("Client: Number of arguments are not 2");
            System.exit(1);
        }
        // port number has to be valid
        try {
            int port = Integer.parseInt(args[1]);
            if(!(port >= 1024 && port <= 49151)){
                System.err.println("The port number is out of range [1024, 49151]");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.err.println("The port you input is not numeric");
            System.exit(1);
        }
        // server address is valid
        String ipv4Pattern =
                "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        Pattern pattern = Pattern.compile(ipv4Pattern);
        Matcher matcher = pattern.matcher(args[0]);
        if(!matcher.matches()){
            System.err.println("The server address is not valid");
            System.exit(1);
        }
    }
    public static void main(String[] args){
        // validate arguments
        validateArguments(args);
        // build socket
        try{
            Socket socket = new Socket(args[0], Integer.parseInt(args[1]));
            ClientRequest request = new ClientRequest(CommandCode.RETRIEVE, "hello", null);
            //ClientRequest request = new ClientRequest(CommandCode.UPDATE, "hello", new ArrayList<>(Arrays.asList("hello 11", "hello 22")));
            //ClientRequest request = new ClientRequest(CommandCode.CREATE, "hello", new ArrayList<>(Arrays.asList("hello 00 ", "hello ~~")));
            //ClientRequest request = new ClientRequest(CommandCode.DELETE, "hello", null);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(request);
            // get the input stream from the connected socket
            InputStream inputStream = socket.getInputStream();
            // create a DataInputStream so we can read data from it.
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ServerResponse response = (ServerResponse)objectInputStream.readObject();
            System.out.println(response.isSuccessful());
            System.out.println(response.getWord());
            System.out.println(response.getMeanings());

        }
        catch(Exception e){
            System.err.print(e.getMessage());
            System.exit(1);
        }
    }
}
