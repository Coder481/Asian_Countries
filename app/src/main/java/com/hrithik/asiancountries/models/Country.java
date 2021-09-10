package com.hrithik.asiancountries.models;

import java.util.List;


public class Country {



    public String name;



    public String capital;



    public String flag;



    public String region;



    public String subregion;



    public long population;


    public List<Language> languages;



    public List<String> borders;


    public Country() {
    }

    public Country(String name, String capital
            , String flag, String region
            , String subregion, long population, List<Language> languages, List<String> borders) {
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
