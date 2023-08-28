// Author: Ziming Wang (1180051) https://github.com/Ziming-W
package Server;

import Communication.ClientRequest;
import Communication.CommandCode;
import Communication.ServerResponse;
import Validator.TextFieldInputValidator;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ServerCRUDRequestHandler {

    /**
     * handle client CRUD request
     * @param commSocket
     */
    public static void handleClientRequest(Socket commSocket, Dictionary dic){
        try {
            ServerResponse response = new ServerResponse();
            //receive ClientRequest payload
            InputStream inputStream = commSocket.getInputStream();
            // create a DataInputStream so we can read data from it.
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ClientRequest request = (ClientRequest)objectInputStream.readObject();
            if(request == null){
                System.err.println("Receive a null request, dangerous, client GUI is bypassed, close socket now");
                commSocket.close();
                return;
            }
            // handle request
            if(request.getCommandCode() == CommandCode.CREATE){
                handleCreateRequest(request, response, dic);
            }
            else if(request.getCommandCode() == CommandCode.RETRIEVE){
                handleRetrieveRequest(request, response, dic);
            }
            else if(request.getCommandCode() == CommandCode.UPDATE){
                handleUpdateRequest(request, response, dic);
            }
            else if(request.getCommandCode() == CommandCode.DELETE){
                handleDeleteRequest(request, response, dic);
            }
            else{
                System.err.println("unknown request type");
                commSocket.close();
                return;
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

    // handle CRUD request

    public static void handleCreateRequest(ClientRequest request, ServerResponse response, Dictionary dic){
        // invalid input
        if(!validateWordMeanings(request.getWord(), true, request.getMeaning())){
            System.err.println("The validator in GUI is bypassed and an invalid create request is sent, dangerous!");
            return;
        }
        response.setSuccessful(dic.create(request.getWord(), request.getMeaning()));
    }

    private static void handleRetrieveRequest(ClientRequest request, ServerResponse response, Dictionary dic){
        // validate input
        if(!validateWordMeanings(request.getWord(), false, null)){
            System.err.println("The validator in GUI is bypassed and an invalid retrieve request is sent, dangerous");
            return;
        }
        ArrayList<String> meanings = dic.retrieve(request.getWord());
        if(meanings == null){
            response.setSuccessful(false);
        }
        else{
            response.setSuccessful(true);
            response.setMeanings(meanings);
        }
    }

    public static void handleUpdateRequest(ClientRequest request, ServerResponse response, Dictionary dic){
        if(!validateWordMeanings(request.getWord(), true, request.getMeaning())){
            System.err.println("The validator in GUI is bypassed and an invalid update request is sent, dangerous");
            return;
        }
        response.setSuccessful(dic.update(request.getWord(), request.getMeaning()));
    }

    public static void handleDeleteRequest(ClientRequest request, ServerResponse response, Dictionary dic){
        if(!validateWordMeanings(request.getWord(), false, null)){
            System.err.println("The validator in GUI is bypassed and an invalid delete request is sent, dangerous");
            return;
        }
        response.setSuccessful(dic.delete(request.getWord()));
    }

    /**
     * validate the word and meanings of a clientRequest.
     * In case if someone bypasses the GUI input validator and send data directly to the server
     */
    public static boolean validateWordMeanings(String word, boolean checkMeanings, ArrayList<String> meanings){
        if(!TextFieldInputValidator.validateWord(word)){
            return false;
        }
        if(checkMeanings && !TextFieldInputValidator.validateMeaningsArrayList(meanings)){
            return false;
        }
        return true;
    }
}
