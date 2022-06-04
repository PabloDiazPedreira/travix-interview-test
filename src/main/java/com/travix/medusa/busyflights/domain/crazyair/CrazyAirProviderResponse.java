package com.travix.medusa.busyflights.domain.crazyair;

import java.util.List;

public class CrazyAirProviderResponse {

    private List<CrazyAirResponse> flights;

    public List<CrazyAirResponse> getFlights() {
        return flights;
    }

    public void setFlights(List<CrazyAirResponse> flights) {
        this.flights = flights;
    }
}
