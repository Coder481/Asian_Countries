package com.hrithik.asiancountries.helpers;

import com.hrithik.asiancountries.models.Country;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetCountries {

    @GET("rest/v2/region/asia")
    Call<List<Country>> countriesList();

}
