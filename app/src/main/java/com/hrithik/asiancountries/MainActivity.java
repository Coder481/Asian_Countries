package com.hrithik.asiancountries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import android.app.Dialog;
import android.os.Bundle;

import com.hrithik.asiancountries.databinding.ActivityMainBinding;
import com.hrithik.asiancountries.models.Country;
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

        roomDBSetup();

        myApp = (MyApplication)getApplicationContext();

        Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        myApp.showDialog(this,"Getting Data...");

        /*retro.create(GetCountries.class).countriesList()
                .enqueue(new Callback<List<Country>>() {
                    @Override
                    public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                        countries = response.body();

                        if (countries == null){
                            new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("Fatal!!")
                                    .setMessage("Null Data")
                                    .show();
                            return;
                        }



                        myApp.hideDialog();

                        countryViewModel.insertAll(countries);
                    }

                    @Override
                    public void onFailure(Call<List<Country>> call, Throwable t) {

                        myApp.hideDialog();

                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Fatal!!")
                                .setMessage(t.getLocalizedMessage())
                                .show();

                    }
                });*/
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
            countries = countriesList;

            showCountriesDialog();
        });
    }


    private void showCountriesDialog() {
        StringBuilder countriesName = new StringBuilder();
        int count = 0;
        for (Country c : countries){
            countriesName.append("Name:").append(c.name)
                    .append(" Capital:").append(c.capital)
//                    .append(" Languages:").append(Arrays.toString(c.languages.toArray()))
                    .append("\n");
            ++count;
            if (count >= 5)
                break;
        }

        new AlertDialog.Builder(this)
                .setMessage(countriesName.toString()).show();

    }
}