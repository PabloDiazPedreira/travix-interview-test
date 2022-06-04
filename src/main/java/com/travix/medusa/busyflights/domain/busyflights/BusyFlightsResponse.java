package com.travix.medusa.busyflights.domain.busyflights;

import java.util.List;

public class BusyFlightsResponse {

    private List<BusyFlightsProviderResponse> flights;

    public List<BusyFlightsProviderResponse> getFlights() {
        return flights;
    }

    public void setFlights(List<BusyFlightsProviderResponse> flights) {
        this.flights = flights;
    }
}
