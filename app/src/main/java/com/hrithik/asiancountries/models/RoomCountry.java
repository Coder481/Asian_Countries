package com.hrithik.asiancountries.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "RoomCountry",primaryKeys = "name")
public class RoomCountry {

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
    public String languages;


    @ColumnInfo(name = "borders")
    public String borders;


    public RoomCountry() {
    }

    public RoomCountry(String name, String capital
            , String flag, String region
            , String subregion, long population, String languages, String borders) {
        this.name = name;
        this.capital = capital;
        this.flag = flag;
        this.region = region;
        this.subregion = subregion;
        this.population = population;
        this.languages = languages;
        this.borders = borders;
    }

}
