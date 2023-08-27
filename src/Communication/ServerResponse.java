// Author: Ziming Wang (1180051) https://github.com/Ziming-W
package Communication;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerResponse implements Serializable {

    private boolean successful;

    private ArrayList<String> meanings;

    public ServerResponse(boolean successful, ArrayList<String> meanings) {
        this.successful = successful;
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

    public ArrayList<String> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<String> meanings) {
        this.meanings = meanings;
    }
}
