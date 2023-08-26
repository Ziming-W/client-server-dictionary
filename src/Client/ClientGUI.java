package Client;

import Communication.CommandCode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Define GUI laylout
 */
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

        // make CRUDActionListenerBuilder
        ClientCRUDActionListenerBuilder crudBuilder = new ClientCRUDActionListenerBuilder(inputField, meaningTextArea, responseTextArea, serverAddress, serverPort);

        // action listeners for CRUD buttons
        createButton.addActionListener(crudBuilder.buildCRUDActionListener(true, CommandCode.CREATE,
                "Fail to create word because it already exists. Use Update to update the meaning if you desire",
                "successfully create the word"));
        retrieveButton.addActionListener(crudBuilder.buildCRUDActionListener(false, CommandCode.RETRIEVE,
                "The word doesn't exist in the dictionary",
                ""));
        updateButton.addActionListener(crudBuilder.buildCRUDActionListener(true, CommandCode.UPDATE,
                "Fail to update word because it doesn't exist. Use Create to create a new word if you desire",
                "Successfully update meaning of the word"));
        deleteButton.addActionListener(crudBuilder.buildCRUDActionListener(false, CommandCode.DELETE,
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

}