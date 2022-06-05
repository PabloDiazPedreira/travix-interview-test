package com.travix.medusa.busyflights.services;

import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsProviderResponse;
import com.travix.medusa.busyflights.domain.busyflights.BusyFlightsRequest;
import org.springframework.web.client.RestClientException;

import javax.validation.ValidationException;
import java.util.List;

public interface BusyFlightsProvider {

    List<BusyFlightsProviderResponse> obtainFlights(BusyFlightsRequest busyFlightsRequest) throws RestClientException;
}
