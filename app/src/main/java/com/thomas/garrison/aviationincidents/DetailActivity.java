package com.thomas.garrison.aviationincidents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thomas.garrison.aviationincidents.RetrofitModels.Detail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    ApiInterface apiService;
    ImageView aircraftImage;
    TextView narrative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24px);

        toolbar.setNavigationOnClickListener(v -> finish());

        apiService = ApiService.getClient().create(ApiInterface.class);

        aircraftImage = findViewById(R.id.detail_aircraft_image);
        narrative = findViewById(R.id.detail_narrative);
        narrative.setMovementMethod(new ScrollingMovementMethod());

        // parse the URL passed by IndidentAdapter into just the ID which can be consumed by Detail.java
        String fullUrl = getIntent().getStringExtra("detail_url");
        String id = fullUrl.substring(fullUrl.indexOf("=") + 1, fullUrl.length());

        Call<Detail> call = apiService.incidentDetail(id);

        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.body() != null) {
                    getSupportActionBar().setTitle(response.body().getIncidentDate());
                    Picasso.get().load(response.body().getAircraftImageUrl()).into(aircraftImage);
                    narrative.setText(response.body().getNarrative());
                }
//                Log.d("!!@@", response.body().getAircraftImageUrl());
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                // TODO: handle error
            }
        });
    }
//    private void getIncomingIntent(){
//
//        Log.d("!!@@##", getIntent().getStringExtra("date"));
////        Log.d("$#@!", getIntent().getStringExtra("aircraft_type"));
////        narrative.setText(getIntent().getStringExtra("aircraft_type"));
//
//    }

}
