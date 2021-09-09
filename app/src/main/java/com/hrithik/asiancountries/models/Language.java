package com.hrithik.asiancountries.models;

import androidx.room.Entity;

@Entity
public class Language {

    public String name;

    public Language(String name) {
        this.name = name;
    }
}
