package com.travix.medusa.busyflights.service;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;

import java.util.List;

public interface BusyFlightsProvider {

    List<BusyFlightsProviderResponse> obtainFlights(BusyFlightsRequest busyFlightsRequest) throws Exception;
}
