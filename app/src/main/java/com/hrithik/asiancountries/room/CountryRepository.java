package com.hrithik.asiancountries.room;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.hrithik.asiancountries.models.Country;

import java.util.List;

public class CountryRepository {

    private CountryDao countryDao;
    private LiveData<List<Country>> mAllCountries;

    CountryRepository(Application application) {
        CountryDatabase db = CountryDatabase.getDatabase(application);
        countryDao = db.countryDao();
    }

    LiveData<List<Country>> getAllCountries() {
        return countryDao.loadAllCountries();
    }

    void insertAll(List<Country> countries) {
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
