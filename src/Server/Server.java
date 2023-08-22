package Server;

import Commnication.ClientRequest;
import Commnication.CommandCode;
import Commnication.ServerResponse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private static Dictionary dic;

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
                Thread clientThread = new Thread(()->handleClientRequest(commSocket));
                clientThread.start();
            }
        }
        catch(Exception e){
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    private static void handleCreateRequest(ClientRequest request, ServerResponse response){
        // TODO: Error handling here
        response.setSuccessful(dic.create(request.getWord(), request.getMeaning()));
    }

    private static void handleRetrieveRequest(ClientRequest request, ServerResponse response){
        // TODO: Error handling here
        ArrayList<String> meanings = dic.retrieve(request.getWord());
        if(meanings == null){
            response.setSuccessful(false);
        }
        else{
            response.setSuccessful(true);
            response.setMeanings(meanings);
        }
    }

    private static void handleUpdateRequest(ClientRequest request, ServerResponse response){
        // TODO: Error handling here
        response.setSuccessful(dic.update(request.getWord(), request.getMeaning()));
    }

    private static void handleDeleteRequest(ClientRequest request, ServerResponse response){
        // TODO: Error handling here
        response.setSuccessful(dic.delete(request.getWord()));
    }

    private static void handleClientRequest(Socket commSocket){
        try {
            ServerResponse response = new ServerResponse();
            //receive ClientRequest payload
            InputStream inputStream = commSocket.getInputStream();
            // create a DataInputStream so we can read data from it.
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ClientRequest request = (ClientRequest)objectInputStream.readObject();
            // handle request
            if(request.getCommandCode() == CommandCode.CREATE){
                handleCreateRequest(request, response);
            }
            else if(request.getCommandCode() == CommandCode.RETRIEVE){
                handleRetrieveRequest(request, response);
            }
            else if(request.getCommandCode() == CommandCode.UPDATE){
                handleUpdateRequest(request, response);
            }
            else if(request.getCommandCode() == CommandCode.DELETE){
                handleDeleteRequest(request, response);
            }
            else{
                // TODO: how to close socket here?
                throw new Exception("Unknow request type");
            }
            // send response to the client
            OutputStream outputStream = commSocket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(response);
            // close socket
            commSocket.close();
        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
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
