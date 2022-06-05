package com.travix.medusa.busyflights.domain.toughjet;

import com.travix.medusa.busyflights.domain.crazyair.CrazyAirResponse;

import java.util.List;

public class ToughJetProviderResponse {

    private List<ToughJetResponse> flights;

    public List<ToughJetResponse> getFlights() {
        return flights;
    }

    public void setFlights(List<ToughJetResponse> flights) {
        this.flights = flights;
    }
}
