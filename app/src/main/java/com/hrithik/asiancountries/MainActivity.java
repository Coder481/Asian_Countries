package com.hrithik.asiancountries;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;

import android.content.DialogInterface;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.chip.Chip;
import com.hrithik.asiancountries.SVGHandlers.SvgSoftwareLayerSetter;
import com.hrithik.asiancountries.databinding.ActivityMainBinding;
import com.hrithik.asiancountries.databinding.CountryViewLayoutBinding;
import com.hrithik.asiancountries.databinding.FlagDialogLayoutBinding;
import com.hrithik.asiancountries.helpers.DataConverter;
import com.hrithik.asiancountries.helpers.GetCountries;
import com.hrithik.asiancountries.models.Country;
import com.hrithik.asiancountries.models.Language;
import com.hrithik.asiancountries.models.RoomCountry;
import com.hrithik.asiancountries.room.CountryViewModel;

import java.util.ArrayList;
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
    boolean getFromAPI = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());



        myApp = (MyApplication)getApplicationContext();

        myApp.showDialog(this,"Getting Data...");

        roomDBSetup();
    }

    private void getCountriesFromAPI() {

        if (myApp.isOffline()){
            Toast.makeText(this, "You are offline. Please connect to internet!", Toast.LENGTH_SHORT).show();
            return;
        }

        Retrofit retro = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

//        myApp.showDialog(this,"Getting Data...");

        retro.create(GetCountries.class).countriesList()
                .enqueue(new Callback<List<Country>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Country>> call, @NonNull Response<List<Country>> response) {
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


//                        myApp.hideDialog();

                        setUpViews();

                        countryViewModel.insertAll(roomCountryList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Country>> call, @NonNull Throwable t) {

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

            if (!getFromAPI)
                return;

            if (countriesList == null || countriesList.isEmpty()){
                getCountriesFromAPI();
                return;
            }


            for (RoomCountry roomCountry : countriesList){
                List<Language> languages = DataConverter.fromJsonToLanguageList(roomCountry.languages);
                List<String> borders = DataConverter.fromJsonToBordersList(roomCountry.borders);

                Country country = new Country(roomCountry.name,roomCountry.capital
                        ,roomCountry.flag
                        ,roomCountry.region,roomCountry.subregion
                        ,roomCountry.population,languages,borders);

                countries.add(country);

            }

            setUpViews();
        });
    }


    private void setUpViews() {

        if (countries.isEmpty()){
            b.noDataTv.setVisibility(View.VISIBLE);
            b.scrollLay.setVisibility(View.GONE);
            return;
        }else {
            b.noDataTv.setVisibility(View.GONE);
            b.scrollLay.setVisibility(View.VISIBLE);
        }

        b.countriesLL.removeAllViews();

        for (Country country : countries){
            CountryViewLayoutBinding binding = CountryViewLayoutBinding.inflate(getLayoutInflater());

            binding.countryName.setText(country.name);
            binding.countryCapital.setText(country.capital);

            String population = "Population: "+country.population;
            binding.countryPopulation.setText(population);

            String region = country.region+", "+country.subregion;
            binding.countryRegion.setText(region);


            binding.showFlag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (myApp.isOffline()){
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("You are offline.\nConnect to internet to view flag!")
                                .show();
                        return;
                    }

                    if (country.flag == null){
                        Toast.makeText(MainActivity.this, "Flag not found!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    FlagDialogLayoutBinding binding = FlagDialogLayoutBinding.inflate(LayoutInflater.from(MainActivity.this));

                    Uri uri = Uri.parse(country.flag);

                    RequestBuilder<PictureDrawable> requestBuilder = Glide.with(MainActivity.this)
                            .as(PictureDrawable.class)
                            .listener(new SvgSoftwareLayerSetter());

                    requestBuilder
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .load(uri)
                            .into(binding.flagImageV);

                    new AlertDialog.Builder(MainActivity.this)
                            .setView(binding.getRoot())
                            .show();
                }
            });

            List<Language> languages = country.languages;
            if (languages.isEmpty()){
                binding.textView1.setVisibility(View.GONE);
                binding.languagesGrp.setVisibility(View.GONE);
            }else {
                binding.languagesGrp.removeAllViews();
                for (Language language : languages){
                    Chip chip = new Chip(MainActivity.this);
                    chip.setText(language.name);
                    chip.setTextSize(12);

                    binding.languagesGrp.addView(chip);
                }
            }


            List<String> borders = country.borders;
            if (borders.isEmpty()){
                binding.textView2.setVisibility(View.GONE);
                binding.bordersGrp.setVisibility(View.GONE);
            }else {
                binding.bordersGrp.removeAllViews();
                for (String border : borders){
                    Chip chip = new Chip(MainActivity.this);
                    chip.setText(border);
                    chip.setTextSize(12);

                    binding.bordersGrp.addView(chip);
                }
            }

            b.countriesLL.addView(binding.getRoot());
        }

        myApp.hideDialog();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAll){
            deleteAllData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllData() {
        new AlertDialog.Builder(this)
                .setTitle("Sure to delete all data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getFromAPI = false;
                        countryViewModel.deleteAll();
                        countries = new ArrayList<>();
                        setUpViews();
                    }
                })
                .setNegativeButton("Cancel",null)
                .show();
    }

    @Override
    protected void onResume() {
        setUpViews();
        super.onResume();
    }
}