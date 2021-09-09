package com.hrithik.asiancountries;

import androidx.room.ProvidedTypeConverter;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hrithik.asiancountries.models.Country;

import java.lang.reflect.Type;
import java.util.List;

@ProvidedTypeConverter
public class DataConverter {


    @TypeConverter
    public String fromCountryToJson(List<Country> countries){
        if (countries == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Country>>(){}.getType();


        String json = gson.toJson(countries, type);
        return json;

    }

    @TypeConverter
    public List<Country> fromJsonToList(String jsonString){
        if (jsonString == null)
            return null;

        Gson gson = new Gson();
        Type type = new TypeToken<List<Country>>(){}.getType();

        List<Country> countries = gson.fromJson(jsonString, type);
        return countries;
    }

}
