// Author: Ziming Wang (1180051) https://github.com/Ziming-W
package Validator;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressAndPortValidator {

    public static String validatePort(String arg){
        try {
            int port = Integer.parseInt(arg);
            if(!(port >= 1024 && port <= 49151)){
                return "Port out of range, has to be within [1024, 49151]";
            }
        } catch (NumberFormatException e) {
            return "The port number you input is not numeric";
        }
        return "";
    }

    public static boolean validateServerAddress(String arg) {
        try {
            InetAddress.getByName(arg);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
