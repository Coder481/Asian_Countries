package com.hrithik.asiancountries.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hrithik.asiancountries.models.Country;

import java.util.List;

@Dao
public abstract class CountryDao {

    @Query("SELECT * FROM Country")
    abstract LiveData<List<Country>> loadAllCountries();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void insertAll(List<Country> countries);

    @Query("DELETE FROM Country")
    abstract void deleteAll();
}
