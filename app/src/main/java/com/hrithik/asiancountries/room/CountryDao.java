package com.hrithik.asiancountries.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hrithik.asiancountries.models.Country;
import com.hrithik.asiancountries.models.RoomCountry;

import java.util.List;

@Dao
public abstract class CountryDao {

    @Query("SELECT * FROM RoomCountry")
    abstract LiveData<List<RoomCountry>> loadAllCountries();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract void insertAll(List<RoomCountry> countries);

    @Query("DELETE FROM RoomCountry")
    abstract void deleteAll();
}
