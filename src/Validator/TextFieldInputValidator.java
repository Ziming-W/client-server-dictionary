// Author: Ziming Wang (1180051) https://github.com/Ziming-W
package Validator;

import java.util.ArrayList;

public class TextFieldInputValidator {

    public static boolean validateWord(String word){
        return !word.isEmpty() && word.matches("[a-zA-Z'-]+");
    }

    public static boolean validateMeaningsArrayList(ArrayList<String> meanings){
        return meanings != null && !(meanings.stream().anyMatch(String::isEmpty));
    }
}
