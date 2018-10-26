package com.thomas.garrison.aviationincidents;

import com.thomas.garrison.aviationincidents.RetrofitModels.Detail;
import com.thomas.garrison.aviationincidents.RetrofitModels.Page;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("database/dblist.php?")
    Call<Page> incidentResponse(@Query("Year") int year, @Query("page") int page);
    @GET("database/record.php?")
    Call<Detail> incidentDetail(@Query("id") String id);
    @GET("database/dblist.php?")
    Call<Page> numberOfPages(@Query("Year") int year);
}