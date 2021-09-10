package com.hrithik.asiancountries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.hrithik.asiancountries.databinding.ActivityMainBinding;
import com.hrithik.asiancountries.models.Country;
import com.hrithik.asiancountries.models.Language;
import com.hrithik.asiancountries.models.RoomCountry;
import com.hrithik.asiancountries.room.CountryViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://restcountries.eu/";
    private ActivityMainBinding b;
    List<Country> countries = new ArrayList<>();
    private MyApplication myApp;


    public CountryViewModel countryViewModel;
    public ViewModelProvider.Factory model;
    public ViewModelStore store = new ViewModelStore();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());



        myApp = (MyApplication)getApplicationContext();

        roomDBSetup();
    }

    private void getCountriesFromAPI() {

        if (isOffline()){
            Toast.makeText(this, "You are offline. Please connect to internet!", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        myApp.showDialog(this,"Getting Data...");

        retro.create(GetCountries.class).countriesList()
                .enqueue(new Callback<List<Country>>() {
                    @Override
                    public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                        countries = response.body();

                        if (countries == null){
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Fatal!!")
                                    .setMessage("Data not found!")
                                    .show();
                            return;
                        }

                        List<RoomCountry> roomCountryList = new ArrayList<>();

                        for (Country country : countries){

                            String languages = DataConverter.fromLanguageListToJson(country.languages);
                            String borders = DataConverter.fromBorderListToJson(country.borders);

                            RoomCountry roomCountry = new RoomCountry(country.name,country.capital
                                    ,country.flag,country.region,country.subregion
                                    ,country.population,languages,borders);

                            roomCountryList.add(roomCountry);
                        }


                        myApp.hideDialog();

                        showCountriesDialog();

                        countryViewModel.insertAll(roomCountryList);
                    }

                    @Override
                    public void onFailure(Call<List<Country>> call, Throwable t) {

                        myApp.hideDialog();

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Fatal!!")
                                .setMessage(t.getLocalizedMessage())
                                .show();

                    }
                });
    }

    private void roomDBSetup() {
        model = new ViewModelProvider.Factory() {
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                CountryViewModel countryViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(CountryViewModel.class);
                return (T) countryViewModel;
            }
        };
        countryViewModel = new ViewModelProvider(store, model).get(CountryViewModel.class);

        countryViewModel.getAllCountries().observe(MainActivity.this, countriesList -> {

            if (countriesList == null || countriesList.isEmpty()){
                getCountriesFromAPI();
                return;
            }

            for (RoomCountry roomCountry : countriesList){
                List<Language> languages = DataConverter.fromJsonToLanguageList(roomCountry.languages);
                List<String> borders = DataConverter.fromJsonToBordersList(roomCountry.borders);

                Country country = new Country(roomCountry.name,roomCountry.capital
                        ,roomCountry.flag,roomCountry.region,roomCountry.subregion
                        ,roomCountry.population,languages,borders);

                countries.add(country);

            }

            showCountriesDialog();
        });
    }


    private void showCountriesDialog() {
        StringBuilder countriesName = new StringBuilder();
        int count = 0;
        for (Country c : countries){
            countriesName.append("Name:").append(c.name)
                    .append(" Capital:").append(c.capital)
                    .append(" Languages:").append(c.languages.get(0).name)
                    .append("\n");
            ++count;
            if (count >= 5)
                break;
        }

        new AlertDialog.Builder(this)
                .setMessage(countriesName.toString()).show();

    }

    public boolean isOffline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return !(wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected());
    }
}