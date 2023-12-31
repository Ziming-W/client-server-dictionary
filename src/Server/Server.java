// Author: Ziming Wang (1180051) https://github.com/Ziming-W
package Server;

import Communication.ClientRequest;
import Communication.CommandCode;
import Communication.ServerResponse;
import HandcraftedThreading.HandcraftedThreadPool;
import Validator.AddressAndPortValidator;

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
        String result = AddressAndPortValidator.validatePort(args[0]);
        if(!result.isEmpty()){
            System.err.println(result);
            System.exit(1);
        }
    }

    private static void runServer(int port){
        try{
            // initialize thread pool
            HandcraftedThreadPool threadPool = new HandcraftedThreadPool(10);
            ServerSocket listenSocket = new ServerSocket(port);
            System.out.println("successfully start listening socket");
            while(true){
                Socket commSocket = listenSocket.accept();
                System.out.println("Accepted connection from " + commSocket.getInetAddress());
                threadPool.submitTask(() -> {
                    System.out.println("Task started in thread for client: " + commSocket.getInetAddress());
                    ServerCRUDRequestHandler.handleClientRequest(commSocket, dic);
                    System.out.println("Task finished in thread for client: " + commSocket.getInetAddress());
                });
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
