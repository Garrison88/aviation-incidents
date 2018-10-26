package com.thomas.garrison.aviationincidents;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.thomas.garrison.aviationincidents.RetrofitModels.Incident;
import com.thomas.garrison.aviationincidents.RetrofitModels.Page;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<Incident> incidentList;
    ApiInterface apiService;
    Spinner yearSpinner;
    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    ProgressBar pSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name) + " in ");

        apiService = ApiService.getClient().create(ApiInterface.class);

        mRecyclerView = findViewById(R.id.recycler_view);
        pSpinner = findViewById(R.id.progress_spinner);

        incidentList = new ArrayList<>();

//        incidentList = new ArrayList<>();
        mAdapter = new IncidentAdapter(this, incidentList);
        mRecyclerView.setAdapter(mAdapter);

        yearSpinner = findViewById(R.id.spinner);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                incidentList.clear();

                int selectedYear = Integer.valueOf(String.valueOf(yearSpinner.getSelectedItem()));

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(mLayoutManager);

                pSpinner.setVisibility(View.VISIBLE);

                Call<Page> call = apiService.numberOfPages(selectedYear);

                call.enqueue(new Callback<Page>() {
                    @Override
                    public void onResponse(Call<Page> call, Response<Page> response) {
                        searchYear(selectedYear,
                                response.body().getNumberOfPages() == null
                                        ? 1
                                        : Integer.parseInt(response.body().getNumberOfPages()));
                    }

                    @Override
                    public void onFailure(Call<Page> call, Throwable t) {
//                        pSpinner.setVisibility(View.INVISIBLE);
                        // the network call was a failure
                        // TODO: handle error
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> years = new ArrayList<>();
        for (int x = 1919; x <= 2018; x++) {
            years.add(String.valueOf(x));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, years);

        yearSpinner.setAdapter(adapter);
    }

    public void searchYear(int year, int numberOfPages) {

        for (int x = 1; x <= numberOfPages; x++) {
            Call<Page> call = apiService.incidentResponse(year, x);
            call.enqueue(new Callback<Page>() {
                @Override
                public void onResponse(Call<Page> call, Response<Page> response) {
                    incidentList.addAll(response.body().getIncidents());
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Page> call, Throwable t) {
//                    pSpinner.setVisibility(View.INVISIBLE);
                    // the network call was a failure
                    // TODO: handle error
                }
            });
        }
        mAdapter.notifyDataSetChanged();


        pSpinner.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
