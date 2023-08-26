package Client;

import Communication.ClientRequest;
import Communication.CommandCode;
import Communication.ServerResponse;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientGUI extends JFrame{
    private JTextField inputField;
    private JTextArea meaningTextArea;
    private JTextArea responseTextArea;
    private String serverAddress;
    private int serverPort;

    public ClientGUI(String serverAddress, int serverPort)  {
        super("Dictionary Client");
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        // components - first row - command and words
        JLabel wordLabel = new JLabel("Word");
        inputField = new JTextField(20);
        JButton createButton = new JButton("Create");
        JButton retrieveButton = new JButton("Retrieve");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearAllButton = new JButton("Clear all");
        // components - second row - request
        JPanel requestPanel = new JPanel(new BorderLayout());
        JLabel requestLabel = new JLabel("Input meanings here if you want to create or update (one meaning per line)");
        meaningTextArea = new JTextArea(10, 30);
        meaningTextArea = new JTextArea(10, 30);
        requestPanel.add(requestLabel, BorderLayout.NORTH);
        requestPanel.add(new JScrollPane(meaningTextArea), BorderLayout.CENTER);
        // components - third row - response
        JPanel responsePanel = new JPanel(new BorderLayout());
        JLabel responseLabel = new JLabel("Server Response");
        responseTextArea = new JTextArea(10, 30);
        responseTextArea.setEditable(false);
        responsePanel.add(responseLabel, BorderLayout.NORTH);
        responsePanel.add(new JScrollPane(responseTextArea), BorderLayout.CENTER);
        // layout
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.add(wordLabel);
        topPanel.add(inputField);
        topPanel.add(createButton);
        topPanel.add(retrieveButton);
        topPanel.add(updateButton);
        topPanel.add(deleteButton);
        topPanel.add(clearAllButton);
        add(topPanel, BorderLayout.NORTH);
        add(requestPanel, BorderLayout.CENTER);
        add(responsePanel, BorderLayout.SOUTH);

        // action listeners for CRUD buttons
        createButton.addActionListener(crudActionListenerBuilder(true, CommandCode.CREATE,
                "Fail to create word because it already exists. Use Update to update the meaning if you desire",
                "successfully create the word"));
        retrieveButton.addActionListener(crudActionListenerBuilder(false, CommandCode.RETRIEVE,
                "The word doesn't exist in the dictionary",
                ""));
        updateButton.addActionListener(crudActionListenerBuilder(true, CommandCode.UPDATE,
                "Fail to update word because it doesn't exist. Use Create to create a new word if you desire",
                "Successfully update meaning of the word"));
        deleteButton.addActionListener(crudActionListenerBuilder(false, CommandCode.DELETE,
                "Fail to delete the word because it doesn't exist",
                "Successfully delete the word from dictionary"));

        // action listener for clear all buttons
        clearAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputField.setText("");
                meaningTextArea.setText("");
                responseTextArea.setText("");
            }
        });

        // Configure JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    /**
     * Builder for CRUD action listener
     * @param needToInputMeanings if this action needs user to input the meanings of a word
     * @param commandCode the command code to invoke with server APIs
     * @param failPrompt If the action fails, prompt this to the user in the server response text box
     * @param successPrompt If the action successes, prompt this to the user in the server response text box
     * @return the action listener
     */
    private ActionListener crudActionListenerBuilder(boolean needToInputMeanings,
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