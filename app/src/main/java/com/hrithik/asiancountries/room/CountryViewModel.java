package com.hrithik.asiancountries.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hrithik.asiancountries.models.Country;

import java.util.List;

public class CountryViewModel extends AndroidViewModel {

    private CountryRepository mRepository;

    public CountryViewModel(@NonNull Application application) {
        super(application);
        mRepository = new CountryRepository(application);
    }

    public LiveData<List<Country>> getAllCountries() {
        return mRepository.getAllCountries();
    }

    public void insertAll(List<Country> countries) {
        mRepository.insertAll(countries);
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }
}
