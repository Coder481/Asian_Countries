package com.hrithik.asiancountries.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hrithik.asiancountries.models.Country;
import com.hrithik.asiancountries.models.RoomCountry;

import java.util.List;

public class CountryRepository {

    private CountryDao countryDao;
    private LiveData<List<RoomCountry>> mAllCountries;

    CountryRepository(Application application) {
        CountryDatabase db = CountryDatabase.getDatabase(application);
        countryDao = db.countryDao();
    }

    LiveData<List<RoomCountry>> getAllCountries() {
        return countryDao.loadAllCountries();
    }

    void insertAll(List<RoomCountry> countries) {
        CountryDatabase.databaseWriteExecutor.execute(() -> {
            countryDao.insertAll(countries);
        });
    }

    void deleteAll(){
        CountryDatabase.databaseWriteExecutor.execute(()->{
            countryDao.deleteAll();
        });
    }
}
