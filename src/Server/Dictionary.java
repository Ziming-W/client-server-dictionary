package Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Dictionary {
    private HashMap<String, ArrayList<String>> entries;

    public Dictionary(String path) {
        entries = new HashMap<>();
        try {
            // read in file as json string
            FileReader fileReader = new FileReader(path);
            StringBuilder stringBuilder = new StringBuilder();
            int data;
            while ((data = fileReader.read()) != -1) {
                stringBuilder.append((char) data);
            }
            fileReader.close();
            // parse json string to json object, and write to hashmap
            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            for (String key : jsonObject.keySet()) {
                JSONArray valuesArray = jsonObject.getJSONArray(key);
                ArrayList<String> values = new ArrayList<>();
                for (int i = 0; i < valuesArray.length(); i++) {
                    values.add(valuesArray.getString(i));
                }
                entries.put(key, values);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + path);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IO exception: " + path);
            System.exit(1);
        } catch (JSONException e) {
            System.err.println("JSON parsing exception: " + path);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred.");
            System.exit(1);
        }
    }
    /* CRUD*/

    /*
    create
     */
    public synchronized boolean create(String word, ArrayList<String> meanings){
        if(entries.containsKey(word)){
            return false;
        }
        entries.put(word, meanings);
        return true;
    }

    /*
    Retrieve
     */
    public ArrayList<String> retrieve(String word){
        return entries.get(word);
    }

    /*
    Update
     */
    public synchronized boolean update(String word, ArrayList<String> meanings){
        if(!entries.containsKey(word)){
            return false;
        }
        entries.put(word, meanings);
        return true;
    }

    /*
    Delete
     */
    public synchronized boolean delete(String word){
        if(!entries.containsKey(word)){
            return false;
        }
        entries.remove(word);
        return true;
    }

}
