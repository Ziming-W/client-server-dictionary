package Server;

import Communication.ClientRequest;
import Communication.CommandCode;
import Communication.ServerResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static Dictionary dic;

    /**
     * Validate commandline arguments
     */
    private static void validateArgument(String[] args){
        // two argument
        if(args.length != 2){
            System.err.println("Number of argument is not 2");
            System.exit(1);
        }
        // port number has to be valid
        try {
            int port = Integer.parseInt(args[0]);
            if(!(port >= 1024 && port <= 49151)){
                System.err.println("The port number is out of range [1024, 49151]");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.err.println("The port you input is not numeric");
            System.exit(1);
        }
    }

    private static void runServer(int port){
        try{
            ServerSocket listenSocket = new ServerSocket(port);
            System.out.println("successfully start listening socket");
            while(true){
                Socket commSocket = listenSocket.accept();
                System.out.println("Accepted connection from " + commSocket.getInetAddress());
                Thread clientThread = new Thread(() -> {
                    System.out.println("Thread started for client: " + commSocket.getInetAddress());
                    ServerCRUDRequestHandler.handleClientRequest(commSocket, dic);
                    System.out.println("Thread terminated for client: " + commSocket.getInetAddress());
                });
                clientThread.start();
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    public static void main(String[] args){
        // validate args[0] and args[1]
        validateArgument(args);
        System.out.println("All arguments are valid");
        // initialize a new dictionary
        dic = new Dictionary(args[1]);
        System.out.println("Dictionary file found and valid, successfully initialize the dictionary");
        // run server
        runServer(Integer.parseInt(args[0]));
    }
}
