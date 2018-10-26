package com.thomas.garrison.aviationincidents.RetrofitModels;

import pl.droidsonroids.jspoon.annotation.Selector;

public class Detail {

    @Selector("table > tbody > tr:nth-child(2) > td:nth-child(2)")
    private String incidentDate;
    @Selector(value = "img[src~=ICAOtype]", attr = "src")
    private String aircraftImageUrl;
    @Selector("#contentcolumn > div > span:nth-child(4)")
    private String narrative;

    public String getIncidentDate() {
        return incidentDate;
    }

    public String getAircraftImageUrl() {
        return "https:" + aircraftImageUrl;
    }

    public String getNarrative() {
        return narrative;
    }
}