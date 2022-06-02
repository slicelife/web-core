package com.slice.auto.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {

    /**
     * Method to deserialize JSON file to an object.
     *
     * @param jsonPath path to JSON file
     * @param classOfT class to which object JSON should be deserialized
     * @param <T>
     * @return deserialized object
     */
    public static <T> T getObjectFromJson(String jsonPath, Class<T> classOfT) {
        Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
        String fileData;
        try {
            fileData = new String(Files.readAllBytes(Paths.get(jsonPath)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
        return gson.fromJson(fileData, classOfT);
    }
}
