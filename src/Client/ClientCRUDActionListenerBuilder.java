package Client;

import Communication.ClientRequest;
import Communication.CommandCode;
import Communication.ServerResponse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientCRUDActionListenerBuilder {

    private JTextField inputField;
    private JTextArea meaningTextArea;
    private JTextArea responseTextArea;
    private String serverAddress;
    private int serverPort;

    public ClientCRUDActionListenerBuilder(JTextField inputField, JTextArea meaningTextArea, JTextArea responseTextArea, String serverAddress, int serverPort) {
        this.inputField = inputField;
        this.meaningTextArea = meaningTextArea;
        this.responseTextArea = responseTextArea;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    /**
     * Builder for CRUD action listener
     * @param needToInputMeanings if this action needs user to input the meanings of a word
     * @param commandCode the command code to invoke with server APIs
     * @param failPrompt If the action fails, prompt this to the user in the server response text box
     * @param successPrompt If the action successes, prompt this to the user in the server response text box
     * @return the action listener
     */
    public ActionListener buildCRUDActionListener(boolean needToInputMeanings,
                                                     CommandCode commandCode, String failPrompt,
                                                     String successPrompt){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // clear response area
                responseTextArea.setText("");
                // retrieve word and meanings from the text fields
                ArrayList<String> wordAndMeanings = inputValidatorAndExtractor(needToInputMeanings);
                if(wordAndMeanings == null){
                    return;
                }
                ServerResponse response = sendRequestGetResponse(new ClientRequest(
                        commandCode,
                        wordAndMeanings.get(0),
                        needToInputMeanings? new ArrayList<>(wordAndMeanings.subList(1, wordAndMeanings.size())):null
                ));
                if(response == null){
                    return;
                }
                else if(!response.isSuccessful()){
                    responseTextArea.setText("");
                    showErrorDialog(failPrompt);
                }
                else{
                    if(commandCode == CommandCode.RETRIEVE){
                        String linedOutput = "";
                        for(String str:response.getMeanings()){
                            linedOutput += str + "\n";
                        }
                        responseTextArea.setText(linedOutput);
                    }
                    else{
                        showSuccessDialog(successPrompt);
                    }
                }
            }
        };
    }

    /**
     * send a request to the server, get a response
     * @param request ClientRequest
     * @return ServerResponse
     */
    private ServerResponse sendRequestGetResponse(ClientRequest request)  {
        try{
            // build socket
            Socket socket = new Socket(serverAddress, serverPort);
            // send request
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(request);
            // get response
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            ServerResponse response = (ServerResponse)objectInputStream.readObject();
            return response;
        }  catch (UnknownHostException e) {
            showErrorDialog("Unknown host exception");
        } catch (IOException e) {
            showErrorDialog("IO exception, the server might be down or doesn't exist at this address/port");
        } catch (ClassNotFoundException e) {
            showErrorDialog("Class not found exception");
        }
        return null;
    }

    /**
     * Display error pop-up box to the user
     * @param message The message to be shown
     */
    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "An error occurred",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Display success message box to the user
     */
    private void showSuccessDialog(String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Get the input from the text fields, validate them, then return
     * @param needToInputMeanings If an action requires the user to input the meanings of a word
     * @return An arrayList of the extracted words and optionally the meanings, the first element if the word, the rest
     * are the meanings
     */
    private ArrayList<String> inputValidatorAndExtractor(boolean needToInputMeanings){
        String word = inputField.getText().trim();
        String meanings = meaningTextArea.getText().trim();
        ArrayList<String> output = new ArrayList<>();
        // validate word input
        if(word.isEmpty() || !word.matches("[a-zA-Z]+")){
            showErrorDialog("The input word is empty or not alphabetical");
            return null;
        }
        else{
            output.add(word);
        }
        // validate meanings input
        if(needToInputMeanings && meanings.isEmpty()){
            showErrorDialog("Please input one or more meanings. One meaning per line");
            return null;
        }
        else{
            String[] meaningArray = meanings.split("\n");
            for(String str:new ArrayList<>(Arrays.asList(meaningArray))){
                if(!str.isEmpty()){
                    output.add(str);
                }
            }
        }
        return output;
    }
}
