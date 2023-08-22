package Server;

import java.io.FileReader;

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

    private static void runServer(int port)

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
