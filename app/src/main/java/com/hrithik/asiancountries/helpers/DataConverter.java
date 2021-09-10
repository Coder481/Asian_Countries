package com.hrithik.asiancountries.helpers;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hrithik.asiancountries.models.Country;
import com.hrithik.asiancountries.models.Language;

import java.lang.reflect.Type;
import java.util.List;


public class DataConverter {



    public static String fromLanguageListToJson(List<Language> languages){
        if (languages == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Language>>(){}.getType();


        String json = gson.toJson(languages, type);
        return json;

    }


    public static List<Language> fromJsonToLanguageList(String jsonString){
        if (jsonString == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Language>>(){}.getType();

        return gson.fromJson(jsonString, type);
    }



    public static String fromBorderListToJson(List<String> borders){
        if (borders == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();


        return gson.toJson(borders, type);

    }


    public static List<String> fromJsonToBordersList(String jsonString){
        if (jsonString == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();

        return gson.fromJson(jsonString, type);
    }

}
