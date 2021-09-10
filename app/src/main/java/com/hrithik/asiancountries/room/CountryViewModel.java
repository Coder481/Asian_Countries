package com.hrithik.asiancountries.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hrithik.asiancountries.models.Country;
import com.hrithik.asiancountries.models.RoomCountry;

import java.util.List;

public class CountryViewModel extends AndroidViewModel {

    private CountryRepository mRepository;

    public CountryViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CountryRepository(application);
    }

    public LiveData<List<RoomCountry>> getAllCountries() {
        return mRepository.getAllCountries();
    }

    public void insertAll(List<RoomCountry> countries) {
        mRepository.insertAll(countries);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
