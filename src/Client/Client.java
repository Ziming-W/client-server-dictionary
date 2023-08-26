package Client;

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
        // run GUI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ClientGUI(args[0], Integer.parseInt(args[1]));
            }
        });
    }
}
