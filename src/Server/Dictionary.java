// Author: Ziming Wang (1180051) https://github.com/Ziming-W
package Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Dictionary {
    private JSONObject jsonObject;
    private String path;

    public Dictionary(String path) {
        try {
            this.path = path;
            // read in file as json string
            FileReader fileReader = new FileReader(path);
            StringBuilder stringBuilder = new StringBuilder();
            int data;
            while ((data = fileReader.read()) != -1) {
                stringBuilder.append((char) data);
            }
            fileReader.close();
            // parse json string to json object, and write to hashmap
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + path);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IO exception: " + path);
            System.exit(1);
        } catch (JSONException e) {
            System.err.println("JSON parsing exception, please double check your json file format: " + path);
            System.err.println("The json file may be in an invalid format, e.g. empty file without braces");
            System.err.println("If you want to initialize an empty dic, at least put an empty pair of braces{}");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred.");
            System.exit(1);
        }
    }

    /* CRUD*/

    public synchronized boolean create(String word, ArrayList<String> meanings){
        if(jsonObject.has(word)){
            return false;
        }
        jsonObject.put(word, meanings);
        writeToJsonFile();
        return true;
    }

    public synchronized ArrayList<String> retrieve(String word){
        if(!jsonObject.has(word)){
            return null;
        }
        ArrayList<String> meanings = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray(word);
        for(int i = 0; i < jsonArray.length(); i++){
            meanings.add(jsonArray.getString(i));
        }
        return meanings;
    }

    public synchronized boolean update(String word, ArrayList<String> meanings){
        if(!jsonObject.has(word)){
            return false;
        }
        jsonObject.put(word, meanings);
        writeToJsonFile();
        return true;
    }

    public synchronized boolean delete(String word){
        if(!jsonObject.has(word)){
            return false;
        }
        jsonObject.remove(word);
        writeToJsonFile();
        return true;
    }

    /**
     * write to json file
     */
    private void writeToJsonFile(){
        try{
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(jsonObject.toString(4));
            fileWriter.flush();
            fileWriter.close();
            System.out.println("write to json file");
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

}
