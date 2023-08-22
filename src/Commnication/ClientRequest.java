package Commnication;


import java.io.Serializable;
import java.util.ArrayList;

public class ClientRequest implements Serializable {

    private CommandCode commandCode;

    private String word;

    private ArrayList<String> meaning;

    public ClientRequest(CommandCode commandCode, String word, ArrayList<String> meaning) {
        this.commandCode = commandCode;
        this.word = word;
        this.meaning = meaning;
    }

    public CommandCode getCommandCode() {
        return commandCode;
    }

    public String getWord() {
        return word;
    }

    public ArrayList<String> getMeaning() {
        return meaning;
    }

    public void setCommandCode(CommandCode commandCode) {
        this.commandCode = commandCode;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMeaning(ArrayList<String> meaning) {
        this.meaning = meaning;
    }
}
