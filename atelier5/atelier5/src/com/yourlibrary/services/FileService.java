package com.yourlibrary.services;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yourlibrary.models.Livre;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
public class FileService {
    private static final String FILE_PATH = "books.json";
    public static void savaData(HashMap<Integer, Livre>books){
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(FILE_PATH)){
            gson.toJson(books, writer);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    public static HashMap<Integer, Livre>loadData(){
        Gson gson = new Gson();
        try(FileReader reader = new FileReader(FILE_PATH)){
            Type type = new TypeToken<HashMap<Integer, Livre>>(){}.getType();
            return gson.fromJson(reader, type);
        } catch (IOException e){e.printStackTrace();}
        return new HashMap<>();
    }
}
