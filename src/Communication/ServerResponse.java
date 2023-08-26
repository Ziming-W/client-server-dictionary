package Communication;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerResponse implements Serializable {

    private boolean successful;

    private String word;

    private ArrayList<String> meanings;

    public ServerResponse(boolean successful, String word, ArrayList<String> meanings) {
        this.successful = successful;
        this.word = word;
        this.meanings = meanings;
    }

    public ServerResponse() {
    }
    public boolean isSuccessful() {
        return successful;
    }

    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ArrayList<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<String> meanings) {
        this.meanings = meanings;
    }
}
