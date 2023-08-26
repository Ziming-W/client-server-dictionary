package Client;

import Validator.AddressAndPortValidator;

import javax.swing.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Client {
    private static void validateArguments(String[] args){
        if(args.length != 2){
            System.err.println("Client: Number of arguments are not 2");
            System.exit(1);
        }
        // port number has to be valid
        String result = AddressAndPortValidator.validatePort(args[1]);
        if(!result.isEmpty()){
            System.err.println(result);
            System.exit(1);
        }
        // server address is valid
        if(!AddressAndPortValidator.validateServerAddress(args[0])){
            System.err.println("The server address you input is no valid");
            System.exit(1);
        }
    }
    public static void main(String[] args){
        // validate arguments
        validateArguments(args);
        // run GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClientGUI(args[0], Integer.parseInt(args[1]));
            }
        });
    }
}
