package com.hrithik.asiancountries.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.TypeConverters;

import com.hrithik.asiancountries.DataConverter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity(tableName = "Country",primaryKeys = "name")
public class Country {


    @ColumnInfo(name = "name")
    @NotNull
    public String name="";


    @ColumnInfo(name = "capital")
    public String capital;


    @ColumnInfo(name = "flag")
    public String flag;


    @ColumnInfo(name = "region")
    public String region;


    @ColumnInfo(name = "subregion")
    public String subregion;


    @ColumnInfo(name = "population")
    public long population;

    @ColumnInfo(name = "languages")
    public List<Language> languages;


//    @ColumnInfo(name = "borders")
//    public List<String> borders;


    public Country() {
    }

    public Country(String name, String capital
            , String flag, String region
            , String subregion, long population, List<Language> languages) {
        this.name = name;
        this.capital = capital;
        this.flag = flag;
        this.region = region;
        this.subregion = subregion;
        this.population = population;
        this.languages = languages;
    }
}
