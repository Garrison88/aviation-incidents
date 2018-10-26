package com.thomas.garrison.aviationincidents.RetrofitModels;

import java.util.List;

import pl.droidsonroids.jspoon.annotation.Selector;

public class Page {
    @Selector("table > tbody > tr:not(:first-child)")
    private List<Incident> incidents;
    @Selector(".pagenumbers > a:last-child")
    private String numberOfPages;

    public List<Incident> getIncidents() {
        return incidents;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }
}
