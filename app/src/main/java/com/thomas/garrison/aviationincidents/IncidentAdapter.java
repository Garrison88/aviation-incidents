package com.thomas.garrison.aviationincidents;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thomas.garrison.aviationincidents.RetrofitModels.Detail;
import com.thomas.garrison.aviationincidents.RetrofitModels.Incident;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncidentAdapter extends
        RecyclerView.Adapter<IncidentAdapter.MyViewHolder> {

    private List<Incident> incidentList;

    private Context mContext;

    /**
     * View holder class
     * */
    class MyViewHolder extends RecyclerView.ViewHolder {
        String link;
        CardView cardView;
        TextView dateText;
        ImageView flagImg;
        TextView countryText;
        TextView operatorText;
        TextView fatalitiesText;
        TextView aircraftTypeText;
        ImageView aircraftImg;

        MyViewHolder(View view) {
            super(view);
            dateText = view.findViewById(R.id.rv_date);
            cardView = view.findViewById(R.id.rv_card_view);
            flagImg = view.findViewById(R.id.rv_flag);
            countryText = view.findViewById(R.id.rv_country);
            operatorText = view.findViewById(R.id.rv_operator);
            fatalitiesText = view.findViewById(R.id.rv_fatalities);
            aircraftTypeText = view.findViewById(R.id.rv_aircraft_type);
            aircraftImg = view.findViewById(R.id.rv_aircraft_img);
        }
    }

    IncidentAdapter(Context context, List<Incident> incidentList) {
        this.mContext = context;
        this.incidentList = incidentList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Incident incident = incidentList.get(position);
        holder.flagImg.setImageDrawable(null);
        holder.aircraftImg.setImageDrawable(null);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("detail_url", incident.getDetailsUrl());
            mContext.startActivity(intent);
        });

        holder.dateText.setText(incident.getIncidentDate().substring(0, 6));

        if (incident.getFlagUrl().contains("flags_15/unk")
                || incident.getFlagUrl().contains("flags_15/atl")
                || incident.getFlagUrl().contains("flags_15/pac")) {
            holder.countryText.setText(incident.getLocation());
        } else {
            holder.flagImg.setVisibility(View.VISIBLE);
            Picasso.get()
                   .load(incident.getFlagUrl())
                   .resize(0, 50)
                   .into(holder.flagImg);
            holder.countryText.setText(" " + incident.getLocation());
        }

        if (incident.getEuBan() != null) {
            holder.operatorText.setText(" " + incident.getOperator());
            holder.operatorText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_airplanemode_inactive_24px, 0, 0, 0);
        } else {
            holder.operatorText.setText(incident.getOperator());
        }

        switch (incident.getFatalities()) {
            case "" : holder.fatalitiesText.setText("Fatalities: N/A");
                break;
            case "0" : holder.fatalitiesText.setText("No fatalities");
                break;
            default : holder.fatalitiesText.setText(String.format("Fatalities: %s", incident.getFatalities()));
//                      holder.fatalitiesText.setBackgroundColor(0xFF00FF00);
        }

        holder.aircraftTypeText.setText(incident.getAircraftType());

        ApiInterface apiService = ApiService.getClient().create(ApiInterface.class);

        String detailsUrl = incident.getDetailsUrl();
        String detailsId = detailsUrl.substring(detailsUrl.indexOf("=") + 1, detailsUrl.length());

        Call<Detail> call = apiService.incidentDetail(detailsId);

        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                if (response.body() != null) {
                    Picasso.get()
                           .load(response.body().getAircraftImageUrl())
                           .into(holder.aircraftImg);
                }
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                // TODO: handle error
            }
        });
    }

    @Override
    public int getItemCount() {
        return incidentList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.incident_card_layout, parent, false);
        return new MyViewHolder(v);
    }
}