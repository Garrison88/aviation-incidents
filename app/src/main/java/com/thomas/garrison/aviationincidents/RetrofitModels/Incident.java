package com.thomas.garrison.aviationincidents.RetrofitModels;

import pl.droidsonroids.jspoon.annotation.Selector;

public class Incident {
    @Selector(value = "a", attr = "href")
    private String detailsUrl;
    @Selector("nobr > a")
    private String incidentDate;
    @Selector(value = "img[src~=flags]", attr = "title")
    private String location;
    @Selector(value = "img[src~=euban]", attr = "src")
    private String euBan;
    @Selector("td:nth-child(4)")
    private String operator;
    @Selector(value = "img[src~=flags]", attr = "src")
    private String flagUrl;
    @Selector(".listdata")
    private String fatalities;
    @Selector("td:nth-child(2) > nobr")
    private String aircraftType;
//    @Selector(value = "img", attr = "src")
//    private String aircraftImageUrl;

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public String getIncidentDate() {
        return incidentDate;
    }

    public String getLocation() {
        return location;
    }

    public String getEuBan() {
        return euBan;
    }

    public String getOperator() {
        return operator;
    }

    public String getFlagUrl() {
        return "https://" + flagUrl;
    }

    public String getFatalities() {
        return fatalities;
    }

    public String getAircraftType() { return aircraftType; }

//    String getAircraftImageUrl() { return "https://" + aircraftImageUrl; }
}
